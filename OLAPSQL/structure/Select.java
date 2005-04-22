/*
 * Created on Feb 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package structure;

import java.util.ArrayList;

import structure.types.predicat.Predicat;

/**
 * @author m1isi17
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Select extends Commande{

	//liste de String  : "fonc(att)"
	private ArrayList agregs = new ArrayList();
	
	private String nomRow="";
	private String withRow="";
	//liste de String : nom[.nom]?
	private ArrayList rows=new ArrayList();
	
	private String nomColumn="";
	private String withColumn="";
	private ArrayList columns = new ArrayList();
	
	private String nomFrom="";
	private Predicat where=null;
	
	
	
	public Select(String nom, int type){super("",-1);}
	/**
	 * @param nom = ""
	 * @param type = -1
	 */
	public Select(ArrayList l) {
		super("",-1);
		agregs = l;
	}

	
	
	/* (non-Javadoc)
	 * @see structure.Commande#afficher()
	 */
	public void afficher() {
		super.afficher();
		
		System.out.println("liste d'aggregs");
		affString(agregs);
		
		System.out.println("RoW :" +nomRow);
		if (withRow !="")
		{
			System.out.println("with " +withRow);
			affString(rows);
		}
		System.out.println("Column :" +nomColumn);
		if (withColumn !="")
		{
			System.out.println("with " +withColumn);
			affString(columns);
		}
		
		System.out.println("FROM " +nomFrom);
		if (where !=null)where.afficher();
		
	}
	
	public void affString(ArrayList l)
	{
		for (int i=0; i<l.size();i++)
		{
				System.out.println(i+" : "+(String)l.get(i));
		}
	}
	
	
	public void ajoutRow(String s){
		rows.add(s);
	}

	public void ajoutColumn(String s){
		columns.add(s);
	}
	
	/**
	 * @return Returns the agregs.
	 */
	public ArrayList getAgregs() {
		return agregs;
	}
	/**
	 * @param agregs The agregs to set.
	 */
	public void setAgregs(ArrayList agregs) {
		this.agregs = agregs;
	}
	/**
	 * @return Returns the columns.
	 */
	public ArrayList getColumns() {
		return columns;
	}
	/**
	 * @param columns The columns to set.
	 */
	public void setColumns(ArrayList columns) {
		this.columns = columns;
	}
	/**
	 * @return Returns the nomColumn.
	 */
	public String getNomColumn() {
		return nomColumn;
	}
	/**
	 * @param nomColumn The nomColumn to set.
	 */
	public void setNomColumn(String nomColumn) {
		this.nomColumn = nomColumn;
	}
	/**
	 * @return Returns the nomFrom.
	 */
	public String getNomFrom() {
		return nomFrom;
	}
	/**
	 * @param nomFrom The nomFrom to set.
	 */
	public void setNomFrom(String nomFrom) {
		this.nomFrom = nomFrom;
	}
	/**
	 * @return Returns the nomRow.
	 */
	public String getNomRow() {
		return nomRow;
	}
	/**
	 * @param nomRow The nomRow to set.
	 */
	public void setNomRow(String nomRow) {
		this.nomRow = nomRow;
	}
	/**
	 * @return Returns the rows.
	 */
	public ArrayList getRows() {
		return rows;
	}
	/**
	 * @param rows The rows to set.
	 */
	public void setRows(ArrayList rows) {
		this.rows = rows;
	}
	/**
	 * @return Returns the where.
	 */
	public Predicat getWhere() {
		return where;
	}
	/**
	 * @param where The where to set.
	 */
	public void setWhere(Predicat where) {
		this.where = where;
	}
	/**
	 * @return Returns the withColumn.
	 */
	public String getWithColumn() {
		return withColumn;
	}
	/**
	 * @param withColumn The withColumn to set.
	 */
	public void setWithColumn(String withColumn) {
		this.withColumn = withColumn;
	}
	/**
	 * @return Returns the withRow.
	 */
	public String getWithRow() {
		return withRow;
	}
	/**
	 * @param withRow The withRow to set.
	 */
	public void setWithRow(String withRow) {
		this.withRow = withRow;
	}
}
