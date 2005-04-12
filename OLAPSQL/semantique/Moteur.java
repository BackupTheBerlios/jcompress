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

import structure.Alter;
import structure.Commande;
import structure.Create;
import structure.Delete;
import structure.Drop;
import structure.Insert;
import structure.Select;

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
	
	//TODO execute...
	
	//statistiques
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
	        	    
	        bd.deconnecter();
	    }
	   
	}
	
	/**
     * a tester se connecter!!
     */
    private void executeDrop() {
        
        //delete la table
        Statement s;
        try {
            s = bd.getConn().createStatement();
            String tname = com.getNom();
            
            //delete la table
            //s.executeUpdate(" DROP tname");
            
            CallableStatement call = null;
             if (com.getType() == Commande.DIMENSION)
             {   call = con.prepareCall("{ call DROP_DIM(?)}");
             	 call.setString(1,tname);}
             else
             {   call = con.prepareCall("{ call DROP_FACT(?)}");
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
        String p = ((Delete)com).getPredicat().toString();
        String t="";
        switch (com.getType())
        {
        	case Commande.DIMENSION :  t= "DIMENSION";break;
        	case Commande.FACT :  t= "FACT";break;
        	default:		break;
        }
        
        String req = "DELETE FROM "+t;
        if (p !=null)
            req+= " WHERE"+p+";";
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
     * 
     */
    private void executeCreate() {
        // TODO Auto-generated method stub
        
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

}
