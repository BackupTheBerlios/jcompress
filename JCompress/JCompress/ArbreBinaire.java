/**
 * Date 		= 21/01/2005
 * Project		= JCompress
 * File name  	= ArbreBinaire.java
 * @author Bosse Laure/Fauroux claire
 *	
 */
package JCompress;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Cette classe représente l'arbre binaire de l'application.
 */
public class ArbreBinaire extends Element {

	public static String ECHAP = "ECHAP";
	public static String EOF = "EOF";
	public static String NOEUD = "NOEUD";
	private ArrayList liste = new ArrayList();

	/**
	 **ArbreBinaire : constructeur d un arbre vide : EOF+ECHAP
	 * 
	 */
	public ArbreBinaire() {
		super("Racine");

		SAG = new Noeud(ECHAP, this);
		SAD = new Noeud(EOF, this);

		//caractere sentinelle, a cause de next  de iterator
		liste.add(new Noeud("sentinelle"));
		liste.add(SAG);
		liste.add(SAD);
		liste.add(this);

	}

	/**
	 **ajoutCaractere : ajoute un caractere dans l'arbre
	 * @param c : le caractere
	 */
	public void ajoutCaractere(String c) {

		Noeud nouveau;
		//System.out.println("ajout caractere "+c);
		if (getNoeud(c) == null) {
			//si nouveau noeud
			Noeud echap = (Noeud) getNoeud(ECHAP);
			Element ancienPereEchap = echap.getPere();
			Element nouveauPereEchap = new Noeud(NOEUD, ancienPereEchap);

			nouveauPereEchap.setSAG(echap); //ECHAP
			ancienPereEchap.setSAG(nouveauPereEchap);
			echap.setPere(nouveauPereEchap);

			nouveau = new Noeud(c, nouveauPereEchap);
			nouveauPereEchap.setSAD(nouveau);

			//maj liste
			liste.add(nouveauPereEchap);
			liste.add(nouveau);
			trierListe();
		} else {
			nouveau = (Noeud) getNoeud(c);
		}
		//modifierArbre
		modifierArbre(nouveau);
	}

	/**
	 **modifierArbre:	reorganise l'arbre
	 * @param noeud:	noeud en cours de modification
	 */
	private void modifierArbre(Noeud nouveau) {

		//recup le dernier succ 
		Noeud dernier = dernierSuccMFreq(nouveau);
		if (dernier != null) {
			intervertir(dernier, nouveau);
		}
		//incremente la freq
		nouveau.majFrequence();

	}

	/**
	 **intervertir :	intervertit e1 et e2 dans l'arbre
	 * @param e1, e2
	 */
	private void intervertir(Noeud e1 /*successeur*/
	, Noeud e2 /*noeud courant*/
	) {

		Element p1 = e1.getPere();
		Element p2 = e2.getPere();

		if (e1.estAGauche())
			p1.setSAG(e2);
		else
			p1.setSAD(e2);
		if (e2.estAGauche())
			p2.setSAG(e1);
		else
			p2.setSAD(e1);
		e1.setPere(p2);
		e2.setPere(p1);
		//maj liste
		trierListe();
	}

	/**
	 **dernierSuccMFreq : retourne le dernier successeur non ancetre
	 * de e et ayant la même frequence
	 * @param e 
	 * @return le successeur, null si pas d'ancetre
	 * 
	 */
	private Noeud dernierSuccMFreq(Element e) {

		int listIndex;
		//int frequence;
		Noeud dernierSucc = null;
		Element succ;

		//recup l'index de e dans liste
		listIndex = liste.indexOf(e) + 1;
		if (listIndex > 0) {
			for (; listIndex < liste.size(); listIndex++) {
				succ = (Element) liste.get(listIndex);
				if (!((Noeud) e).isAncetre(succ))
					if (e.getFrequence() == succ.getFrequence())
						dernierSucc = (Noeud) succ;
			}
		}
		return dernierSucc;
	}

	/**
	 ** trierListe :	reconstruis la liste des successeurs de l'arbre
	 */
	private void trierListe() {

		ArrayList a1 = new ArrayList();
		Element e;
		int nb = liste.size() - 1;

		liste.clear();
		liste.add(new Noeud("sentinelle"));

		a1.add(this);
		for (; nb > 0 && a1.size() != 0; nb--) {
			e = (Element) a1.get(0);
			if (e.getSAG() != null)
				a1.add(e.getSAD());
			if (e.getSAD() != null)
				a1.add(e.getSAG());
			liste.add(1, e);
			a1.remove(0);
		}
	}

	/**
	 **getNoeud:	retourne le premier element de this.liste ayant
	 * pour caractere c
	 * @param String c
	 * @return Element, null sinon
	 * 
	 */
	public Element getNoeud(String c) {
		Element e;
		for (Iterator it = liste.iterator(); it.hasNext();) {
			if ((e = (Element) it.next()).getCaractere().equals(c))
				return e;
		}
		return null;
	}

	/**
	 ** afficherListe : affiche l'arbre suivant l'ordre de la liste
	 */
	public void afficherListe() {
		for (Iterator it = liste.iterator(); it.hasNext();) {
			((Element) it.next()).afficherSeul();
		}
	}
}
