
package ressources;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FichierDestinationBinaire extends FichierDestination {

	private FileOutputStream redacteurBinaire=null;
	
	/**
	 * @param nom, nom du fichier a écrire
	 */
	public FichierDestinationBinaire(String nom) {
		super(nom);
		try {
			//ouverture en mode append
			redacteurBinaire = new FileOutputStream(nom,true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**Ecrit le symbole en binaire dans le fichier
	 * @param symb, le symbole non null de type NOMBRE a ecrire en binaire
	 */
//	la valeur du symbole doit etre un chiffre compris entre 0 et 255
	public void ecrireSymboleBinaire(Symbole symb)
	{ 
		if(symb!=null && symb.getType()==Symbole.NOMBRE)
		{
			int tmp = Integer.parseInt(symb.getValeur());
			if (tmp>=0 && tmp<=255)
			{
				try {
					redacteurBinaire.write(tmp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/** ferme les ressources utilisées
	 * @see ressources.Fichier#fermer()
	 */
	public void fermer() {
		super.fermer();
		try {
			redacteurBinaire.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		FichierDestinationBinaire f = new FichierDestinationBinaire("T:\\IUP Master 1\\sem2\\BOT\\compress2\\sources\\testP5.pgm");
		
		f.ecrireString("P5");
		f.ecrireEntree();
		f.ecrireSymboleBinaire(new Symbole("32"));
		
		
		f.fermer();
		System.out.println("ends");
		
		
	}
}
