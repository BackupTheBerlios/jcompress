package src;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.MaskFormatter;

import IHM.EditeurPGM;
import IHM.Verifier;

import ressources.Fichier;



//lire fichier P2 ok
//lire fichier P5 ca a lair ok...a verifier graphiquement
//ecrire fichier p2 ok
//ecrire fichier p5 non-->"010101"

/**
 * @author claire TODO To change the template for this generated type comment go
 *         to Window - Preferences - Java - Code Style - Code Templates
 */
public class Application {

	// Attribut pour l'IHM
	private static final String NEW_LINE = "\n";

	private static JTextArea textArea;
	private static JCheckBox boutonNormal;
	private static JCheckBox boutonSansPerte;
	private static JCheckBox boutonAvecPerte;
	private static JLabel lTaux;
	private static JComboBox cTypeFichier;
	private static JFormattedTextField fTaux = null;


	public static void main(String[] args) {
		init();
	}

	/**
	 * Permet la decompression.
	 * @param pTypeFichier Type de fichier PGM qu'on souhaite en sortie.
	 */
	protected static void decompresser(String pTypeFichier) {
		//String ficDest = ouvrirFichier(".txt");
		String fic = ouvrirFichier(".pgm");
		//textArea.append("Decompression terminée." + NEW_LINE);
		//Arbre ab = new Arbre("D:\\fac\\bot\\Projet2\\exemple\\enonce.txt");
		//System.out.println(ab.construireLigne());
		afficherImage(fic);
	}

	/**
	 * Permet la compression normale.
	 * @param pFichierSource Chemin du fichier source à compresser.
	 */
	protected static void compressionNormal(String pFichierSource) {
		String ficDest = ouvrirFichier(".txt");
		textArea.append("Compression normale terminé." + NEW_LINE);
	}

	/**
	 * Permet une compression sans perte.
	 * @param pFichierSource Chemin du fichier source à compresser.
	 */
	protected static void compressionSansPerte(String pFichierSource) {
		String ficDest = ouvrirFichier(".txt");
		textArea.append("Compression sans perte terminé." + NEW_LINE);
	}

	/**
	 * Fait la compression avec perte.
	 * @param pFichierSource Chemin du fichier source à compresser.
	 */
	protected static void compressionAvecPerte(String pFichierSource) {
		String ficDest = ouvrirFichier(".txt");
		textArea.append("Compression avec perte terminé." + NEW_LINE);
	}

	/**
	 * Créer et initialise l'IHM.
	 */
	private static void init() {
		// Initialisation de la fenetre
		JFrame jFrame = new JFrame("ICompress");
		jFrame.setSize(300, 310);

		// Affichage de la fenetre au centre de l'ecran
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		jFrame.setLocation(dim.width / 2 - jFrame.getWidth() / 2, dim.height
				/ 2 - jFrame.getHeight() / 2);

		// Permet la cloture de l'application
		jFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Label
		JLabel lChoixComp = new JLabel("Choisissez la méthode de compression :");
		JLabel lChoixDecomp = new JLabel("Choisissez le type du fichier PGM :");
		lTaux = new JLabel("Taux de compression :");
		lTaux.setVisible(false);

		// Champ texte formatte
		try{
			// Formatage d'un champ text
			MaskFormatter mask = new MaskFormatter("#"+"."+"##");
			fTaux = new JFormattedTextField(mask);
			fTaux.setColumns(4);
			fTaux.setInputVerifier(new Verifier());
			fTaux.setEditable(true);
			fTaux.setVisible(false);
		}
		catch(ParseException e1){
			textArea.append("Erreur au cours du formattage du champ texte"+NEW_LINE);
		}

		// Combo box permettant le type du fichier PGM souhaité en sortie de la
		// decompression
		cTypeFichier = new JComboBox();
		cTypeFichier.addItem(Fichier.P2);
		cTypeFichier.addItem(Fichier.P5);
		cTypeFichier.setVisible(true);

		// Case a cocher pour determiner la methode de compression
		boutonNormal = new JCheckBox("Normal");
		boutonSansPerte = new JCheckBox("Sans perte");
		boutonAvecPerte = new JCheckBox("Avec perte");
		boutonAvecPerte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Rend visible le champ de saisi du code de compression si la
				// case Avec perte est coché
				if (boutonAvecPerte.isSelected()) {
					fTaux.setVisible(true);
					lTaux.setVisible(true);
				} else {
					fTaux.setVisible(false);
					lTaux.setVisible(false);
				}
			}
		});

		// Bouton compresser
		JButton BComp = new JButton("Compresser");
		BComp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fichierSource = ouvrirFichier(".pgm");
				if (boutonNormal.isSelected())
					compressionNormal(fichierSource);
				if (boutonSansPerte.isSelected())
					compressionSansPerte(fichierSource);
				if (boutonAvecPerte.isSelected()){
					// Verification du taux de compression
					float tx = Float.parseFloat(fTaux.getText());
					if(tx > 0.50 && tx <= 1){
						compressionAvecPerte(fichierSource);
					}
					else{
						textArea.append("Le taux de compression est invalide."+NEW_LINE);
					}
				}
			}
		});

		// Bouton decompresser
		JButton BDeComp = new JButton("Décompresser");
		BDeComp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				decompresser((String) cTypeFichier.getSelectedItem());
			}
		});

		// Champ texte pour prevenir l'utilisateur
		textArea = new JTextArea(5, 20);
		textArea.setEditable(false);
		JScrollPane jFiel = new JScrollPane(textArea);
		jFiel.setPreferredSize(new Dimension(250, 100));

		// Ajout des elements a la fenetre
		FlowLayout fLayout = new FlowLayout(FlowLayout.CENTER);
		jFrame.getContentPane().setLayout(fLayout);
		jFrame.getContentPane().add(lChoixComp, 0);
		jFrame.getContentPane().add(boutonNormal, 1);
		jFrame.getContentPane().add(boutonSansPerte, 2);
		jFrame.getContentPane().add(boutonAvecPerte, 3);
		jFrame.getContentPane().add(lTaux, 4);
		jFrame.getContentPane().add(fTaux, 5);
		jFrame.getContentPane().add(BComp, 6);
		jFrame.getContentPane().add(lChoixDecomp, 7);
		jFrame.getContentPane().add(cTypeFichier, 8);
		jFrame.getContentPane().add(BDeComp, 9);
		jFrame.getContentPane().add(jFiel, 10);

		// Rend visible la fenetre
		jFrame.setVisible(true);
	}

	/**
	 * Ouvre une fenetre permettant de choisir un fichier ayant pour extension
	 * le parametre.
	 * @param extension Type d'extension (exemple : ".txt").
	 * @return Chemin du ficheir choisi par l'utilisateur
	 */
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
				return (ext.compareTo(extension) == 0);
			}

			public String getDescription() {
				return extension;
			}
		};
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(new JFrame("open"));
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			System.out.println("file: " + chooser.getSelectedFile().getName());
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	/**
	 * Affiche l'image PGM dans une nouvelle fenetre.
	 * @param mFichierImage Chemin de l'image à afficher. void
	 */
	public static void afficherImage(String mFichierImage) {
		new EditeurPGM(mFichierImage);
	}
}
