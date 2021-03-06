/**
 * Date = 15/01/2005 
 * Project = ICompress 
 * File name = Application.java
 * @author Bosse Laure/Fauroux claire 
 * 
 * Ce projet permet la compression et la
 *         decompression de fichier PGM de type P5 et P2.
 */

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

import arbre.Arbre;

import ressources.Fichier;
import ressources.Image;
import IHM.EditeurPGM;
import IHM.Verifier;

/**
 * Gestion de l'application.
 */
public class Application {
	public static String NEW_LINE = "\n";

	public static JTextArea textArea;
	private static JCheckBox boutonNormal;
	private static JCheckBox boutonSansPerte;
	private static JCheckBox boutonAvecPerte;
	private static JLabel lTaux;
	private static JComboBox cTypeFichier;
	private static JFormattedTextField fTaux = null;

	public static void main(String[] args){
		init();
	}

	/**
	 * Permet la decompression.
	 * @param Type de fichier PGM qu'on souhaite en sortie (P2 ou P5).
	 */
	protected static void decompresser(String pTypeFichier){
		String ficSource = ouvrirFichier(".txt", "Fichier txt a decompresser");

		if(ficSource != null){
			String ficDestination = ouvrirFichier(".pgm", "Fichier PGM destination");
			if(ficDestination != null){
				Image im = new Image(new Arbre(ficSource));
				
				int typeFic = 2;
				if(pTypeFichier.equals(Fichier.P2))
					typeFic = 2;
				if(pTypeFichier.equals(Fichier.P5))
					typeFic = 5;
				im.sauvImage(ficDestination, typeFic);
				
				textArea.append("Decompression du fichier " + ficSource
						+ " termin�e." + NEW_LINE);
				textArea.append("Affichage de l'image " + ficDestination + "."
						+ NEW_LINE);
				afficherImage(ficDestination);
			}
			else{
				textArea.append("Vous avez annul� l'operation." + NEW_LINE);
			}
		}
		else{
			textArea.append("Vous avez annul� l'operation." + NEW_LINE);
		}

	}

	/**
	 * Permet une compression normale (transformation d'une matrice en arbre).
	 * @param Chemin du fichier source � compresser.
	 */
	protected static void compressionNormal(String pFichierSource){
		String ficDest = ouvrirFichier(".txt", "Fichier txt contenant l'affichage de l'arbre");

		if(ficDest != null){
			String ficDest2 = ouvrirFichier(".pgm", "Fichier PGM contenant la matrice associ� � l'arbre");
			if(ficDest2 != null){
				Image im = new Image(pFichierSource);
				Arbre ab = im.construireArbre();
				ab.creerFichier(ficDest);
				Image imageDest = new Image(ab.construireMatrice());
				
				int type = 2;
				if(cTypeFichier.getSelectedItem().equals(Fichier.P2))
					type = 2;
				if(cTypeFichier.getSelectedItem().equals(Fichier.P5))
					type = 5;
				imageDest.sauvImage(ficDest2, type);
				
				afficherImage(ficDest2);
				textArea.append("Compression normale termin�." + NEW_LINE);
				textArea.append("Matrice correspondant � l'arbre affich�." + NEW_LINE);
				
				// Gain de compression
				Arbre abOrigine = im.construireArbre();
				Float gain = Float.valueOf(abOrigine.tauxDeCompression(ab));
				textArea.append("Gain de compression : "+gain.intValue()+"%."+NEW_LINE);
			}
			else{
				textArea.append("Vous avez annul� l'operation." + NEW_LINE);
			}
		}
		else{
			textArea.append("Vous avez annul� l'operation." + NEW_LINE);
		}
	}

