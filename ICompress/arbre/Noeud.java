
package arbre;

import ressources.FichierSource;
import ressources.Matrice;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class Noeud {

	private Noeud pere = null;
	
	public Noeud (Noeud p){
		pere = p;
	}
	
	public Noeud getPere(){
	  return pere;
	}
	
	public void setPere(Noeud pPere){
	  pere = pPere;
	}
	
	/**
	 * Retourne la profondeur de l'arbre.
	 * @return
	 * int
	 */
	public abstract int getProfondeur();
	
	/**
	 * Construit la ligne codant l'arbre.
	 * @return
	 * String
	 */
	public abstract String construireLigne();
	
	/**
	 * Construit la matrice correspondant à l'arbre.
	 * @param m
	 * @return
	 * Matrice
	 */
	public abstract Matrice construireMatrice();
	
}
