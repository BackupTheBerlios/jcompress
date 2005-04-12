package semantique;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import exception.AttributException;
import exception.DimensionException;
import exception.FactException;
import exception.HierarchyException;

import structure.Alter;
import structure.AlterHierarchy;
import structure.Commande;
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
import structure.types.predicat.Jointure;
import structure.types.predicat.Predicat;

public class Semantique {
	private Commande commande;
	private BaseDonnees bd;

	/**
	 * Création d'un objet Semantique permettant l'initialisation de la base de
	 * données et de la commande à analiser.
	 * @param com
	 */
	public Semantique(Commande com){
		bd = new BaseDonnees();
		bd.connecter();
		commande = com;
	}
	
	public Semantique(Commande com, BaseDonnees b){
		bd =b;
		bd.connecter();
		commande = com;
	}

	/**
	 * Ferme la connexion à la base de données.
	 */
	public void close(){
		bd.deconnecter();
	}

	public static void main(String[] argv){
		Drop c = new Drop("produit",Commande.DIMENSION);

		Semantique s = new Semantique(c);
		try{
			s.analyze();
		}
		catch(FactException e){
			e.printStackTrace();
		}
		catch(DimensionException e){
			e.printStackTrace();
		}
		catch(HierarchyException e){
			e.printStackTrace();
		}
		catch(AttributException e){
			e.printStackTrace();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		finally{
			s.close();
		}
		
		System.out.println("Fin");
	}

	/**
	 * Analiser la semantique d'une commande.
	 * @throws FactException
	 * @throws HierarchyException
	 * @throws AttributException
	 * @throws SQLException
	 * @throws DimensionException
	 */
	public void analyze() throws FactException, HierarchyException,
			AttributException, SQLException, DimensionException{
		// TODO un attribut de meme nom peut etre pour plusieurs tables
		if(commande instanceof CreateFact){
			analyzeCreateFact();
		}
		if(commande instanceof CreateDimension){
			analyzeCreateDimension();
		}
		if(commande instanceof Alter){
			analyzeAlter();
		}
		if(commande instanceof AlterHierarchy){
			analyzeAlterHierarchy(); // passe également comme classe Alter
		}
		if(commande instanceof Insert){
			analyzeInsert();
		}
		if(commande instanceof InsertFact){
			analyzeInsertFact();
		}
		if(commande instanceof Drop){
			analyseDrop();
		}
		if(commande instanceof Delete){
			analyseDelete();
		}
		if(commande instanceof Select){
			analyseSelect();
		}
	}

	/**
	 * Analise la sémantique d'une selection.
	 * @throws FactException
	 * @throws DimensionException
	 * @throws HierarchyException
	 * @throws SQLException
	 * @throws AttributException
	 */
	private void analyseSelect() throws FactException,
			DimensionException, HierarchyException, SQLException, AttributException{
		Select select = (Select) commande;

		//fait existe
		if(!bd.existFact(select.getNomFrom()))
			throw new FactException("Le fait '" + select.getNomFrom()
					+ "' n'existe pas.");

		//dimension existe
		if(!bd.existDimension(select.getNomRow()))
			throw new DimensionException("La dimension '" + select.getNomRow()
					+ "' n'exist pas.");
		if(!bd.existDimension(select.getNomColumn()))
			throw new DimensionException("La dimension '"
					+ select.getNomColumn() + "' n'exist pas.");

		//hierarchy existe
		if(!bd.existHierarchy(select.getWithColumn()))
			throw new HierarchyException("La hierarchy '"
					+ select.getWithColumn() + "' n'existe pas.");
		if(!bd.existHierarchy(select.getWithRow()))
			throw new HierarchyException("La hierarchy '" + select.getWithRow()
					+ "' n'existe pas.");

		//hierarchy appartient dim
		if(!bd.existHierarchyToDimension(select.getNomColumn(), select
				.getWithColumn()))
			throw new DimensionException("La hierarchy '"
					+ select.getWithColumn()
					+ "' n'existe pas pour la dimension '"
					+ select.getNomColumn() + "'.");
		if(!bd.existHierarchyToDimension(select.getNomRow(), select
				.getWithRow()))
			throw new DimensionException("La hierarchy '" + select.getWithRow()
					+ "' n'existe pas pour la dimension '" + select.getNomRow()
					+ "'.");

		//parametre de la hierarchy existe
		ArrayList liste = select.getColumns();
		Iterator it = liste.iterator();
		while(it.hasNext()){
			String nomParam = (String) it.next();
			if(!bd.existAttributToHierarchy(select.getWithColumn(), nomParam))
				throw new HierarchyException("L'attribut '" + nomParam
						+ "' n'existe pas pour la hierarchy '"
						+ select.getWithColumn() + "'.");
		}
		liste = select.getRows();
		it = liste.iterator();
		while(it.hasNext()){
			String nomParam = (String) it.next();
			if(!bd.existAttributToHierarchy(select.getWithRow(), nomParam))
				throw new HierarchyException("L'attribut '" + nomParam
						+ "' n'existe pas pour la hierarchy '"
						+ select.getWithRow() + "'.");
		}

		// verif predicat
		Predicat pred = select.getWhere();
		Iterator itJoin = pred.getJointures().iterator();
		while(itJoin.hasNext()){
			Jointure join = (Jointure) itJoin.next();
			String exp = join.getExpr1();
			if(exp.contains(".")){
				String[] s = exp.split(".");
				Jointure join2 = new Jointure(null, s[1], join.getExpr2(), join.getOperateur());
				analyzePredicat(join2, s[0]);
			}
			else{
				analyzePredicat(join, select.getNomFrom());
			}
		}
		
		// TODO verif select
	}

	/**
	 * Analise la sémantique d'un delete.
	 * @throws DimensionException
	 * @throws FactException
	 * @throws SQLException
	 * @throws AttributException
	 */
	private void analyseDelete() throws DimensionException,
			FactException, SQLException, AttributException{
		Delete delete = (Delete) commande;

		// relation exist ?
		if(delete.getType() == Commande.DIMENSION){
			if(!bd.existDimension(delete.getNom()))
				throw new DimensionException(DimensionException.EXIST);
		}
		if(delete.getType() == Commande.FACT){
			if(!bd.existFact(delete.getNom()))
				throw new FactException(FactException.EXIST);
		}

		// verif predicat
		Predicat pred = delete.getPredicat();
		Iterator it = pred.getJointures().iterator();
		while(it.hasNext()){
			Jointure join = (Jointure) it.next();
			analyzePredicat(join, delete.getNom());
		}
	}

	/**
	 * Analise la sémantique d'un drop.
	 * @throws DimensionException
	 * @throws SQLException
	 * @throws FactException
	 */
	// OK
	private void analyseDrop() throws SQLException, DimensionException,
			FactException{
		Drop drop = (Drop) commande;

		if(drop.getType() == Commande.DIMENSION){
			// relation exist ?
			if(!bd.existDimension(drop.getNom()))
				throw new DimensionException("La dimension '"+drop.getNom()+"' n'existe pas.");

			// fait utilise la dim ?
			if(bd.existFactConnectToDimension(drop.getNom())){
				throw new DimensionException(
						"Un ou des fait(s) utilise(nt) la dimension '"+drop.getNom()+"'.");
			}
		}

		if(drop.getType() == Commande.FACT){
			// relation exist ?
			if(!bd.existFact(drop.getNom()))
				throw new FactException("Le fait '"+drop.getNom()+"' n'existe pas.");
		}
	}

	/**
	 * Analise la sémantique d'une insertion dans un fait.
	 * @throws DimensionException
	 * @throws SQLException
	 * @throws AttributException
	 */
	private void analyzeInsertFact() throws DimensionException, SQLException, AttributException{
		InsertFact insert = (InsertFact) commande;

		// nb dimension correspond
		int nbDim = bd.getNumberDimension(insert.getNom());
		if(nbDim != insert.getConnects().size())
			throw new DimensionException(
					"Le nombre de dimension ne correspond pas au fait.");

		// verif dim relié au fait
		Iterator it = insert.getConnects().keySet().iterator();
		while(it.hasNext()){
			String nomDimension = (String) it.next();
			if(bd.existDimension(nomDimension))
				throw new DimensionException(DimensionException.EXIST);
			if(!bd.factConnectDimension(insert.getNom(), nomDimension))
				throw new DimensionException(
						"La dimension n'est pas connecté au fait.");
		}

		// verif validité des predicats
		it = insert.getConnects().keySet().iterator();
		while(it.hasNext()){
			String nomDim = (String) it.next();
			Predicat pred = (Predicat) insert.getConnects().get(nomDim);
			ArrayList listJoin = pred.getJointures();
			Iterator itJoin = listJoin.iterator();
			while(itJoin.hasNext()){
				Jointure join = (Jointure) it.next();
				analyzePredicat(join, nomDim);
			}
		}
		
		// TODO predicat retourne 1 seul enregistrement
	}

	/**
	 * Analise la sémantique d'un insert.
	 * @throws AttributException
	 * @throws DimensionException
	 * @throws SQLException
	 */
	private void analyzeInsert() throws AttributException, SQLException,
			DimensionException{
		Insert insert = (Insert) commande;

		// nomRelation existe
		if(!bd.existDimension(insert.getNom()))
			throw new DimensionException(DimensionException.EXIST);

		// nb valeur correspond
		int nbAttribut = bd.getNumberAttribut(insert.getNom());
		if(nbAttribut != insert.getValues().size())
			throw new AttributException(
					"Le nombre d'attribut ne correspond pas à la dimension.");
	}

	/**
	 * Analise la sémantique d'un alter sur une hierarchy.
	 * @throws HierarchyException
	 * @throws SQLException
	 */
	private void analyzeAlterHierarchy() throws SQLException,
			HierarchyException{
		AlterHierarchy alter = (AlterHierarchy) commande;

		switch(alter.getAlteration()){
			case AlterHierarchy.ADD_HIERARCHY :
				// verifie que les noms de hierarchy n'existe pas déjà
				Iterator it = alter.getAttributs().iterator();
				while(it.hasNext()){
					String nomHierarchy = (String) it.next();
					if(bd.existHierarchy(nomHierarchy))
						throw new HierarchyException(HierarchyException.UNIQUE);
				}
				break;
			case AlterHierarchy.DROP_HIERARCHY :
				// hierarchy deja existante et appartient à la dimension qu'on
				// veut modifier
				Iterator itH = alter.getAttributs().iterator();
				while(itH.hasNext()){
					String nom = (String) itH.next();
					if(!bd.existHierarchy(nom))
						throw new HierarchyException(
								"La hierarchy n'existe pas.");
					if(bd.existHierarchyToDimension(alter.getNom(), nom))
						throw new HierarchyException(
								"La hierarchy n'existe pas pour cette dimension.");
				}
				break;
		}
	}

	/**
	 * Analise la sémantique d'un alter.
	 * @throws AttributException
	 * @throws FactException
	 * @throws SQLException
	 * @throws ExistenceFactException
	 * @throws ExistenceDimensionException
	 */
	private void analyzeAlter() throws DimensionException, AttributException,
			SQLException, FactException{
		Alter alter = (Alter) commande;

		// vérif nom
		if(alter.getType() == Commande.FACT && !bd.existFact(alter.getNom())){
			throw new FactException(FactException.EXIST);
		}
		if(alter.getType() == Commande.DIMENSION
				&& !bd.existDimension(alter.getNom())){
			throw new DimensionException(DimensionException.EXIST);
		}

		switch(alter.getAlteration()){
			case Alter.DISCONNECT :
				// dim existe et dim relié au fait
				ArrayList listeDimension = alter.getAttributs();
				analyzeDimensionExist(listeDimension);
				Iterator itDim = listeDimension.iterator();
				while(itDim.hasNext()){
					String nomDim = (String) itDim.next();
					if(!bd.factConnectDimension(alter.getNom(), nomDim)){
						throw new FactException(FactException.DIMENSION);
					}
				}
				break;
			case Alter.CONNECT :
				// dim existe
				analyzeDimensionExist(alter.getAttributs());
				break;
			case Alter.DROP :
				// attr exist pour le fait
				Iterator it = alter.getAttributs().iterator();
				while(it.hasNext()){
					Attribut att = (Attribut) it.next();
					if(!bd.existAttribut(alter.getNom(), att.getNom()))
						throw new AttributException(AttributException.EXIST);
					if(alter.getType() == Commande.DIMENSION){
						if(!bd.existAttributToDimension(alter.getNom(), att
								.getNom()))
							throw new AttributException(
									"L'attribut n'existe pas pour cette dimension.");
					}
					if(alter.getType() == Commande.FACT){
						if(!bd
								.existAttributToFact(alter.getNom(), att
										.getNom()))
							throw new AttributException(
									"L'attribut n'existe pas pour ce fait.");
					}
				}
				break;
			case Alter.ADD :
				// attr n'existe pas déjà
				Iterator itA = alter.getAttributs().iterator();
				while(itA.hasNext()){
					Attribut att = (Attribut) itA.next();
					if(bd.existAttribut(alter.getNom(), att.getNom()))
						throw new AttributException("L'attribut existe déjà.");
				}
				break;
		}
	}

	/**
	 * Analise la sémantique de la création d'une dimension.
	 * @throws DimensionException
	 * @throws SQLException
	 * @throws UniciteDimensionException
	 * @throws UniciteHierarchyException
	 * @throws ExistenceAttributException
	 */
	// OK
	private void analyzeCreateDimension() throws HierarchyException,
			AttributException, SQLException, DimensionException{
		CreateDimension dim = (CreateDimension) commande;

		// verifie l'unicite du nom de la dimension
		if(bd.existDimension(dim.getNom())){
			throw new DimensionException("La dimension '"+dim.getNom()+"' existe déjà.");
		}

		ArrayList hierarchi = dim.getHierarchys();
		Iterator it = hierarchi.iterator();
		while(it.hasNext()){
			Hierarchy hierar = (Hierarchy) it.next();
			// Verif unicite nom hierarchie
			if(bd.existHierarchy(hierar.getNom())){
				throw new HierarchyException("La hierarchie '"+hierar.getNom()+"' existe déjà.");
			}

			// verif nom param et attr faible present dans les attributs de la
			// dimension
			ArrayList listeLevel = hierar.getLevels();
			Iterator itLevel = listeLevel.iterator();
			while(itLevel.hasNext()){
				Level level = (Level) itLevel.next();

				// nom param present dans la liste des attributs de la dimension
				if(!dim.existAttributs(level.getNom())){
					throw new AttributException("L'attribut '"+level.getNom()+"' est inexistant dans la dimension '"+dim.getNom()+"'.");
				}

				// attributs faibles present ds liste attributs de la dimension
				ArrayList listeAttributFaible = level.getAttributs();
				Iterator itAttributFaible = listeAttributFaible.iterator();
				while(itAttributFaible.hasNext()){
					String nomAttributFaible = (String) itAttributFaible.next();
					if(!dim.existAttributs(nomAttributFaible)){
						throw new AttributException("L'attribut '"+nomAttributFaible+"' est inexistant dans la dimension '"+dim.getNom()+"'.");
					}
				}
			}
		}

		// TODO param racine de hierarchi identique pour toutes les hierarchies
	}

	/**
	 * Analise la sémantique de la création d'un fait.
	 * @throws DimensionException
	 * @throws SQLException
	 * @throws FactException void
	 */
	// OK
	private void analyzeCreateFact() throws DimensionException, SQLException,
			FactException{
		CreateFact fait = (CreateFact) commande;

		// verifie l'unicite du nom du fait
		if(bd.existFact(fait.getNom())){
			throw new FactException("Le fait '"+fait.getNom()+"' existe déjà.");
		}

		// verifie que les dimensions existe
		analyzeDimensionExist(fait.getConnects());
	}

	/**
	 * Analise si la liste de dimension existe.
	 * @param listeDimension
	 * @throws SQLException
	 * @throws DimensionException
	 */
	private void analyzeDimensionExist(ArrayList listeDimension)
			throws SQLException, DimensionException{
		Iterator itDimension = listeDimension.iterator();
		while(itDimension.hasNext()){
			String nomDimension = (String) itDimension.next();
			if(!bd.existDimension(nomDimension)){
				throw new DimensionException("La dimension '"+nomDimension+"' n'existe pas.");
			}
		}
	}

	/**
	 * Analise le predicat.
	 * @param jointure
	 * @param nomRelation
	 * @throws AttributException
	 * @throws SQLException
	 */
	private void analyzePredicat(Jointure jointure, String nomRelation) throws SQLException, AttributException{
		String exp = jointure.getExpr1();
		
		if(!bd.existAttribut(nomRelation, exp))
			throw new AttributException("L'attribut '"+exp+"' n'existe pour '"+nomRelation+"'.");
	}
}