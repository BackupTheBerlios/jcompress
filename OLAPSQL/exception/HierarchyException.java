/*
 * Created on 9 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package exception;

/**
 * @author lalo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HierarchyException extends Exception {
	public static final String UNIQUE = "Le nom d'une hierarchy doit être unique.";
	
	public HierarchyException(String message){
		super(message);
	}
}
