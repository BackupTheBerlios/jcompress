
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

/**
 * *
 * 
 * @param
 * @return
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

		/*String test = "a";
		byte[] tab = test.getBytes();
		for (int i=0; i< tab.length;i++)
			System.out.println(tab[i]);
			*/
		init();
	

	}

	public static void compresser() {

		System.out.println("clic compress");
		
		//choix des fichiers
		String fic = ouvrirFichier(".txt");
		String ficDest = ouvrirFichier(".jcomp");

		System.out.println("source :" + fic);
		System.out.println("destination :" + ficDest);
		
		
		
		/*
		 Ressources fichiers = new Ressources(fic, ficDest);
		 ArbreBinaire arbre = new Arbre();
		 
		 //tq pas fin de fichier
		 for (String car = fichiers.lireOctet();car!= ArbreBinaire.EOF;
		 		car = fichiers.lireOctet()){
		 {
		 	Noeud n = getNoeud(c);
		 	if (n==null)
		 	{
		 			byte[] tab = car.getBytes();
		 			//nouveau caractere
		 			fichiers.ecrireCaractere(getNoeud(ArbreBinaire.ECHAP).getCodeDansArbreBinaire());
		 			fichiers.ecrireCaractere(tab[0]);
		 	}
		 	else
		 	{
		 		//caractere redondant
		 		fichiers.ecrireCaractere(n.getCodeDansArbreBinaire());	
		 	}
		 	arbre.ajoutCaractere(car);
		 }
		 //rajout de EOF dans arbre???
		 ressources.fermer();
		 System.out.println("compression terminée");
		 
		 */

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