/*
 * Created on Feb 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package structure;

import java.util.ArrayList;

import structure.types.Attribut;
import structure.types.Hierarchy;

/**
 * @author m1isi17
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CreateDimension extends Create{

	//	liste de Hierarchy
	private ArrayList hierarchys = new ArrayList();
	
	/**
	 * @param nom
	 * @param type
	 */
	public CreateDimension(String nom, int type, ArrayList attrs) {
		super(nom, type, attrs);
		// TODO Auto-generated constructor stub
	}

	
	public void ajoutHierarchy (Hierarchy h){
		
		hierarchys.add(h);
	}
	
	/* (non-Javadoc)
	 * @see structure.Commande#afficher()
	 */
	public void afficher() {
		super.afficher();
		for (int i=0; i<hierarchys.size();i++)
		{
			Hierarchy a = (Hierarchy)hierarchys.get(i);
			a.afficher();
		}
	}
}
