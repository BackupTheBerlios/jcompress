package IHM;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;

import javax.swing.JFrame;

import ressources.Fichier;
import ressources.FichierSource;
import ressources.FichierSourceBinaire;
import ressources.Symbole;


/**
 * @author lalo TODO To change the template for this generated type comment go
 *         to Window - Preferences - Java - Code Style - Code Templates
 */
public class EditeurPGM {
	private String typeFichier;
	private int width;
	private int height;
	private int size;
	private int niveau;

	public EditeurPGM(String mFichier){
		JFrame image = new JFrame(mFichier);
		FichierSource fichier = new FichierSource(mFichier);

		typeFichier = fichier.next(); // type
		width = Integer.parseInt(fichier.next()); // largeur
		height = Integer.parseInt(fichier.next()); // longueur
		niveau = Integer.parseInt(fichier.next()); // niveau de gris
		size = width * height;

		if(typeFichier.equals(Fichier.P5)){
			fichier = new FichierSourceBinaire(mFichier);
/*			typeFichier = fichier.next(); // type
			width = Integer.parseInt(fichier.next()); // largeur
			height = Integer.parseInt(fichier.next()); // longueur
			niveau = Integer.parseInt(fichier.next()); // niveau de gris
			size = width * height;*/
		}
		else{
		}

		//System.out.println(fichier.nextSymbole().getValeur());

		int[] pixels = new int[size];
		for(int i = 0 ; i < size ; i++){
			Symbole sym = fichier.nextSymbole();
			if(sym != null){
				if(sym.getType().equals(Symbole.NOMBRE)){
					pixels[i] = 0xFF000000 + Integer.parseInt(sym.getValeur()) * 0x010101;
				}
				else{
					i--;
				}
			}
			else{
				System.out.println(i+"\n"+size);
				i = size;
			}
		}

		MemoryImageSource source = new MemoryImageSource(width, height, pixels,
				0, width);
		Image img = Toolkit.getDefaultToolkit().createImage(source);
		image.add(new DisplayImage(img));
		image.pack();
		image.setVisible(true);
	}
}

class DisplayImage extends Canvas {

	Image img;

	public DisplayImage(Image pImg){
		this.img = pImg;
		setSize(img.getWidth(this), img.getHeight(this));
	}

	public void paint(Graphics gr){
		gr.drawImage(img, 0, 0, this);
	}

}