
package ressources;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Matrice {
  
  	public static final String NORD_OUEST = "NO";
  	public static final String NORD_EST = "NE";
  	public static final String SUD_OUEST = "SO";
  	public static final String SUD_EST = "SE";

	public Symbole[][] mat;
	
	//coordonnées du prochain symbole à inserer de haut en bas et de gauche a droite
	private int iCourant=0;
	private int jCourant=0;
	private int taille;
	
	public Matrice (int t){
	  taille = t;
		if (t>0)
			mat = new Symbole[t][t];
	}
	
	public Symbole get(int i, int j){
		if (i>=0 && j>=0 && i<mat.length && j<mat.length)
			return mat[i][j];
		return null;
	}
	
	public int getTaille(){
	  return taille;
	}
	
	/**
	 * Ajoute la matrice m a la position position.
	 */
	public void ajoutMatrice(Matrice m, String position){
	  if(m.getTaille()*2 == taille){
	    int i = 0;
	    int j = 0;
	    if(position.equals(NORD_EST)){
	      j = taille / 2;
	    }
	    if(position.equals(SUD_OUEST)){
	      i = taille / 2;
	    }
	    if(position.equals(SUD_EST)){
	      i = taille / 2;
	      j = taille / 2;
	    }
	    
	    for(int x = 0 ; x < m.getTaille() ; x++){
	      for(int y = 0 ; y < m.getTaille() ; y++){
	        mat[x+i][y+j] = m.get(x,y);
	      }
	    }
	  }
	}
	
	//ajoute un symbole a la suite
	public void ajoutSymbole(Symbole s){
		mat[iCourant][jCourant] = s;
		
		if (jCourant+1<mat.length)
		{
			jCourant++;
		}
		else if (iCourant+1<mat.length)
			{
				//nouvelle ligne
				jCourant=0;
				iCourant++;
			}
			else
			{	//System.out.println("matrice pleine");
				iCourant=0;
				jCourant=0;
			}
	}
	
	public void afficher(){
		for (int i = 0; i<mat.length;i++)
		{
		  for (int j = 0; j<mat[i].length;j++)
				System.out.print((mat[i][j]).getValeur()+" ");
		  System.out.println("");
		}
	}
	
	public static void main(String[] args) {
		Matrice m = new Matrice(2);
		
		m.ajoutSymbole(new Symbole("01"));
		m.ajoutSymbole(new Symbole("02"));
		m.ajoutSymbole(new Symbole("05"));
		m.ajoutSymbole(new Symbole("06"));
		
		Matrice m1 = new Matrice(4);
		m1.ajoutMatrice(m,NORD_OUEST);
		m.ajoutSymbole(new Symbole("03"));
		m.ajoutSymbole(new Symbole("04"));
		m.ajoutSymbole(new Symbole("07"));
		m.ajoutSymbole(new Symbole("08"));
		m1.ajoutMatrice(m,NORD_EST);
		m.ajoutSymbole(new Symbole("09"));
		m.ajoutSymbole(new Symbole("10"));
		m.ajoutSymbole(new Symbole("13"));
		m.ajoutSymbole(new Symbole("14"));
		m1.ajoutMatrice(m,SUD_OUEST);
		m.ajoutSymbole(new Symbole("11"));
		m.ajoutSymbole(new Symbole("12"));
		m.ajoutSymbole(new Symbole("15"));
		m.ajoutSymbole(new Symbole("16"));
		m1.ajoutMatrice(m,SUD_EST);
		//System.out.println(m.get(0,0).getValeur());
		System.out.println(m1.getTaille());
		m.afficher();
		m1.afficher();
		
		
	}
}


