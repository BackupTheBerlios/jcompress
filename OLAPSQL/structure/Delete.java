/*
 * Created on Feb 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package structure;

import structure.types.predicat.Predicat;

/**
 * @author m1isi17
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Delete extends Commande{


	// peut etre null
	private Predicat predicat=null;
	
	/**
	 * @param nom
	 * @param type
	 */
	public Delete(String nom, int type) {
		super(nom, type);
		// TODO Auto-generated constructor stub
	}


}
