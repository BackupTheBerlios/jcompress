/**
 * Date = 15/01/2005 
 * Project = ICompress 
 * File name = Arbre.java
 * @author Bosse Laure/Fauroux claire 
 * 
 * Ce projet permet la compression et la
 *         decompression de fichier PGM de type P5 et P2.
 */

package arbre;

import ressources.FichierDestination;
import ressources.FichierSource;
import ressources.Matrice;
import ressources.Symbole;
import arbre.Noeud;

/**
 * Cette classe permet de construire un arbre
 */
public class Arbre {

	private Noeud racine = null;
	private int taille;

	/**
	 * Constructeur de l'arbre.
	 * @param n Racine de l'arbre
	 * @param t Taille de l'arbre.
	 */
	public Arbre(Noeud n, int t){
		racine = n;
		taille = t;
	}

	/**
	 * Construire arbre a partir expression du fichier txt
	 * @param nomFichier
	 */
	public Arbre(String nomFichier){
		FichierSource fichier = new FichierSource(nomFichier);
		taille = Integer.parseInt(fichier.next());
		if(fichier.nextSymbole().getType().equals(Symbole.P_OUVRANTE)){
			racine = new GrisCompose(null, fichier);
		}
	}

	/**
	 * retourne le nombre de feuille de l'arbre
	 * @return int, nb de feuilles(couleurs)
	 */
	public int grandeurArbre(){
		return racine.grandeurNoeud();
	};

	/**
	 * Compare la taille entre this et a et retourne un taux en pourcentage
	 * @param a, Arbre a comparer
	 * @return double, taux < 100 si a est "plus petit" que this
	 */
	public float tauxDeCompression(Arbre a){
		float f1 = a.grandeurArbre();
		float f2 = this.grandeurArbre();
		return ((f1 / f2) * 100);
	}

	/**
	 * Construit une matrice correspondant à l'arbre.
	 * @return Matrice
	 */
	public Matrice construireMatrice(){
		Matrice ma = racine.construireMatrice();
		ma = ma.agrandiMatrice(taille);
		return ma;
	}

	/**
	 * Créer un fichier contenant l'expression de l'arbre.
	 * @param nomFichier Nom du fichier à créer.
	 */
	public void creerFichier(String nomFichier){
		FichierDestination ficDest = new FichierDestination(nomFichier);
		ficDest.ecrireString(construireLigne());
		ficDest.fermer();
	}

	/**
	 * Construit l'expression associé à l'arbre.
	 * @return Expression de l'arbre
	 */
	public String construireLigne(){
		return taille + " " + racine.construireLigne();
	}
}