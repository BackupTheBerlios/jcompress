
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
public final class GrisCompose extends Noeud {

	private Noeud NO=null;
	private Noeud NE=null;
	private Noeud SO=null;
	private Noeud SE=null;
	
	/**
	 * @param p
	 */
	public GrisCompose(Noeud p) {
		super(p);
	}

	public GrisCompose(Noeud p, Noeud no, Noeud ne, Noeud so, Noeud se){
	  super(p);
	  NO = no;
	  NE = ne;
	  SO = so;
	  SE = se;
	}
	
	public GrisCompose(Noeud p, FichierSource f){
	  super(p);
	  Symbole car;
	  String orientation = Matrice.NORD_OUEST;
	  while(!orientation.equals("")){
	    car = f.nextSymbole();
	    if(car == null){
	      orientation = "";
	    }
	    else{
		    if(car.getType().equals(Symbole.P_OUVRANTE)){
		      Noeud tmp = new GrisCompose(this,f);
		      orientation = ajouteNoeud(tmp, orientation);
		    }
		    if(car.getType().equals(Symbole.NOMBRE)){
		      Noeud tmp = new Couleur(this,Integer.parseInt(car.getValeur()));
		      orientation = ajouteNoeud(tmp, orientation);
		    }
	    }
	  }
	}
	
	private String ajouteNoeud(Noeud tmp, String s){
      if(s.equals(Matrice.SUD_EST)){
        s = "";
        SE = tmp;
      }
      if(s.equals(Matrice.SUD_OUEST)){
        s = Matrice.SUD_EST;
        SO = tmp;
      }
      if(s.equals(Matrice.NORD_EST)){
        s = Matrice.SUD_OUEST;
        NE = tmp;
      }
      if(s.equals(Matrice.NORD_OUEST)){
        s = Matrice.NORD_EST;
        NO = tmp;
      }
	  return s;
	}
	
	public Noeud getNO(){
	  return NO;
	}
	
	public Noeud getNE(){
	  return NE;
	}
	
	public Noeud getSO(){
	  return SO;
	}
	
	public Noeud getSE(){
	  return SE;
	}
	
	public void setNO(Noeud pNo){
	  NO = pNo;
	}
	
	public void setNE(Noeud pNe){
	  NE = pNe;
	}
	
	public void setSO(Noeud pSo){
	  SO = pSo;
	}
	
	public void setSE(Noeud pSe){
	  SE = pSe;
	}

  /* (non-Javadoc)
   * @see arbre.Noeud#construireLigne()
   */
  public String construireLigne ()
  {
    String ligne = "( ";
    ligne += NO.construireLigne();
    ligne += " ";
    ligne += NE.construireLigne();
    ligne += " ";
    ligne += SO.construireLigne();
    ligne += " ";
    ligne += SE.construireLigne();
    ligne += " )";
    return ligne;
  }

  /* (non-Javadoc)
   * @see arbre.Noeud#getProfondeur()
   */
  public int getProfondeur ()
  {
    int max = NO.getProfondeur();
    int iNe = NE.getProfondeur();
    int iSo = SO.getProfondeur();
    int iSe = SE.getProfondeur();
    
    if(iNe > max)
      max = iNe;
    if(iSo > max)
      max = iSo;
    if(iSe > max)
      max = iSe;
    max ++;
    
    return max ;
  }

  /* (non-Javadoc)
   * @see arbre.Noeud#construireMatrice(ressources.Matrice)
   */
  public Matrice construireMatrice ()
  {
    Matrice matNo = NO.construireMatrice();
    Matrice matNe = NE.construireMatrice();
    Matrice matSo = SO.construireMatrice();
    Matrice matSe = SE.construireMatrice();
    
    int taille = matNo.getTaille();
    if(taille < matNe.getTaille())
      taille = matNe.getTaille();
    if(taille < matSo.getTaille())
      taille = matSo.getTaille();
    if(taille < matSe.getTaille())
      taille = matSe.getTaille();
    
    Matrice mat = new Matrice(taille*2);
    mat.ajoutMatrice(agrandiMatrice(matNo,taille),Matrice.NORD_OUEST);
    mat.ajoutMatrice(agrandiMatrice(matNe,taille),Matrice.NORD_EST);
    mat.ajoutMatrice(agrandiMatrice(matSo,taille),Matrice.SUD_OUEST);
    mat.ajoutMatrice(agrandiMatrice(matSe,taille),Matrice.SUD_EST);
    return mat ;
  }
  
  /**
   * Agrandi la matrice.
   * @param mat Matrice a agrandir.
   * @param taille Taille de la matrice du resultat.
   * @return Matrice agrandi
   */
  public Matrice agrandiMatrice(Matrice pMat, int pTaille){
    Matrice mat = pMat;
    while(pTaille > mat.getTaille()){
      Matrice temp = new Matrice(mat.getTaille()*2);
      temp.ajoutMatrice(mat, Matrice.NORD_OUEST);
      temp.ajoutMatrice(mat, Matrice.NORD_EST);
      temp.ajoutMatrice(mat, Matrice.SUD_OUEST);
      temp.ajoutMatrice(mat, Matrice.SUD_EST);
      mat = temp;
    }
    return mat;
  }
}
