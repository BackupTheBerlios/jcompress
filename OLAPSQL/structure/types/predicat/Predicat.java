/*
 * Created on Feb 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package structure.types.predicat;

import java.util.ArrayList;

/**
 * @author m1isi17
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Predicat {
	
	protected ElementAbstrait racine= null;
	
	public Predicat (){}
	public Predicat (ElementAbstrait rac){
	racine = rac;	
	}
	
	
	

	/**
	 * @return Returns the racine.
	 */
	public ElementAbstrait getRacine() {
		return racine;
	}
	/**
	 * @param racine The racine to set.
	 */
	public void setRacine(ElementAbstrait racine) {
		this.racine = racine;
	}
	
	public void afficher (){
		
		if (racine !=null) racine.afficher();
	}
	
	/**
	 * Retourne la liste des jointures de l'arbre predicat.
	 * @return
	 * ArrayList
	 */
	public ArrayList getJointures(){
		ArrayList liste = new ArrayList();
		return liste;
	}
}
