
package structure.types.predicat;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Jointure extends ElementAbstrait {
	
	public static final String EGAL = "=";
	public static final String NEGAL = "<>";
	public static final String SUP = ">";
	public static final String SUPEGAL = ">=";
	public static final String  INF= "<";
	public static final String INFEGAL = "<=";
	
	private String operateur = null;
	private String expr1=null;
	private String expr2 = null;
	
	public  Jointure (Liaison pere,String e1, String e2, String op)
	{
		super(pere);
		expr1 = e1;
		expr2 = e2;
		operateur = op;
	}

	/* (non-Javadoc)
	 * @see structure.types.predicat.ElementAbstrait#afficher()
	 */
	public void afficher() {
		System.out.println ("\texpress : "+expr1+operateur+expr2);	
	}
	
	
}
