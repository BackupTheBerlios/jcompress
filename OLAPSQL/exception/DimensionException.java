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
public class DimensionException extends Exception {
	public final static String UNIQUE = "Le nom d'une dimension doit être unique.";
	public final static String EXIST = "La dimension n'existe pas.";
	
	
	public DimensionException(String message){
		super(message);
	}
}
