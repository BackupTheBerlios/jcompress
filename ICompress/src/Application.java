/*
 * SOAP Supervising, Observing, Analysing Projects Copyright (C) 2003-2004 SOAPteam This program is
 * free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or any
 * later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package src ;


import java.awt.Dimension ;
import java.awt.FlowLayout ;
import java.awt.Toolkit ;
import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import java.awt.event.WindowAdapter ;
import java.awt.event.WindowEvent ;
import java.io.File ;

import javax.swing.JButton ;
import javax.swing.JCheckBox ;
import javax.swing.JFileChooser ;
import javax.swing.JFrame ;
import javax.swing.JLabel ;
import javax.swing.JScrollPane ;
import javax.swing.JTextArea ;
import javax.swing.filechooser.FileFilter ;

import arbre.Arbre;
import arbre.Couleur;
import arbre.GrisCompose;
import arbre.Noeud;

import ressources.Image;
//lire fichier P2 ok
//lire fichier P5 ca a lair ok...a verifier graphiquement
//ecrire fichier p2 ok
//ecrire fichier p5 non-->"010101"


/**
 * @author claire TODO To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Style - Code Templates
 */
public class Application
{

  private static JTextArea textArea ;
  private static String NEW_LINE = "\n" ;
  protected static JCheckBox boutonNormal ;
  protected static JCheckBox boutonSansPerte ;
  protected static JCheckBox boutonAvecPerte ;


  public static void main (String [] args)
  {
    init () ;
  }

  /**
   * Permet la decompression
   */
  protected static void decompresser ()
  {
	//String ficDest = ouvrirFichier(".icomp");
	//String fic = ouvrirFichier(".pmg");
    textArea.append ("Decompression." + NEW_LINE) ;
    
    // test arbre
    GrisCompose r = new GrisCompose(null);
    GrisCompose r1 = new GrisCompose(r);
    r1.setNE(new Couleur(r,Couleur.BLANC));
    r1.setNO(new Couleur(r,Couleur.GRIS));
    r1.setSE(new Couleur(r,Couleur.NOIR));
    r1.setSO(new Couleur(r,Couleur.GRIS));
    r.setNE(r1);
    r.setNO(new Couleur(r,Couleur.GRIS));
    r.setSE(new Couleur(r,Couleur.NOIR));
    r.setSO(new Couleur(r,Couleur.GRIS));
    Arbre ab = new Arbre(r);
    ab.construireImage().afficher();
    System.out.println(ab.construireLigne());
  }

  /**
   * Permet la compression normale
   */
  protected static void compressionNormal ()
  {
	String fic = ouvrirFichier(".pmg");
	String ficDest = ouvrirFichier(".icomp");
    textArea.append ("Compression normale." + NEW_LINE) ;
  }

  /**
   * Fait une compression sans perte.
   */
  protected static void compressionSansPerte ()
  {
	String fic = ouvrirFichier(".pmg");
	String ficDest = ouvrirFichier(".icomp");
    textArea.append ("Compression sans perte." + NEW_LINE) ;
  }

  /**
   * Fait la compression avec perte.
   */
  protected static void compressionAvecPerte ()
  {
	String fic = ouvrirFichier(".pmg");
	String ficDest = ouvrirFichier(".icomp");
    textArea.append ("Compression avec perte." + NEW_LINE) ;
  }

  private static void init ()
  {
    // Initialisation de la fenetre
    JFrame jFrame = new JFrame ("ICompress") ;
    jFrame.setSize (300, 230) ;

    // Affichage de la fenetre au centre de l'ecran
    Dimension dim = Toolkit.getDefaultToolkit ().getScreenSize () ;
    jFrame.setLocation (dim.width / 2 - jFrame.getWidth () / 2, dim.height / 2
                                                                - jFrame.getHeight () / 2) ;

    // Permet la cloture de l'application
    jFrame.addWindowListener (new WindowAdapter ()
    {
      public void windowClosing (WindowEvent e)
      {
        System.exit (0) ;
      }
    }) ;

    // Label
    JLabel label = new JLabel ("Choisissez la méthode de compression :") ;

    // Case a cocher pour determiner la methode de compression
    boutonNormal = new JCheckBox ("Normal") ;
    boutonSansPerte = new JCheckBox ("Sans perte") ;
    boutonAvecPerte = new JCheckBox ("Avec perte") ;

    // Bouton compresser
    JButton BComp = new JButton ("Compresser") ;
    BComp.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
        if (boutonNormal.isSelected ())
          compressionNormal () ;
        if (boutonSansPerte.isSelected ())
          compressionSansPerte () ;
        if (boutonAvecPerte.isSelected ())
          compressionAvecPerte () ;
      }
    }) ;

    // Bouton decompresser
    JButton BDeComp = new JButton ("Décompresser") ;
    BDeComp.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
        decompresser () ;
      }
    }) ;

    // Champ texte pour prevenir l'utilisateur
    textArea = new JTextArea (5, 20) ;
    textArea.setEditable (false) ;
    JScrollPane jFiel = new JScrollPane (textArea) ;
    jFiel.setPreferredSize (new Dimension (250, 100)) ;

    // Ajout des elements a la fenetre
    FlowLayout fLayout = new FlowLayout (FlowLayout.CENTER) ;
    jFrame.getContentPane ().setLayout (fLayout) ;
    jFrame.getContentPane ().add (label, 0) ;
    jFrame.getContentPane ().add (boutonNormal, 1) ;
    jFrame.getContentPane ().add (boutonSansPerte, 2) ;
    jFrame.getContentPane ().add (boutonAvecPerte, 3) ;
    jFrame.getContentPane ().add (BComp, 4) ;
    jFrame.getContentPane ().add (BDeComp, 5) ;
    jFrame.getContentPane ().add (jFiel, 6) ;

    // Rend visible la fenetre
    jFrame.setVisible (true) ;
  }

  // Fenetre pour le choix du fichier
  private static String ouvrirFichier (final String extension)
  {

    JFileChooser chooser = new JFileChooser () ;
    FileFilter filter = new FileFilter ()
    {
      public boolean accept (File f)
      {
        //recupere l'extension
        int index = f.getAbsolutePath ().lastIndexOf ('.') ;
        String ext = "" ;
        if (index != -1)
          ext = f.getAbsolutePath ().substring (index) ;
        else
          //directories
          return true ;
        return (ext.compareTo (extension) == 0);
      }

      public String getDescription ()
      {
        return extension ;
      }
    } ;
    chooser.setFileFilter (filter) ;
    int returnVal = chooser.showOpenDialog (new JFrame ("open")) ;
    if (returnVal == JFileChooser.APPROVE_OPTION)
    {

      System.out.println ("file: " + chooser.getSelectedFile ().getName ()) ;
      return chooser.getSelectedFile ().getAbsolutePath () ;
    }
    return null ;
  }
}
