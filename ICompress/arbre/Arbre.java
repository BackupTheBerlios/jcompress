
package arbre;

import ressources.Matrice;
import arbre.Noeud;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Arbre {
	
	private Noeud racine = null;
	// TODO
	private int taille;

	public Arbre(Noeud n){
	  racine = n;
	}
	
	/**
	 * Construire arbre a partir expression du fichier txt
	 * @param nomFichier
	 */
	public Arbre(String nomFichier){
	  // TODO
	  // new fichierSource(nomFichier)
	  // taille = next()
	  // nextSymbole;
	  // null qd fin fichier
	}
	
	/**
	 * 
	 * @return
	 * Matrice
	 */
	public Matrice construireImage()
	{
	  //Matrice mat = new Matrice(racine.getProfondeur());
	  return racine.construireMatrice();
	}
	
	public String construireLigne()
	{
	  return racine.construireLigne();
	}
}
