package structure;

/**
 * Projet : OLAPSQL*PLUS
 * Auteur : 
 * 		Laure Bosse
 * 		Claire Fauroux
 */

import java.util.ArrayList;
import java.util.Iterator;

import structure.types.Attribut;
import structure.types.Hierarchy;

/**
 * Classe représentant la commande CREATE d'une dimension.
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
	
	/**
	 * @return Retourne la valeur de l'attribut hierarchys.
	 */
	public ArrayList getHierarchys(){
		return hierarchys;
	}
	
	/**
	 * @param initialse hierarchys avec pHierarchys.
	 */
	public void setHierarchys(ArrayList pHierarchys){
		hierarchys = pHierarchys;
	}
	
	/**
	 * true si le nom en parametre correspond au nom d'un attribut
	 * @param nom
	 * @return
	 */
	public boolean existAttributs(String nom){
		Iterator itAttributs = getAttributs().iterator();
		while(itAttributs.hasNext()){
			Attribut attribut = (Attribut) itAttributs.next();
			if(attribut.getNom().equals(nom)){
				return true;
			}
		}
		return false;
	}
}
