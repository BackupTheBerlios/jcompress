
package arbre;

import ressources.FichierSource;
import ressources.Matrice;
import ressources.Symbole;
import arbre.Noeud;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Arbre {
	
	private Noeud racine = null;
	private int taille;

	public Arbre(Noeud n){
	  racine = n;
	}
	
	public Arbre(Noeud n, int t){
	  racine = n;
	  taille = t;
	}
	
	/**
	 * Construire arbre a partir expression du fichier txt
	 * @param nomFichier
	 * @throws Exception
	 */
	public Arbre(String nomFichier){
	  // TODO
	  // new fichierSource(nomFichier)
	  // taille = next()
	  // nextSymbole;
	  // null qd fin fichier
	  FichierSource fichier = new FichierSource(nomFichier);
	  taille = Integer.parseInt(fichier.next());
	  if(fichier.nextSymbole().getType().equals(Symbole.P_OUVRANTE)){
	    racine = new GrisCompose(null,fichier);
	  }
	}
	
	/**
	 * 
	 * @return
	 * Matrice
	 */
	public Matrice construireImage()
	{
	  // TODO prendre en compte taille
	  //Matrice mat = new Matrice(racine.getProfondeur());
	  return racine.construireMatrice();
	}
	
	public String construireLigne()
	{
	  return taille+" "+racine.construireLigne();
	}
}
