
package ressources;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

//sous matrice carree de taille paire!
public class Matrice {
  
  	public static final String NORD_OUEST = "NO";
  	public static final String NORD_EST = "NE";
  	public static final String SUD_OUEST = "SO";
  	public static final String SUD_EST = "SE";

	private Symbole[][] mat;
	private int taille=0;
	
	//coordonnées du prochain symbole à inserer de haut en bas et de gauche a droite
	private int iCourant=0;
	private int jCourant=0;
	
	public Matrice (int t){
		if (t>0 /*TODO et t est pair*/)
		{	mat = new Symbole[t][t];
			taille=t;
		}
	}
	
	/**
	 * @return Returns the taille.
	 */
	public int getTaille() {
		return taille;
	}

	public Symbole get(int i, int j){
		if (i>=0 && j>=0 && i<mat.length && j<mat.length)
			return mat[i][j];
		return null;
	}
//	public void set(int i, int j, Symbole s){
//		if (i>=0 && j>=0 && i<mat.length && j<mat.length)
//		{
//			mat[i][j]=s;
//		}
//	}
	
	
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
			{	System.out.println("matrice pleine");
				iCourant=0;
				jCourant=0;
			}
	}
	/**
	 * retourne une sous matrice de mat , id 	1	2
	 * 											3	4
	 * @param id, l identifiant de la sous matrice voulue 1=NO, 2=NE,3=SO,4=SE
	 * @return Matrice, la sous matrice
	 */
	//ok
	public Matrice sousMatrice(int id){
			
			Matrice sMat=null;
			if (getTaille()!=0 && id>0 && id<5)
			{
				int staille = getTaille()/2;
				int ptEntreeI=0,ptEntreeJ=0;
				switch(id)
				{
					case 1:		break;
					case 2:		ptEntreeJ=staille;break;
					case 3:		ptEntreeI=staille;break;
					case 4:		ptEntreeI=staille;ptEntreeJ=staille;break;
					default:	System.out.println("erreur identification sous matrice");
								break;
				}
				
				sMat = new Matrice(staille);
				//im, jm index de la mat
				//is, js index de la Smat
				for (int im= ptEntreeI, is=0; im<ptEntreeI+(staille);im++,is++)
				{	for (int jm =ptEntreeJ,js=0;jm<ptEntreeJ+(staille);jm++,js++)
						sMat.ajoutSymbole(get(im,jm));
				}		
			}
			return sMat;
	}
	
	
	
	/**
	 * retourne true si tous ses Symboles ont la m valeur
	 * @return boolean
	 **/
	//ok
	public boolean isUnie(){
		
		boolean ok=false;
		if (getTaille()!=0)
		{
			String val  = get(0,0).getValeur();
			ok=true;
			for (int i = 0; i<getTaille() && ok;i++)
			{
				for (int j=0;j<getTaille() && ok ;j++ )
					if (val != get(i,j).getValeur())
						ok=false;
			}
		}
		return ok;
		
	}
	
	public void afficher(){
		for (int i = 0; i<mat.length;i++)
		{	
			//System.out.println("ligne "+i+": ");
			for (int j = 0; j<mat[i].length;j++)
				if ((mat[i][j])!=null)
					System.out.print((mat[i][j]).getValeur()+" ");
				else
					System.out.println("null");
			System.out.println("");
		}	
	}
	
	public static void main(String[] args) {
		Matrice m = new Matrice(4);
		
		m.ajoutSymbole(new Symbole("1"));
		m.ajoutSymbole(new Symbole("1"));
		m.ajoutSymbole(new Symbole("3"));
		m.ajoutSymbole(new Symbole("4"));
		m.ajoutSymbole(new Symbole("1"));
		m.ajoutSymbole(new Symbole("1"));
		m.ajoutSymbole(new Symbole("7"));
		m.ajoutSymbole(new Symbole("8"));
		m.ajoutSymbole(new Symbole("9"));
		m.ajoutSymbole(new Symbole("10"));
		m.ajoutSymbole(new Symbole("11"));
		m.ajoutSymbole(new Symbole("12"));
		m.ajoutSymbole(new Symbole("13"));
		m.ajoutSymbole(new Symbole("14"));
		m.ajoutSymbole(new Symbole("15"));
		m.ajoutSymbole(new Symbole("16"));
		
		
		
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
		
		//ok avec les 4 identificateurs
		Matrice m2 = m.sousMatrice(1);
		System.out.println("taille m2 "+m2.getTaille());
		m2.afficher();
		
		System.out.println("m unie? "+m.isUnie());
		System.out.println("m2 unie? "+m2.isUnie());
		
	}
	
	/**
	 * @return Returns the mat.
	 */
	public Symbole[][] getMat() {
		return mat;
	}
}


