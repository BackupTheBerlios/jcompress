/*
 * Created on Feb 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package structure;

import java.util.ArrayList;

import structure.types.Attribut;

/**
 * @author m1isi17
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Insert extends Commande {

	//liste de String
	protected ArrayList values=new ArrayList();

	/**
	 * @param nom
	 * @param type
	 */
	public Insert(String nom, int type) {
		super(nom, type);
		// TODO Auto-generated constructor stub
	}
	
	public void ajoutValeur(String val){
		values.add(val);
	}
	
	

	/* (non-Javadoc)
	 * @see structure.Commande#afficher()
	 */
	public void afficher() {
		super.afficher();
		afficherValues();
	}
	
	public void afficherValues (){
		
		for (int i=0; i<values.size();i++)
		{
				System.out.println("valeur : "+(String)values.get(i));
		}
	}
	
	/**
	 * @return Retourne la valeur de l'attribut values.
	 */
	public ArrayList getValues(){
		return values;
	}
	
	/**
	 * @param initialse values avec pValues.
	 */
	public void setValues(ArrayList pValues){
		values = pValues;
	}
}