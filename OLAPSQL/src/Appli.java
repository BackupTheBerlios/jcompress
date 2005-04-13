
package src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import javaCC.Analyzer;
import javaCC.ParseException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

import semantique.BaseDonnees;
import semantique.Moteur;
import semantique.Semantique;
import structure.Commande;
import exception.AttributException;
import exception.DimensionException;
import exception.FactException;
import exception.HierarchyException;
import exception.PredicatException;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

//Base de donnees communes a semantique et moteur

//TODO commentaires dans parser --
public class Appli {

    private  String NEW_LINE = "\n";

    private  JFrame jFrame =new JFrame("OLAPSQL");
	private  JTextArea textArea=null;
	private  JButton boutonOuvrir=null;
	private  String chemin ="T:\\IUP Master 1\\sem2\\BOT\\projet3 BOT";
	private JTable table = null;
	static Analyzer parser=null;
    
	
	
    /**
     */
    public Appli(){
    }
    
    public static void main(String[] args) {
        Appli test = new Appli();
        test.init();
    }

    /**
     * 
     */
    void init() {
		// Initialisation de la fenetre
		//jFrame = new JFrame("OLAPSQL");
		jFrame.setSize(500, 310);

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
		
		//Bouton OuvrirFichier
		boutonOuvrir = new JButton("requete OLAPSQL");
		boutonOuvrir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String fichierSource = ouvrirFichier(".olapsql");
				if(fichierSource != null){
				    System.out.println(fichierSource);
				    //TODO effacer le textArea
				    analyser(fichierSource);
				}
				else
				    textArea.append("Fichier inconnu." + NEW_LINE);
			}});
		// Champ texte pour prevenir l'utilisateur
		textArea = new JTextArea(5, 20);
		textArea.setEditable(false);
		JScrollPane jFiel = new JScrollPane(textArea);
		jFiel.setPreferredSize(new Dimension(250, 100));
		
		//Ajout des elements a la fenetre
		FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
		jFrame.getContentPane().setLayout(layout);
		jFrame.getContentPane().add(new JLabel("LOG:"));
		jFrame.getContentPane().add(textArea,BorderLayout.NORTH);
		jFrame.getContentPane().add(boutonOuvrir,BorderLayout.CENTER);
		
		if(table!=null)
			jFrame.getContentPane().add(table);
		
		//Rend visible la fenetre
		jFrame.setVisible(true);
    }
    
    /**
     * @param fichierSource, nom du fichier contenant requetes
     */void analyser(String fichierSource) {

        try {
			FileReader f = new FileReader(fichierSource);
			if (parser == null)
			{
				parser = new Analyzer (f);
			}
			else
			{
				parser.ReInit(f);
			}
			try {
				ArrayList l = parser.execute();
				BaseDonnees bd = new BaseDonnees();
				try {			    
					for (int i = 0; i<l.size();i++){
					    bd.connecter();
					    Commande c  = (Commande)l.get(i);
					    c.afficher();
					    Semantique s = new Semantique(c, bd);
					    s.analyze();
					    System.out.println("analyze ok");
					    Moteur m = new Moteur (c, bd);
					    m.execute();
						bd.deconnecter();
					}
					
				} catch (FactException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (DimensionException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (HierarchyException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (AttributException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				catch(SQLException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch(PredicatException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally{
				    bd.deconnecter();    
				}
				
				
				
				
				System.out.println("ok");
			} catch (ParseException e) {
				System.out.println("Erreur de syntaxe OLAPSQL");
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
    }

    /**
	 * Ouvre une fenetre permettant de choisir un fichier ayant pour extension
	 * le parametre.
	 * @param extension Type d'extension (exemple : ".txt").
	 * @return Chemin du ficheir choisi par l'utilisateur
	 */
	private String ouvrirFichier(final String extension){

		JFileChooser chooser = new JFileChooser();
		//chemin
		chooser.setCurrentDirectory(new File(chemin));
		// titre
		chooser.setDialogTitle("test");
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

			System.out.println("file: " + chooser.getSelectedFile().getName());
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}
	
	
    /**
     * @return Returns the table.
     */
    public JTable getTable() {
        return table;
    }
    /**
     * @param table The table to set.
     */
    public void setTable(JTable table) {
       if (table !=null){
    	this.table = table;
        table.setVisible(true);
        jFrame.getContentPane().add(table);
       }
    }
    
    
}
