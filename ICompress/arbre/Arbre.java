
package arbre;

import ressources.Fichier;
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
	  //racine = new GrisCompose(null);
	  Noeud courant = null;
	  Symbole car ;
	  String orientation = "";
	  if(fichier.nextSymbole().getType().equals(Symbole.P_OUVRANTE)){
	    racine = new GrisCompose(null,fichier);
	  }
/*	  while((car = fichier.nextSymbole()) != null){
	    System.out.println(car.getValeur());
	    if(car.getType().equals(Symbole.P_OUVRANTE)){
	      courant = new GrisCompose(courant);
	      //courant = ((GrisCompose) courant).getNO();
	      orientation = Matrice.NORD_OUEST;
	    }
	    if(car.getType().equals(Symbole.NOMBRE)){
	      Noeud tmp = new Couleur(courant,Integer.parseInt(car.getValeur()));
	      if(orientation.equals(Matrice.SUD_EST)){
	        ((GrisCompose) courant).setSE(tmp);
	        orientation = "";
	      }
	      if(orientation.equals(Matrice.SUD_OUEST)){
	        ((GrisCompose) courant).setSO(tmp);
	        orientation = Matrice.SUD_EST;
	      }
	      if(orientation.equals(Matrice.NORD_EST)){
	        ((GrisCompose) courant).setNE(tmp);
	        orientation = Matrice.SUD_OUEST;
	      }
	      if(orientation.equals(Matrice.NORD_OUEST)){
	        ((GrisCompose) courant).setNO(tmp);
	        orientation = Matrice.NORD_EST;
	      }
	    }
	    if(car.getType().equals(Symbole.P_FERMANTE)){
	      Noeud tmp = courant;
	      courant = courant.getPere();
	      if(courant != null){
	      if(((GrisCompose) courant).getSE() == tmp)
	        orientation = "";
	      if(((GrisCompose) courant).getSO() == tmp)
	        orientation = Matrice.SUD_EST;
	      if(((GrisCompose) courant).getNE() == tmp)
	        orientation = Matrice.SUD_OUEST;
	      if(((GrisCompose) courant).getNO() == tmp)
	        orientation = Matrice.NORD_EST;
	      }
	      else{
	        courant = tmp;
	      }
	    }
	  }*/
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
