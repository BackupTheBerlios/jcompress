
package ressources;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Image {
	
	private Matrice mat;
	private String type;
	private int taille;
	private int nvGrisMax = 255;
	
	//ok
	//suppose un fichier non vide
	//et la taille annoncée est concordante avec le fichier-->NullPointerException
	public Image (FichierSource f){
		
		type = f.nextSymbole().getValeur();
		
		//taille carree
		//next tout court??
		taille = Integer.parseInt(f.nextSymbole().getValeur());
		taille = Integer.parseInt(f.nextSymbole().getValeur());
		
		mat = new Matrice(getTaille());
		
		nvGrisMax = Integer.parseInt(f.nextSymbole().getValeur());
		
		for (int i=0; i<getTaille();i++)
			for (int j=0; j<getTaille();j++)
			{
				getMat().ajoutSymbole(f.nextSymbole());
			}
	}
	
	//TODO public Arbre construireArbre(){}
	
	public void sauvImage (FichierDestination f) {
		
		f.ecrireString(type);
		f.ecrireEntree();
		f.ecrireString(Integer.toString(taille));
		f.ecrireEntree();
		f.ecrireString(Integer.toString(taille));
		f.ecrireEntree();
		
		//donner la responsabilité a matrice?
		for (int i=0; i<getTaille();i++)
		{	for (int j=0; j<getTaille();j++)
			{
				f.ecrireSymbole(getMat().get(i,j));
				f.ecrireBlancs();
			}
			f.ecrireEntree();
		}
		//f.fermer();
	
	}
	
	public static void main(String[] args) {
		
		FichierSource f = new FichierSource("T:/IUP Master 1/sem2/BOT/compress2/sources/enonce.pgm");
		
		Image im = new Image(f);
		
		im.getMat().afficher();
		System.out.println("taille "+ im.getTaille());
		System.out.println("type "+ im.getType());
		
		FichierDestination fd = new FichierDestination("T:/IUP Master 1/sem2/BOT/compress2/sources/sauvImenonce.pgm");
		im.sauvImage(fd);
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
		return taille;
	}
	/**
	 * @param taille The taille to set.
	 */
	public void setTaille(int taille) {
		this.taille = taille;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

}
