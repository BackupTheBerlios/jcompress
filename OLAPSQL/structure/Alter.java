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
public class Alter extends Commande{
	
	public static final int ADD = 0;
	public static final int DROP = 1;
	public static final int CONNECT = 2;
	public static final int DISCONNECT = 3;
	

	protected int alteration;
	//liste d Attributs
	protected ArrayList attributs;	
	
}
