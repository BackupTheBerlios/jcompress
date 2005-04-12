/*
 * Created on Feb 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package structure;

import structure.types.predicat.Predicat;

/**
 * @author m1isi17
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
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
