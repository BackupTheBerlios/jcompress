package JCompress;

/**
 * Date = 21/01/2005 Project = JCompress File name = Element.java
 * 
 * @author Bosse Laure/Fauroux claire
 *  
 */
public abstract class Element {

	protected Element() {
	}

	protected Element(String c) {
		caractere = c;
	}

	protected Element SAG = null;

	protected Element SAD = null;

	protected String caractere = ArbreBinaire.ECHAP;

	protected int frequence = 0;

	/**
	 * @param sad
	 *            The sAD to set.
	 */
	public void setSAD(Element sad) {
		SAD = sad;
	}

	/**
	 * @return Returns the sAD.
	 */
	public Element getSAD() {
		return SAD;
	}

	/**
	 * @return Returns the sAG.
	 */
	public Element getSAG() {
		return SAG;
	}

	/**
	 * @param sag
	 *            The sAG to set.
	 */
	public void setSAG(Element sag) {
		SAG = sag;
	}

	/**
	 * @param caractere
	 *            The caractere to set.
	 */
	public void setCaractere(String caractere) {
		this.caractere = caractere;
	}

	/**
	 * @param frequence
	 *            The frequence to set.
	 */
	public void setFrequence(int frequence) {
		this.frequence = frequence;
	}

	public String getCaractere() {
		return caractere;
	}

	public int getFrequence() {
		return frequence;
	}

	/**
	 * *afficheSeul : affiche l'element sans ses sous arbre
	 */
	public void afficherSeul() {
		System.out.println(" ( " + caractere + " , " + frequence + " )");
	}

	/**
	 * *majFrequence : incremente de 1 la frequence
	 */
	public void majFrequence() {
		frequence++;
	}

	/**
	 * *getNoeudToCode : retourne l'élément de l'arbre ayant pour code compresse code
	 * @param String code compressse
	 * @return Element
	 */
	public Element getNoeudToCode(String code) {
		int i = Integer.parseInt(code.substring(0, 1));
		code = code.substring(1, code.length());

		if (i == 0) {
			if (code.length() == 0)
				return getSAG();
			else
				return getSAG().getNoeudToCode(code);
		}

		if (i == 1) {
			if (code.length() == 0)
				return getSAD();
			else
				return getSAD().getNoeudToCode(code);
		}

		return null;
	}
}