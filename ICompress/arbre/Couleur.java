
package arbre;

import ressources.Matrice;
import ressources.Symbole;
import arbre.Noeud;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public final class Couleur extends Noeud {

	private int valeur;
	
	/**
	 * @param p
	 */
	public Couleur(Noeud p, int v) {
		super(p);
		valeur = v;
	}
	
  /**
   * @return Retourne la valeur de l'attribut valeur.
   */
  public int getValeur ()
  {
    return valeur ;
  }
  
  /**
   * @param initialse valeur avec pValeur.
   */
  public void setValeur (int pValeur)
  {
    valeur = pValeur ;
  }

  /* (non-Javadoc)
   * @see arbre.Noeud#construireLigne()
   */
  public String construireLigne ()
  {
    String ligne = String.valueOf(valeur);
    return ligne ;
  }

  /* (non-Javadoc)
   * @see arbre.Noeud#getProfondeur()
   */
  public int getProfondeur ()
  {
    return 0 ;
  }

  /* (non-Javadoc)
   * @see arbre.Noeud#construireMatrice(ressources.Matrice)
   */
  public Matrice construireMatrice ()
  {
    Matrice m = new Matrice(1);
    m.ajoutSymbole(new Symbole(String.valueOf(valeur)));
    return m ;
  }

/**retourne le nb de feuille...c une feuille donc 1
 * @return int=1
 */
public int grandeurNoeud() {
	return 1;
}
	
}
