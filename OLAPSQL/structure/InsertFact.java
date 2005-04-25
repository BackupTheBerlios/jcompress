package structure;

/**
 * Projet : OLAPSQL*PLUS
 * Auteur : 
 * 		Laure Bosse
 * 		Claire Fauroux
 */
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import structure.types.predicat.Predicat;

/**
 * Classe représentant la commande INSERT d'un fait.
 */
public class InsertFact extends Insert{


	//cle ; String nom du connect
	//valeur : Predicat
	private HashMap connects = new HashMap(); 
	
	/**
	 * @param nom
	 * @param type
	 */
	public InsertFact(String nom, int type) {
		super(nom, type);
	}
	
	public InsertFact(String nom, int type, String premValue) {
		super(nom, type);
		ajoutValeur(premValue);
	}
	
	public void ajoutConnect (String nomC, Predicat p){
		connects.put(nomC, p);
	}
	
	/* (non-Javadoc)
	 * @see structure.Commande#afficher()
	 */
	public void afficher() {
		super.afficher();
		afficherConnects();
	}
	
	public void afficherConnects (){
		
		Set keys=connects.keySet();
		//Iterator it = keys.iterator();
		
		for (Iterator it = keys.iterator(); it.hasNext();)
		{
				String key = (String)it.next();
				System.out.println("connect : key "+key+ " value");
				((Predicat)(connects.get(key))).afficher();
		}
	}

	/**
	 * @return Retourne la valeur de l'attribut connects.
	 */
	public HashMap getConnects(){
		return connects;
	}
	
	/**
	 * @param initialse connects avec pConnects.
	 */
	public void setConnects(HashMap pConnects){
		connects = pConnects;
	}

}
