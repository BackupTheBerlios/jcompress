
package ressources;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Symbole {

	static String P_OUVRANTE="P_OUVRANTE";
	static String P_FERMANTE="P_FERMANTE";
	static String NOMBRE="NOMBRE";
	static String OTHER="OTHER";
	
	private String type="";
	private String valeur="";
	
	//construit soit ( soit ) soit nombre
	//ajouter le type other
	public Symbole (String symb)
	{
		valeur = symb;
		
		if (valeur =="(")
			type = P_OUVRANTE;
		else if (valeur ==")")
			type = P_FERMANTE;
			else type=NOMBRE;
	}	
	
	public static void main(String[] args) {
		Symbole s = new Symbole("255");
		Symbole ss = new Symbole(")");
		Symbole sss = new Symbole("(");
		
		
		System.out.println("symbole : "+ s.getValeur()+" de type "+s.getType());
		System.out.println("symbole : "+ ss.getValeur()+" de type "+ss.getType());
		System.out.println("symbole : "+ sss.getValeur()+" de type "+sss.getType());
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
