
package ressources;

import arbre.Arbre;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
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
		
		System.out.println("taille mat "+mat.getTaille());
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
		//System.out.println("nb mot "+nb_mot);
	}
	//------------------------------------------
	//relations avec arbre
	public Image(Arbre a){
	  // TODO
	}
	
	public Arbre construireArbre(){
	  // TODO
		return null;
	}
	public Arbre construireArbreCompresseSansPerte(){
	  // TODO
		return null;
	}
	public Arbre construireArbreCompresseAvecPerte(){
	  // TODO
		return null;
	}
	//------------------------------------------
	public void sauvImage (String filename, int type) {
		
		if (type!=2 && type!=5)
			System.err.println("type incorrect : 2 ou 5");
		else
		{
			FichierDestination f = new FichierDestination(filename);
			
			f.ecrireString("P"+type);
			f.ecrireEntree();
			f.ecrireString(Integer.toString(getTaille()));
			f.ecrireEntree();
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
						f.ecrireSymboleBinaire(getMat().get(i,j));
					}
					f.ecrireEntree();
				}
			f.fermer();
		}
	}
	
	public static void main(String[] args) {
		
		//FichierSource f = new FichierSource("T:/IUP Master 1/sem2/BOT/compress2/sources/lena.pgm");
		
		Image im = new Image("T:/IUP Master 1/sem2/BOT/compress2/sources/enonce.pgm");
		
		//im.getMat().afficher();
		System.out.println("taille "+ im.getTaille());
		//System.out.println("type "+ im.getType());
		
		FichierDestination fd = new FichierDestination("T:/IUP Master 1/sem2/BOT/compress2/sources/lenatest.pgm");
		//im.sauvImage(fd);
		fd.fermer();
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
