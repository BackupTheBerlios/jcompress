/*
 * Created on Feb 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package structure;

import java.util.HashMap;

/**
 * @author m1isi17
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InsertFact extends Insert{


	//cle ; String nom du connect
	//valeur : Predicat
	private HashMap connects; 
	
	
	/**
	 * @param nom
	 * @param type
	 */
	public InsertFact(String nom, int type) {
		super(nom, type);
		// TODO Auto-generated constructor stub
	}

}
