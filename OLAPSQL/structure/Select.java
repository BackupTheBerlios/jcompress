/*
 * Created on Feb 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package structure;

import java.util.ArrayList;

import structure.types.predicat.Predicat;

/**
 * @author m1isi17
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Select extends Commande{

	private String nomRow;
	private String withRow;
	private ArrayList rows;
	
	private String nomColumn;
	private String withColumn;
	private ArrayList columns;
	
	private String nomFrom;
	private Predicat where;
	
	/**
	 * @param nom
	 * @param type
	 */
	public Select(String nom, int type) {
		super(nom, type);
		// TODO Auto-generated constructor stub
	}


}
