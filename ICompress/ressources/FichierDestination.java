
package ressources;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FichierDestination extends Fichier{

	private FileWriter diskFile;
	
	public FichierDestination(String nom){
		super(nom);	
		try {
				diskFile = new FileWriter(nom);
			} catch (IOException e1) {

				e1.printStackTrace();
			}	
	}
	
//	//la valeur du symbole doit etre un chiffre compris entre 0 et 255
//	public void ecrireSymboleBinaire(Symbole symb)
//	{ 
//		int tmp = Integer.parseInt(symb.getValeur());
//		String octet= Integer.toBinaryString(tmp);
//		
//		if (octet.length() < 8) {
//			int cond = 8 - octet.length();
//			for (int i = 0; i < cond; i++) {
//				octet = "0" + octet;
//			}
//		}else if (octet.length()>8){
//			System.out.println("Erreur, symbole.valeur > 255");
//		}
//		ecrireString(octet);
//	}
	
	public void ecrireSymbole (Symbole symb){
		if (symb !=null)
		ecrireString(symb.getValeur());
		else
			System.out.println("symbole null:ecriture impossible");
	}
	
	public void ecrireString (String s){
		try {
			diskFile.write(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void ecrireEntree()
	{
		try {
			diskFile.write(13);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//ecrit 4 espaces consecutifs
	public void ecrireBlancs(){
		try {
			diskFile.write("    ");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	ecrit 1 espaces
	public void ecrireBlanc(){
		try {
			diskFile.write(" ");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//ecrit 1 espace
	public void ecrireEspace(){
		try {
			diskFile.write(" ");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		 Symbole symb = new Symbole("salut");
		 Symbole symb1 = new Symbole("255");
		 Symbole symb2 = new Symbole("1");
		 
		 FichierDestination f = new FichierDestination("T:/IUP Master 1/sem2/BOT/compress2/sources/test.pgm");
		 
		 f.ecrireSymbole(symb);
		 f.ecrireEntree();
		 f.ecrireSymbole(symb2);
		 f.ecrireBlancs();
		 f.ecrireSymbole(symb2);
		 f.ecrireEspace();
		 f.ecrireSymbole(symb);
		 //f.ecrireSymboleBinaire(symb1);
		
		 System.out.println("ok test ecriture");
		 f.fermer();
		
		
	}

	public void fermer() {
		try {
			diskFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
