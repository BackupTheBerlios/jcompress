
package structure.types.predicat;

import java.util.ArrayList;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Liaison extends ElementAbstrait {
	
	public final static String AND = "and";
	public final static String OR  = "or";
	
	//liste de d'Element abstraits
	private ArrayList preds = new ArrayList();
	private String liaison = "";
	
	
	public Liaison (Liaison pere,String l){
		super(pere);
		liaison = l;
	}
	
	public Liaison (Liaison pere,String l, ElementAbstrait p1){
		super(pere);
		liaison = l;
		ajoutElmt(p1);
	}
	
	public void ajoutElmt(ElementAbstrait elmt){
		preds.add(elmt);
	}
	
	public static void main(String[] args) {
	}

	/* (non-Javadoc)
	 * @see structure.types.predicat.ElementAbstrait#afficher()
	 */
	public void afficher() {
		System.out.println("Noeud (");
		if (preds !=null)
			for (int i=0; i<preds.size();i++)
			{
					ElementAbstrait a = (ElementAbstrait)preds.get(i);
					a.afficher();
					if (i<preds.size()-1)
						System.out.println("\t"+liaison);
			}
		System.out.println(")");
	}
	
	
}
