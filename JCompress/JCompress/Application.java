/**
 * Date 		= 21/01/2005
 * Project		= JCompress
 * File name  	= Application.java
 * @author Bosse Laure/Fauroux claire
 *  
 */

package JCompress;

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
 * Cette classe représente les fonctions principales de l'application 
 * tel que compresser et décompresser ainsi que la fonction de lancement
 * de l'application.
 */
public class Application {
    private static JTextArea textArea;
    private static String NEW_LINE = "\n";

    /**
     * *main : lance l'application
     *  
     */
    public static void main(String[] args) {
        init();
    }

    /**
     * *compresser : fonction principale de compression
     *  
     */
    public static void compresser() {

        //choix des fichiers
        String fic = ouvrirFichier(".txt");
        String ficDest = ouvrirFichier(".jcomp");

        if (fic != null && ficDest != null) {
            Ressources fichiers;
            try {
                fichiers = new Ressources(fic, ficDest);

                ArbreBinaire arbre = new ArbreBinaire();

                //tq pas fin de fichier
                for (String car = fichiers.lireOctet(); !(car
                        .equals("11111111")); car = fichiers.lireOctet()) {
                    {

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

                textArea.append("Compression terminée." + NEW_LINE);

            } catch (FileNotFoundException e) {
                textArea.append("Fichier " + fic + " inexistant." + NEW_LINE);
            } catch (IOException e1) {
                textArea.append("Erreur d'écriture d'un fichier." + NEW_LINE);
            } catch (Exception e2) {
                textArea.append("Une erreur est survenue." + NEW_LINE);
            }
        } else {
            textArea.append("Fichier non identifié." + NEW_LINE);
        }

    }

    /**
     * *decompresser : fonction principale de decompression
     *  
     */
    public static void decompresser() {

        //choix des fichiers
        String fic = ouvrirFichier(".jcomp");
        String ficDest = ouvrirFichier(".txt");

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
                    // Si correspond à un car
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
                textArea.append("Décompression terminée." + NEW_LINE);
            } catch (FileNotFoundException e) {
                textArea.append("Fichier " + fic + " inexistant." + NEW_LINE);
            } catch (IOException e1) {
                textArea.append("Erreur d'écriture d'un fichier." + NEW_LINE);
            } catch (Exception e2) {
                textArea.append("Une erreur est survenue." + NEW_LINE);
            }
        } else {
            textArea.append("Fichier non identifié." + NEW_LINE);
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

        // Boutton Compresser
        JButton BComp = new JButton("Compresser");
        BComp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                compresser();
            }
        });
        
        // Boutton Décompresser 
        JButton BDeComp = new JButton("Décompresser");
        BDeComp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                decompresser();
            }
        });

        // Champ texte pour dialoguer avec l'utilisateur
        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        JScrollPane jFiel = new JScrollPane(textArea);
        jFiel.setPreferredSize(new Dimension(250, 100));

        // Configuration de la fenêtre
        FlowLayout flayout = new FlowLayout(FlowLayout.CENTER);
        fenetre.getContentPane().setLayout(flayout);
        fenetre.getContentPane().add(BComp, 0);
        fenetre.getContentPane().add(BDeComp, 1);
        fenetre.getContentPane().add(jFiel, 2);

        fenetre.setVisible(true);
    }

    /**
     * *ouvrirFichier : ouvre un browser
     * 
     * @param String
     *            extension du fichier a ouvrir
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
            return chooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
}