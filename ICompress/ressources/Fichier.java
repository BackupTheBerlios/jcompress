/**
 * Date = 15/01/2005 
 * Project = ICompress 
 * File name = Fichier.java
 * @author Bosse Laure/Fauroux claire 
 * 
 * Ce projet permet la compression et la
 *         decompression de fichier PGM de type P5 et P2.
 */

package ressources;

/**
 * superclasse des fichiers E/S exploités dans ICompress
 */
public abstract class Fichier {

	public static final String P5 = "P5";
	public static final String P2 = "P2";

	protected String filename;

	protected Fichier(String name){
		filename = name;
	}

	/**
	 * ferme les accesseurs utilisés
	 */
	public abstract void fermer();

}