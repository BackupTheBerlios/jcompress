
/**
 * Date 		= 21/01/2005
 * Project		= JCompress
 * File name  	= Noeud.java
 * @author Bosse Laure/Fauroux claire
 *	
 */


public class Noeud extends Element {

	private Element pere=null;
	
	/**
	 * *Noeud : constructeur pour la sentinelle, sans pere
	 * 
	 * @param String
	 *            c
	 */
	public Noeud(String c) {
		super(c);
	}
	
	/**
	 **Noeud : constructeur de caractere c et de pere p
	 * @param String, Element
	 */
	public Noeud(String c, Element p) {
		super(c);
		pere = p;
	}
	
	//ok
	/**
	 **majFrequence : incremente la frequence de 1 pour this
	 * et ses ancetres
	 */
	public void majFrequence()
	{
		//System.out.println("majFrequence de "+getCaractere());
		super.majFrequence();
		getPere().majFrequence();
		//System.out.println("pére de "+getCaractere()+" = "+getPere().getCaractere());
	}

	public void afficher (){
		}
	
	public Element getPere(){
		return pere;
	}

	public void setPere(Element e){
		pere = e;
	}

	/**
	 **isFeuille : retourne true si this est une Feuille
	 * @return boolean
	 */
	public boolean isFeuille(){
	return (SAD == null && SAG == null);
	}
	
	//retourne true si si e est un ancetre de this
	//ok
	/**
	 **isAncetre : retourne true si e est un ancetre de this
	 * @param e : ancetre de this?
	 * @return boolean
	 * 
	 */
	public boolean isAncetre(Element e)
	{
		Element ancetre= getPere();
		//TODO pas de methode isArbre?
		for(;(e !=ancetre) && (!(ancetre instanceof ArbreBinaire));
				ancetre = ((Noeud)ancetre).getPere())		
		{}
		if (e == ancetre)
			return true;
		else
			return false;
		
	}
	
	public boolean estAGauche()
	{
		return (getPere().getSAG()==this);
	}
	public boolean estADroite()
	{
		return (getPere().getSAD()==this);
	}
	
	//ok
	/**
	 **getCodeDansArbreBinaire : retourne le code du noeud dans l'arbre
	 * binaire
	 * @return String
	 * TODO recursif
	 */
	public String getCodeDansArbreBinaire(){
		String code="";
		Element pere = getPere();
		Noeud fils = this;
		if (fils.estAGauche())
			code = "0"+code;
		else
			code = "1"+code;
		while (!(pere instanceof ArbreBinaire))
		{
			fils = (Noeud)pere;
			pere = ((Noeud)pere).getPere();
			if (fils.estAGauche())
			{	
				code = "0"+code;}
			else
				code = "1"+code;
		}
		return code;
	}
	
}
