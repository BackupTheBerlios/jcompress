/**
 * Date = 15/01/2005 
 * Project = ICompress 
 * File name = FichierSourceBinaire.java
 * @author Bosse Laure/Fauroux claire 
 * 
 * Ce projet permet la compression et la
 *         decompression de fichier PGM de type P5 et P2.
 */

package ressources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Gestion des fichiers d Entrée en binaire
 */
public class FichierSourceBinaire extends FichierSource {

	private FileInputStream lecteurBinaire = null;

	private String buffer = "";

	private int nb_next = 0;

	/**
	 * constructeur
	 * @param fileName, nom absolu du fichier
	 */
	public FichierSourceBinaire(String fileName){
		super(fileName);

		try{
			lecteurBinaire = new FileInputStream(filename);

		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}

	/**
	 * @see ressources.FichierSource#next()
	 */
	public String next(){

		String next = "";
		//interdit de sen servir apres l entete du fichier P5
		if(nb_next < 4){
			next = super.next();

			if(next != null){
				nb_next++;
				if(nb_next == 4){
					//place sur la fin nb_ligne;
					int intLu = 0;
					for(int i = 0 ; i < nb_ligne ;){
						try{
							intLu = lecteurBinaire.read();
						}
						catch(IOException e2){
							e2.printStackTrace();
						}
						//compte les retours chariots
						if(intLu == 10 || intLu == 13)
							i++;
					}
				}
			}
		}
		return next;
	}

	/**
	 * retourne l octet suivant, null si fin de fichier
	 * @return String, la chaine correspondante lue (ici entier)
	 */
	private String nextBinaire(){
		int intLu = -1;
		try{
			//lit un octet
			intLu = lecteurBinaire.read();

		}
		catch(IOException e){
			e.printStackTrace();
		}
		if(intLu != -1)
			return Integer.toString(intLu);
		return null;
	}

	/**
	 * @see FichierSource#nextSymbole()
	 */
	public Symbole nextSymbole(){
		String valeur = "";

		if((valeur = nextBinaire()) != "-1"){
			if(valeur == null)
				return null;
			return new Symbole(valeur);
		}
		return null;

	}

	/**
	 * @see Fichier#fermer()
	 */
	public void fermer(){
		try{
			lecteurBinaire.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

}