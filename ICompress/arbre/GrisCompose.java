/**
 * Date = 15/01/2005 
 * Project = ICompress 
 * File name = GrisCompose.java
 * @author Bosse Laure/Fauroux claire 
 * 
 * Ce projet permet la compression et la
 *         decompression de fichier PGM de type P5 et P2.
 */

package arbre;

import ressources.FichierSource;
import ressources.Matrice;
import ressources.Symbole;
import arbre.Noeud;

/**
 * Représente un noeud de l'arbre.
 */
public final class GrisCompose extends Noeud {

	private Noeud NO = null;
	private Noeud NE = null;
	private Noeud SO = null;
	private Noeud SE = null;

	/**
	 * Constructeur.
	 * @param p Pere du noeud.
	 */
	public GrisCompose(Noeud p){
		super(p);
	}

	/**
	 * Constructeur.
	 * @param p Pere du noeud.
	 * @param no Noeud nord ouest.
	 * @param ne Noeud nord est.
	 * @param so Noeud sud ouest.
	 * @param se Noeud sud est.
	 */
	public GrisCompose(Noeud p, Noeud no, Noeud ne, Noeud so, Noeud se){
		super(p);
		NO = no;
		NE = ne;
		SO = so;
		SE = se;
	}

	/**
	 * Constructeur.
	 * @param p Pere du noeud.
	 * @param f Fichier permettant de construire le noeud.
	 */
	public GrisCompose(Noeud p, FichierSource f){
		super(p);
		Symbole car;
		String orientation = Matrice.NORD_OUEST;
		while(!orientation.equals("")){
			car = f.nextSymbole();
			if(car == null){
				orientation = "";
			}
			else{
				if(car.getType().equals(Symbole.P_OUVRANTE)){
					Noeud tmp = new GrisCompose(this, f);
					orientation = ajouteNoeud(tmp, orientation);
				}
				if(car.getType().equals(Symbole.NOMBRE)){
					Noeud tmp = new Couleur(this, Integer.parseInt(car
							.getValeur()));
					orientation = ajouteNoeud(tmp, orientation);
				}
			}
		}
	}

	/**
	 * Ajoute le noeud a la position demande.
	 * @param tmp Noeud a ajouter.
	 * @param s Position du noeud a ajouter.
	 * @return Position du prochain noeud.
	 */
	private String ajouteNoeud(Noeud tmp, String s){
		if(s.equals(Matrice.SUD_EST)){
			s = "";
			SE = tmp;
		}
		if(s.equals(Matrice.SUD_OUEST)){
			s = Matrice.SUD_EST;
			SO = tmp;
		}
		if(s.equals(Matrice.NORD_EST)){
			s = Matrice.SUD_OUEST;
			NE = tmp;
		}
		if(s.equals(Matrice.NORD_OUEST)){
			s = Matrice.NORD_EST;
			NO = tmp;
		}
		return s;
	}

	/**
	 * @return Noeud nord ouest.
	 */
	public Noeud getNO(){
		return NO;
	}

	/**
	 * @return Noeud nord est.
	 */
	public Noeud getNE(){
		return NE;
	}

	/**
	 * @return Noeud sud ouest.
	 */
	public Noeud getSO(){
		return SO;
	}

	/**
	 * @return Noeud sud est.
	 */
	public Noeud getSE(){
		return SE;
	}

	/**
	 * @param pNo Noeud nord ouest.
	 */
	public void setNO(Noeud pNo){
		NO = pNo;
	}

	/**
	 * @param pNo Noeud nord est.
	 */
	public void setNE(Noeud pNe){
		NE = pNe;
	}

	/**
	 * @param pNo Noeud sud ouest.
	 */
	public void setSO(Noeud pSo){
		SO = pSo;
	}

	/**
	 * @param pNo Noeud sud est.
	 */
	public void setSE(Noeud pSe){
		SE = pSe;
	}

	/**
	 * @see arbre.Noeud#construireLigne()
	 */
	public String construireLigne(){
		String ligne = "( ";
		ligne += NO.construireLigne();
		ligne += " ";
		ligne += NE.construireLigne();
		ligne += " ";
		ligne += SO.construireLigne();
		ligne += " ";
		ligne += SE.construireLigne();
		ligne += " )";
		return ligne;
	}

	/**
	 * @see arbre.Noeud#getProfondeur()
	 */
	public int getProfondeur(){
		int max = NO.getProfondeur();
		int iNe = NE.getProfondeur();
		int iSo = SO.getProfondeur();
		int iSe = SE.getProfondeur();

		if(iNe > max)
			max = iNe;
		if(iSo > max)
			max = iSo;
		if(iSe > max)
			max = iSe;
		max++;

		return max;
	}

	/**
	 * @see arbre.Noeud#construireMatrice(ressources.Matrice)
	 */
	public Matrice construireMatrice(){
		Matrice matNo = NO.construireMatrice();
		Matrice matNe = NE.construireMatrice();
		Matrice matSo = SO.construireMatrice();
		Matrice matSe = SE.construireMatrice();

		int taille = matNo.getTaille();
		if(taille < matNe.getTaille())
			taille = matNe.getTaille();
		if(taille < matSo.getTaille())
			taille = matSo.getTaille();
		if(taille < matSe.getTaille())
			taille = matSe.getTaille();

		Matrice mat = new Matrice(taille * 2);
		mat.ajoutMatrice(matNo.agrandiMatrice(taille), Matrice.NORD_OUEST);
		mat.ajoutMatrice(matNe.agrandiMatrice(taille), Matrice.NORD_EST);
		mat.ajoutMatrice(matSo.agrandiMatrice(taille), Matrice.SUD_OUEST);
		mat.ajoutMatrice(matSe.agrandiMatrice(taille), Matrice.SUD_EST);
		return mat;
	}

	/**
	 * Retourne le nombre de feuille du noeud
	 * @return int, nb de feuille
	 */
	public int grandeurNoeud(){
		int som = 0;
		if(NO != null)
			som += NO.grandeurNoeud();
		if(NE != null)
			som += NE.grandeurNoeud();
		if(SO != null)
			som += SO.grandeurNoeud();
		if(SE != null)
			som += SE.grandeurNoeud();

		return som;
	}
}