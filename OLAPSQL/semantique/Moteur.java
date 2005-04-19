/*
 * Created on 18 mars 2005 To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package semantique;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import src.Appli;
import structure.Alter;
import structure.AlterHierarchy;
import structure.Commande;
import structure.Create;
import structure.CreateDimension;
import structure.CreateFact;
import structure.Delete;
import structure.Drop;
import structure.Insert;
import structure.InsertFact;
import structure.Select;
import structure.types.Attribut;
import structure.types.Hierarchy;
import structure.types.Level;
import structure.types.predicat.Predicat;

/**
 * @author cfauroux To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Moteur {

	private Commande com = null;
	public BaseDonnees bd = null;
	public Connection con = null;

	public Moteur(Commande c, BaseDonnees b){
		com = c;
		bd = b;
		con = BaseDonnees.getConn();
	}

    /**
    	 * Execute la commande com sur la Base bd Les conditions sont dans l ordre
    	 * de probabilite de frequence
    	 * ok
    	 */
    	public void execute (){
    	    if (com != null){
    	        
    	        bd.connecter();
    	        if (com instanceof Select)
    	            executeSelect();
    	        else if (com instanceof Insert)
    	            executeInsert();
    	        	else if (com instanceof Create)
    	        	    executeCreate();
    	        		else if (com instanceof Alter)
    	        		    executeAlter();
    	        			else if (com instanceof Delete)
    	        			    executeDelete();
    	        			else if (com instanceof Drop)
    	        			    executeDrop();
    	        			    else
    	        			        System.out.println("error : command unknown");
    //	       try {
    //            // TODO con.close();
    //        } catch (SQLException e) {
    //            e.printStackTrace();
    //        }
    	    }
    	   
    
    	}

	/**
	 * ok
	 */
	private void executeDrop(){

		//delete la table
		Statement s;
		try{
			s = bd.getConn().createStatement();
			String tname = com.getNom();

			//delete la table
			s.executeUpdate("DROP TABLE  " + tname);
			System.out.println("table droppee : " + tname);

			CallableStatement call = null;
			if(com.getType() == Commande.DIMENSION){
				call = con.prepareCall("{ call GEST_BASE_3D.DROP_DIM(?)}");
				System.out.println("appel de DROP_DIM");
				call.setString(1, tname);
			}
			else{
				call = con.prepareCall("{ call GEST_BASE_3D.DROP_FACT(?)}");
				call.setString(1, tname);
			}

			call.executeUpdate();

		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

    /**
	 * DELETE FROM FACT ventes WHERE montant = 100,00; a tester 
	 * DELETE FROM
	 * DIMENSION produits WHERE idp = 1;
	 */
	private void executeDelete(){
		String tname = com.getNom();
		String p = null;
		if(((Delete) com).getPredicat() != null)
			p = ((Delete) com).getPredicat().toString();
		String t = "";
		switch(com.getType()){
			case Commande.DIMENSION :
				t = "DIMENSION";
				break;
			case Commande.FACT :
				t = "FACT";
				break;
			default :
				break;
		}

		String req = "DELETE FROM " + t;
		if(p != null)
			req += " WHERE" + p;
		System.out.println(" req SQL de delete: " + req);

		try{
			//execution
			Statement s = con.createStatement();
			s.executeUpdate(req);

		}
		catch(SQLException e){
			e.printStackTrace();
		}

	}

	/**
	 * ok
	 */
	private void executeCreate(){
		String tname = com.getNom();
		int idElmt;
		CallableStatement call = null;
		Statement statement;

		//insere une dimension
		if(com instanceof CreateDimension){
			//BASE
			//la pk devrait etre en auto increment
			CreateDimension cd = (CreateDimension) com;
			String cleP = "pk_" + tname;
			String req = "CREATE TABLE " + tname + " (" + "" + cleP
					+ " number(5), " + "constraint pk_" + tname
					+ " PRIMARY KEY (" + cleP + ")";
			ArrayList l = cd.getAttributs();
			for(int i = 0 ; i < l.size() ; i++){
				Attribut att = (Attribut) l.get(i);
				req += ", " + att.toString();
			}
			req += ")";
			System.out.println(req);
			//METABASE
			try{
				statement = con.createStatement();
				boolean resultt = statement.execute(req);
				statement.close();
				idElmt = insereFD(tname, Commande.DIMENSION, cd.getAttributs());

				
				l = cd.getHierarchys();
				for(int i = 0 ; i < l.size() ; i++){
					Hierarchy h = (Hierarchy) l.get(i);					
					newHierarchy (idElmt, h);	
				}
			}
			catch(SQLException e){
				e.printStackTrace();
			}
			//
		}
		else
			if(com instanceof CreateFact){
				//BASE
				CreateFact cf = (CreateFact) com;
				String cleP = "pk_" + tname;
				String req = "CREATE TABLE " + tname + " (" + "" + cleP
						+ " number(5), " + "constraint pk_" + tname
						+ " PRIMARY KEY (" + cleP + ")";
				ArrayList l = cf.getAttributs();
				for(int i = 0 ; i < l.size() ; i++){
					Attribut att = (Attribut) l.get(i);
					req += ", " + att.toString();
				}
				//connects
				l = cf.getConnects();
				for(int i = 0 ; i < l.size() ; i++){
					String cleF = (String) l.get(i);
					req += ", fk_" + cleF + " number(5)";
					req += ", constraint fk_" + tname + "_" + cleF
							+ " FOREIGN KEY (fk_" + cleF + ")" + " REFERENCES "
							+ cleF + " ( pk_" + cleF + ")";
				}
				req += ")";
				System.out.println(req);
				//
				try{
					statement = con.createStatement();
					boolean result = statement.execute(req);
					//
					//METABASE
					idElmt = insereFD(tname, Commande.FACT, cf.getAttributs());
					//liaisons ds meta_star
					call = con.prepareCall("{"
							+ "call GEST_BASE_3D.CONNECT_DIM (?,?)}");
					call.setInt(1, idElmt);

					//connect
					CallableStatement callC = con.prepareCall("{"
							+ "?=call GEST_BASE_3D.GET_ID_ELMT(?,?)}");
					callC.registerOutParameter(1, java.sql.Types.INTEGER);
					callC.setString(3, "D");
					for(int i = 0 ; i < l.size() ; i++){
						String connect = (String) l.get(i);
						callC.setString(2, connect);
						System.out.println(" GEST_BASE_3D.GET_ID_ELMT(" + ""
								+ connect + ", D)");
						callC.execute();
						//recup id dimension
						int idD = callC.getInt(1);

						if(idD != -1){//connecte dim et fait
							call.setInt(2, idD);
							System.out.println(" GEST_BASE_3D.CONNECT_DIM("
									+ "" + idElmt + ", " + idD + ")");
							call.execute();
							System.out.println("liaison star ok:" + idElmt
									+ "," + idD);
						}
						else
							System.out.println("liaison star pas ok:" + idElmt
									+ "," + idD);

					}
				}
				catch(SQLException e){
					e.printStackTrace();
					rollback();
				}
			}

	}

	
	//ok
	private void newHierarchy(int idDim, Hierarchy h){
	    {   
		CallableStatement call;
        try {
            call = con.prepareCall("{ "
            		+ "?=call GEST_BASE_3D.CREATE_HIERARCHY (?,?)}");
        
        call.registerOutParameter(1, java.sql.Types.INTEGER);
		
		call.setInt(2, idDim);
		call.setString(3, h.getNom());
		call.execute();
		System.out.println("Hierarchy ajoutee : " + h.getNom());

		int idH = call.getInt(1);
		//insertions des levs et sslevs
		ArrayList levs = h.getLevels();
		for(int j = 0 ; j < levs.size() ; j++){
			Level lev = (Level) levs.get(j);
			System.out.println("level fort :" + lev.getNom());

			ArrayList slevs = lev.getAttributs();
			for(int t = 0 ; t < slevs.size() ; t++){
				insereLevel(idDim,idH, ((String) slevs.get(t)), t + 1,
						"W");
				System.out.println("level W ajoute : "
						+ (String) slevs.get(t));
			}
			insereLevel(idDim,idH, lev.getNom(), j + 1, "P");
			System.out.println("level P ajoute : " + lev.getNom());
		}
        } catch (SQLException e) {
            e.printStackTrace();
            rollback();
        }
	    }
	}
	
	
	

	/**
	 * Insertion du level nomAtt dans la hierachy idH en position pos
	 * @param idH, l id de la hierarchy dans BASE
	 * @param nomAtt, le nom du level
	 * @param pos, position du level ds hierarchy idH PreCondition : connection
	 *            ouverte
	 */
	//ok
	private void insereLevel(int idD,int idH, String nomAtt, int pos, String typ){
		try{
			//recup l id de l attribut correspondant au level
			CallableStatement call;
			call = con.prepareCall("{ "
					+ "?=call GEST_BASE_3D.GET_ID_ATT (?,?)}");
			call.registerOutParameter(1, java.sql.Types.INTEGER);
			call.setInt(2, idD);
			call.setString(3, nomAtt);
			call.execute();
			int idAtt = call.getInt(1);
			//			

				
			System.out.println("attribut "+nomAtt+ " de id:" +idAtt);
			System.out.println(" GEST_BASE_3D.ADD_LEVEL(" + "" + idH + ", "
					+ idAtt + ", " + pos + ", " + typ + ")");

			//insere le level
			call = con.prepareCall("{ "
					+ "call GEST_BASE_3D.ADD_LEVEL (?,?,?,?)}");
			call.setInt(1, idH);
			call.setInt(2, idAtt);
			call.setInt(3, pos);
			call.setString(4, typ);
			call.execute();
			//

		}
		catch(SQLException e){
			e.printStackTrace();
		}

	}

	/**
	 * Insere un Fait/Dimension et retourne son id ds BASE
	 * @param nom, name
	 * @param typ, typ = FAIT ou DIMENSION
	 * @param atts, liste d Attributs
	 * @return l id de l elmt insere
	 * ok
	 */
	private int insereFD(String nom, int typ, ArrayList atts){
		CallableStatement call;
		int idElmt = 0;
		try{
			call = con.prepareCall("{ "
					+ "?=call GEST_BASE_3D.CREATE_ELEMENT (?,?)}");

			call.registerOutParameter(1, java.sql.Types.INTEGER);
			call.setString(2, nom);

			call.setString(3, getType(typ));
			call.execute();
			idElmt = call.getInt(1);

			//insertion des attributs ds metabase
			call = con.prepareCall("{ "
					+ "?=call GEST_BASE_3D.CREATE_ATT(?,?,?,?,?)}");
			call.registerOutParameter(1, java.sql.Types.INTEGER);
			for(int i = 0 ; i < atts.size() ; i++){
				Attribut att = (Attribut) atts.get(i);
				call.setInt(2, idElmt);
				call.setString(3, att.getNom());
				call.setInt(4, att.getType());
				call.setInt(5, att.getTaille());
				call.setInt(6, att.getPrecision());
				call.execute();
				//call.getInt(1);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}

		return idElmt;
	}

	/**
	 * 
	 */
	private void executeSelect(){
		Select sel = (Select) com;
		String req, tmp, tmp2, group = "";
		int i, idFai, nbEnregistrement = 0;
		ResultSet rs;
		Statement st;
		ArrayList listSelect = new ArrayList();
		ArrayList listDim = new ArrayList();
		ArrayList listIdDim = new ArrayList();
		ArrayList listArray = new ArrayList();
		Iterator it;

		//    	SELECT AVG(montant)
		//    	ROW produits WITH h_prod(categorie, sous-categ)
		//    	COLUMN clients WITH h_geo(pays)
		//    	FROM ventes
		//    	WHERE temps.annee=2004;

		//    	SELECT AVG(montant), categorie, sous-categ, pays
		//		FROM ventes, produits, clients, temps
		//		WHERE ventes*produits
		//		AND ventes*clients
		//		AND ventes*temps
		//		AND annee=2004
		//		GROUP BY categorie, sous-categ, pays;

		// selection des dim pas mentionné ds select
		try{
			st = con.createStatement();
			rs = st.executeQuery("select id from meta_element where name='"
					+ sel.getNomFrom() + "'");
			rs.next();
			i = rs.findColumn("id");
			idFai = rs.getInt(i);

			rs = st.executeQuery("select idd from meta_star where idf="
					+ String.valueOf(idFai));
			while(rs.next()){
				i = rs.findColumn("idd");
				idFai = rs.getInt(i);
				
				listIdDim.add(new Integer(idFai));
			}
			
			it = listIdDim.iterator();
			while(it.hasNext()){
				idFai = ((Integer)it.next()).intValue();
				rs = st.executeQuery("select name from meta_element where id="+String.valueOf(idFai));
				rs.next();
				i = rs.findColumn("name");
				tmp = rs.getString(i);
				
				if(!tmp.equals(sel.getNomColumn()) && !tmp.equals(sel.getNomRow()))
					listDim.add(tmp);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		// select
		req = "select ";
		// attribut du select
		it = sel.getAgregs().iterator();
		while(it.hasNext()){
			tmp = (String) it.next();
			listSelect.add(tmp);
			req += tmp;
			if(it.hasNext())
				req += ", ";
		}
		// attribut de la hierarchie row
		it = sel.getRows().iterator();
		while(it.hasNext()){
			tmp = (String) it.next();
			req += ", " + sel.getNomRow() + ".";
			group += ", " + sel.getNomRow() + ".";
			//tmp2 = sel.getNomRow()+".";
			if(tmp.indexOf(".") == 0){
				i = tmp.indexOf(".");
				String[] s = new String[2];
				s[0] = tmp.substring(0, i);
				s[1] = tmp.substring(i + 1, tmp.length());
				req += s[1];
				group += s[1];
				tmp2 = s[1];
			}
			else{
				req += tmp;
				group += tmp;
				tmp2 = tmp;
			}
			listSelect.add(tmp2);
		}
		// attribut de la hierarchie colomn
		it = sel.getColumns().iterator();
		while(it.hasNext()){
			tmp = (String) it.next();
			req += ", " + sel.getNomColumn() + ".";
			group += ", " + sel.getNomColumn() + ".";
			//tmp2 = sel.getNomColumn() + ".";
			if(tmp.indexOf(".") == 0){
				i = tmp.indexOf(".");
				String[] s = new String[2];
				s[0] = tmp.substring(0, i);
				s[1] = tmp.substring(i + 1, tmp.length());
				req += s[1];
				group += s[1];
				tmp2 = s[1];
			}
			else{
				req += tmp;
				group += tmp;
				tmp2 = tmp;
			}
			listSelect.add(tmp2);
		}

		// from
		req += " from " + sel.getNomFrom();
		req += ", " + sel.getNomRow() + " " + sel.getNomRow();
		req += ", " + sel.getNomColumn() + " " + sel.getNomColumn();
		it = listDim.iterator();
		while(it.hasNext()){
			tmp = (String) it.next();
			req += ", " + tmp +" "+tmp;
		}

		// where
		req += " where " + sel.getWhere().getSQLMoteur(sel.getNomFrom());

		// jointure
		req += " AND ";
		req += "fk_" + sel.getNomRow() + "=" + sel.getNomRow() + ".pk_"
				+ sel.getNomRow();
		req += " AND ";
		req += "fk_" + sel.getNomColumn() + "=" + sel.getNomColumn() + ".pk_"
				+ sel.getNomColumn();

		// jointure avec les dim qui sont pas dans row ou column
		it = listDim.iterator();
		while(it.hasNext()){
			tmp = (String) it.next();
			req += " AND fk_" + tmp +"="+tmp+".pk_"+tmp;
		}

		req += " GROUP BY " + group.substring(2, group.length());

		// TODO execute de la requete
		try{
			String[] nomCol = new String[listSelect.size()];
			for(int j=0 ; j<listSelect.size() ; j++){
				nomCol[j] = (String) listSelect.get(j);
			}
			
			st = con.createStatement();
			rs = st.executeQuery(req);
			while(rs.next()){
				String[] sauv = new String[listSelect.size()];
				for(int j=0 ; j<listSelect.size() ; j++){
					i = rs.findColumn((String) listSelect.get(j));
					tmp = rs.getString(i);
					sauv[j] = tmp;
				}
				listArray.add(sauv);
			}
			
			String[][] table = new String[listArray.size()][listSelect.size()];
			for(int j=0 ; j<listArray.size() ; j++){
				table[j] = (String[]) listArray.get(j);
			}
			TableModel tableModel = new DefaultTableModel(table, nomCol);
			JTable jTable = new JTable();
			// affichage nom colonne
			//this.getContentPane().add(table.getTableHeader(), BorderLayout.NORTH);
			jTable.setModel(tableModel);
			Appli.setTable(jTable);
			
		}
		catch(SQLException e1){
			e1.printStackTrace();
		}
	}

    /**
     * execute une commande de type Alter/AlterHierarchy
     */
    private void executeAlter() {
        if (com instanceof AlterHierarchy)
        {
            AlterHierarchy ca = (AlterHierarchy)com;
            switch (ca.getAlteration()){
            	case AlterHierarchy.ADD_HIERARCHY:   	
            	    	int idElmt =getIdMere (ca);        
                		newHierarchy(idElmt, ca.getHierarchy());   
                		break;
            	case AlterHierarchy.DROP_HIERARCHY : dropHierarchy (ca);break;
            	default: return;
            }
        }
        else
        {
            Alter ca = (Alter)com;
            switch (ca.getAlteration()){        
	            case Alter.ADD: addColumn(ca);break;
	            case Alter.CONNECT: connect(ca);break;
	            case Alter.DISCONNECT: disconnect(ca);break;
	            case Alter.DROP: delColumn(ca);break;
	            default : 	return;
            }  
        }
        
    }
    
    /**
     * @param ca
     * 
     */
    private void dropHierarchy(AlterHierarchy ca) {
        
        
        
        
    }

    //a tester si ok, a mettre un peu partout
    private int getIdMere (Commande c){
        CallableStatement call;
        try {
            call = con.prepareCall("{ " +
            "?=call GEST_BASE_3D.GET_ID_ELMT(?,?)}");
        
        call.registerOutParameter(1, java.sql.Types.INTEGER);
        call.setString(2,c.getNom());
        if (com.getType() == Commande.DIMENSION)
            call.setString(3,"D");
        else
            call.setString(3,"F");
        call.execute();
        return call.getInt(1);
        
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    /**
     * @param ca
     * GET ID retourne -1....je sai pas TODO
     */
    private void addHierarchy(AlterHierarchy ca) {
        //recup id de dimension mere
            int idElmt =getIdMere (ca);        
            newHierarchy(idElmt, ca.getHierarchy());
    }

    /**
     * @param ca
     * ok..pte a ameliorer sur acces BD
     */
    private void disconnect(Alter ca) {
        String req, tname = ca.getNom();
        Statement statement;
//      -- ajout column fk_ dans table
        ArrayList l = ca.getAttributs();
        
        try{
        statement = con.createStatement();
        for (int j = 0; j< l.size();j++){
            String cleF=(String)l.get(j);            
            req = "ALTER TABLE "+tname+" DROP COLUMN "+ "fk_"+cleF;
            boolean result = statement.execute(req);
            System.out.println(req);
        }
        
          //      METABASE TODO
            	//liaisons ds meta_star
        	CallableStatement call = con.prepareCall("{" +
    		"call GEST_BASE_3D.ALT_FACT_DISCONNECT (?,?)}");
        	call.setString(1,tname);
            	
            for (int i = 0; i< l.size();i++){
            	call.setString(2,(String)l.get(i));
            	call.execute();
            }	
        } 
        catch (SQLException e) {
           e.printStackTrace();
           rollback();
       }
        
        
    }


    /**
     * @param ca
     * a tester ok sauf la contrainte
     */
    private void connect(Alter ca) {
 
        //-- BASE
        //-- ajout column fk_magasins dans table ventes qvec contraintes de cles
        //-- METABASE
        //-- insertion liaison dans meta_star
        //--END
        String req, tname = ca.getNom();
        Statement statement;
//      -- ajout column fk_ dans table
        
        try {
        ArrayList l = ca.getAttributs();
        statement = con.createStatement();
        
        //baisse de performances sur mais surete des contraintes
        for (int j = 0; j< l.size();j++){
            String cleF=(String)l.get(j);
            req = "ALTER TABLE "+tname+" ADD "+ "fk_"+cleF+" number(5)";
            
            boolean result = statement.execute(req);
            System.out.println(req);
            
            req= "ALTER TABLE "+tname+" ADD constraint fk_"+tname+"_" +
            		""+ cleF+" FOREIGN KEY(fk_"+cleF+") REFERENCES "+cleF+" (pk_"+cleF+"))"; 
            System.out.println(req);
            //contrainte marche pas...TODO
            //result = statement.execute(req);
            //System.out.println(req);

            //      METABASE
            	//liaisons ds meta_star
            	CallableStatement call = con.prepareCall("{" +
        		"call GEST_BASE_3D.ALT_FACT_CONNECT (?,?)}");
            	//call.setInt(1,idElmt);
            	call.setString(1,tname);
            	
            for (int i = 0; i< l.size();i++){
            	call.setString(2,(String)l.get(i));
            	call.execute();
            }	
        } 	
        }
     catch (SQLException e) {
        e.printStackTrace();
        rollback();  
    }
//        PROCEDURE CONNECT_DIM (idf  meta_element.id%type,
//                                idde meta_element.id%type);
//        
    }
    
    private void rollback(){
        try {
            Statement stm = con.createStatement();
            stm.execute("rollback");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    
    }
    

    //ok
    private void addColumn(Alter com)
    {
        String req, tname = com.getNom();
        Statement statement;
        
        
        try {
        //BASE
        //-- ajout column dans table
        req = "ALTER TABLE "+tname+" ADD ";
        ArrayList l = com.getAttributs();
        for (int j = 0; j< l.size();j++){
            Attribut att  = (Attribut)l.get(j);
            req += att.toString()+",";
        }
        req = req.substring (0,req.length()-1);
        System.out.println(req);
        
        statement = con.createStatement();
        boolean result = statement.execute(req);
        
        //
        //METABASE
        // insertion quantite dans meta_attribute
        //liaison ds meta measure
        
        //recup id de table mere
        CallableStatement call;
        
            call = con.prepareCall("{ " +
            "?=call GEST_BASE_3D.GET_ID_ELMT(?,?)}");
            call.registerOutParameter(1, java.sql.Types.INTEGER);
            call.setString(2,com.getNom());
            if (com.getType() == Commande.DIMENSION)
                call.setString(3,"D");
            else
                call.setString(3,"F");
            
            call.execute();
            int idElmt =call.getInt(1);
            
            //insertion des attributs ds metabase
            call = con.prepareCall("{ " +
            "?=call GEST_BASE_3D.CREATE_ATT(?,?,?,?,?)}");
            call.registerOutParameter(1, java.sql.Types.INTEGER);
            for (int i = 0; i< l.size();i++){
                Attribut att=  (Attribut)l.get(i);
                call.setInt(2,idElmt);
                call.setString(3,att.getNom());
                call.setInt(4, att.getType());
                call.setInt(5,att.getTaille());
                call.setInt(6,att.getPrecision());
                call.execute();  
            }     
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //
    }
    
    
    //ok fait
    //pour dim il faut enlever les levels associes
    private void delColumn(Alter com)
    {
        String req, tname = com.getNom();
        Statement statement;
        
        //BASE
        //-- ajout column dans table
        try {
        req = "ALTER TABLE "+tname+" DROP COLUMN ";
        ArrayList l = com.getAttributs();
        for (int j = 0; j< l.size();j++){
            req += (String)l.get(j)+",";
        }
        req = req.substring (0,req.length()-1);
        System.out.println(req);
        
        statement = con.createStatement();
        boolean result = statement.execute(req);
        
        //
        //METABASE
        // insertion quantite dans meta_attribute
        //liaison ds meta measure
        
        //recup id de table mere
        CallableStatement call;
            call = con.prepareCall("{ " +
            "?=call GEST_BASE_3D.GET_ID_ELMT(?,?)}");
            call.registerOutParameter(1, java.sql.Types.INTEGER);
            call.setString(2,com.getNom());
            if (com.getType() == Commande.DIMENSION)
                call.setString(3,"D");
            else
                call.setString(3,"F");
            
            call.execute();
            int idElmt=0 ;
            idElmt= call.getInt(1);
        
            
            //deletion des attributs ds metabase
            call = con.prepareCall("{ " +
            "call GEST_BASE_3D.DELETE_ATT(?,?)}");
            for (int i = 0; i< l.size();i++){
                call.setInt(1,idElmt);
                call.setString(2,(String)l.get(i));
                call.execute();  
            }     
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //         
    }

    /**
     * a tester
     */
    private void executeInsert() {
        String req="";
        Statement statement=null;
        String tname = com.getNom();
        try {
        if (com instanceof InsertFact)
        {
            InsertFact ci = (InsertFact) com;

            req = "SELECT * from "+tname;
            ResultSet rs = statement.executeQuery( req );
            ResultSetMetaData mdt = rs.getMetaData();
            
            req = "INSERT INTO "+tname+" (";
            String reqS = " VALUES (";
           
                for (int i= 0, v=0; i<mdt.getColumnCount();i++){
                    String colon =mdt.getColumnName(i);
                    req  = colon+", ";
                    if (colon.startsWith("fk_"))
                    {
                        Predicat p = (Predicat)ci.getConnects().get(colon);
                        //on peut mettre le select dans le values??
//                        INSERT INTO "table1" ("column1", "column2", ...)
//                        SELECT "column3", "column4", ...
//                        FROM "table2" 
                    }
                    else
                    {
                        String valeur = (String)ci.getValues().get(v);
                        try{
                            Float t = new Float (valeur);
                            reqS = valeur+", ";
                        }
                        catch (NumberFormatException e){
                            reqS = "'"+valeur+"', ";
                        }
                        v++;
                    }
                }
                
                req = req.substring (0,req.length()-1)+") ";
                reqS = reqS.substring (0,reqS.length()-1)+") ";
                
                req = req +reqS;
                System.out.println(req);
                
                statement = con.createStatement();
                boolean result = statement.execute(req); 
        }
        else
        {
            //rajouter le premier id
            Insert ci = (Insert)com;
            req = ci.toSQL();
            System.out.println(req);
            statement = con.createStatement();
            boolean result = statement.execute(req);
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        
    } 
    
    //return F ou D ou H
    public String getType(int t){
        switch (t)
        {
        case Commande.DIMENSION: 	return "D";		
        case Commande.FACT: 		return "F";     
        default: 					return "H";
        }
    }

}




//
////connect
//CallableStatement callC = con.prepareCall("{" +
//"?=call GEST_BASE_3D.GET_ID_ELMT(?,?)}");
//callC.registerOutParameter(1, java.sql.Types.INTEGER);
//callC.setString(3, "D");
//for (int i = 0; i< l.size();i++){
//  String connect  = (String)l.get(i);
//  callC.setString(2,connect);
//  System.out.println(" GEST_BASE_3D.GET_ID_ELMT(" +
//  		"" + connect+", D)");
//  callC.execute();
//  //recup id dimension
//  int idD = callC.getInt(1);
//  
//  if (idD!=-1)
//      {//connecte dim et fait
//      call.setInt(2, idD);
//      //System.out.println(" GEST_BASE_3D.CONNECT_DIM(" +
//      //		"" + idElmt+", "+idD+")");
//      call.execute();
//      //System.out.println("liaison star ok:"+idElmt+","+idD);
//      }
//  else
//      System.out.println("liaison star pas ok: "+idD);
// } //        
////connect
//CallableStatement callC = con.prepareCall("{" +
//"?=call GEST_BASE_3D.GET_ID_ELMT(?,?)}");
//callC.registerOutParameter(1, java.sql.Types.INTEGER);
//callC.setString(3, "D");
//for (int i = 0; i< l.size();i++){
//  String connect  = (String)l.get(i);
//  callC.setString(2,connect);
//  System.out.println(" GEST_BASE_3D.GET_ID_ELMT(" +
//  		"" + connect+", D)");
//  callC.execute();
//  //recup id dimension
//  int idD = callC.getInt(1);
//  
//  if (idD!=-1)
//      {//connecte dim et fait
//      call.setInt(2, idD);
//      //System.out.println(" GEST_BASE_3D.CONNECT_DIM(" +
//      //		"" + idElmt+", "+idD+")");
//      call.execute();
//      //System.out.println("liaison star ok:"+idElmt+","+idD);
//      }
//  else
//      System.out.println("liaison star pas ok: "+idD);
// } 