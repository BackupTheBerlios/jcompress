/*
 * Created on Feb 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package structure.types;

/**
 * @author m1isi17
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Attribut{

	private final static String O_DATE = "date";
	private final static String O_VARCHAR = "varchar";
	private final static String O_NUMBER = "number";
	
	public final static int DATE = 0;
	public final static int VARCHAR = 1;
	public final static int NUMBER = 2;
	
	private String nom; 		// nom de l'attribut
	private int type; 		// "type" de l'attribut (date xx/xx/xxxx, varchar, number)
	private int taille; 	  	//varchar et number
	private int precision = 0; 	// number -->si reel ou non
	
	
	//type date
	public Attribut (int t){
		type = t;
	}
	
	//	type float
	public Attribut (int t, int ta, int p){
		type = t;
		taille = ta;
		precision= p;
	}
	
	//type varchar ou integer
	public Attribut (int t, int ta){
		type = t;
		taille = ta;
	}
	
	/**
	 * rajoute une precision a un float
	 *		-->le nombre de chiffres apres la virgule
	 * @param p, precision
	 */
	public void setPrecision(int p)
	{
		if (type == NUMBER)
		{	precision = p;
		}
	}
	
	public boolean isFloat ()
	{
		return (type == NUMBER && precision!=0);
	}
	/**
	 * @return Returns the nom.
	 */
	public String getNom() {
		return nom;
	}
	/**
	 * @param nom The nom to set.
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	/**
	 * @return Returns the precision.
	 */
	public int getPrecision() {
		return precision;
	}
	/**
	 * @return Returns the taille.
	 */
	public int getTaille() {
		return taille;
	}
	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}
	
	public void afficher(){
		System.out.println("nom attribut :"+getNom());
		switch (type)
		{
			case 0: System.out.println("type : "+O_DATE);
					break;
			case 1: System.out.println("type : "+O_VARCHAR +"("+getTaille()+")");
					break;
			case 2: System.out.println("type : "+O_NUMBER +"("+getTaille()+","+getPrecision()+")");
					break;
		}
	}
	
	public String toString(){
	    String st= getNom()+" ";
		switch (type)
		{
			case 0: st+=O_DATE;
					break;
			case 1: st+=O_VARCHAR +"("+getTaille()+")";
					break;
			case 2: st+=O_NUMBER +"("+getTaille()+","+getPrecision()+")";
					break;
		}
	    return st;
	    
	}
}
