
package structure.types.predicat;

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

}
