
package ressources;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import arbre.Arbre;
import arbre.Couleur;
import arbre.GrisCompose;
import arbre.Noeud;


public class Image {
	
	private Matrice mat;
	//private int taille;
	private int nvGrisMax = 255;
	
	//ok
	//suppose un fichier non vide
	//et la taille annoncée est concordante avec le fichier sinon-->NullPointerException
	public Image (String filename){
		
		String type;
		Symbole symb=null;
		boolean EOF=false;
		
		//par defaut, c un type P2 quon construit afin de lire le type
		FichierSource f = new FichierSource(filename);

		if ((type=f.next()).equals("P5"))
		{		f=null;
				f=new FichierSourceBinaire(filename);
				type = f.next();
		}
		//remplissage des attributs de l image
		f.next();										//1ere taille
		mat = new Matrice(Integer.parseInt(f.next()));	//2e taille, identiques
		nvGrisMax = Integer.parseInt(f.next());
		
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
	public Image(Matrice m){
		  // TODO
			setMat(m);
		}
	
	//------------------------------------------
	//relations avec arbre
	//ok
	public Image(Arbre a){
	  // TODO
		setMat(a.construireImage());
	}
	/**
	 * construit un arbre a partir de l image this
	 * @return Arbre, l arbre construit
	 */
	public Arbre construireArbre(){
		return new Arbre (construireNoeud(null));
	}
	
	/**
	 * fonction recursive de construction descendante des noeuds
	 * @param p, noeud courant qui est le pere des prochains
	 * @return Noeud
	 */
	//peut etre long
	//a tester sur vrai fichier, ok avec enonce
	public Noeud construireNoeud(Noeud p){
		  
		if (getMat()!=null)
		{
			if (getMat().getTaille()>1)
			{	
				Image im1 = new Image(getMat().sousMatrice(1));
				Image im2 = new Image(getMat().sousMatrice(2));
				Image im3 = new Image(getMat().sousMatrice(3));
				Image im4 = new Image(getMat().sousMatrice(4));
				return new GrisCompose(p,im1.construireNoeud(p),im2.construireNoeud(p),
									im3.construireNoeud(p),im4.construireNoeud(p));
			}
			else
				return new Couleur(p,Integer.parseInt(getMat().get(0,0).getValeur()));	
		}	
		
		return null;
		}
	
	public Arbre construireArbreCompresseSansPerte(){
		return new Arbre (construireNoeudSansPerte(null));
	}
	
	/**
	 * fonction recursive de construction descendante des noeuds compresse sans perte
	 * @param p, noeud courant qui est le pere des prochains
	 * @return Noeud
	 */
	public Noeud construireNoeudSansPerte(Noeud p){
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
				
				//System.out.println("val1 "+val1+ " val2 "+val2+" val3 "+val3+" val4 "+val4);
				
				if (val1!=-1 && val2!=-1 && val3!=-1 && val4 !=-1 && val1==val2 && val2==val3 && val3==val4)
					return new Couleur(p, val1);
				
				return new GrisCompose(p,im1.construireNoeudSansPerte(p),im2.construireNoeudSansPerte(p),
									im3.construireNoeudSansPerte(p),im4.construireNoeudSansPerte(p));
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
	
	public Arbre construireArbreCompresseAvecPerte(double taux){

	if (taux>=0 && taux <= 1)
	  return new Arbre (construireNoeudAvecPerte(null,taux));
	return null;
	}

	/**
	 * fonction recursive de construction descendante des noeuds compresse avec perte de 1-taux
	 * @param p, noeud courant qui est le pere des prochains 
	 * @param tx, taux de sauvegarde des couleurs
	 * @return
	 */
	public Noeud construireNoeudAvecPerte(Noeud p, double tx)
	{
		if (getMat()!=null)
		{
			
			if (getMat().getTaille()>1)
			{
				
				//deb perte
				int occTX  = (int)(getTaille()*getTaille()*tx);
				System.out.println("occ demandees "+occTX);
				HashMap cpt = getMat().nbSymbDiff();
				
				Integer key = (Integer)maxValue(cpt);
				if (((Integer)cpt.get(key)).intValue() > occTX)
					return new Couleur(p, ((Integer)cpt.get(key)).intValue());
				//end perte
				
				Image im1 = new Image(getMat().sousMatrice(1));
				Image im2 = new Image(getMat().sousMatrice(2));
				Image im3 = new Image(getMat().sousMatrice(3));
				Image im4 = new Image(getMat().sousMatrice(4));
				return new GrisCompose(p,im1.construireNoeudAvecPerte(p,tx),im2.construireNoeudAvecPerte(p,tx),
									im3.construireNoeudAvecPerte(p,tx),im4.construireNoeudAvecPerte(p,tx));
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
		f.ecrireBlanc();
		f.ecrireString(Integer.toString(getTaille()));
		f.ecrireEntree();
		f.ecrireString(Integer.toString(nvGrisMax));
		f.ecrireEntree();
			
		//donner la responsabilité a matrice?
		if (type==2)
			for (int i=0; i<getTaille();i++)
			{	for (int j=0; j<getTaille();j++)
				{
					f.ecrireSymbole(getMat().get(i,j));
					f.ecrireBlancs();
				}
				f.ecrireEntree();
			}
		else if (type==5)
			for (int i=0; i<getTaille();i++)
			{	for (int j=0; j<getTaille();j++)
				{
					((FichierDestinationBinaire)f).ecrireSymboleBinaire(getMat().get(i,j));
				}
				//f.ecrireEntree();
			}
		f.fermer();
		
	}
	
	public static void main(String[] args) {
		
		//FichierSource f = new FichierSource("T:/IUP Master 1/sem2/BOT/compress2/sources/lena.pgm");
//		
		Image im = new Image("T:/IUP Master 1/sem2/BOT/compress2/sources/enonce.pgm");
//		
//		System.out.println("taille "+ im.getTaille());
//		
//		im.sauvImage("T:/IUP Master 1/sem2/BOT/compress2/sources/babtest.pgm",5);
//		try {
//			new DisplayPixmapAWT("T:/IUP Master 1/sem2/BOT/compress2/sources/babtest.pgm");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		//Arbre a = new Arbre(new Noeud(null));
		Matrice m = new Matrice(2);
		m.ajoutSymbole(new Symbole("1"));
		m.ajoutSymbole(new Symbole("1"));
		m.ajoutSymbole(new Symbole("1"));
		m.ajoutSymbole(new Symbole("1"));
//		m.ajoutSymbole(new Symbole("1"));
//		m.ajoutSymbole(new Symbole("1"));
//		m.ajoutSymbole(new Symbole("7"));
//		m.ajoutSymbole(new Symbole("8"));
//		m.ajoutSymbole(new Symbole("9"));
//		m.ajoutSymbole(new Symbole("10"));
//		m.ajoutSymbole(new Symbole("11"));
//		m.ajoutSymbole(new Symbole("12"));
//		m.ajoutSymbole(new Symbole("13"));
//		m.ajoutSymbole(new Symbole("14"));
//		m.ajoutSymbole(new Symbole("15"));
//		m.ajoutSymbole(new Symbole("16"));
		
//		Image im = new Image(m);
		
		Arbre a = im.construireArbre();
		Arbre a1 = im.construireArbreCompresseSansPerte();
		double t =  0.5;
		Arbre a2 = im.construireArbreCompresseAvecPerte(t);
		System.out.println("arbre"+a.construireLigne());
		System.out.println("arbre CompresseSPerte "+a1.construireLigne());
		System.out.println("arbre CompresseAPerte  0.5"+a1.construireLigne());
		//System.out.println("arbre "+a.construireLigne());
		
//		Image im2 = new Image(a.construireImage());
//		Image im1 = new Image (a1.construireImage());
//		im1.getMat().afficher();
//		im2.getMat().afficher();
		
		
		
		System.out.println("end");
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
