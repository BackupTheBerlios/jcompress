package semantique;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDonnees {
	private static String url = null;
	private static String user = null;
	private static String passwd = null;
	private static Connection conn = null;

	public BaseDonnees(){
		// initialisation de url, user et pwd depuis un fichier conf
		FileReader f;
		try{
			f = new FileReader("BaseDonnees.conf");
			char[] lu = new char[1];
			String ligne = "";
			int i;

			while(url == null || user == null || passwd == null){
				f.read(lu);
				while(!Character.valueOf(lu[0]).toString().equals("\n")
						&& !Character.valueOf(lu[0]).toString().equals("\r")){
					ligne += lu[0];
					f.read(lu);
				}

				i = ligne.indexOf("=");
				if(i >= 0){
					if(ligne.substring(0, i).equals("url"))
						url = ligne.substring(i + 1, ligne.length());
					if(ligne.substring(0, i).equals("login"))
						user = ligne.substring(i + 1, ligne.length());
					if(ligne.substring(0, i).equals("password"))
						passwd = ligne.substring(i + 1, ligne.length());
				}

				ligne = "";
			}
		}
		catch(IOException e1){
			// Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * permet de se connecter à la base de données. void
	 */
	public void connecter(){
		try{
			//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(url, user, passwd);
			//conn = DriverManager.getConnection(url,user,passwd);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * permet de se déconnecter de la base de données. void
	 */
	public void deconnecter(){
		try{
			conn.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * est-ce que le nom de type type existe ?
	 * @param nom
	 * @param type
	 * @return
	 * @throws SQLException boolean
	 */
	private boolean exist(String nom, String type) throws SQLException{
		Statement st = conn.createStatement();
		ResultSet rs = st
				.executeQuery("select COUNT(*) from meta_element where name='"
						+ nom + "' AND typ='" + type + "'");
		rs.next();
		int i = rs.findColumn("count(*)");
		boolean b = (rs.getInt(i) == 1);

		rs.close();
		st.close();

		return b;
	}

	/**
	 * est-ce que le fait existe ?
	 * @param nom
	 * @return
	 * @throws SQLException boolean
	 */
	public boolean existFact(String nom) throws SQLException{
		return exist(nom, "F");
	}

	/**
	 * est-ce que la dimension existe ?
	 * @param nom
	 * @return
	 * @throws SQLException boolean
	 */
	public boolean existDimension(String nom) throws SQLException{
		return exist(nom, "D");
	}

	/**
	 * est-ce que la hierarchy exist ?
	 * @param nom
	 * @return
	 * @throws SQLException boolean
	 */
	public boolean existHierarchy(String nom) throws SQLException{
		return exist(nom, "H");
	}

	/**
	 * est-ce que l'attribut appartient à la relation ?
	 * @param nomRelation
	 * @param nomAttribut
	 * @return
	 * @throws SQLException boolean
	 */
	public boolean existAttribut(String nomRelation, String nomAttribut)
			throws SQLException{
		int idRel, idAtt, i, nb;
		ResultSet rs;
		Statement st = conn.createStatement();

		//Recuperation id relation
		idRel = getIdRelation(nomRelation);

		//Recuperation id attribut
		if(existAttribut(nomAttribut)){
			idAtt = getIdAttribut(nomAttribut);

			//Recup nb rel avec idRel et idAtt
			rs = st.executeQuery("select count(*) from meta_measure where idf="
					+ idRel + " and idm=" + idAtt);
			rs.next();
			i = rs.findColumn("count(*)");
			nb = rs.getInt(i);

			rs.close();
			st.close();
		}
		else
			nb = 0;

		return (nb == 1);
	}

	/**
	 * @param pNomAttribut
	 * @return boolean
	 * @throws SQLException
	 */
	private boolean existAttribut(String pNomAttribut) throws SQLException{
		int nbAtt, i;
		ResultSet rs;
		Statement st = conn.createStatement();

		rs = st.executeQuery("select count(*) from meta_attribute where name='"
				+ pNomAttribut + "'");
		rs.next();
		i = rs.findColumn("count(*)");
		nbAtt = rs.getInt(i);

		rs.close();
		st.close();
		return (nbAtt != 0);
	}

	/**
	 * est-ce que la hierarchy est reliè à la dimension ?
	 * @param nomDimension
	 * @param nomHierarchy
	 * @return
	 * @throws SQLException boolean
	 */
	public boolean existHierarchyToDimension(String nomDimension,
			String nomHierarchy) throws SQLException{
		int idDim, idHie, i, nbRel;
		ResultSet rs;
		Statement st = conn.createStatement();

		// Récupération id dimension
		idDim = getIdDimension(nomDimension);

		// Récupération id hierarchi
		idHie = getIdHierarchy(nomHierarchy);

		// Récupération nb rel avec id dim et id h
		rs = st.executeQuery("select count(*) from meta_hierarchy where idd="
				+ idDim + " and idh=" + idHie);
		rs.next();
		i = rs.findColumn("count(*)");
		nbRel = rs.getInt(i);

		rs.close();
		st.close();
		return (nbRel == 1);
	}

	/**
	 * est-ce que l'attribut existe dans la hierarchy ?
	 * @param nomHierarchy
	 * @param nomAtt
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean existAttributToHierarchy(String nomHierarchy, String nomAtt)
			throws SQLException{
		int i, nb, idAtt, idHie;
		ResultSet rs;
		Statement st = conn.createStatement();

		//recuperation id Hierarchy
		idHie = getIdHierarchy(nomHierarchy);

		//recuperation id attribut
		if(existAttribut(nomAtt)){
			idAtt = getIdAttribut(nomAtt);

			//nb relation entre les 2
			rs = st.executeQuery("select count(*) from meta_level where idh="
					+ idHie + " and idp=" + idAtt);
			rs.next();
			i = rs.findColumn("count(*)");
			nb = rs.getInt(i);

			rs.close();
			st.close();
		}
		else
			nb = 0;

		return (nb == 1);
	}

	/**
	 * est ce que l'attribut existe dans la dimension ?
	 * @param nomDimension
	 * @param nomAttribut
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean existAttributToDimension(String nomDimension,
			String nomAttribut) throws SQLException{
		int i, nb, idDim, idAtt;
		ResultSet rs;
		Statement st = conn.createStatement();

		//Recuperation id Dimension
		idDim = getIdDimension(nomDimension);

		//Recuperation id Attribut
		if(existAttribut(nomAttribut)){
			idAtt = getIdAttribut(nomAttribut);

			//nb relation entre dim et att existe
			rs = st.executeQuery("select count(*) from meta_measure where idf="
					+ idDim + " and idm=" + idAtt);
			rs.next();
			i = rs.findColumn("count(*)");
			nb = rs.getInt(i);

			rs.close();
			st.close();
		}
		else
			nb = 0;

		return (nb == 1);
	}

	/**
	 * est-ce que l'attribut est relié au fait ?
	 * @param nomFact
	 * @param nomAttribut
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean existAttributToFact(String nomFact, String nomAttribut)
			throws SQLException{
		int i, nb, idFai, idAtt;
		ResultSet rs;
		Statement st = conn.createStatement();

		//Recuperation id Fait
		idFai = getIdFait(nomFact);

		//Recuperation id Attribut
		if(existAttribut(nomAttribut)){
			idAtt = getIdAttribut(nomAttribut);

			//nb relation entre dim et att existe
			rs = st.executeQuery("select count(*) from meta_measure where idf="
					+ idFai + " and idm=" + idAtt);
			rs.next();
			i = rs.findColumn("count(*)");
			nb = rs.getInt(i);

			rs.close();
			st.close();
		}
		else
			nb = 0;

		return (nb == 1);
	}

	public boolean existDimensionToFact(String nomDim, String nomFai)
			throws SQLException{
		int i, idDim, idFai, nb;
		ResultSet rs;
		Statement st = conn.createStatement();

		idDim = getIdDimension(nomDim);

		idFai = getIdFait(nomFai);

		rs = st.executeQuery("select count(*) from meta_star where idf="
				+ idFai + " and idd=" + idDim);
		rs.next();
		i = rs.findColumn("count(*)");
		nb = rs.getInt(i);

		rs.close();
		st.close();

		return (nb == 1);
	}

	/**
	 * retourne le nombre d'attribut associé à la relation
	 * @param nomRelation
	 * @return int
	 * @throws SQLException
	 */
	public int getNumberAttribut(String nomRelation) throws SQLException{
		int idRel, i, nb;
		ResultSet rs;
		Statement st = conn.createStatement();

		//recuperation id relation
		idRel = getIdRelation(nomRelation);

		//recuperation nb attribut associé à la relation
		rs = st.executeQuery("select count(*) from meta_measure where idf="
				+ idRel);
		rs.next();
		i = rs.findColumn("count(*)");
		nb = rs.getInt(i);

		rs.close();
		st.close();

		return nb;
	}

	/**
	 * retourne le nombre de dimension associé au fait
	 * @param nomFact
	 * @return
	 * @throws SQLException int
	 */
	public int getNumberDimension(String nomFact) throws SQLException{
		int idFait, nbDim = 0, i;
		ResultSet rs;
		Statement st = conn.createStatement();

		//Récupération id fait
		idFait = getIdFait(nomFact);

		//Récupération nb dimension
		rs = st.executeQuery("select count(*) from meta_star where idf="
				+ idFait);
		rs.next();
		i = rs.findColumn("count(*)");
		nbDim = rs.getInt(i);

		rs.close();
		st.close();
		return nbDim;
	}

	/**
	 * retourne l'id de l'attribut.
	 * @param nomAttribut
	 * @return int
	 * @throws SQLException
	 */
	private int getIdAttribut(String nomAttribut) throws SQLException{
		int idAtt, i;
		ResultSet rs;
		Statement st = conn.createStatement();

		rs = st.executeQuery("select ida from meta_attribute where name='"
				+ nomAttribut + "'");
		rs.next();
		i = rs.findColumn("ida");
		idAtt = rs.getInt(i);

		rs.close();
		st.close();
		return idAtt;
	}

	/**
	 * retourne l'id de la relation.
	 * @param nomRel
	 * @return
	 * @throws SQLException int
	 */
	private int getIdRelation(String nomRel) throws SQLException{
		int i, idRel;
		ResultSet rs;
		Statement st = conn.createStatement();

		rs = st.executeQuery("select id from meta_element where name='"
				+ nomRel + "'");
		rs.next();
		i = rs.findColumn("id");
		idRel = rs.getInt(i);

		rs.close();
		st.close();
		return idRel;
	}

	/**
	 * retourne l'id du nom du fait
	 * @param nomFait
	 * @return
	 * @throws SQLException int
	 */
	private int getIdFait(String nomFait) throws SQLException{
		int i, idFai;
		ResultSet rs;
		Statement st = conn.createStatement();

		rs = st.executeQuery("select id from meta_element where name='"
				+ nomFait + "' and typ='F'");
		rs.next();
		i = rs.findColumn("id");
		idFai = rs.getInt(i);

		rs.close();
		st.close();
		return idFai;
	}

	/**
	 * retourne id du nom de la dimension
	 * @param nomDimension
	 * @return
	 * @throws SQLException int
	 */
	private int getIdDimension(String nomDimension) throws SQLException{
		int i, idDim;
		ResultSet rs;
		Statement st = conn.createStatement();

		rs = st.executeQuery("select id from meta_element where name='"
				+ nomDimension + "' and typ='D'");
		rs.next();
		i = rs.findColumn("id");
		idDim = rs.getInt(i);

		rs.close();
		st.close();

		return idDim;
	}

	/**
	 * retourne id du nom de la hierarchy
	 * @param nomHierarchy
	 * @return
	 * @throws SQLException int
	 */
	private int getIdHierarchy(String nomHierarchy) throws SQLException{
		int i, idHie;
		ResultSet rs;
		Statement st = conn.createStatement();

		rs = st.executeQuery("select id from meta_element where name='"
				+ nomHierarchy + "' and typ='H'");
		rs.next();
		i = rs.findColumn("id");
		idHie = rs.getInt(i);

		rs.close();
		st.close();

		return idHie;
	}

	/**
	 * La dimension est elle relié au fait ?
	 * @param nomFact Nom du fait
	 * @param nomDimension Nom de la dimension.
	 * @return Réponse à la question.
	 * @throws SQLException
	 */
	public boolean factConnectDimension(String nomFact, String nomDimension)
			throws SQLException{
		int idDim, idFai, nb, i;
		ResultSet rs;
		Statement st = conn.createStatement();

		//recuperation idDim
		if(existDimension(nomDimension) && existFact(nomFact)){
			idDim = getIdDimension(nomDimension);

			//recuperation idFai
			idFai = getIdFait(nomFact);

			//relation existe ?
			rs = st.executeQuery("select count(*) from meta_star where idf="
					+ idFai + " and idd=" + idDim);
			rs.next();
			i = rs.findColumn("count(*)");
			nb = rs.getInt(i);

			rs.close();
			st.close();
		}
		else
			nb = 0;

		return (nb == 1);
	}

	/**
	 * est-ce qu'il existe des fait connecté à la dimension ?
	 * @param nomDimension
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean existFactConnectToDimension(String nomDimension)
			throws SQLException{
		int idDim, i, nb;
		ResultSet rs;
		Statement st = conn.createStatement();

		//Recuperation id dimension
		idDim = getIdDimension(nomDimension);

		//nb fait utilisant la dim
		rs = st.executeQuery("select count(*) from meta_star where idd="
				+ idDim);
		rs.next();
		i = rs.findColumn("count(*)");
		nb = rs.getInt(i);

		rs.close();
		st.close();
		return (nb > 0);
	}

	/**
	 * Predicat select une et une seule donnée ?
	 * @param nomRelation
	 * @param predicat (String)
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean predicatSelectOneRow(String nomRelation, String predicat)
			throws SQLException{
		int i, nb;
		ResultSet rs;
		Statement st = conn.createStatement();

		rs = st.executeQuery("select count(*) from " + nomRelation + " where "
				+ predicat);
		rs.next();
		i = rs.findColumn("count(*)");
		nb = rs.getInt(i);

		rs.close();
		st.close();
		return (nb == 1);
	}

	/**
	 * @param pNom
	 * @return String
	 * @throws SQLException
	 */
	public String getParamRacineHierarchyToDimension(String pNom)
			throws SQLException{
		int idDim, i, idHie, idPar;
		ResultSet rs;
		String nom = "";
		Statement st = conn.createStatement();

		idDim = getIdDimension(pNom);

		rs = st.executeQuery("select idh from meta_hierarchy where idd="
				+ idDim);
		rs.next();
		i = rs.findColumn("idh");
		idHie = rs.getInt(i);

		rs = st.executeQuery("select idp from meta_level where idh=" + idHie
				+ " and pos=1 and typ='P'");
		rs.next();
		i = rs.findColumn("idp");
		idPar = rs.getInt(i);

		rs = st.executeQuery("select name from meta_attribute where ida="
				+ idPar);
		rs.next();
		i = rs.findColumn("name");
		nom = rs.getString(i);

		rs.close();
		st.close();
		return nom;
	}

	/**
	 * @return Returns the conn.
	 */
	public static Connection getConn(){
		return conn;
	}

	/**
	 * @param pNomHierarchy
	 * @param pString
	 * @param pString2
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean isAttributFaible(String pNomHierarchy, String pAttributFort,
			String pAttributFaible) throws SQLException{
		int idAttFor, idAttFai, idHie, i, posFor, posFai;
		ResultSet rs;
		Statement st = conn.createStatement();

		idHie = getIdHierarchy(pNomHierarchy);
		idAttFor = getIdAttribut(pAttributFort);
		idAttFai = getIdAttribut(pAttributFaible);

		rs = st.executeQuery("select pos from meta_level where idh=" + idHie
				+ " and idp=" + idAttFor);
		rs.next();
		i = rs.findColumn("pos");
		posFor = rs.getInt(i);

		rs = st.executeQuery("select pos from meta_level where idh=" + idHie
				+ " and idp=" + idAttFai);
		rs.next();
		i = rs.findColumn("pos");
		posFai = rs.getInt(i);

		return (posFor == posFai);
	}
}