/**
 * Date = 15/01/2005 
 * Project = ICompress 
 * File name = Symbole.java
 * @author Bosse Laure/Fauroux claire 
 * 
 * Ce projet permet la compression et la
 *         decompression de fichier PGM de type P5 et P2.
 */

package ressources;

/**
 * Classe représentant un symbole
 */
public class Symbole {

	public static String P_OUVRANTE = "P_OUVRANTE";
	public static String P_FERMANTE = "P_FERMANTE";
	public static String NOMBRE = "NOMBRE";

	private String type = "";
	private String valeur = "";

	/**
	 * constructeur
	 * @param symb
	 */
	public Symbole(String symb){
		valeur = symb;

		if(valeur.equals("("))
			type = P_OUVRANTE;
		else
			if(valeur.equals(")"))
				type = P_FERMANTE;
			else
				type = NOMBRE;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType(){
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(String ptype){
		this.type = ptype;
	}

	/**
	 * @return Returns the valeur.
	 */
	public String getValeur(){
		return valeur;
	}

	/**
	 * @param valeur The valeur to set.
	 */
	public void setValeur(String pvaleur){
		this.valeur = pvaleur;
	}
}