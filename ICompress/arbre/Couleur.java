/**
 * Date = 15/01/2005 
 * Project = ICompress 
 * File name = Couleur.java
 * @author Bosse Laure/Fauroux claire 
 * 
 * Ce projet permet la compression et la
 *         decompression de fichier PGM de type P5 et P2.
 */

package arbre;

import ressources.Matrice;
import ressources.Symbole;
import arbre.Noeud;

/**
 * Cette classe represente une feuille de l'arbre.
 */
public final class Couleur extends Noeud {

	private int valeur;

	/**
	 * Constructeur.
	 * @param p Noeud pere.
	 * @param v Valeur de la feuille.
	 */
	public Couleur(Noeud p, int v){
		super(p);
		valeur = v;
	}

	/**
	 * @return Retourne la valeur de l'attribut valeur.
	 */
	public int getValeur(){
		return valeur;
	}

	/**
	 * @param initialse valeur avec pValeur.
	 */
	public void setValeur(int pValeur){
		valeur = pValeur;
	}

	/**
	 * @see arbre.Noeud#construireLigne()
	 */
	public String construireLigne(){
		String ligne = String.valueOf(valeur);
		return ligne;
	}

	/**
	 * @see arbre.Noeud#getProfondeur()
	 */
	public int getProfondeur(){
		return 0;
	}

	/**
	 * @see arbre.Noeud#construireMatrice(ressources.Matrice)
	 */
	public Matrice construireMatrice(){
		Matrice m = new Matrice(1);
		m.ajoutSymbole(new Symbole(String.valueOf(valeur)));
		return m;
	}

	/**
	 * retourne le nb de feuille...c une feuille donc 1
	 * @return int=1
	 */
	public int grandeurNoeud(){
		return 1;
	}

}