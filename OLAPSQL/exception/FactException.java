/*
 * Created on 9 mars 2005 TODO To change the template for this generated file go
 * to Window - Preferences - Java - Code Style - Code Templates
 */
package exception;

/**
 * @author lalo TODO To change the template for this generated type comment go
 *         to Window - Preferences - Java - Code Style - Code Templates
 */
public class FactException extends Exception {
	public static final String UNIQUE = "Le nom d'un fait doit être unique.";
	public static final String EXIST = "Le fait n'existe pas.";
	public static final String DIMENSION = "La dimension n'est pas connecté au fait.";

	public FactException(String message){
		super(message);
	}
}