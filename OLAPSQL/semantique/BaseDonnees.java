package semantique;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import structure.CreateDimension;

public class BaseDonnees {
	private static String url = "jdbc:oracle:thin:@telline.cict.fr:1526:etu923";
	private static String user = "m1isi17";
	private static String passwd = "cict";
	private static Connection conn = null;

	public BaseDonnees(){
		// TODO initialisation de url, user et pwd depuis un fichier conf
	}

	public void connecter(){
		try{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			conn = DriverManager.getConnection(url, user, passwd);
			//conn = DriverManager.getConnection(url,user,passwd);
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

	public void deconnecter(){
		try{
			conn.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

	private boolean exist(String nom, String type){
		// TODO
		return false;
	}

	public boolean existFact(String nom){
		return exist(nom, "F");
	}

	public boolean existDimension(String nom){
		return exist(nom, "D");
	}

	public boolean existHierarchy(String nom){
		return exist(nom, "H");
	}

	public boolean existAttribut(String nomRelation, String nomAttribut){
		// TODO
		return false;
	}
	
	public boolean existHierarchyToDimension(String nomDimension, String nomHierarchy){
		// TODO
		return false;
	}
	
	public boolean existAttributToDimension(String nomDimension, String nomAttribut){
		// TODO
		return false;
	}
	
	public boolean existAttributToFact(String nomFact, String nomAttribut){
		// TODO
		return false;
	}
	
	public int getNumberAttribut(String nomRelation){
		// TODO
		return 0;
	}
	
	public int getNumberDimension(String nomFact){
		// TODO
		return 0;
	}

	public boolean createFact(String nom, ArrayList listeMesure,
			ArrayList listeDimension){
		// TODO
		return false;
	}

	/**
	 * @param dim
	 */
	public void createDimension(CreateDimension dim){
		// TODO Auto-generated method stub

	}

	/**
	 * La dimension est elle reli� au fait ?
	 * @param nomFact Nom du fait
	 * @param nomDimension Nom de la dimension.
	 * @return R�ponse � la question.
	 */
	public boolean factConnectDimension(String nomFact, String nomDimension){
		// TODO
		return false;
	}
}