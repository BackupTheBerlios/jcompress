
package ressources;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class Fichier {
	
  public static final String P5 = "P5";
  public static final String P2 = "P2";
  
	protected String filename;
	
	public Fichier(String name){
		filename = name;
	}
	
	public abstract void fermer ();

}
