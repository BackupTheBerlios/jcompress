package exception;

/**
 * Projet : OLAPSQL*PLUS
 * Auteur : 
 * 		Laure Bosse
 * 		Claire Fauroux
 */

/**
 * Exception concernant une hierarchie.
 */
public class HierarchyException extends Exception {
	public static final String UNIQUE = "Le nom d'une hierarchy doit être unique.";
	
	public HierarchyException(String message){
		super(message);
	}
}
