package editeurPGMP5;
import java.io.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;

// affiche une image PGM en gris

class DisplayImage extends Canvas {
  
  Image img;

  public DisplayImage(Image pImg) {
    this.img = pImg;
    setSize(img.getWidth(this), img.getHeight(this));
  }

  public void paint(Graphics gr) {
    gr.drawImage(img, 0, 0, this);
  }

}

public class DisplayPixmapAWT extends Frame {
  
  // constructeur pour un argument Pixmap
  public DisplayPixmapAWT(String name, BytePixmap p) {
    super(name);
    addWindowListener (new WindowAdapter ()
                              {
                                public void windowClosing (WindowEvent e)
                                {
                                  System.exit (0) ;
                                }
                              }) ;

    setLocation(50, 50);
    
    // fabrication des pixels gris au format usuel AWT : ColorModel.RGBdefault
    int[] pixels = new int[p.size];
    for (int i = 0; i < pixels.length; i++)
      pixels[i] = 0xFF000000 + Pixmap.intValue(p.data[i]) * 0x010101; // rŽplique l'octet 3 fois
    // construit une image avec ces pixels
    MemoryImageSource source = new MemoryImageSource(p.width, p.height, pixels, 0, p.width);
    Image img = Toolkit.getDefaultToolkit().createImage(source);
    add(new DisplayImage(img));
    pack();
    show();
  }

  // constructeur pour un argument fichier PGM
  public DisplayPixmapAWT(String filename) throws IOException {
    this(filename, new BytePixmap(filename));
  }

  public static void main(String[] args) {
    try {
      new DisplayPixmapAWT("D:\\fac\\bot\\Projet2\\exemple\\boat.pgm");
    }
    catch (IOException e) {System.err.println(e);}
  }

}
