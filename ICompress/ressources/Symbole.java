
package ressources;

/**
 * Classe représentant un symbole
 * */
public class Symbole {

	public static String P_OUVRANTE="P_OUVRANTE";
	public static String P_FERMANTE="P_FERMANTE";
	public static String NOMBRE="NOMBRE";
	
	private String type="";
	private String valeur="";
	
	/**
	 * constructeur
	 * @param symb
	 */
	public Symbole (String symb)
	{
		valeur = symb;
		
		if (valeur.equals("("))
			type = P_OUVRANTE;
		else if (valeur.equals(")"))
			type = P_FERMANTE;
			else type=NOMBRE;
	}	
	
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return Returns the valeur.
	 */
	public String getValeur() {
		return valeur;
	}
	/**
	 * @param valeur The valeur to set.
	 */
	public void setValeur(String valeur) {
		this.valeur = valeur;
	}
}
