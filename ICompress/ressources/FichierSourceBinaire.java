
package ressources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FichierSourceBinaire extends FichierSource {

	
	private FileInputStream lecteurBinaire=null;
	private String buffer="";
	
	/**
	 * @param fileName
	 */
	public FichierSourceBinaire(String fileName) {
		super(fileName);
		
		try {
			//ouvre et positionne le lecteurBinaire sur fin 3e ligne
			lecteurBinaire = new FileInputStream(filename);
			int intLu = 0;
			for (int i = 0;i<3;)
			{
								try {
					intLu = lecteurBinaire.read();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				//compte les retours chariots
				if (intLu==13)
					i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//retourne l octet suivant, null si fin de fichier
	private String nextBinaire()
	{
			int intLu=-1;
			try {
				//lit un octet
				intLu = lecteurBinaire.read();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (intLu!=-1)
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
		System.out.println(f.nextSymbole().getValeur());	
	}
	
	public void fermer(){
		try {
			lecteurBinaire.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
