/*
 * Created on Feb 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package structure;

/**
 * @author m1isi17
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

// TODO protected/private
// TODO ArrayList /autres
//ca gere pas les doubles quot dans une valeur

public abstract class Commande{
	
	public static final int DIMENSION = 0;
	public static final int FACT = 1;


	protected String nom;
	protected int type;
	
	/**
	 * @param nom
	 * @param type
	 */
	public Commande(String nom, int type) {
		super();
		this.nom = nom;
		this.type = type;
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
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	public void afficher (){
		String typ;
		if (getType() ==0) typ="DIMENSION";
			else typ="FACT";
		System.out.println("commande "+typ+" de nom "+getNom());
	}
	
}

