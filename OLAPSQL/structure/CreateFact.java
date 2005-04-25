package structure;

/**
 * Projet : OLAPSQL*PLUS
 * Auteur : 
 * 		Laure Bosse
 * 		Claire Fauroux
 */

import java.util.ArrayList;

/**
 * Classe représentant la commande CREATE d'un fait.
 */
public class CreateFact extends Create{

	/**
	 * @param nom
	 * @param type
	 */
	public CreateFact(String nom, int type, ArrayList attrs) {
		super(nom, type, attrs);
	}

	//liste de String
	private ArrayList connects;
	
	public void addConnections (ArrayList l){
		connects = l;
	}
	
	
	/**
	 * @return Returns the connects.
	 */
	public ArrayList getConnects() {
		return connects;
	}
	
	/**
	 * @param connects The connects to set.
	 */
	public void setConnects(ArrayList connects) {
		this.connects = connects;
	}
}
