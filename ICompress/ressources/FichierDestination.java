
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
	
	public void ecrireSymbole (Symbole symb){
		ecrireString(symb.getValeur());
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
		 Symbole symb1 = new Symbole("2");
		 Symbole symb2 = new Symbole("1");
		 
		 FichierDestination f = new FichierDestination("T:/IUP Master 1/sem2/BOT/compress2/sources/test.pgm");
		 
		 f.ecrireSymbole(symb);
		 f.ecrireEntree();
		 f.ecrireSymbole(symb2);
		 f.ecrireBlancs();
		 f.ecrireSymbole(symb2);
		 f.ecrireEspace();
		 f.ecrireSymbole(symb);
		
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
