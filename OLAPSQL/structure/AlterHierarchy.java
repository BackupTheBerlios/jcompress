package structure;

/**
 * Projet : OLAPSQL*PLUS
 * Auteur : 
 * 		Laure Bosse
 * 		Claire Fauroux
 */

import structure.types.Hierarchy;

/**
 * Classe représentant la commande ALTER concernant une hierarchie d'une dimension.
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
