/**
 * Date = 15/01/2005 
 * Project = ICompress 
 * File name = Noeud.java
 * @author Bosse Laure/Fauroux claire 
 * 
 * Ce projet permet la compression et la
 *         decompression de fichier PGM de type P5 et P2.
 */

package arbre;

import ressources.Matrice;

/**
 * Classe abstraite permettant un traitement generalise d'un noeud quelconque.
 */
public abstract class Noeud {

	private Noeud pere = null;

	/**
	 * Constructeur.
	 * @param p Pere du noeud.
	 */
	protected Noeud(Noeud p){
		pere = p;
	}

	/**
	 * @return Pere du Noeud.
	 */
	public Noeud getPere(){
		return pere;
	}

	/**
	 * @param pPere Pere du noeud.
	 */
	public void setPere(Noeud pPere){
		pere = pPere;
	}

	/**
	 * @return Retourne la profondeur de l'arbre.
	 */
	public abstract int getProfondeur();

	/**
	 * @return Construit la ligne codant l'arbre.
	 */
	public abstract String construireLigne();

	/**
	 * @return Construit la matrice correspondant à l'arbre.
	 */
	public abstract Matrice construireMatrice();

	/**
	 * Compte le nb de feuille contenu dans le noeud
	 * @return int, nb de feuille
	 */
	public abstract int grandeurNoeud();

}