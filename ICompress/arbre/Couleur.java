
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

	public static String NOIR="NOIR";
	public static String BLANC="BLANC";
	public static String GRIS="GRIS";
	
	private String couleur;
	
	/**
	 * @param p
	 */
	public Couleur(Noeud p, String c) {
		super(p);
		couleur = c;
	}
	
	public String getCouleur(){
	  return couleur;
	}
	
	public void setCouleur(String pCouleur){
	  couleur = pCouleur;
	}

  /* (non-Javadoc)
   * @see arbre.Noeud#construireLigne()
   */
  public String construireLigne ()
  {
    String ligne = couleur;
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
    m.ajoutSymbole(new Symbole(couleur));
    return m ;
  }
	
}
