
package ressources;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import arbre.Arbre;
import arbre.Couleur;
import arbre.GrisCompose;
import arbre.Noeud;

/**
 * Gestion d'une image en forme de matrice
 * encapsulation de la matrice associee
 * import/export fichiers
 * import/export arbre
 */
public class Image {
	
	private Matrice mat;
	//private int taille;
	private int nvGrisMax = 255;
	
	/**construteur
	 * suppose un fichier non vide
	 * et la taille annoncée est concordante avec le fichier sinon-->NullPointerException
	 * @param filename, nom du fichier absolu contenant une image P2/P5
	 */
	public Image (String filename){
		
		String type;
		Symbole symb=null;
		boolean EOF=false;
		
		//par defaut, c un type P2 quon construit afin de lire le type
		FichierSource f = new FichierSource(filename);

		if ((type=f.next()).equals(Fichier.P5))
		{		f=null;
				f=new FichierSourceBinaire(filename);
				type = f.next();
		}
		//remplissage des attribussts de l image
		f.next();										//1ere taille
		mat = new Matrice(Integer.parseInt(f.next()));	//2e taille, identiques
		String tmp = f.next();
		nvGrisMax = Integer.parseInt(tmp);
		
		System.out.println("taille mat "+getMat().getTaille());
		System.out.println("nvG "+nvGrisMax);
		
		
		//remplissage de la matrice associée
		int nb_mot=0;
		for (int i=0; i<getTaille() && !(EOF);i++)
			for (int j=0; j<getTaille() && !(EOF);j++)
				if ((symb=f.nextSymbole()) ==null)
				{	System.out.println("i,j :"+i+" "+j);
					EOF = true;
				}
				else
				{
					nb_mot++;
					getMat().ajoutSymbole(symb);
				}
	}
	
	/**
	 * constructeur avec une matrices
	 * @param m, Matrice a associer
	 */
	public Image(Matrice m){
			setMat(m);
		}
	
	//------------------------------------------
	//IMPORT/EXPORT ARBRE
	//------------------------------------------

	/**constructeur, rien si a null
	 * @param a, Arbre
	 */
	public Image(Arbre a){
		if (a!=null)
			setMat(a.construireMatrice());
	}
	
	/**
	 * construit un arbre a partir de l image this
	 * @return Arbre, l arbre construit
	 */
	public Arbre construireArbre(){
		return new Arbre (construireNoeud(null), getTaille());
	}
	
	/**
	 * fonction recursive de construction descendante des noeuds
	 * @param p, noeud courant qui est le pere des prochains
	 * @return Noeud
	 */
	private Noeud construireNoeud(Noeud p){
		  
		if (getMat()!=null)
		{
			if (getMat().getTaille()>1)
			{	
				Image im1 = new Image(getMat().sousMatrice(1));
				Image im2 = new Image(getMat().sousMatrice(2));
				Image im3 = new Image(getMat().sousMatrice(3));
				Image im4 = new Image(getMat().sousMatrice(4));
				
				GrisCompose g = new GrisCompose(p);
				g.setNO(im1.construireNoeud(g));
				g.setNE(im2.construireNoeud(g));
				g.setSO(im3.construireNoeud(g));
				g.setSE(im4.construireNoeud(g));
				return g;
				
				}
			else{
				return new Couleur(p,Integer.parseInt(getMat().get(0,0).getValeur()));
			}
		}	
		return null;
		}
	
	/**
	 * construit un arbre sans perte a partir de l image this
	 * @return Arbre, l arbre construit
	 */
	public Arbre construireArbreCompresseSansPerte(){
		return new Arbre (construireNoeudSansPerte(null), getTaille());
	}
	
	/**
	 * fonction recursive de construction descendante des noeuds compresse sans perte
	 * @param p, noeud courant qui est le pere des prochains
	 * @return Noeud
	 */
	private Noeud construireNoeudSansPerte(Noeud p){
		if (getMat()!=null)
		{
			if (getMat().getTaille()>1)
			{	
				Image im1 = new Image(getMat().sousMatrice(1));
				Image im2 = new Image(getMat().sousMatrice(2));
				Image im3 = new Image(getMat().sousMatrice(3));
				Image im4 = new Image(getMat().sousMatrice(4));
				
				//teste l unicite
				int val1 = im1.getMat().isUnie();
				int val2 = im2.getMat().isUnie();
				int val3 = im3.getMat().isUnie();
				int val4 = im4.getMat().isUnie();
				
				if (val1!=-1 && val2!=-1 && val3!=-1 && val4 !=-1 && val1==val2 && val2==val3 && val3==val4)
					return new Couleur(p, val1);
				
				GrisCompose g = new GrisCompose(p);
				g.setNO(im1.construireNoeudSansPerte(g));
				g.setNE(im2.construireNoeudSansPerte(g));
				g.setSO(im3.construireNoeudSansPerte(g));
				g.setSE(im4.construireNoeudSansPerte(g));
				return g;
			}
			else
				return new Couleur(p,Integer.parseInt(getMat().get(0,0).getValeur()));	
		}
		return null;
	}
	/**
	 * construit un arbre compresse avec perte sur le taux a partir de l image this 
	 * @param taux, taux de sauvegarde de l 'image = 1 - taux de perte
	 * @return Arbre construit, null si taux>1 ou taux<0
	 */
	