	/**
	 * Permet une compression sans perte.
	 * @param pFichierSource Chemin du fichier source � compresser.
	 */
	protected static void compressionSansPerte(String pFichierSource){
		String ficDest = ouvrirFichier(".txt", "Fichier txt contenant l'affichage de l'arbre");

		if(ficDest != null){
			String ficDest2 = ouvrirFichier(".pgm", "Fichier PGM contenant la matrice associ� � l'arbre");
			if(ficDest2 != null){
				Image im = new Image(pFichierSource);
				Arbre ab = im.construireArbreCompresseSansPerte();
				ab.creerFichier(ficDest);
				Image imageDest = new Image(ab.construireMatrice());
				
				int type = 2;
				if(cTypeFichier.getSelectedItem().equals(Fichier.P2))
					type = 2;
				if(cTypeFichier.getSelectedItem().equals(Fichier.P5))
					type = 5;
				imageDest.sauvImage(ficDest2, type);
				
				afficherImage(ficDest2);
				textArea.append("Compression sans perte termin�." + NEW_LINE);
				textArea.append("Matrice correspondant � l'arbre affich�." + NEW_LINE);
			}
			else{
				textArea.append("Vous avez annul� l'operation." + NEW_LINE);
			}
		}
		else{
			textArea.append("Vous avez annul� l'operation." + NEW_LINE);
		}
	}

	/**
	 * Fait la compression avec perte.
	 * @param pFichierSource Chemin du fichier source � compresser.
	 * @param pTx
	 */
	protected static void compressionAvecPerte(String pFichierSource, float pTx){
		String ficDest = ouvrirFichier(".txt", "Fichier txt contenant l'affichage de l'arbre");

		if(ficDest != null){
			String ficDest2 = ouvrirFichier(".pgm", "Fichier PGM contenant la matrice associ� � l'arbre");
			if(ficDest2 != null){
				Image im = new Image(pFichierSource);
				Arbre ab = im.construireArbreCompresseAvecPerte(pTx);
				ab.creerFichier(ficDest);
				Image imageDest = new Image(ab.construireMatrice());
				
				int type = 2;
				if(cTypeFichier.getSelectedItem().equals(Fichier.P2))
					type = 2;
				if(cTypeFichier.getSelectedItem().equals(Fichier.P5))
					type = 5;
				imageDest.sauvImage(ficDest2, type);
				
				afficherImage(ficDest2);
				textArea.append("Compression avec perte termin�." + NEW_LINE);
				textArea.append("Matrice correspondant � l'arbre affich�." + NEW_LINE);
				
				// Gain de compression
				Arbre abOrigine = im.construireArbre();
				Float gain = Float.valueOf(abOrigine.tauxDeCompression(ab));
				textArea.append("Gain de compression : "+gain.intValue()+"%."+NEW_LINE);
			}
			else{
				textArea.append("Vous avez annul� l'operation." + NEW_LINE);
			}
		}
		else{
			textArea.append("Vous avez annul� l'operation." + NEW_LINE);
		}
	}

