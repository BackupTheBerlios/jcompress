
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
		/*String test = "a";
		byte[] tab = test.getBytes();
		for (int i=0; i< tab.length;i++)
			System.out.println(tab[i]);
			*/
		init();
	

	}

	public static void compresser() throws FileNotFoundException {

		System.out.println("clic compress");
		
		//choix des fichiers
		String fic = ouvrirFichier(".txt");
		String ficDest = ouvrirFichier(".jcomp");

		System.out.println("source :" + fic);
		System.out.println("destination :" + ficDest);
		
		
		if (fic!=null && ficDest !=null)
		{
		 Ressources fichiers;
		try {
			fichiers = new Ressources(fic, ficDest);
		
			ArbreBinaire arbre = new ArbreBinaire();
		 
			//tq pas fin de fichier
			 for (String car = fichiers.lireOctet();!(car.equals("11111111"));
			 		car = fichiers.lireOctet()){
			 {
			 	
			 	System.out.println("caractere lu :"+ car);
			 	Noeud n = (Noeud)arbre.getNoeud(car);
			 	if (n==null)
			 	{
			 			//nouveau caractere
			 			fichiers.ecrireCaractere(((Noeud)arbre.getNoeud(ArbreBinaire.ECHAP)).getCodeDansArbreBinaire());
			 			fichiers.ecrireCaractere(car);
			 	}
			 	else
			 	{
			 		//caractere redondant
			 		fichiers.ecrireCaractere(n.getCodeDansArbreBinaire());	
			 	}
			 	arbre.ajoutCaractere(car);
			 }
			 
			 }
			 arbre.ajoutCaractere(ArbreBinaire.EOF);
			 fichiers.finEcrire(((Noeud)arbre.getNoeud(ArbreBinaire.EOF)).getCodeDansArbreBinaire());
			 System.out.println("compression terminée");
			 arbre.afficherListe();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}else
		{
			System.out.println("fichiers de ressources non identifies");
		}

	}

	public static void decompresser() {
		System.out.println("clic decompress");
		
		//choix des fichiers
		String fic = ouvrirFichier(".jcomp");
		String ficDest = ouvrirFichier(".txt");

		System.out.println("source :" + fic);
		System.out.println("destination :" + ficDest);
		
		/*Ressources fichiers = new Ressources(fic, ficDest);
		 ArbreBinaire arbre = new Arbre();
		 BitSet bits = new BitSet();
		 
		 for (int bit = fichiers.lireBit();
		 	bit != -1;	bit= fichiers.lireBit())
		 
		*/

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
				try {
					compresser();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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

	private static String ouvrirFichier(final String extension) {

		JFileChooser chooser = new JFileChooser();
		FileFilter filter = new FileFilter() {
			public boolean accept(File f) {
				//recupere l'extension
				int index = f.getAbsolutePath().lastIndexOf('.');
				String ext = "";
				if (index != -1)
					ext = f.getAbsolutePath().substring(index);
				else
					//directories
					return true;
				if (ext.compareTo(extension) == 0) {
					return true;
				} else
					return false;
			}

			public String getDescription() {
				return extension;
			}
		};
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(new JFrame("open"));
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			System.out.println("file: " + chooser.getSelectedFile().getName());
			//contains vient de java 5.0
			/*if (chooser.getSelectedFile().getName().contains(
					new CharSequence() {
						public int length() {
							// TODO Auto-generated method stub
							return extension.length();
						}

						public char charAt(int index) {
							// TODO Auto-generated method stub
							return extension.charAt(index);
						}

						public CharSequence subSequence(int start, int end) {
							// TODO Auto-generated method stub
							return null;
						}

						public String toString() {
							// TODO Auto-generated method stub
							return extension;
						}
					}))
			*/
				return chooser.getSelectedFile().getAbsolutePath();
			/*else {
				return chooser.getSelectedFile().getAbsolutePath() + extension;
			}*/
		}
		return null;
	}
}