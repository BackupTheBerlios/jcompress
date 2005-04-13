
package src;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import javaCC.Analyzer;
import javaCC.ParseException;

import semantique.BaseDonnees;
import semantique.Moteur;
import structure.Commande;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Test {

	public static void main(String[] args) {
		
		Analyzer parser=null;
		//String cde= "drop fact test;";
		try {
			FileReader f = new FileReader("T:\\IUP Master 1\\sem2\\BOT\\projet3 BOT\\exemple_test.olapsql");
			if (parser == null)
			{
				parser = new Analyzer (f);
			}
			else
			{
				parser.ReInit(f);
			}
			try {
				//Commande c = parser.execute();
				//System.out.println("type de classe "+c.getClass().toString());
				//System.out.println("type : "+c.getType());
				//System.out.println("nom du fait/dimension: "+c.getNom());
				
				//Predicat c = parser.executePredicat();
				//c.afficher();
				ArrayList l = parser.execute();
				
				Commande c = null;
				for (int i = 0; i<l.size(); i++)
				{   c= (Commande)l.get(i);
				    //c.afficher();
				    Moteur m = new Moteur(c, new BaseDonnees());
				    m.execute();
				}
				
				System.out.println("ok");
			} catch (ParseException e) {
				System.out.println("Erreur de syntaxe OLAPSQL");
				e.printStackTrace();

			}
		
		
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		
	}
}
