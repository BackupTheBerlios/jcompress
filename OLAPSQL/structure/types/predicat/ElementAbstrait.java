
package structure.types.predicat;

/**
 * Projet : OLAPSQL*PLUS
 * Auteur : 
 * 		Laure Bosse
 * 		Claire Fauroux
 */

import java.util.ArrayList;

/**
 * Classe mère des feuilles et des noeuds de l'arbre représentant un prédicat.
 */
public abstract class ElementAbstrait {
	
	protected Liaison pere=null;
	
	protected ElementAbstrait(Liaison pere){
		this.pere = pere;
	}
	
	
	
	/**
	 * @return Returns the pere.
	 */
	public Liaison getPere() {
		return pere;
	}
	/**
	 * @param pere The pere to set.
	 */
	public void setPere(Liaison pere) {
		this.pere = pere;
	}
	public abstract void afficher ();
	public abstract String toString ();

	/**
	 * Retourne la liste des jointures.
	 * @return
	 * ArrayList
	 */
	public abstract ArrayList getJointures();
	
	/**
	 * retourne l'expression SQL du predicat.
	 * @return
	 * String
	 */
	public abstract String getSQL();



	/**
	 * retourne le requete SQL pour le select du moteur
	 * @return
	 * String
	 */
	public abstract String getSQLMoteur(String table);
}
