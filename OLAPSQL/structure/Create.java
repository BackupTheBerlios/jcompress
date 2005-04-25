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
 * Classe représentant la commande CREATE.
 */
public abstract class Create extends Commande{

	//	liste d Attribut
	protected ArrayList attributs;
	
	/**
	 * @param nom
	 * @param type
	 */
	public Create(String nom, int type, ArrayList attrs) {
		super(nom, type);
		attributs = attrs;
	}

	
	
	
	/* (non-Javadoc)
	 * @see structure.Commande#afficher()
	 */
	public void afficher() {
		super.afficher();
		afficherAttributs();
	}
	public void afficherAttributs (){
		
		for (int i=0; i<attributs.size();i++)
		{
			Attribut a = (Attribut)attributs.get(i);
			a.afficher();
		}
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
}
