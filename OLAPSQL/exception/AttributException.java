package exception;

/**
 * Projet : OLAPSQL*PLUS
 * Auteur : 
 * 		Laure Bosse
 * 		Claire Fauroux
 */

/**
 * Exception concernant un attribut.
 */
public class AttributException extends Exception {
	public static final String EXIST = "L'attribut est inexistant dans la relation.";
	
	public AttributException(String message){
		super(message);
	}
}
