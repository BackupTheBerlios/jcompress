
package src;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class Noeud {

	protected Noeud pere = null;
	
	public Noeud (Noeud p){
		pere = p;
	}
	
	
}
