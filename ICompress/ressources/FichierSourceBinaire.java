package ressources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author claire
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class FichierSourceBinaire extends FichierSource {

	private FileInputStream lecteurBinaire = null;

	private String buffer = "";

	private int nb_next = 0;

	/**
	 * @param fileName
	 */
	public FichierSourceBinaire(String fileName) {
		super(fileName);

		try {
			lecteurBinaire = new FileInputStream(filename);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**ca a lair ok..
	 * @see ressources.FichierSource#next()
	 */
	public String next() {

		String next = "";
		//interdit de sen servir apres l entete du fichier P5
		if (nb_next < 4) {
			next = super.next();

			if (next != null) {
				nb_next++;
				if (nb_next == 4) {
					//place sur la fin nb_ligne;
					int intLu = 0;
					for (int i = 0; i < nb_ligne;) {
						try {
							intLu = lecteurBinaire.read();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
						//compte les retours chariots
						if (intLu == 10)
							i++;
					}
				}
			}
		}
		return next;
	}

	//retourne l octet suivant, null si fin de fichier
	private String nextBinaire() {
		int intLu = -1;
		try {
			//lit un octet
			intLu = lecteurBinaire.read();

		} catch (IOException e) {
			e.printStackTrace();
		}
		if (intLu != -1)
			return Integer.toString(intLu);
		return null;
	}

	public Symbole nextSymbole() {
		String valeur="";
		
		if ((valeur=nextBinaire())!="-1"){
			if(valeur == null)
				return null;
			return new Symbole(valeur);
		}
		return null;

	}

	public static void main(String[] args) {
		FichierSourceBinaire f = new FichierSourceBinaire(
				"T:/IUP Master 1/sem2/BOT/compress2/sources/baboon.pgm");

		System.out.println(f.next());
		System.out.println(f.next());
		System.out.println(f.next());
		System.out.println(f.next());
		System.out.println(f.nextBinaire());
	}

	public void fermer() {
		try {
			lecteurBinaire.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}