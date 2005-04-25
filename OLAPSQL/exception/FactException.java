package exception;

/**
 * Projet : OLAPSQL*PLUS
 * Auteur : 
 * 		Laure Bosse
 * 		Claire Fauroux
 */

/**
 * Exception concernant un fait.
 */
public class FactException extends Exception {
	public static final String UNIQUE = "Le nom d'un fait doit être unique.";
	public static final String EXIST = "Le fait n'existe pas.";
	public static final String DIMENSION = "La dimension n'est pas connecté au fait.";

	public FactException(String message){
		super(message);
	}
}