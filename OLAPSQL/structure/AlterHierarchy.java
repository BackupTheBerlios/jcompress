/*
 * Created on Feb 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package structure;

import structure.types.Hierarchy;

/**
 * @author m1isi17
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AlterHierarchy extends Alter{


	public static final int ADD_HIERARCHY = 4;
	public static final int DROP_HIERARCHY = 5;	//hierarchys vide ou null
	
	private Hierarchy hierarchy =null;
	//attributs : devient null	
	
	/**
	 * @param nom
	 * @param type
	 */
	public AlterHierarchy(String nom, int type) {
		super(nom, type);
		attributs=null;
	}
	
	public AlterHierarchy(String nom, int type, int alter) {
		super(nom, type);
		alteration = alter;
		attributs=null;
	} 
	
	public void ajoutHierarchy (Hierarchy h){
		hierarchy = h;
	}
	
	
	/* (non-Javadoc)
	 * @see structure.Commande#afficher()
	 */
	public void afficher() {
		super.afficher();
		
		switch (alteration)
		{
			case ADD_HIERARCHY: System.out.println("alteration ADD_HIERARCHY");
						break;
			case DROP_HIERARCHY: System.out.println("alteration DROP_HIERARCHY");
			break;
			default:
		}
		if (hierarchy !=null)
			hierarchy.afficher();
		
	}
	/**
	 * @return Retourne la valeur de l'attribut hierarchy.
	 */
	public Hierarchy getHierarchy(){
		return hierarchy;
	}
	/**
	 * @param initialse hierarchy avec pHierarchy.
	 */
	public void setHierarchy(Hierarchy pHierarchy){
		hierarchy = pHierarchy;
	}
}
