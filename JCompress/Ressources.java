import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.String;

/**
 * Gére les fichiers
 */
public class Ressources {

	///////////////////////////////////////
	// attributes

	private FileInputStream fichierSource;
	private FileOutputStream fichierDestination;
	private String bufferInput;
	private String bufferOutput;

	///////////////////////////////////////
	// constructeurs

	public Ressources(String source, String destination)
			throws FileNotFoundException {
		fichierSource = new FileInputStream(source);
		fichierDestination = new FileOutputStream(destination);

		bufferInput = new String();
		bufferOutput = new String();
	}

	///////////////////////////////////////
	// operations

	
	/**
	 * Retourne le bit suivant du fichier source.
	 * @return Entier représentant un bit du fichier source (0 ou 1).
	 * @throws IOException
	 */
	public int lireBit() throws IOException {
		if (bufferInput.length() < 1) {
			int intLu = fichierSource.read();
			String binaireLu = Integer.toBinaryString(intLu);

			if (binaireLu.length() < 8) {
				int cond = 8 - binaireLu.length();
				for (int i = 0; i < cond; i++) {
					binaireLu = "0" + binaireLu;
				}
			}

			bufferInput = bufferInput + binaireLu;
		}

		Integer res = new Integer(bufferInput.substring(0, 1));
		bufferInput = bufferInput.substring(1, bufferInput.length());
		return res.intValue();
	}

	/**
	 * Retourne l'octet suivant du fichier source.
	 * @return String représentant le code ascii en binaire de l'octet suivant du fichier source.
	 * @throws IOException
	 */
	public String lireOctet() throws IOException {
		int intLu = fichierSource.read();
		String binaireLu = Integer.toBinaryString(intLu);

		if (binaireLu.length() < 8) {
			int cond = 8 - binaireLu.length();
			for (int i = 0; i < cond; i++) {
				binaireLu = "0" + binaireLu;
			}
		}

		bufferInput = bufferInput + binaireLu;

		String res = bufferInput.substring(0, 8);
		bufferInput = bufferInput.substring(8, bufferInput.length());
		return res;
	}

	/**
	 * Permet d'ecrire un suite de bit (passé en parametre) dans le fichier destination.
	 * @param caractere Chaine de caractere représentant une suite de bit du caractere à écrire dans le fichier destination
	 * @throws IOException
	 */
	public void ecrireCaractere(String caractere) throws IOException {
		bufferOutput = bufferOutput + caractere;

		if (bufferOutput.length() >= 8) {
			fichierDestination.write(binaireToDecimal(bufferOutput.substring(0,
					8)));
			bufferOutput = bufferOutput.substring(8, bufferOutput.length());
		}
	}

	/**
	 * Fonction interne utilisé pour convertir une chaine de bit en sa valeur décimal.
	 * @param num Chaine de bit à convertir.
	 * @return Valeur de num en décimal.
	 */
	private int binaireToDecimal(String num) {
		int numDec = 0;
		for (int i = 0; i < num.length(); i++) {
			int j = Integer.parseInt(bufferOutput.substring(i, i + 1));
			numDec = numDec * 2 + j;
		}
		return numDec;
	}

	/**
	 * Ecrit le caractere de fin dans le fichier destination.
	 * @param fin Chaine de bit correspondant au code de fin du fichier.
	 * @throws IOException
	 */
	public void finEcrire(String fin) throws IOException {
		ecrireCaractere(fin);
		
		while(bufferOutput.length()>=8)
		{
			ecrireCaractere("");
		}
		
		if(bufferOutput.length()>0)
		{
			int num0 = 8 - bufferOutput.length();
			for(int i=0 ; i<num0 ; i++)
			{
				bufferOutput = bufferOutput+"0";
			}
			ecrireCaractere("");
		}
	}

	/**
	 * Ferme les fichiers source et destination.
	 */
	public void fermer() {
		try {
			fichierSource.close();
			fichierDestination.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}