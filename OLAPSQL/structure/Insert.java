package structure;

/**
 * Projet : OLAPSQL*PLUS
 * Auteur : 
 * 		Laure Bosse
 * 		Claire Fauroux
 */

import java.util.ArrayList;

/**
 * Classe représentant la commande INSERT.
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
	
	public String toSQL ()
	{
	    String req = "INSERT INTO "+getNom()+" VALUES (null,";
	    
	    for (int i = 0; i<values.size();i++)
	    {
	        String val = ((String) values.get(i));
	        try{
		        Float t = new Float(val);	
	        }
	        catch (NumberFormatException e){
	            val = "'"+val+"'";
	        }
	        
            req+= val+",";
	    }
	    req = (req.substring (0,req.length()-1))+")";
	    return req;
	
	    
	    
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