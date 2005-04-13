/*
 * Created on 18 mars 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package semantique;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import structure.Alter;
import structure.Commande;
import structure.Create;
import structure.CreateDimension;
import structure.CreateFact;
import structure.Delete;
import structure.Drop;
import structure.Insert;
import structure.Select;
import structure.types.Attribut;
import structure.types.Hierarchy;
import structure.types.Level;

/**
 * @author cfauroux
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Moteur {

	private Commande com=null;
	public BaseDonnees bd=null;
	public Connection con=null;
	
	public Moteur(Commande c, BaseDonnees b){
		com = c;
		bd = b;
		con=BaseDonnees.getConn();
	}
	
	
	/**
	 * Execute la commande com sur la Base bd
	 * Les conditions sont dans l ordre de probabilite de frequence
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
	       try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	        //bd.deconnecter();
	    }
	   
	}
	
	/**
     * ok pour drop dim
     */
    private void executeDrop() {
        
        //delete la table
        Statement s;
        try {
            s = bd.getConn().createStatement();
            String tname = com.getNom();
            
            //delete la table
            s.executeUpdate("DROP TABLE  " + tname );
            System.out.println("table droppee : "+ tname);

            
            CallableStatement call = null;
             if (com.getType() == Commande.DIMENSION)
             {   call = con.prepareCall("{ call GEST_BASE_3D.DROP_DIM(?)}");
             	System.out.println("appel de DROP_DIM");
             	 call.setString(1,tname);}
             else
             {   call = con.prepareCall("{ call GEST_BASE_3D.DROP_FACT(?)}");
                 call.setString(1,tname);}
             
             call.executeUpdate();
       
        } catch (SQLException e) {
           e.printStackTrace();
        }   
    }

    /**
     * DELETE FROM FACT ventes WHERE montant = 100,00;
a tester
	DELETE FROM DIMENSION produits WHERE idp = 1;	
     */
    private void executeDelete() {
        String tname = com.getNom();
        String p=null;
        if (((Delete)com).getPredicat()!=null)
        	p = ((Delete)com).getPredicat().toString();
        String t="";
        switch (com.getType())
        {
        	case Commande.DIMENSION :  t= "DIMENSION";break;
        	case Commande.FACT :  t= "FACT";break;
        	default:		break;
        }
        
        String req = "DELETE FROM "+t;
        if (p !=null)
           req+= " WHERE"+p;
        System.out.println(" req SQL de delete: "+req);
        
        try {
            //execution
            Statement s = con.createStatement();
            s.executeUpdate(req);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
    }

    /**
     * 
     */
    private void executeAlter() {
        // TODO Auto-generated method stub
        
    }

    /**
     * ok pour dim sauf level
     */
    private void executeCreate() {
        String tname = com.getNom();
        int idElmt;
        CallableStatement call =null;
        Statement statement;
        
        //insere une dimension
        if (com instanceof CreateDimension)
        {
            //BASE
            //la pk devrait etre en auto increment
            CreateDimension cd = (CreateDimension)com;
            String cleP = "pk_"+tname;
            String req = "CREATE TABLE "+tname+" (" +
            		"" + cleP+" number(5), " +
            				"constraint pk_"+tname+" PRIMARY KEY ("+cleP+")" ;
            ArrayList l = cd.getAttributs();
            for (int i = 0; i< l.size();i++){
                Attribut att=  (Attribut)l.get(i);
                req += ", "+att.toString();
            }
            req+=")";
            

            
            //METABASE
            try {               
                statement = con.createStatement();
                boolean result = statement.execute(req);
                System.out.println(req);
                
                statement.close();
                idElmt=insereFD(tname, Commande.DIMENSION,cd.getAttributs());
                
                //creation hierarchies
                call = con.prepareCall("{ " +
        		"?=call GEST_BASE_3D.CREATE_HIERARCHY (?,?)}");
                call.registerOutParameter(1, java.sql.Types.INTEGER);
                l= cd.getHierarchys();
                for (int i = 0; i< l.size();i++){
                    Hierarchy h=  (Hierarchy)l.get(i);
                    call.setInt(2,idElmt);
                    call.setString(3,h.getNom());
                    call.execute();
                    System.out.println("Hierqrchy ajoutee : "+h.getNom());
                    
                    int idH = call.getInt(1);
                    //insertions des levs et sslevs
                    ArrayList levs = h.getLevels();
                    for (int j = 0; j< levs.size();j++){
                        Level lev  = (Level)levs.get(j);
                        System.out.println("level fort :" + lev.getNom());
                        
                        ArrayList slevs = lev.getAttributs();
                        for (int t = 0; t< slevs.size();t++){           
                            insereLevel(idElmt,((String)slevs.get(t)),t+1,"W");
                            System.out.println("level W ajoute : "+ (String)slevs.get(t));
                        }
                        insereLevel(idElmt,lev.getNom(),j+1, "P");
                        System.out.println("level P ajoute : "+lev.getNom());
                    }
                }
            	} catch (SQLException e) {
                e.printStackTrace();}
            //
        }
        else if (com instanceof CreateFact)
        {
            //BASE
            CreateFact cf = (CreateFact)com;
            String cleP = "pk_"+tname;
            String req = "CREATE TABLE "+tname+" (" +
            		"" + cleP+" number(5), " +
            				"constraint pk_"+tname+" PRIMARY KEY ("+cleP+")" ;
            ArrayList l = cf.getAttributs();
            for (int i = 0; i< l.size();i++){
                Attribut att=  (Attribut)l.get(i);
                req += ", "+att.toString() ;
            }
            //connects
            l = cf.getConnects();
            for (int i = 0; i< l.size();i++){
                String cleF = (String)l.get(i);
                req += ", fk_"+cleF+" number(5)";
                req += ", constraint fk_"+tname+"_"+cleF+" FOREIGN KEY (fk_"+cleF+")"+
                		" REFERENCES "+cleF+" ( pk_"+cleF+")";
            }
            req+=")";  
            System.out.println(req);
            //
            try {
                statement = con.createStatement();
                boolean result = statement.execute(req);
                //
                //METABASE
                idElmt = insereFD(tname, Commande.FACT,cf.getAttributs());
                //liaisons ds meta_star
                call = con.prepareCall("{" +
                		"call GEST_BASE_3D.CONNECT_DIM (?,?)}");
                call.setInt(1,idElmt);
                
                //connect
                CallableStatement callC = con.prepareCall("{" +
        		"?=call GEST_BASE_3D.GET_ID_ELMT(?,?)}");
                callC.registerOutParameter(1, java.sql.Types.INTEGER);
                callC.setString(3, "D");
                for (int i = 0; i< l.size();i++){
                    String connect  = (String)l.get(i);
                    callC.setString(2,connect);
                    System.out.println(" GEST_BASE_3D.GET_ID_ELMT(" +
                    		"" + connect+", D)");
                    callC.execute();
                    //recup id dimension
                    //ici ca retourne -1...pkoi? TODO GET_ID_ELMT marche en test unitaire...
                    int idD = callC.getInt(1);
                    
                    if (idD!=-1)
	                    {//connecte dim et fait
	                    call.setInt(2, idD);
	                    System.out.println(" GEST_BASE_3D.CONNECT_DIM(" +
	                    		"" + idElmt+", "+idD+")");
	                    call.execute();
	                    System.out.println("liaison star ok:"+idElmt+","+idD);
	                    }
                    else
                        System.out.println("liaison star pas ok:"+idElmt+","+idD);
                    
                } 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
    }

    /**
     * Insertion du level nomAtt dans la hierachy idH en position pos
     * @param idH, l id de la hierarchy dans BASE
     * @param nomAtt, le nom du level
     * @param pos, position du level ds hierarchy idH
     * PreCondition : connection ouverte
     */
    //insertion du level ne marche pas...procedure pslqs mqrche...pige pas
    private void insereLevel(int idH, String nomAtt, int pos, String typ){
        try {
          //recup l id de l attribut correspondant au level
            CallableStatement call;
            call = con.prepareCall("{ " +
            "?=call GEST_BASE_3D.GET_ID_ATT (?,?)}");
            call.registerOutParameter(1, java.sql.Types.INTEGER);
            call.setInt(2, idH);
            call.setString(3, nomAtt);
            call.execute();
            int idAtt = call.getInt(1);
            //
            
            System.out.println(" GEST_BASE_3D.ADD_LEVEL(" +
            		"" + idH+", "+idAtt+", "+pos+", "+typ+")");
            
            //insere le level
            call = con.prepareCall("{ " +
    		"call GEST_BASE_3D.ADD_LEVEL (?,?,?,?)}");
            call.setInt(1,idH);
            call.setInt(2,idAtt);
            call.setInt(3,pos);
            call.setString(4,typ);
            call.execute();
            //
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * Insere un Fait/Dimension et retourne son id ds BASE
     * @param nom, name
     * @param typ, typ = FAIT ou DIMENSION
     * @param atts, liste d Attributs
     * @return l id de l elmt insere
     */
    //insere un fait ou dimension avec ses attributs
    private int  insereFD(String nom,int typ, ArrayList atts){
        CallableStatement call;
        int idElmt=0;
        try {
            call = con.prepareCall("{ " +
            "?=call GEST_BASE_3D.CREATE_ELEMENT (?,?)}");
            
            call.registerOutParameter(1, java.sql.Types.INTEGER);
            call.setString(2,nom);
            
            call.setString(3,getType(typ));
            call.execute();
            idElmt = call.getInt(1);

            //insertion des attributs ds metabase
            call = con.prepareCall("{ " +
            "?=call GEST_BASE_3D.CREATE_ATT(?,?,?,?,?)}");
            call.registerOutParameter(1, java.sql.Types.INTEGER);
            for (int i = 0; i< atts.size();i++){
                Attribut att=  (Attribut)atts.get(i);
                call.setInt(2,idElmt);
                call.setString(3,att.getNom());
                call.setInt(4, att.getType());
                call.setInt(5,att.getTaille());
                call.setInt(6,att.getPrecision());
                call.execute();
                //call.getInt(1);  
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return idElmt;
    }
    
    /**
     * 
     */
    private void executeInsert() {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     */
    private void executeSelect() {
        // TODO Auto-generated method stub
        
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
