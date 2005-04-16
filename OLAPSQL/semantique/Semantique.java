package semantique;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import exception.AttributException;
import exception.DimensionException;
import exception.FactException;
import exception.HierarchyException;
import exception.PredicatException;

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
//		SELECT AVG(montant)
//		ROW produits WITH h_produit (categorie, souscateg)
//		COLUMN clients WITH h_geogr (pays, ville, idc.nom)
//		FROM ventes
//		WHERE temps.annee = 2004;
		
		Jointure j = new Jointure(null, "temps.annee", "2004", Jointure.EGAL);
		Predicat p = new Predicat(j);

		Select c = new Select("", Commande.FACT);
		c.setNomFrom("vente");

		c.setNomColumn("client");
		c.setWithColumn("h_geogr");
		ArrayList l = new ArrayList();
		l.add("Pays");
		l.add("Ville");
		l.add("idc.Nom");
		c.setColumns(l);

		c.setNomRow("produit");
		c.setWithRow("h_produit");
		l = new ArrayList();
		l.add("categorie");
		l.add("souscateg");
		c.setRows(l);

		l = new ArrayList();
		l.add("AVG(montant)");
		c.setAgregs(l);
		
		c.setWhere(p);

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
		catch(PredicatException e){
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
	 * @throws DimensionException
	 * @throws SQLException
	 * @throws AttributException
	 * @throws PredicatException
	 */
	public void analyze() throws FactException, HierarchyException,
			DimensionException, SQLException, AttributException,
			PredicatException{

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
	 * @throws PredicatException
	 */
	private void analyseSelect() throws FactException, DimensionException,
			HierarchyException, SQLException, AttributException,
			PredicatException{
		Select select = (Select) commande;

		//fait existe
		if(!bd.existFact(select.getNomFrom()))
			throw new FactException("Le fait '" + select.getNomFrom()
					+ "' n'existe pas.");

		//dimension existe
		if(!bd.existDimension(select.getNomRow()))
			throw new DimensionException("La dimension '" + select.getNomRow()
					+ "' n'existe pas.");
		if(!bd.existDimension(select.getNomColumn()))
			throw new DimensionException("La dimension '"
					+ select.getNomColumn() + "' n'existe pas.");

		//dimension relié au fait
		if(!bd.existDimensionToFact(select.getNomRow(), select.getNomFrom()))
			throw new DimensionException("La dimension '" + select.getNomRow()
					+ "' n'est pas connecté au fait '" + select.getNomFrom()
					+ "'.");
		if(!bd.existDimensionToFact(select.getNomColumn(), select.getNomFrom()))
			throw new DimensionException("La dimension '"
					+ select.getNomColumn() + "' n'est pas connecté au fait '"
					+ select.getNomFrom() + "'.");

		ArrayList liste;
		Iterator it;
		if(!select.getWithRow().equals("")){
			//hierarchy existe
			if(!bd.existHierarchy(select.getWithRow()))
				throw new HierarchyException("La hierarchy '"
						+ select.getWithRow() + "' n'existe pas.");

			//hierarchy appartient dim
			if(!bd.existHierarchyToDimension(select.getNomRow(), select
					.getWithRow()))
				throw new DimensionException("La hierarchy '"
						+ select.getWithRow()
						+ "' n'existe pas pour la dimension '"
						+ select.getNomRow() + "'.");

			//parametre de la hierarchy existe
			liste = select.getRows();
			if(liste.size() > 0){
				it = liste.iterator();
				while(it.hasNext()){
					String nomParam = (String) it.next();
					if(nomParam.contains(".")){
						analyzeAtribut(nomParam, select.getWithRow());
					}
					else{
						if(!bd.existAttributToHierarchy(select.getWithRow(),
								nomParam))
							throw new HierarchyException("L'attribut '"
									+ nomParam
									+ "' n'existe pas pour la hierarchy '"
									+ select.getWithRow() + "'.");
					}
				}
			}
			else
				throw new AttributException(
						"Le with de ROW doit comporter des attributs.");
		}

		if(!select.getWithColumn().equals("")){
			//hierarchy existe
			if(!bd.existHierarchy(select.getWithColumn()))
				throw new HierarchyException("La hierarchy '"
						+ select.getWithColumn() + "' n'existe pas.");

			//hierarchy appartient dim
			if(!bd.existHierarchyToDimension(select.getNomColumn(), select
					.getWithColumn()))
				throw new DimensionException("La hierarchy '"
						+ select.getWithColumn()
						+ "' n'existe pas pour la dimension '"
						+ select.getNomColumn() + "'.");

			//parametre de la hierarchy existe
			liste = select.getColumns();
			if(liste.size() > 0){
				it = liste.iterator();
				while(it.hasNext()){
					String nomParam = (String) it.next();
					if(nomParam.contains(".")){
						analyzeAtribut(nomParam, select.getWithColumn());
					}
					else{
						if(!bd.existAttributToHierarchy(select.getWithColumn(),
								nomParam))
							throw new HierarchyException("L'attribut '" + nomParam
									+ "' n'existe pas pour la hierarchy '"
									+ select.getWithColumn() + "'.");
					}
				}
			}
			else
				throw new AttributException(
						"Le with de COLUMN doit comporter des attributs.");
		}

		Predicat pred = select.getWhere();
		if(pred != null){
			Iterator itJoin = pred.getJointures().iterator();
			while(itJoin.hasNext()){
				// verif validite predicat
				Jointure join = (Jointure) itJoin.next();
				String exp = join.getExpr1();
				if(exp.contains(".")){
					// Séparation -> point
					int indicePt = exp.indexOf(".");
					String[] s = new String[2];
					s[0] = exp.substring(0, indicePt);
					s[1] = exp.substring(indicePt + 1, exp.length());

					if(!bd.existDimension(s[0]))
						throw new DimensionException("La dimension '" + s[0]
								+ "' n'existe pas.");

					if(!bd.existDimensionToFact(s[0], select.getNomFrom()))
						throw new DimensionException("La dimension '" + s[0]
								+ "' n'est pas connecté au fait '"
								+ select.getNomFrom() + "'.");

					Jointure join2 = new Jointure(null, s[1], join.getExpr2(),
							join.getOperateur());
					analyzePredicat(join2, s[0]);
				}
				else{
					analyzePredicat(join, select.getNomFrom());
				}
				exp = join.getExpr2();
				if(!exp.contains("'") && !typeInt(exp)){
					Jointure join2;
					if(exp.contains(".")){
						// Séparation -> point
						int indicePt = exp.indexOf(".");
						String[] s = new String[2];
						s[0] = exp.substring(0, indicePt);
						s[1] = exp.substring(indicePt + 1, exp.length());

						if(!bd.existDimension(s[0]))
							throw new DimensionException("La dimension '"
									+ s[0] + "' n'existe pas.");

						if(!bd.existDimensionToFact(s[0], select.getNomFrom()))
							throw new DimensionException("La dimension '"
									+ s[0] + "' n'est pas connecté au fait '"
									+ select.getNomFrom() + "'.");

						join2 = new Jointure(null, s[1], join.getExpr2(), join
								.getOperateur());
						analyzePredicat(join2, s[0]);
					}
					else{
						join2 = new Jointure(null, exp, join.getExpr1(), join
								.getOperateur());
						analyzePredicat(join2, select.getNomFrom());
					}
				}
			}
		}

		// verification des attributs utlisé dans le select
		Iterator itAgreg = select.getAgregs().iterator();
		if(select.getAgregs().size() == 0)
			throw new AttributException("Le select doit être suivi par une fonction d'agrégation.");
		while(itAgreg.hasNext()){
			String agreg = (String) itAgreg.next();
			String mesure = agreg.substring(agreg.indexOf("(")+1, agreg
					.indexOf(")"));
			if(!bd.existAttribut(select.getNomFrom(), mesure))
				throw new AttributException("L'attribut '" + mesure
						+ "' n'existe pas pour le fait '" + select.getNomFrom()
						+ "'.");
		}
	}

	/**
	 * @param pNomParam
	 * void
	 * @throws HierarchyException
	 * @throws SQLException
	 * @throws AttributException
	 */
	private void analyzeAtribut(String nomParam, String nomHierarchy) throws SQLException, HierarchyException, AttributException{
		// Séparation -> point
		int indicePt = nomParam.indexOf(".");
		String[] s = new String[2];
		s[0] = nomParam.substring(0, indicePt);
		s[1] = nomParam.substring(indicePt + 1, nomParam.length());

		if(!bd.existAttributToHierarchy(nomHierarchy,s[0]))
			throw new HierarchyException("L'attribut '"+s[0]+"' n'existe pas pour la hierarchy '"+nomHierarchy+"'.");
		if(!bd.existAttributToHierarchy(nomHierarchy,s[1]))
			throw new HierarchyException("L'attribut '"+s[1]+"' n'existe pas pour la hierarchy '"+nomHierarchy+"'.");
		
		// attribut faible appartient à attribut fort
		if(!bd.isAttributFaible(nomHierarchy, s[0], s[1]))
			throw new AttributException("L'attribut '"+s[1]+"' n'est pas un attribut faible de l'attribut '"+s[0]+"' pour la hierarchie '"+nomHierarchy+"'.");
	}

	private boolean typeInt(String s){
		try{
			Integer.parseInt(s);
			return true;
		}
		catch(NumberFormatException e){
			return false;
		}
	}

	/**
	 * Analise la sémantique d'un delete.
	 * @throws DimensionException
	 * @throws FactException
	 * @throws SQLException
	 * @throws AttributException
	 * @throws PredicatException
	 */
	private void analyseDelete() throws DimensionException, FactException,
			SQLException, AttributException, PredicatException{
		Delete delete = (Delete) commande;

		// relation exist ?
		if(delete.getType() == Commande.DIMENSION){
			if(!bd.existDimension(delete.getNom()))
				throw new DimensionException(DimensionException.EXIST);
		}
		if(delete.getType() == Commande.FACT){
			if(!bd.existFact(delete.getNom()))
				throw new FactException("Le fait '" + delete.getNom()
						+ "' n'existe pas.");
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
	private void analyseDrop() throws SQLException, DimensionException,
			FactException{
		Drop drop = (Drop) commande;

		if(drop.getType() == Commande.DIMENSION){
			// relation exist ?
			if(!bd.existDimension(drop.getNom()))
				throw new DimensionException("La dimension '" + drop.getNom()
						+ "' n'existe pas.");

			// fait utilise la dim ?
			if(bd.existFactConnectToDimension(drop.getNom())){
				throw new DimensionException(
						"Un ou des fait(s) utilise(nt) la dimension '"
								+ drop.getNom() + "'.");
			}
		}

		if(drop.getType() == Commande.FACT){
			// relation exist ?
			if(!bd.existFact(drop.getNom()))
				throw new FactException("Le fait '" + drop.getNom()
						+ "' n'existe pas.");
		}
	}

	/**
	 * Analise la sémantique d'une insertion dans un fait.
	 * @throws DimensionException
	 * @throws SQLException
	 * @throws AttributException
	 * @throws PredicatException
	 */
	private void analyzeInsertFact() throws DimensionException, SQLException,
			AttributException, PredicatException{
		InsertFact insert = (InsertFact) commande;

		// nb dimension correspond
		int nbDim = bd.getNumberDimension(insert.getNom());
		if(nbDim != insert.getConnects().size())
			throw new DimensionException(
					"Le nombre de dimension ne correspond pas au fait '"
							+ insert.getNom() + "'.");

		// verif dim relié au fait
		Iterator it = insert.getConnects().keySet().iterator();
		while(it.hasNext()){
			String nomDimension = (String) it.next();
			if(!bd.existDimension(nomDimension))
				throw new DimensionException(DimensionException.EXIST);
			if(!bd.factConnectDimension(insert.getNom(), nomDimension))
				throw new DimensionException("La dimension '" + nomDimension
						+ "' n'est pas connecté au fait '" + insert.getNom()
						+ "'.");
		}

		// verif validité des predicats
		it = insert.getConnects().keySet().iterator();
		while(it.hasNext()){
			String nomDim = (String) it.next();
			Predicat pred = (Predicat) insert.getConnects().get(nomDim);
			ArrayList listJoin = pred.getJointures();
			Iterator itJoin = listJoin.iterator();
			while(itJoin.hasNext()){
				Jointure join = (Jointure) itJoin.next();
				analyzePredicat(join, nomDim);
			}

			// predicat retourne 1 seul enregistrement
			if(!bd.predicatSelectOneRow(nomDim, pred.getSQL()))
				throw new DimensionException(
						"Le predicat de sélection de la dimension '" + nomDim
								+ "' doit renvoyer un enregistrement unique.");
		}
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
		if(insert.getType() == Commande.DIMENSION
				&& !bd.existDimension(insert.getNom()))
			throw new DimensionException("La dimension '" + insert.getNom()
					+ "' n'existe pas.");
		if(insert.getType() == Commande.FACT && !bd.existFact(insert.getNom()))
			throw new DimensionException("Le fait '" + insert.getNom()
					+ "' n'existe pas.");

		// nb valeur correspond
		int nbAttribut = bd.getNumberAttribut(insert.getNom());
		if(nbAttribut != insert.getValues().size())
			throw new AttributException(
					"Le nombre d'attribut est incorrect pour la relation '"
							+ insert.getNom() + "'.");
	}

	/**
	 * Analise la sémantique d'un alter sur une hierarchy.
	 * @throws HierarchyException
	 * @throws SQLException
	 * @throws AttributException
	 */
	private void analyzeAlterHierarchy() throws SQLException,
			HierarchyException, AttributException{
		AlterHierarchy alter = (AlterHierarchy) commande;

		switch(alter.getAlteration()){
			case AlterHierarchy.ADD_HIERARCHY :
				// verifie que le nom de hierarchy n'existe pas déjà
				Hierarchy hierarchy = alter.getHierarchy();
				String nomHierarchy = hierarchy.getNom();
				if(bd.existHierarchy(nomHierarchy))
					throw new HierarchyException("La hierarchy '"
							+ nomHierarchy + "' existe deja.");

				// verif nom param et attr faible present dans les attributs de
				// la
				// dimension
				ArrayList listeLevel = hierarchy.getLevels();
				if(listeLevel.size() == 0)
					throw new HierarchyException("La hierarchy '"+nomHierarchy+"doit comporter des Level.");
				Iterator itLevel = listeLevel.iterator();
				while(itLevel.hasNext()){
					Level level = (Level) itLevel.next();

					// nom param present dans la liste des attributs de la
					// dimension
					if(!bd.existAttributToDimension(alter.getNom(), level
							.getNom())){
						throw new AttributException("L'attribut '"
								+ level.getNom()
								+ "' est inexistant dans la dimension '"
								+ alter.getNom() + "'.");
					}

					// attributs faibles present ds liste attributs de la
					// dimension
					ArrayList listeAttributFaible = level.getAttributs();
					Iterator itAttributFaible = listeAttributFaible.iterator();
					while(itAttributFaible.hasNext()){
						String nomAttributFaible = (String) itAttributFaible
								.next();
						if(!bd.existAttributToDimension(alter.getNom(),
								nomAttributFaible)){
							throw new AttributException("L'attribut '"
									+ nomAttributFaible
									+ "' est inexistant dans la dimension '"
									+ alter.getNom() + "'.");
						}
					}
				}

				// param racine de hierarchi identique pour toutes les
				// hierarchies
				String racine = ((Level) listeLevel.get(0)).getNom();
				String paramRacine = bd
						.getParamRacineHierarchyToDimension(alter.getNom());
				if(paramRacine == null)
					paramRacine = racine;
				if(!paramRacine.equals(racine))
					throw new HierarchyException(
							"La hierarchy '"
									+ hierarchy.getNom()
									+ "' ne présente pas le même parametre racine que les autres hierarchies de la dimension '"
									+ alter.getNom() + "'.");
				break;
			case AlterHierarchy.DROP_HIERARCHY :
				// hierarchy deja existante et appartient à la dimension qu'on
				// veut modifier
				// hierar non null
				Hierarchy hierar = alter.getHierarchy();
				if(hierar == null)
					throw new HierarchyException("La hierarchy doit être non null.");
				String nom = hierar.getNom();
				if(!bd.existHierarchy(nom))
					throw new HierarchyException("La hierarchy '" + nom
							+ "' n'existe pas.");
				if(!bd.existHierarchyToDimension(alter.getNom(), nom))
					throw new HierarchyException("La hierarchy '" + nom
							+ "' n'existe pas pour la dimension '"
							+ alter.getNom() + "'.");
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
			throw new FactException("Le fait '" + alter.getNom()
					+ "' n'existe pas.");
		}
		if(alter.getType() == Commande.DIMENSION
				&& !bd.existDimension(alter.getNom())){
			throw new DimensionException("La dimension '" + alter.getNom()
					+ "' n'existe pas.");
		}

		switch(alter.getAlteration()){
			case Alter.DISCONNECT :
				// au moins une dim
				// dim existe et dim relié au fait
				ArrayList listeDimension = alter.getAttributs();
				if(listeDimension.size() == 0)
					throw new FactException("La commande ALTER DISCONNECT doit comporter au moins un nom de dimension.");
				analyzeDimensionExist(listeDimension);
				Iterator itDim = listeDimension.iterator();
				while(itDim.hasNext()){
					String nomDim = (String) itDim.next();
					if(!bd.factConnectDimension(alter.getNom(), nomDim)){
						throw new FactException("La dimension '" + nomDim
								+ "' n'est pas connecté au fait '"
								+ alter.getNom() + "'.");
					}
				}
				break;
			case Alter.CONNECT :
				// au moins une dim
				// dim existe
				if(alter.getAttributs().size() == 0)
					throw new FactException("La commande ALTER CONNECT nécessite au moins une dimension.");
				analyzeDimensionExist(alter.getAttributs());
				break;
			case Alter.DROP :
				// au moins un attribut
				// attr exist pour le fait
				if(alter.getAttributs().size() == 0)
					throw new AttributException("La commande ALTER DROP nécessite au moins un attributs.");
				Iterator it = alter.getAttributs().iterator();
				while(it.hasNext()){
					String att = (String) it.next();
					if(!bd.existAttribut(alter.getNom(), att))
						throw new AttributException("L'attribut '" + att
								+ "' n'existe pas pour la relation '"
								+ alter.getNom() + "'.");
					if(alter.getType() == Commande.DIMENSION){
						if(!bd.existAttributToDimension(alter.getNom(), att))
							throw new AttributException("L'attribut '" + att
									+ "' n'existe pas pour la dimension '"
									+ alter.getNom() + "'.");
					}
					if(alter.getType() == Commande.FACT){
						if(!bd.existAttributToFact(alter.getNom(), att))
							throw new AttributException("L'attribut '" + att
									+ "' n'existe pas pour le fait '"
									+ alter.getNom() + "'.");
					}
				}
				break;
			case Alter.ADD :
				// au moins un attribut
				// attr n'existe pas déjà pour la relation
				if(alter.getAttributs().size() == 0)
					throw new AttributException("La commande ALTER ADD nécessite au moins un attribut.");
				Iterator itA = alter.getAttributs().iterator();
				while(itA.hasNext()){
					Attribut att = (Attribut) itA.next();
					if(bd.existAttribut(alter.getNom(), att.getNom()))
						throw new AttributException("L'attribut '"
								+ att.getNom()
								+ "' existe déjà pour la relation '"
								+ alter.getNom() + "'.");
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
	private void analyzeCreateDimension() throws HierarchyException,
			AttributException, SQLException, DimensionException{
		CreateDimension dim = (CreateDimension) commande;

		// verifie l'unicite du nom de la dimension
		if(bd.existDimension(dim.getNom())){
			throw new DimensionException("La dimension '" + dim.getNom()
					+ "' existe déjà.");
		}

		// verif au moins un attribut
		if(dim.getAttributs().size() == 0)
			throw new AttributException("La création de la dimension '"+dim.getNom()+"' nécessite au moins un attribut.");
		
		ArrayList hierarchi = dim.getHierarchys();
		Iterator it = hierarchi.iterator();
		String paramRacine = null;
		while(it.hasNext()){
			Hierarchy hierar = (Hierarchy) it.next();
			// Verif unicite nom hierarchie
			if(bd.existHierarchy(hierar.getNom())){
				throw new HierarchyException("La hierarchie '"
						+ hierar.getNom() + "' existe déjà.");
			}

			// verif nom param et attr faible present dans les attributs de la
			// dimension
			ArrayList listeLevel = hierar.getLevels();
			Iterator itLevel = listeLevel.iterator();
			while(itLevel.hasNext()){
				Level level = (Level) itLevel.next();

				// nom param present dans la liste des attributs de la dimension
				if(!dim.existAttributs(level.getNom())){
					throw new AttributException("L'attribut '" + level.getNom()
							+ "' est inexistant dans la dimension '"
							+ dim.getNom() + "'.");
				}

				// attributs faibles present ds liste attributs de la dimension
				ArrayList listeAttributFaible = level.getAttributs();
				Iterator itAttributFaible = listeAttributFaible.iterator();
				while(itAttributFaible.hasNext()){
					String nomAttributFaible = (String) itAttributFaible.next();
					if(!dim.existAttributs(nomAttributFaible)){
						throw new AttributException("L'attribut '"
								+ nomAttributFaible
								+ "' est inexistant dans la dimension '"
								+ dim.getNom() + "'.");
					}
				}
			}

			// param racine de hierarchi identique pour toutes les hierarchies
			String racine = ((Level) listeLevel.get(0)).getNom();
			if(paramRacine == null)
				paramRacine = racine;
			if(!paramRacine.equals(racine))
				throw new HierarchyException(
						"La hierarchy '"
								+ hierar.getNom()
								+ "' ne présente pas le même parametre racine que la hierarchy précédente");
		}
	}

	/**
	 * Analise la sémantique de la création d'un fait.
	 * @throws DimensionException
	 * @throws SQLException
	 * @throws AttributException
	 * @throws FactException void
	 */
	private void analyzeCreateFact() throws DimensionException, SQLException,
			FactException, AttributException{
		CreateFact fait = (CreateFact) commande;

		// verifie l'unicite du nom du fait
		if(bd.existFact(fait.getNom())){
			throw new FactException("Le fait '" + fait.getNom()
					+ "' existe déjà.");
		}
		
		// verif au moins un attribut
		if(fait.getAttributs().size() == 0)
			throw new AttributException("La création du fait '"+fait.getNom()+"' nécessite au moins un attribut.");

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
				throw new DimensionException("La dimension '" + nomDimension
						+ "' n'existe pas.");
			}
		}
	}

	/**
	 * Analise le predicat.
	 * @param jointure
	 * @param nomRelation
	 * @throws AttributException
	 * @throws SQLException
	 * @throws PredicatException
	 */
	private void analyzePredicat(Jointure jointure, String nomRelation)
			throws SQLException, AttributException, PredicatException{
		String exp = jointure.getExpr1();

		if(jointure.getExpr1() == null || jointure.getExpr2() == null)
			throw new PredicatException("Le predicat est invalide.");

		if(jointure.getExpr1().equals("") || jointure.getExpr2().equals(""))
			throw new PredicatException("Le predicat est invalide");

		if(!bd.existAttribut(nomRelation, exp))
			throw new AttributException("L'attribut '" + exp
					+ "' n'existe pas pour '" + nomRelation + "'.");
	}
}