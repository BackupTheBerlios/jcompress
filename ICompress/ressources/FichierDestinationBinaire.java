
package ressources;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Gestion des fichiers de S binaires
 */
public class FichierDestinationBinaire extends FichierDestination {

	private FileOutputStream redacteurBinaire=null;
	
	/**
	 * @param nom, nom du fichier a écrire
	 */
	public FichierDestinationBinaire(String nom) {
		super(nom);
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
	
	/**
	 * passage en mode d ecriture binaire, fermeture des autres modes
	 * obligatoire pour utiliser #ecrireSymboleBinaires
	 * ne peut plus utiliser #ecrire et cie apres
	 */
	public void transitionBinaire(){
		super.fermer();
		try {
			redacteurBinaire = new FileOutputStream(filename,true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
}
