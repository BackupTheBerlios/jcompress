
package structure.types.predicat;

import java.util.ArrayList;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class ElementAbstrait {
	
	protected Liaison pere=null;
	
	protected ElementAbstrait(Liaison pere){
		this.pere = pere;
	}
	
	
	
	/**
	 * @return Returns the pere.
	 */
	public Liaison getPere() {
		return pere;
	}
	/**
	 * @param pere The pere to set.
	 */
	public void setPere(Liaison pere) {
		this.pere = pere;
	}
	public abstract void afficher ();

	/**
	 * Retourne la liste des jointures.
	 * @return
	 * ArrayList
	 */
	public abstract ArrayList getJointures();
}
