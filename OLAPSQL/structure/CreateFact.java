/*
 * Created on Feb 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package structure;

import java.util.ArrayList;

/**
 * @author m1isi17
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CreateFact extends Create{

	/**
	 * @param nom
	 * @param type
	 */
	public CreateFact(String nom, int type, ArrayList attrs) {
		super(nom, type, attrs);
		// TODO Auto-generated constructor stub
	}

	//liste de String
	private ArrayList connects;
	
	public void addConnections (ArrayList l){
		connects = l;
	}
	
	
}
