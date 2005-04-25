package exception;

/**
 * Projet : OLAPSQL*PLUS
 * Auteur : 
 * 		Laure Bosse
 * 		Claire Fauroux
 */

/**
 * Exception concernant une dimension.
 */
public class DimensionException extends Exception {
	public final static String UNIQUE = "Le nom d'une dimension doit être unique.";
	public final static String EXIST = "La dimension n'existe pas.";
	
	
	public DimensionException(String message){
		super(message);
	}
}
