
package arbre;

import ressources.Matrice;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class Noeud {

	private Noeud pere = null;
	
	protected Noeud (Noeud p){
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
	 * Construit la matrice correspondant � l'arbre.
	 * @param m
	 * @return
	 * Matrice
	 */
	public abstract Matrice construireMatrice();

	/**compte le nb de feuille contenu dans le noeud
	 * @return int, nb de feuille
	 */
	//ok
	public abstract int grandeurNoeud() ;
	
}
