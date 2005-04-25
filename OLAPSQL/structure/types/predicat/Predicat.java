package structure.types.predicat;

/**
 * Projet : OLAPSQL*PLUS
 * Auteur : 
 * 		Laure Bosse
 * 		Claire Fauroux
 */

import java.util.ArrayList;

/**
 * Racine de l'arbre représentant un prédicat.
 */
public class Predicat {
	
	protected ElementAbstrait racine= null;
	
	public Predicat (){}
	public Predicat (ElementAbstrait rac){
	racine = rac;	
	}
	
	
	

	/**
	 * @return Returns the racine.
	 */
	public ElementAbstrait getRacine() {
		return racine;
	}
	/**
	 * @param racine The racine to set.
	 */
	public void setRacine(ElementAbstrait racine) {
		this.racine = racine;
	}
	
	public void afficher (){
		
		if (racine !=null) racine.afficher();
	}
	
	public String toString (){
		
		if (racine !=null) 
		    return racine.toString();
		return null;
	}
	
	/**
	 * Retourne la liste des jointures de l'arbre predicat.
	 * @return
	 * ArrayList
	 */
	public ArrayList getJointures(){
		ArrayList liste = new ArrayList();
		liste.addAll(racine.getJointures());
		return liste;
	}
	
	public String getSQL(){
		return racine.getSQL();
	}
	
	public String getSQLMoteur(String table){
		return racine.getSQLMoteur(table);
	}
}
