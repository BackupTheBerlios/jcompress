package structure.types.predicat;

/**
 * Projet : OLAPSQL*PLUS
 * Auteur : 
 * 		Laure Bosse
 * 		Claire Fauroux
 */

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Noeud de l'arbre permettant la liaison entre plusieurs jointures
 */
public class Liaison extends ElementAbstrait {

	public final static String AND = "and";
	public final static String OR = "or";

	//liste de d'Element abstraits
	private ArrayList preds = new ArrayList();
	private String liaison = "";

	public Liaison(Liaison pere, String l){
		super(pere);
		liaison = l;
	}

	public Liaison(Liaison pere, String l, ElementAbstrait p1){
		super(pere);
		liaison = l;
		ajoutElmt(p1);
	}

	public void ajoutElmt(ElementAbstrait elmt){
		preds.add(elmt);
	}

	public static void main(String[] args){
	}

	/*
	 * (non-Javadoc)
	 * @see structure.types.predicat.ElementAbstrait#afficher()
	 */
	public void afficher(){
		System.out.println("Noeud (");
		if(preds != null)
			for(int i = 0 ; i < preds.size() ; i++){
				ElementAbstrait a = (ElementAbstrait) preds.get(i);
				a.afficher();
				if(i < preds.size() - 1)
					System.out.println("\t" + liaison);
			}
		System.out.println(")");
	}

	/*
	 * (non-Javadoc)
	 * @see structure.types.predicat.ElementAbstrait#getJointures()
	 */
	public ArrayList getJointures(){
		ArrayList liste = new ArrayList();
		Iterator it = preds.iterator();
		while(it.hasNext()){
			ElementAbstrait el = (ElementAbstrait) it.next();
			liste.addAll(el.getJointures());
		}
		return liste;
	}

	/*
	 * (non-Javadoc)
	 * @see structure.types.predicat.ElementAbstrait#getSQL()
	 */
	public String getSQL(){
		Iterator it = preds.iterator();
		String sql = "(";
		while(it.hasNext()){
			ElementAbstrait el = (ElementAbstrait) it.next();
			sql += el.getSQL();
			if(it.hasNext())
				sql += " " + liaison + " ";
		}
		sql += ")";
		return sql;
	}

	/*
	 * (non-Javadoc)
	 * @see structure.types.predicat.ElementAbstrait#toString()
	 */
	public String toString(){
		String noeud = " ";

		if(preds != null)
			for(int i = 0 ; i < preds.size() ; i++){
				ElementAbstrait a = (ElementAbstrait) preds.get(i);
				noeud += a.toString();
				if(i < preds.size() - 1)
					noeud += " " + liaison;
			}
		return noeud;
	}

	/**
	 * (non-Javadoc)
	 * @see structure.types.predicat.ElementAbstrait#getSQLMoteur()
	 */
	public String getSQLMoteur(String table){
		ElementAbstrait el;
		String tmp = "(";
		Iterator it = preds.iterator();

		while(it.hasNext()){
			el = (ElementAbstrait) it.next();
			tmp += el.getSQLMoteur(table);
			if(it.hasNext()){
				tmp += " " + liaison + " ";
			}
		}
		tmp += ")";
		return tmp;
	}

}