package src;

import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import src.DemoTableBDD.LudoDemoCellRenderer;


class JDBC {

	private  String url    = "jdbc:oracle:thin:@telline.cict.fr:1526:etu923";
	private  String user="";
	private  String passwd="";
	public  Connection conn=null;
    Color foreColor = new Color( 0, 0, 50 );
    Hashtable couleur = new Hashtable( 10 );
    Color defoBack = new Color( 194, 194, 194);
	
	
	public JDBC(String us, String pass){
	
		user = us;
		passwd = pass;
	
	}
	
	
	public String initconnexion() {
		try {
			
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				//Connection con = DriverManager.getConnection(url,user,passwd);
				//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
				//Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection(url, user, passwd);
			
			System.out.println("connection ok");
			}
			
		catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
			ex.printStackTrace();
			return "SQLException: " + ex.getMessage();
			}
		return "";
}
	
	public void ferme(){
		
		if (conn !=null)
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("connection closed");
	}
	
	public static void main(String[] args) {
		JDBC jd = new JDBC("m1isi13","cict");
		jd.initconnexion();
		
		 Statement stm;
		try {
			stm = jd.conn.createStatement();
			String req = "select * from meta_element";
		
		 JTable JTable1 = new JTable();
		 JTable1.removeAll();
		 ResultSet rs;

			rs = stm.executeQuery(req);
			ResultSetMetaData mdt = rs.getMetaData();
	        int num = mdt.getColumnCount();
	        System.out.println("nb colonne :"+num);
	        //System.out.println("nb enre");
	        DefaultTableModel df = new DefaultTableModel() {   // la ossi c juste pour faire un exemple
	                                   public boolean isCellEditable( int row, int col )
	                                   { if ( (row>>1)<<1 == row ) return true; return false; } };
	        for ( int i = 0; i < num; i++ ) { df.addColumn( mdt.getColumnName( i + 1 ) ); }
	        JTable1.setModel( df );
	        TableCellRenderer tbcH = jd.createDemoHeaderRenderer();
            for ( int i = 0; i < num; i++ )
            {
                TableColumn tc = JTable1.getTableHeader().getColumnModel().getColumn( i );
                //tc.setHeaderValue( mdt.getColumnName( i + 1 ) );
                //if ( (int)((i+1)/2) == (int)((i)/2) )
                //if ( (i>>1)<<1 == i )
//                JTable1.getColumnModel().getColumn()
                tc.setHeaderRenderer( tbcH );
                //tc.setCellRenderer( new DefaultCellEditor );
            }
            int line = 0;
            while ( rs.next() )
            {
                Vector v = new Vector( 10 );
                for ( int col = 0; col < num; col++ )
                    v.addElement( rs.getString( col  + 1 ) );
                df.addRow( v );
                line++;
            }

			Appli test = new Appli();
			//test.init();
			test.setTable(JTable1);
	        test.init();
			rs.close();
	        
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		jd.ferme();
	}

	private TableCellRenderer createDemoHeaderRenderer() 
    {
	    DefaultTableCellRenderer label = new 
	        DefaultTableCellRenderer() 
	        {
	            public Component getTableCellRendererComponent(JTable table, Object value,
                         boolean isSelected, boolean hasFocus, int row, int column) 
                {
                    setText((value == null) ? "" : value.toString());
	                if (table != null) 
	                {
	                    JTableHeader header = table.getTableHeader();
	                    if (header != null) 
	                    {
//	                        String nom = value.toString();
	                        Color back = (Color)couleur.get( getText() ); // recherche une couleur pour cette colonne
	                        setForeground( foreColor );
	                        setBackground( (back==null)?defoBack:back );
	                        setFont(header.getFont());
            	        }
                    }
		            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
	                return this;
                }
	        };
	    label.setHorizontalAlignment(JLabel.CENTER);
	    return label;
    }

}
