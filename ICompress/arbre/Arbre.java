
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

	public Arbre(Noeud n, int t){
	  racine = n;
	  taille = t;
	}
	
	/**
	 * retourne le nombre de feuille de l'arbre
	 * @return int, nb de feuilles(couleurs)
	 */
	public int grandeurArbre(){
		return racine.grandeurNoeud();
	};
	
	/**TODO
	 * compare la taille entre this et a et retourne un taux en pourcentage
	 * @param a, Arbre a comparer
	 * @return double, taux < 100 si a est "plus petit" que this
	 */
	public float tauxDeCompression(Arbre a){
		float f1 = a.grandeurArbre();
		float f2 = this.grandeurArbre();
		return ((f1/f2)*100);
	}
	
	/**
	 * Construire arbre a partir expression du fichier txt
	 * @param nomFichier
	 * @throws Exception
	 */
	public Arbre(String nomFichier){
	  // TODO
	  // new fichierSource(nomFichier)
	  // taille = next()
	  // nextSymbole;
	  // null qd fin fichier
	  FichierSource fichier = new FichierSource(nomFichier);
	  taille = Integer.parseInt(fichier.next());
	  if(fichier.nextSymbole().getType().equals(Symbole.P_OUVRANTE)){
	    racine = new GrisCompose(null,fichier);
	  }
	}
	
	/**
	 * 
	 * @return
	 * Matrice
	 */
	public Matrice construireMatrice()
	{
	  // TODO prendre en compte taille
	  //Matrice mat = new Matrice(racine.getProfondeur());
	  return racine.construireMatrice();
	}
	
	public void creerFichier(String nomFichier){
		FichierDestination ficDest = new FichierDestination(nomFichier);
		ficDest.ecrireString(construireLigne());
		ficDest.fermer();
	}
	
	public String construireLigne()
	{
	  return taille+" "+racine.construireLigne();
	}
}
