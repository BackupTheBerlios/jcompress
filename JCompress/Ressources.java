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
		
		try {
			// Créer les fichiers source et destination
			fichierSource = new FileInputStream(source);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("fichier source :"+source+" inexsitant");
		}
			try {
				fichierDestination = new FileOutputStream(destination);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			// Initialise les buffer
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
		try {
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
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Retourne l'octet suivant du fichier source.
	 * @return String représentant le code ascii en binaire de l'octet suivant du fichier source.
	 * @throws IOException
	 */
	public String lireOctet() throws IOException {
		try {
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Permet d'ecrire un suite de bit (passé en parametre) dans le fichier destination.
	 * @param caractere Chaine de caractere représentant une suite de bit du caractere à écrire dans le fichier destination
	 * @throws IOException
	 */
	public void ecrireCaractere(String caractere) throws IOException {
		try {
			bufferOutput = bufferOutput + caractere;

			if (bufferOutput.length() >= 8) {
				
				System.out.println("ecriture dans fichier de :"+binaireToDecimal(bufferOutput.substring(0,8)));
				fichierDestination.write(binaireToDecimal(bufferOutput.substring(0,
						8)));
				if (bufferOutput.length()>8)
				{	System.out.println("bufferoutput avant ecriture"+bufferOutput);
					bufferOutput = bufferOutput.substring(8, bufferOutput.length());
				}else
					bufferOutput="";
				System.out.println("bufferoutput apres ecriture"+bufferOutput);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		try {
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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