	public Arbre construireArbreCompresseAvecPerte(float taux){

	if (taux>=0 && taux <= 1)
	  return new Arbre (construireNoeudAvecPerte(null,taux), getTaille());
	return null;
	}

	/**
	 * fonction recursive de construction descendante des noeuds compresse avec perte de 1-taux
	 * @param p, noeud courant qui est le pere des prochains 
	 * @param tx, taux de sauvegarde des couleurs
	 * @return Noeud
	 */
	private Noeud construireNoeudAvecPerte(Noeud p, float tx)
	{
		if (getMat()!=null)
		{
			
			if (getMat().getTaille()>1)
			{
				
				//deb perte
				int occTX  = (int)(getTaille()*getTaille()*tx);
				//System.out.println("occ demandees "+occTX);
				HashMap cpt = getMat().nbSymbDiff();
				
				Integer key = (Integer)maxValue(cpt);
				if (((Integer)cpt.get(key)).intValue() >= occTX)
					return new Couleur(p, key.intValue());
				//end perte
				
				Image im1 = new Image(getMat().sousMatrice(1));
				Image im2 = new Image(getMat().sousMatrice(2));
				Image im3 = new Image(getMat().sousMatrice(3));
				Image im4 = new Image(getMat().sousMatrice(4));
				
				GrisCompose g = new GrisCompose(p);
				g.setNO(im1.construireNoeudAvecPerte(g,tx));
				g.setNE(im2.construireNoeudAvecPerte(g,tx));
				g.setSO(im3.construireNoeudAvecPerte(g,tx));
				g.setSE(im4.construireNoeudAvecPerte(g,tx));
				return g;
			}
			else
				return new Couleur(p,Integer.parseInt(getMat().get(0,0).getValeur()));	
		}
		return null;
	}
		
		/**
		 * retourne la cle de la plus grande valeur de hm
		 * @return Object, cle de la valeur max
		 */
		private Object maxValue (HashMap hm)
		{
			Object key=null;
			int max =0;
			
			Set st = hm.keySet();
			for (Iterator it = st.iterator();it.hasNext();)
			{	
				key = (Integer)it.next();
				if (((Integer)hm.get(key)).intValue() > max) 
					max = ((Integer)hm.get(key)).intValue();
			}
			
			
			return key;
		}
	//------------------------------------------
	// END IMPORT/EXPORT ARBRE
	//------------------------------------------
		
	/**
	 * Enregistre l'image dans un fichier dont le nom est filename et de type 2 ou 5 (P2/P5)
	 * @param filename, nom du fichier
	 * @param type, type de sauvegarde, 2: décimale, 5:binaire
	 */
	public void sauvImage (String filename, int type) {
		
		FichierDestination f=null;
		if (type==2)
			{	f = new FichierDestination(filename);}
		else if (type==5)
		{
				f = new FichierDestinationBinaire(filename);
		}
		else
		{
			System.err.println("type incorrect : 2 ou 5");
			System.exit(99);
		}
		f.ecrireString("P"+String.valueOf(type));
		f.ecrireEntree();
		f.ecrireString(Integer.toString(getTaille()));
		f.ecrireEspace();
		f.ecrireString(Integer.toString(getTaille()));
		f.ecrireEntree();
		f.ecrireString(Integer.toString(nvGrisMax));
		f.ecrireEntree();
			
		//donner la responsabilité a matrice?
		if (type==2)
		{	for (int i=0; i<getTaille();i++)
			{	for (int j=0; j<getTaille();j++)
				{
					f.ecrireSymbole(getMat().get(i,j));
					f.ecrireBlancs();
				}
				f.ecrireEntree();
			}
			System.out.println("end ecriture P2");
		}
		else if (type==5)
			{((FichierDestinationBinaire)f).transitionBinaire();
			for (int i=0; i<getTaille();i++)
			{	for (int j=0; j<getTaille();j++)
				{
					((FichierDestinationBinaire)f).ecrireSymboleBinaire(getMat().get(i,j));
				}
			}}
		f.fermer();
		
	}
	
	/**
	 * @return Returns the mat.
	 */
	public Matrice getMat() {
		return mat;
	}
	/**
	 * @param mat The mat to set.
	 */
	public void setMat(Matrice mat) {
		this.mat = mat;
	}
	/**
	 * @return Returns the taille.
	 */
	public int getTaille() {
		return getMat().getTaille();
	}
	
}
