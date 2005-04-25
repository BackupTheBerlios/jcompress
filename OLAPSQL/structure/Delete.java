package structure;

/**
 * Projet : OLAPSQL*PLUS
 * Auteur : 
 * 		Laure Bosse
 * 		Claire Fauroux
 */

import structure.types.predicat.Predicat;

/**
 * Classe représentant la commande DELETE.
 */
public class Delete extends Commande{


	// peut etre null
	private Predicat predicat=null;
	
	/**
	 * @param nom
	 * @param type
	 */
	public Delete(String nom, int type, Predicat p ) {
		super(nom, type);
		predicat = p;
	}

	/* (non-Javadoc)
	 * @see structure.Commande#afficher()
	 */
	public void afficher() {
		super.afficher();
		if (predicat !=null)
		    predicat.afficher();
	}
	
	/**
	 * @return Retourne la valeur de l'attribut predicat.
	 */
	public Predicat getPredicat(){
		return predicat;
	}
}
