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
public class AttributException extends Exception {
	public static final String EXIST = "L'attribut est inexistant dans la relation.";
	
	public AttributException(String message){
		super(message);
	}
}
