
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

/**
 * Date 		= 21/01/2005
 * Project		= JCompress
 * File name  	= Application.java
 * @author Bosse Laure/Fauroux claire
 *	
 */
public class Application {

	public Application() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		//ArbreBinaire ab = new ArbreBinaire();
		//ab.afficherListe();

		//ab.ajoutCaractere("test");
		//ab.afficherListe();

		init();

	}

	public static void compresser() {

		System.out.println("clic compress");
		String fic = ouvrirFichier(".txt");
	}

	public static void decompresser() {
		System.out.println("clic decompress");
	}

	/**
	 * * init : initialise l'interface
	 *  
	 */
	private static void init() {
		JFrame fenetre = new JFrame("JCompress");
		fenetre.setSize(300, 100);
		fenetre.setLocation(300, 300);
		//fenetre.setAlwaysOnTop(true);

		JButton BComp = new JButton("Compresser");
		BComp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				compresser();
			}
		});
		JButton BDeComp = new JButton("Décompresser");
		BDeComp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				decompresser();
			}
		});
		FlowLayout flayout = new FlowLayout(FlowLayout.CENTER);
		fenetre.getContentPane().setLayout(flayout);
		fenetre.getContentPane().add(BComp, 0);
		fenetre.getContentPane().add(BDeComp, 1);

		fenetre.setVisible(true);
	}

	private static String ouvrirFichier(final String extension ){
		
		JFileChooser chooser  = new JFileChooser();
	    FileFilter filter = new FileFilter(){
			public boolean accept(File f) {
				//marche pas pour les txt sai pas pkoi
				//recupere l'extension
				int index =f.getAbsolutePath().lastIndexOf('.');
				String ext="";
				if (index!=-1)
						ext = f.getAbsolutePath().substring(index);
				else return true;
				System.out.println("ext : "+ext);
				System.out.println("extension : "+extension);
				if (ext == extension)
					return true;
				else return false;
			}
			public String getDescription() {
				// TODO Auto-generated method stub
				return extension;
			}
	    };
	    //filter.addExtension("jpg");
	    //filter.addExtension("gif");
	    //filter.setDescription("JPG & GIF Images");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(new JFrame("open"));
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("file: " +
	            chooser.getSelectedFile().getName());
	    }
		
		return null;
	}
}