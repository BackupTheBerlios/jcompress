
package arbre;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public final class Couleur extends Noeud {

	static String NOIR="NOIR";
	static String BLANC="BLANC";
	static String GRIS="GRIS";
	
	private String couleur;
	
	/**
	 * @param p
	 */
	public Couleur(Noeud p, String c) {
		super(p);
		couleur = c;
	}
	
	
	
	
	
}
