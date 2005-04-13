/*
 * Created on Feb 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package structure;

import java.util.ArrayList;
import structure.types.Attribut;

/**
 * @author m1isi17
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class Create extends Commande{

	//	liste d Attribut
	protected ArrayList attributs;
	
	/**
	 * @param nom
	 * @param type
	 */
	public Create(String nom, int type, ArrayList attrs) {
		super(nom, type);
		attributs = attrs;
	}

	
	
	
	/* (non-Javadoc)
	 * @see structure.Commande#afficher()
	 */
	public void afficher() {
		super.afficher();
		afficherAttributs();
	}
	public void afficherAttributs (){
		
		for (int i=0; i<attributs.size();i++)
		{
			Attribut a = (Attribut)attributs.get(i);
			a.afficher();
		}
	}
	

	/**
	 * @return Returns the attributs.
	 */
	public ArrayList getAttributs() {
		return attributs;
	}
	/**
	 * @param attributs The attributs to set.
	 */
	public void setAttributs(ArrayList attributs) {
		this.attributs = attributs;
	}
}
