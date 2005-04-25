package structure.types;

/**
 * Projet : OLAPSQL*PLUS
 * Auteur : 
 * 		Laure Bosse
 * 		Claire Fauroux
 */

import java.util.ArrayList;

/**
 * Classe représentant les hiérarchies des dimensions.
 */
public class Hierarchy{
	private String nom; // nom de hierarchy
	//liste de levels
	private ArrayList levels;
	
	
	public Hierarchy(String n, ArrayList levs){
		nom=n;
		levels = levs;
	}
	
	public void ajoutLevel(Level lev){
		getLevels().add(lev);
	}
	
	
	/**
	 * @return Returns the levels.
	 */
	public ArrayList getLevels() {
		return levels;
	}
	/**
	 * @param levels The levels to set.
	 */
	public void setLevels(ArrayList levels) {
		this.levels = levels;
	}
	/**
	 * @return Returns the nom.
	 */
	public String getNom() {
		return nom;
	}
	
	public void afficher (){
		
		System.out.println("Hierarchy "+getNom());
		if (levels !=null)
			for (int i=0; i<levels.size();i++)
			{
					Level a = (Level)levels.get(i);
					a.afficher();
			}
	
	}

}
