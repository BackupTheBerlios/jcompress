/**
 * Date = 15/01/2005 
 * Project = ICompress 
 * File name = FichierDestination.java
 * @author Bosse Laure/Fauroux claire 
 * 
 * Ce projet permet la compression et la
 *         decompression de fichier PGM de type P5 et P2.
 */

package ressources;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Gestin des fichiers Sortie de ICompress
 */
public class FichierDestination extends Fichier {

	private FileWriter diskFile;

	/**
	 * constructeur
	 * @param nom, nom absolu du fichier
	 */
	public FichierDestination(String nom){
		super(nom);
		try{
			diskFile = new FileWriter(nom);
		}
		catch(IOException e1){

			e1.printStackTrace();
		}
	}

	/**
	 * ecrit le Symbole symb dans le fichier, rien si null
	 * @param symb
	 */
	public void ecrireSymbole(Symbole symb){
		if(symb != null)
			ecrireString(symb.getValeur());
		else
			System.out.println("symbole null:ecriture impossible");
	}

	/**
	 * ecrit la chaine de caractere s dans le fichier, rien si null
	 * @param s,la chaine
	 */
	public void ecrireString(String s){

		if(s != null)
			try{
				diskFile.write(s);
			}
			catch(IOException e){
				e.printStackTrace();
			}
	}

	/**
	 * ecrit un caractere de Retour Chariot dans le fichier
	 */
	public void ecrireEntree(){
		try{
			diskFile.write(13);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * ecrit 4 espaces consecutifs dans le fichier
	 */
	public void ecrireBlancs(){
		try{
			diskFile.write("    ");
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * ecrit 1 espace dans le fichier
	 */
	public void ecrireEspace(){
		try{
			diskFile.write(" ");
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * @see Fichier#fermer()
	 */
	public void fermer(){
		try{
			diskFile.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}

	}
}