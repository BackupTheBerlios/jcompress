import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

/**
 * Date 		= 21/01/2005
 * Project		= JCompress
 * File name  	= Application.java
 * @author Bosse Laure/Fauroux claire
 *  
 */
public class Application {

	private static JTextArea textArea;
	private static String NEW_LINE = "\n";
	
	public Application() {
		super();
	}

	public static void main(String[] args) {
		init();
	}

	public static void compresser() {

		System.out.println("clic compress");

		//choix des fichiers
		String fic = ouvrirFichier(".txt");
		String ficDest = ouvrirFichier(".jcomp");

		System.out.println("source :" + fic);
		System.out.println("destination :" + ficDest);

		if (fic != null && ficDest != null) {
			Ressources fichiers;
			try {
				fichiers = new Ressources(fic, ficDest);

				ArbreBinaire arbre = new ArbreBinaire();

				//tq pas fin de fichier
				for (String car = fichiers.lireOctet(); !(car
						.equals("11111111")); car = fichiers.lireOctet()) {
					{

						System.out.println("caractere lu :" + car);
						Noeud n = (Noeud) arbre.getNoeud(car);
						if (n == null) {
							//nouveau caractere
							fichiers.ecrireCaractere(((Noeud) arbre
									.getNoeud(ArbreBinaire.ECHAP))
									.getCodeDansArbreBinaire());
							fichiers.ecrireCaractere(car);
						} else {
							//caractere redondant
							fichiers.ecrireCaractere(n
									.getCodeDansArbreBinaire());
						}
						arbre.ajoutCaractere(car);
					}

				}
				//arbre.ajoutCaractere(ArbreBinaire.EOF);
				fichiers.finEcrire(((Noeud) arbre.getNoeud(ArbreBinaire.EOF))
						.getCodeDansArbreBinaire());
				arbre.ajoutCaractere(ArbreBinaire.EOF);

				System.out.println("Compression termin�e.");
				textArea.append("Compression termin�e."+NEW_LINE);
				arbre.afficherListe();
			} catch (FileNotFoundException e) {
				textArea.append("Fichier "+fic+" inexistant."+NEW_LINE);
				e.printStackTrace();
			} catch (IOException e1) {
				textArea.append("Erreur d'�criture d'un fichier."+NEW_LINE);
				e1.printStackTrace();
			} catch (Exception e2) {
				textArea.append("Une erreur est survenue."+NEW_LINE);
			}
		} else {
			textArea.append("Fichier non identifi�."+NEW_LINE);
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

		if (fic != null && ficDest != null) {
			Ressources res;
			try {
				// Initialisation
				res = new Ressources(fic, ficDest);
				ArbreBinaire arbre = new ArbreBinaire();

				// Lecture d'un bit
				String bit = res.lireBit();
				Noeud n = (Noeud) arbre.getNoeudToCode(bit);

				// Tant que le bit ne correspond pas a EOF
				while (n.getCaractere() != ArbreBinaire.EOF) {
					// Si correspond � un car
					if (n.isFeuille()) {
						// Si car echap
						if (n.getCaractere() == ArbreBinaire.ECHAP) {
							// Alors lecture de l'octet
							String octet = res.lireOctet();
							// Insertion ds l'arbre
							arbre.ajoutCaractere(octet);
							// Ecriture de l'octet dans le fichier destination
							res.ecrireCaractere(octet);
						} else {
							// Ecriture du car qui correspond dans l'arbre
							res.ecrireCaractere(n.getCaractere());
							// Modification de l'arbre
							arbre.ajoutCaractere(n.getCaractere());
						}
						bit = "";
					}
					// Lecture du bit suivant
					bit = bit + res.lireBit();
					n = (Noeud) arbre.getNoeudToCode(bit);
				}
				textArea.append("D�compression termin�e."+NEW_LINE);
			} catch (FileNotFoundException e) {
				textArea.append("Fichier "+fic+" inexistant."+NEW_LINE);
			} catch (IOException e1) {
				textArea.append("Erreur d'�criture d'un fichier."+NEW_LINE);
			} catch (Exception e2) {
				textArea.append("Une erreur est survenue."+NEW_LINE);
			}
		} else {
			textArea.append("Fichier non identifi�."+NEW_LINE);
			System.out.println("fichiers de ressources non identifies");
		}
	}

	/**
	 * * init : initialise l'interface
	 *  
	 */
	private static void init() {
		JFrame fenetre = new JFrame("JCompress");
		fenetre.setSize(300, 200);
		fenetre.setLocation(300, 300);
		//fenetre.setAlwaysOnTop(true);

		JButton BComp = new JButton("Compresser");
		BComp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				compresser();
			}
		});
		JButton BDeComp = new JButton("D�compresser");
		BDeComp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				decompresser();
			}
		});
		
		textArea = new JTextArea(5,20);
		textArea.setEditable(false);
		JScrollPane jFiel = new JScrollPane(textArea);
		jFiel.setPreferredSize(new Dimension(250,100));

		FlowLayout flayout = new FlowLayout(FlowLayout.CENTER);
		fenetre.getContentPane().setLayout(flayout);
		fenetre.getContentPane().add(BComp, 0);
		fenetre.getContentPane().add(BDeComp, 1);
		fenetre.getContentPane().add(jFiel, 2);

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
							return extension.length();
						}

						public char charAt(int index) {
							return extension.charAt(index);
						}

						public CharSequence subSequence(int start, int end) {
							return null;
						}

						public String toString() {
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