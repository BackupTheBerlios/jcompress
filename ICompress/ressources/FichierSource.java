package ressources;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


//classe ok
/**
 * Gere les fichiers en lecture
 */
public class FichierSource extends Fichier{

	private FileReader diskFile;

	private BufferedReader buff;

	private String courant = "";

	private StringTokenizer st;

	/**constructeur
	 * @param fileName : nom absolu du fichier
	 */
	public FichierSource(String fileName) {
		super(fileName);
		try {
			diskFile = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			System.out.println("Probleme a l'ouverture du fichier " + fileName);
			System.exit(1);
		}
		buff = new BufferedReader(diskFile);
		String ligne = "";
		try {
			ligne = buff.readLine();
			//System.out.println("ligne lue "+ligne);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(9);
		}
		if (ligne == null) {
			System.out.println("Le fichier selectionné est vide");
			System.exit(9);
		} else
			st = new StringTokenizer(ligne);

	}

	/** 
	 * @return le prochain symbole, null si fin de fichier
	 */
	public Symbole nextSymbole() {
		Symbole symb = null;

		if (courant == "") {
			courant = next();
		}
		if (courant != "") {
			String symbStr = "";
			
			//TODO a revoir la condition, a generaliser pte..
			if (courant.length() > 1
					&& (courant.startsWith(")") || courant.startsWith("("))) {
				//token compose ex: ()(())))
				symbStr = courant.substring(0, 1);
				courant = courant.substring(1, courant.length());
			} else {
				symbStr = courant;
				courant = "";
			}

			//construit le symbole
			symb = new Symbole(symbStr);

		} else {
			//fin de fichier
			System.out.println("Fin de fichier");
		}
		return symb;

	}

	/**
	 * @return le prochain token, vide si fin de fichier
	 */
	public String next() {
		String tmp = "";
		if (st.hasMoreTokens()) {
			tmp = st.nextToken();
		} else {
			try {
				String ligne = buff.readLine();
				//System.out.println("ligne lue "+ligne);
				if (ligne != null) {
					st = new StringTokenizer(ligne);
					//System.out.println("nb de token "+ st.countTokens());
					if (st.hasMoreTokens()) {
						tmp = st.nextToken();
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		//System.out.println("next "+tmp);
		return tmp;

	}
	
	public static void main(String[] args) {
		//FichierSource f = new FichierSource("T:/IUP Master
		// 1/sem2/BOT/compress2/sources/enonce2.txt");
		FichierSource f = new FichierSource(
				"T:/IUP Master 1/sem2/BOT/compress2/sources/baboon.pgm");

		System.out.println(f.next());
		System.out.println(f.next());
		System.out.println(f.next());
		System.out.println(f.next());
		//System.out.println(f.nextBinaire());
//		System.out.println("symbole binaire "+f.nextSymboleP5().getValeur());
//		System.out.println("symbole binaire "+f.nextSymboleP5().getValeur());
//		System.out.println("symbole binaire "+f.nextSymboleP5().getValeur());
//		System.out.println("symbole binaire "+f.nextSymboleP5().getValeur());
		
//		for (Symbole token = f.nextSymbole(); token != null; token = f
//				.nextSymbole())
//			System.out.println("symbole " + token.getValeur());
//		System.out.println("end");
	}
	
	public void fermer() {
		try {
			buff.close();
			diskFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}