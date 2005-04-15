
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
	public abstract String toString ();

	/**
	 * Retourne la liste des jointures.
	 * @return
	 * ArrayList
	 */
	public abstract ArrayList getJointures();
	
	/**
	 * retourne l'expression SQL du predicat.
	 * @return
	 * String
	 */
	public abstract String getSQL();



	/**
	 * retourne le requete SQL pour le select du moteur
	 * @return
	 * String
	 */
	public abstract String getSQLMoteur(String table);
}
