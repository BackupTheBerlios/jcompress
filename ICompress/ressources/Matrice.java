
package ressources;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Gestion d'une matrice carrée de 2^n de Symbole
 */
public class Matrice {
  
  	public static final String NORD_OUEST = "NO";
  	public static final String NORD_EST = "NE";
  	public static final String SUD_OUEST = "SO";
  	public static final String SUD_EST = "SE";

	private Symbole[][] mat=null;
	private int taille=0;
	
	//coordonnées du prochain symbole à inserer de haut en bas et de gauche a droite
	private int iCourant=0;
	private int jCourant=0;
	
	/**
	 * constructeur
	 * @param t, taille de la matrice
	 */
	public Matrice (int t){
		if (t>0)
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

	/**
	 * retourne le symbole a la ligne i colonne j de la matrice
	 * @param i, ligne
	 * @param j, colonne
	 * @return Symbole
	 */
	public Symbole get(int i, int j){
		if (i>=0 && j>=0 && i<mat.length && j<mat.length)
			return mat[i][j];
		return null;
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
	
	/**
	 * ajoute le symbole s a la suite dans la matrice, au debut si pleine
	 * @param s, le Symbole a ajouter
	 */
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
	
	/**
	 * retourne une sous matrice de mat , id 	1	2
	 * 											3	4
	 * @param id, l identifiant de la sous matrice voulue 1=NO, 2=NE,3=SO,4=SE
	 * @return Matrice, la sous matrice
	 */
	public Matrice sousMatrice(int id){
			
			Matrice sMat=null;
			if (getTaille()==1)
				return this;
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
	public int isUnie(){
		
		boolean ok=false;
		String val="-1";
		if (getTaille()!=0)
		{
			val  = get(0,0).getValeur();
			ok=true;
			for (int i = 0; i<getTaille() && ok;i++)
			{
				for (int j=0;j<getTaille() && ok ;j++ )
					if (!(val.equals(get(i,j).getValeur())))
						ok=false;
			}
		}
		if (ok)
			return Integer.parseInt(val);
		else
			return -1;
		
	}
	
	/**
	 * construit une map contenant les couleurs et leur occurrence dans mat
	 * @return Hashmap
	 */
	public HashMap nbSymbDiff(){
		HashMap somme=new HashMap();		
		
		for (int i = 0; i<getTaille();i++)
		{
			for (int j=0;j<getTaille() ;j++ )
			{	Integer tmpK = Integer.valueOf(get(i,j).getValeur());
				Integer tmpV=null;
				if (somme.containsKey(tmpK))
					tmpV= new Integer((((Integer)somme.get(tmpK)).intValue()+1));
				else
					tmpV=new Integer(1);
				somme.put(tmpK,tmpV);
			}	
		}
		return somme;
		
	}
	
	
	/**
	 * affiche la matrice sur la console
	 */
	public void afficher(){
		for (int i = 0; i<mat.length;i++)
		{	
			for (int j = 0; j<mat[i].length;j++)
				if ((mat[i][j])!=null)
					System.out.print((mat[i][j]).getValeur()+" ");
				else
					System.out.println("null");
			System.out.println("");
		}	
	}
	
	/**
	 * @return Returns the mat.
	 */
	public Symbole[][] getMat() {
		return mat;
	}
}


