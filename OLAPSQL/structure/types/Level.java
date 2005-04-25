package structure.types;

/**
 * Projet : OLAPSQL*PLUS
 * Auteur : 
 * 		Laure Bosse
 * 		Claire Fauroux
 */

import java.util.ArrayList;

/**
 * Classe représentant les level des hierarchies.
 */
public class Level{
	private String nom; // nom du level
	//liste de String
	private ArrayList attributs = new ArrayList();
	
	
	public Level(String n){
		nom=n;
	}
	
	public void ajoutAttFaible(String att){
		attributs.add(att);
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
	/**
	 * @return Returns the nom.
	 */
	public String getNom() {
		return nom;
	}
	
	public void afficher (){
		
		System.out.println("Level "+getNom());
		for (int i=0; i<attributs.size();i++)
		{
				System.out.println("att faible : "+(String)attributs.get(i));
		}
	
	}
}
