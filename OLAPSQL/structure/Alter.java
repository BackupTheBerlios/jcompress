package structure;

/**
 * Projet : OLAPSQL*PLUS
 * Auteur : 
 * 		Laure Bosse
 * 		Claire Fauroux
 */

import java.util.ArrayList;
import structure.types.Attribut;

/**
 * Classe représentant la commande ALTER.
 */
public class Alter extends Commande{
	
	
	public static final int ADD = 0;
	public static final int DROP = 1;
	public static final int CONNECT = 2;
	public static final int DISCONNECT = 3;
	

	protected int alteration;
	
	//liste d Attributs si ADD
	//liste de String si reste
	protected ArrayList attributs = new ArrayList();	
	
	
	/**
	 * @param nom
	 * @param type
	 */
	public Alter(String nom, int type) {
		super(nom, type);
		// TODO Auto-generated constructor stub
	}
	
	public Alter(String nom, int type, String att) {
		super(nom, type);
		ajoutString(att);
	}
	
	public Alter(String nom, int type, int alter,ArrayList l) {
		super(nom, type);
		setAttributs(l);
		setAlteration(alter);
	}
	public Alter(String nom, int type, int alter) {
		super(nom, type);
		setAlteration(alter);
	}
	
	
	public void ajoutString(String n){
		attributs.add(n);
	}
	
	/**
	 * @return Returns the alteration.
	 */
	public int getAlteration() {
		return alteration;
	}
	/**
	 * @param alteration The alteration to set.
	 */
	public void setAlteration(int alteration) {
		this.alteration = alteration;
	}
	/**
	 * @return Returns the attributs.
	 */
	public ArrayList getAttributs() {
		return attributs;
	}
	/**
	 * @param attributs The attributs to set.
	 */
	public void setAttributs(ArrayList attributs) {
		this.attributs = attributs;
	}
	
	
	/* (non-Javadoc)
	 * @see structure.Commande#afficher()
	 */
	public void afficher() {
		super.afficher();
		
		switch (alteration)
		{
			case ADD: System.out.println("alteration ADD");
						break;
			case DROP: System.out.println("alteration DROP");
			break;
			case CONNECT: System.out.println("alteration CONNECT");
			break;
			case DISCONNECT: System.out.println("alteration DISCONNECT");
			break;
			default:
		}
		if (attributs !=null)
			afficherAttributs();
		
	}
	
	public void afficherAttributs (){
		
		for (int i=0; i<attributs.size();i++)
		{
			if (alteration == ADD)
			{Attribut a = (Attribut)attributs.get(i);
			a.afficher();}
			else
			{
				System.out.println("att : "+(String)attributs.get(i));
			}
		}
	}
}
