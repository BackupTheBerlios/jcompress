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
    	        try {
    	        con.setAutoCommit(false);
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

    	       con.commit();
    	        } catch (SQLException e) {
                    e.printStackTrace();
                    try {con.rollback();} catch (SQLException e1) {e1.printStackTrace();}
                }
    	        finally{
    	            try {con.setAutoCommit(false);} catch (SQLException e1) {e1.printStackTrace();}
    	        }
    	    }
    	}

    	//ok
 private void drop (String tname, int type){

		//delete la table
		Statement s;
		CallableStatement call=null;
		try{
			s = bd.getConn().createStatement();

			if(type == Commande.DIMENSION){
			    //delete colonnes dans les tables faits
			    String req = "SELECT me2.name from meta_star ms, meta_element me1, meta_element me2 " +
	    		" WHERE me1.name= '"+tname+"' and me1.typ='D' and me1.id=ms.idd and" +
	    				" me2.id=ms.idf";
			    System.out.println(req);
			    ResultSet rs = s.executeQuery(req);
			    while(rs.next()){
			        String fait = rs.getString(1);
			        req= "ALTER TABLE "+fait+" DROP COLUMN fk_"+tname;
			        System.out.println(req);
			        s.execute(req);
			    }
				call = con.prepareCall("{ call GEST_BASE_3D.DROP_DIM(?)}");
				System.out.println("appel de DROP_DIM");
				call.setString(1, tname);
			}
			else{
			    //delete ses dimensions...magouille
			    String req = "SELECT me2.name from meta_star ms, meta_element me1, meta_element me2 " +
			    		" WHERE me1.name= '"+tname+"' and me1.typ='F' and me1.id=ms.idf and" +
			    				" me2.id=ms.idd";
			    System.out.println(req);
			    ResultSet rs = s.executeQuery(req);
			    while(rs.next()){
			        drop(rs.getString(1), Commande.DIMENSION);
			    }
			    call = con.prepareCall("{ call GEST_BASE_3D.DROP_FACT(?)}");
				call.setString(1, tname);
			}
			//delete la table
			s.executeUpdate("DROP TABLE  " + tname);
			System.out.println("table droppee : " + tname);
			drop_sequence(tname, getType(/*com.getType()*/type));

			call.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		finally {
         try {if (call!=null)call.close();} catch (SQLException e1) {e1.printStackTrace();}
     }
 }   	
    	
    	
	/**
	 * drop dimension ok
	 * drop fait ok
	 */
	private void executeDrop(){
	    drop (com.getNom(), com.getType());
	}

    /**
	 * ok fait et dim sans referencement
	 */
	private void executeDelete(){
		String tname = com.getNom();
		String p = null;
		String req="";
		if(((Delete) com).getPredicat() != null)
			p = ((Delete) com).getPredicat().toString();
		String t = "";
		switch(com.getType()){
			case Commande.DIMENSION :
				t = "DIMENSION";
			
				//TODO deletion des tuples concernes dans les faits
					//recup les cles des att a disparaitre select
					//recup fait concernes select 
					//pour chq fait, delete lestuples avec clef = cles boucle+del

				//recup les cles des att a disparaitre select
				//recup fait concernes select
				//pour chq fait, delete lestuples avec clef = cles boucle+del

				break;
			case Commande.FACT :
				t = "FACT";
				break;
			default :
				break;
		}
	
		req = "DELETE FROM " + tname;
		if(p != null)
			req += " WHERE" + p;
		System.out.println(" req SQL de delete: " + req);
		Statement s=null;
		try{
			//execution
			s = con.createStatement();
			int result = s.executeUpdate(req);
			if (result!=0)
			    System.out.println("DELETE TROUVE");
			else
			    System.out.println("DELETE PAS OK");
		}
		catch(SQLException e){
			e.printStackTrace();
			rollback();
		}
		finally{
		if(s!=null){try{s.close();}catch(Exception e){e.printStackTrace();}}
		}
	}

	/**
	 * ok avec sequence
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
			
					
			String req = "CREATE TABLE " + tname + " (" + cleP
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
				sequence(tname, getType(cd.getType()));
				
				
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
				
//				//generate sequence
				String seq = sequence(tname, "F");
				int id=-1;
				if (seq == null ) return;
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
		int i, idFai = 0;
		ResultSet rs;
		Statement st;
		ArrayList listSelect = new ArrayList();
		ArrayList listDim = new ArrayList();
		ArrayList listIdDim = new ArrayList();
		ArrayList listArray = new ArrayList();
		Iterator it;

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
			if(tmp.indexOf(".") >= 0){
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
			if(tmp.indexOf(".") >= 0){
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
		if(sel.getWhere() != null && sel.getRows().size()>0 && sel.getColumns().size()>0){
			req += " where ";
		
			if(sel.getWhere() != null){
				req += sel.getWhere().getSQLMoteur(sel.getNomFrom());

				// jointure
				req += " AND ";
				
				if(sel.getRows().size()>0){
					req += "fk_" + sel.getNomRow() + "=" + sel.getNomRow() + ".pk_"
					+ sel.getNomRow();
					if(sel.getColumns().size()>0)
						req += " AND ";
				}
				
				if(sel.getColumns().size()>0){
					req += "fk_" + sel.getNomColumn() + "=" + sel.getNomColumn() + ".pk_"
					+ sel.getNomColumn();
				}

				if(sel.getRows().size()>0 && sel.getColumns().size()>0){
					// jointure avec les dim qui sont pas dans row ou column
					it = listDim.iterator();
					while(it.hasNext()){
						tmp = (String) it.next();
						req += " AND fk_" + tmp +"="+tmp+".pk_"+tmp;
					}
				}
			}
		}

		req += " GROUP BY " + group.substring(2, group.length());

		try{
			String[] nomCol = new String[listSelect.size()];
			for(int j=0 ; j<listSelect.size() ; j++){
				nomCol[j] = (String) listSelect.get(j);
			}
			
			System.out.println(req);
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
     * ok
     */
    private void dropHierarchy(AlterHierarchy ca) {
        
        CallableStatement call=null;
        try {
            call = con.prepareCall("{ " +
            "?=call GEST_BASE_3D.GET_ID_ELMT(?,?)}");
        
            call.registerOutParameter(1, java.sql.Types.INTEGER);
            call.setString(2,ca.getHierarchy().getNom());
            call.setString(3, "H");
            call.execute();
            int idh = call.getInt(1);
            
            call = con.prepareCall("{ "
            		+ "call GEST_BASE_3D.DELETE_HIERARCHY (?)}");
            call.setInt(1, idh);
		    call.execute();
		    System.out.println("Hierarchy deletee : " + ca.getNom());
        } catch (SQLException e) {
            e.printStackTrace();
            rollback();
        }
        finally {
            try {if (call!=null)call.close();} catch (SQLException e1) {e1.printStackTrace();}
        }
	}

    //a tester si ok, a mettre un peu partout
    private int getIdMere (Commande c){
        CallableStatement call=null;
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
        finally {
            try {if (call!=null)call.close();} catch (SQLException e1) {e1.printStackTrace();}
        }
        
    }
    /**
     * @param ca
     * 
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
        CallableStatement call=null;
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
        	call = con.prepareCall("{" +
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
        
        finally {
            try {if (call!=null)call.close();} catch (SQLException e1) {e1.printStackTrace();}
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
        CallableStatement call=null;
        
        try {
        ArrayList l = ca.getAttributs();
        statement = con.createStatement();
        
        //baisse de performances sur mais modularite des contraintes
        for (int j = 0; j< l.size();j++){
            String cleF=(String)l.get(j);
            req = "ALTER TABLE "+tname+" ADD "+ "fk_"+cleF+" number(5)";
            
            boolean result = statement.execute(req);
            System.out.println(req);
            
            req= "ALTER TABLE "+tname+" ADD CONSTRAINT fk_"+tname+"_" +
            		""+ cleF+" FOREIGN KEY(fk_"+cleF+") REFERENCES "+cleF+" (pk_"+cleF+")"; 
            System.out.println(req);
            result = statement.execute(req);

            //      METABASE
            	//liaisons ds meta_star
            	call = con.prepareCall("{" +
        		"call GEST_BASE_3D.ALT_FACT_CONNECT (?,?)}");
            	//call.setInt(1,idElmt);
            	call.setString(1,tname);
            	
            //for (int i = 0; i< l.size();i++){
            	call.setString(2,cleF);
            	call.execute();
            	
            //}	
        } 	
        }
     catch (SQLException e) {
        e.printStackTrace();
        rollback();  
    }
     finally {
         try {if (call!=null)call.close();} catch (SQLException e1) {e1.printStackTrace();}
     }      
    }
    
    private void rollback(){
        Statement stm=null;
        try {
            con.rollback();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(stm!=null){try{stm.close();}catch(Exception e){e.printStackTrace();}}	    
        }
    }
    
    private void commit(){
        Statement stm=null;
        try {
            con.commit();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(stm!=null){try{stm.close();}catch(Exception e){e.printStackTrace();}}	    
        }
    }
    

    //ok
    private void addColumn(Alter com)
    {
        String req, tname = com.getNom();
        Statement statement;
        CallableStatement call=null;
        
        try {
        //BASE
        //-- ajout column dans table
        req = "ALTER TABLE "+tname+" ADD (";
        ArrayList l = com.getAttributs();
        for (int j = 0; j< l.size();j++){
            Attribut att  = (Attribut)l.get(j);
            req += att.toString()+",";
        }
        req = req.substring (0,req.length()-1)+")";
        System.out.println(req);
        
        statement = con.createStatement();
        boolean result = statement.execute(req);
        
        //
        //METABASE
        // insertion quantite dans meta_attribute
        //liaison ds meta measure
        
        //recup id de table mere
        
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
        finally {
            try {if (call!=null)call.close();} catch (SQLException e1) {e1.printStackTrace();}
        }
    }    
    
    //ok fait
    //ok dim
    private void delColumn(Alter com)
    {
        String req, tname = com.getNom();
        Statement statement;
        CallableStatement call=null;
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
        
//        //recup id de table mere
//            call = con.prepareCall("{ " +
//            "?=call GEST_BASE_3D.GET_ID_ELMT(?,?)}");
//            call.registerOutParameter(1, java.sql.Types.INTEGER);
//            call.setString(2,com.getNom());
//            if (com.getType() == Commande.DIMENSION)
//                call.setString(3,"D");
//            else
//                call.setString(3,"F");
//            
//            call.execute();
//            int idElmt=0 ;
        	int idElmt= getIdMere(com);
        
            
            //deletion des attributs ds metabase
            call = con.prepareCall("{ " +
            "? = call GEST_BASE_3D.DELETE_ATT(?,?)}");
            call.registerOutParameter(1, java.sql.Types.INTEGER);
            for (int i = 0; i< l.size();i++){
                call.setInt(2,idElmt);
                call.setString(3,(String)l.get(i));
                call.execute();
                int idatt = call.getInt(1);
                
                req = "delete from meta_level where meta_level.idp = "+idatt;
                System.out.println(req);
                statement.execute(req);    
            }     
        } catch (SQLException e) {
            e.printStackTrace();
            rollback();
        }
        finally {
            try {if (call!=null)call.close();} catch (SQLException e1) {e1.printStackTrace();}
        }         
    }

    /**
     * ok
     */
    private void executeInsert() {
        String req="";
        Statement statement=null;
        String tname = com.getNom();
        int id;
        try {
          statement = con.createStatement();
        if (com instanceof InsertFact)
        {
            InsertFact ci = (InsertFact) com;

            req = "SELECT * from "+tname;
            ResultSet rs = statement.executeQuery( req );
            ResultSetMetaData mdt = rs.getMetaData();
           
            if ((id = nvID(tname, "F"))==-1)
		    	return;
            
            req = "INSERT INTO "+tname+" (";//pk_"+tname+",";
            String reqS = " VALUES ("+id+",";
           
            System.out.println("nb colonne : "+mdt.getColumnCount());
            int v=0;
            int nbColo = mdt.getColumnCount();
            for (int i= 1; i<=nbColo;i++){
    			System.out.println("-----------------------------------------------");
                    System.out.println("i just avant getCol: " +i);
                    //TODO ca commence a 1, mais je n accede apres la e colonne erreur SQL..pige pas
                    String colon =mdt.getColumnName(i);
                    System.out.println("i : " + i);
                    System.out.println("la");
                    req  += colon+", ";
                    System.out.println(colon);
                    if (colon.startsWith("FK_"))
                    {
                        String tn = colon.substring(3);
                        Predicat p = (Predicat)(ci.getConnects().get( tn.toLowerCase()));
                        if (p==null) return;
                        String reqq = "SELECT pk_"+tn+" FROM "+tn+" WHERE "+p.toString();
                       System.out.println("executeInsert fact_ fk: "+reqq);
                        
                       int fk=0;
                       Statement statement1 = con.createStatement();
                        ResultSet rss = statement1.executeQuery(reqq);
                        //un seul retour cf sujet
                        if  (rss.next())
                        {    fk= rss.getInt(1);
                        }
                        reqS += Integer.toString(fk)+",";
                        statement1.close();
                    }
                    else
                    {
                    	if(!colon.startsWith("PK_")){
                    	System.out.println(v);
                        String valeur = (String)ci.getValues().get(v);
                        
                        if (valeur.contains(","))
                            valeur=valeur.replace(',','.');
                        
                        try{
                            Float t = new Float (valeur);
                            reqS += valeur+", ";
                        }
                        catch (NumberFormatException e){
                            reqS += "'"+valeur+"', ";
                        }
                        v++;
                    	}
                    }
                    System.out.println("i  : "+i);
                    System.out.println("nbColo : "+nbColo);
                }
                
                req = req.substring (0,req.length()-2)+") ";
                reqS = reqS.substring (0,reqS.length()-1)+") ";
                
                req = req +reqS;
                System.out.println(req);
                
                statement = con.createStatement();
                boolean result = statement.execute(req); 
        }
        else
        {
            //dmension
            Insert ci = (Insert)com;
            
            req = "SELECT * from "+tname;
            ResultSet rs = statement.executeQuery( req );
            ResultSetMetaData mdt = rs.getMetaData();
           
            if ((id = nvID(tname, "D"))==-1)
		    	return;
            
            req = "INSERT INTO "+tname+" (pk_"+tname+",";
            String reqS = " VALUES ("+id+",";
           
                for (int i= 0, v=0; i<mdt.getColumnCount()-1;i++){
                    String colon =mdt.getColumnName(i+2);
                    req  += colon+", ";
                    System.out.println(colon);
                    String valeur = (String)(ci.getValues().get(i));
                    try{
                        Float t = new Float (valeur);
                        reqS += valeur+", ";
                    }
                    catch (NumberFormatException e){
                        reqS += "'"+valeur+"', ";
                    }
                    //System.out.println();
                }

                req = req.substring (0,req.length()-2)+") ";
                reqS = reqS.substring (0,reqS.length()-2)+") ";
                
                req = req +reqS;
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
    

    
    //cree une sequence et un trigger associe sur les pk des faits/dimensions
    private String sequence (String name, String type){
        Statement s=null;
		try{
			s = con.createStatement();
			String nomS = "seq_" +type+"_"+ name;
			String req = "CREATE SEQUENCE "+nomS;
			System.out.println(req);
			s.execute(req);
			req = "select "+nomS+".nextval from dual";
			System.out.println(req);
			s.execute(req);
			System.out.println("sequence initialisee");
			return nomS;
		}
		catch(SQLException e){
			e.printStackTrace();
			rollback();
			return "";
		}
		finally{
		if(s!=null){try{s.close();}catch(Exception e){e.printStackTrace();}}
		} 
    }
    
    private void drop_sequence(String name, String type){
        Statement s=null;
		try{
			s = con.createStatement();
			String nomS = "seq_" +type+"_"+ name;
			String req = "DROP SEQUENCE "+nomS;
			System.out.println(req);
			s.execute(req);
			System.out.println("sequence dropee");
		}
		catch(SQLException e){
			e.printStackTrace();
			rollback();
		}
		finally{
		if(s!=null){try{s.close();}catch(Exception e){e.printStackTrace();}}
		} 
        
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


	private int nvID (String tname, String type){
	    int id = -1;
	    
	    try {
            Statement st = con.createStatement();
            
            String req = "SELECT seq_"+type+"_"+ tname+".nextval from dual";
            System.out.println(req);
            ResultSet rs =st.executeQuery(req);
            if (rs.next())
            	id = rs.getInt(1);
            System.out.println("nvID :" + id);
            return id;
            
        } catch (SQLException e) {
            e.printStackTrace();
            rollback();
            return id;
        }
        finally{
            //return id;
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