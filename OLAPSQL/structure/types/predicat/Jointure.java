
package structure.types.predicat;

import java.util.ArrayList;

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

	/* (non-Javadoc)
	 * @see structure.types.predicat.ElementAbstrait#getJointures()
	 */
	public ArrayList getJointures(){
		ArrayList liste = new ArrayList();
		liste.add(this);
		return liste;
	}
	
	/**
	 * @return Retourne la valeur de l'attribut expr1.
	 */
	public String getExpr1(){
		return expr1;
	}
	
	/**
	 * @param initialse expr1 avec pExpr1.
	 */
	public void setExpr1(String pExpr1){
		expr1 = pExpr1;
	}
	
	/**
	 * @return Retourne la valeur de l'attribut expr2.
	 */
	public String getExpr2(){
		return expr2;
	}
	
	/**
	 * @return Retourne la valeur de l'attribut operateur.
	 */
	public String getOperateur(){
		return operateur;
	}

    /* (non-Javadoc)
     * @see structure.types.predicat.ElementAbstrait#toString()
     */
    public String toString() {
        
        //remplacement de la virgule sur premier appel des reels
        if (expr2.contains(","))
            expr2=expr2.replace(',','.'); 
        
        return  (" "+expr1+" "+operateur+" "+expr2);	
    	
    }
}