	/**
	 * Cr�er et initialise l'IHM.
	 */
	private static void init(){
		// Initialisation de la fenetre
		JFrame jFrame = new JFrame("ICompress");
		jFrame.setSize(350, 330);

		// Affichage de la fenetre au centre de l'ecran
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		jFrame.setLocation(dim.width / 2 - jFrame.getWidth() / 2, dim.height
				/ 2 - jFrame.getHeight() / 2);

		// Permet la cloture de l'application
		jFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});

		// Label
		JLabel lChoixComp = new JLabel(
				"Choisissez la m�thode de compression (entre 0.51 et 1) :");
		JLabel lChoixDecomp = new JLabel(
				"Choisissez le type du fichier PGM (en sortie) :");
		lTaux = new JLabel("Taux de compression :");
		lTaux.setVisible(false);

		// Champ texte formatte
		try{
			// Formatage d'un champ text
			MaskFormatter mask = new MaskFormatter("#" + "." + "##");
			fTaux = new JFormattedTextField(mask);
			fTaux.setColumns(4);
			fTaux.setInputVerifier(new Verifier());
			fTaux.setEditable(true);
			fTaux.setVisible(false);
		}
		catch(ParseException e1){
			textArea.append("Erreur au cours du formattage du champ texte"
					+ NEW_LINE);
		}

		// Combo box permettant le type du fichier PGM souhait� en sortie de la
		// decompression
		cTypeFichier = new JComboBox();
		cTypeFichier.addItem(Fichier.P2);
		cTypeFichier.addItem(Fichier.P5);
		cTypeFichier.setVisible(true);

		// Case a cocher pour determiner la methode de compression
		boutonNormal = new JCheckBox("Normal");
		boutonSansPerte = new JCheckBox("Sans perte");
		boutonAvecPerte = new JCheckBox("Avec perte");
		boutonAvecPerte.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// Rend visible le champ de saisi du code de compression si la
				// case Avec perte est coch�
				if(boutonAvecPerte.isSelected()){
					fTaux.setVisible(true);
					lTaux.setVisible(true);
				}
				else{
					fTaux.setVisible(false);
					lTaux.setVisible(false);
				}
			}
		});

		// Bouton compresser
		JButton BComp = new JButton("Compresser");
		BComp.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				float tx = 0;
				// Verification du taux de compression
				if(boutonAvecPerte.isSelected()){
					if(!fTaux.getText().equals(" .  "))
						tx = Float.parseFloat(fTaux.getText());
				}

				if(boutonNormal.isSelected() || boutonSansPerte.isSelected()
						|| boutonAvecPerte.isSelected()){
					if((boutonAvecPerte.isSelected() && tx > 0.50 && tx <= 1)
							|| boutonNormal.isSelected() || boutonSansPerte.isSelected()){
						String fichierSource = ouvrirFichier(".pgm", "Fichier PGM a compresser");
						if(fichierSource != null){
							if(boutonNormal.isSelected())
								compressionNormal(fichierSource);
							if(boutonSansPerte.isSelected())
								compressionSansPerte(fichierSource);
							if(boutonAvecPerte.isSelected()){
								compressionAvecPerte(fichierSource, tx);
							}
						}
						else{
							textArea.append("Vous avez annul� l'operation."
									+ NEW_LINE);
						}
					}
					else{
						textArea.append("Le taux de compression est invalide."
								+ NEW_LINE);
					}
				}
				else{
					textArea.append("Selectionnez un type de compression."
							+ NEW_LINE);
				}
			}
		});

		// Bouton decompresser
		JButton BDeComp = new JButton("D�compresser");
		BDeComp.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				decompresser((String) cTypeFichier.getSelectedItem());
			}
		});
		
		// Bouton reset
		JButton bReset = new JButton("Reset");
		bReset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				textArea.setText("");
			}
		});

		// Champ texte pour prevenir l'utilisateur
		textArea = new JTextArea(5, 20);
		textArea.setEditable(false);
		JScrollPane jFiel = new JScrollPane(textArea);
		jFiel.setPreferredSize(new Dimension(300, 100));

		// Ajout des elements a la fenetre
		FlowLayout fLayout = new FlowLayout(FlowLayout.CENTER);
		jFrame.getContentPane().setLayout(fLayout);
		jFrame.getContentPane().add(lChoixComp, 0);
		jFrame.getContentPane().add(boutonNormal, 1);
		jFrame.getContentPane().add(boutonSansPerte, 2);
		jFrame.getContentPane().add(boutonAvecPerte, 3);
		jFrame.getContentPane().add(lTaux, 4);
		jFrame.getContentPane().add(fTaux, 5);
		jFrame.getContentPane().add(lChoixDecomp, 6);
		jFrame.getContentPane().add(cTypeFichier, 7);
		jFrame.getContentPane().add(BComp, 8);
		jFrame.getContentPane().add(BDeComp, 9);
		jFrame.getContentPane().add(jFiel, 10);
		jFrame.getContentPane().add(bReset, 11);

		// Rend visible la fenetre
		jFrame.setVisible(true);
	}

	/**
	 * Ouvre une fenetre permettant de choisir un fichier ayant pour extension
	 * le parametre.
	 * @param extension Type d'extension (exemple : ".txt").
	 * @param titre Titre de la fenetre.
	 * @return Chemin du ficheir choisi par l'utilisateur
	 */
	private static String ouvrirFichier(final String extension, String titre){

		JFileChooser chooser = new JFileChooser();
		// titre
		chooser.setDialogTitle(titre);
		FileFilter filter = new FileFilter(){
			public boolean accept(File f){
				//recupere l'extension
				int index = f.getAbsolutePath().lastIndexOf('.');
				String ext = "";
				if(index != -1)
					ext = f.getAbsolutePath().substring(index);
				else
					//directories
					return true;
				return (ext.compareTo(extension) == 0);
			}

			public String getDescription(){
				return extension;
			}
		};
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(new JFrame("open"));
		if(returnVal == JFileChooser.APPROVE_OPTION){
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	/**
	 * Affiche l'image PGM dans une nouvelle fenetre.
	 * @param mFichierImage Chemin de l'image � afficher. void
	 */
	public static void afficherImage(String mFichierImage){
		new EditeurPGM(mFichierImage);
	}

}