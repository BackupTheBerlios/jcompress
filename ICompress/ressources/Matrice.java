
package ressources;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Matrice {

	public Symbole[][] mat;
	
	//coordonnées du prochain symbole à inserer de haut en bas et de gauche a droite
	private int iCourant=0;
	private int jCourant=0;
	
	public Matrice (int t){
	
		if (t>0)
			mat = new Symbole[t][t];
	}
	
	public Symbole get(int i, int j){
		if (i>=0 && j>=0 && i<mat.length && j<mat.length)
			return mat[i][j];
		return null;
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
		{	for (int j = 0; j<mat[i].length;j++)
				System.out.print((mat[i][j]).getValeur()+" ");
			System.out.println("");
		}	
	}
	
	public static void main(String[] args) {
		Matrice m = new Matrice(2);
		
		m.ajoutSymbole(new Symbole("1"));
		m.ajoutSymbole(new Symbole("2"));
		m.ajoutSymbole(new Symbole("3"));
		m.ajoutSymbole(new Symbole("4"));
		
		//System.out.println(m.get(0,0).getValeur());
		m.afficher();
		
		
	}
}


