package semantique;

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
import structure.Drop;
import structure.Insert;
import structure.InsertFact;
import structure.types.Attribut;
import structure.types.Hierarchy;
import structure.types.Level;

public class Semantique {
	private Commande commande;
	private BaseDonnees bd;

	public Semantique(Commande com){
		bd = new BaseDonnees();
		bd.connecter();
		commande = com;
	}

	public void close(){
		bd.deconnecter();
	}
	
	public static void main(String[] argv){
		InsertFact c = new InsertFact("test",Commande.FACT);
		HashMap m = new HashMap();
		m.put("test","f");
		m.put("a","r");
		m.put("w","p");
		c.setConnects(m);
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
	}

	public void analyze() throws FactException, DimensionException,
			HierarchyException, AttributException{
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
	}

	/**
	 * 
	 * void
	 * @throws DimensionException
	 */
	private void analyzeInsertFact() throws DimensionException{
		InsertFact insert = (InsertFact) commande;
		
		// nb dimension correspond
		int nbDim = bd.getNumberDimension(insert.getNom());
		if(nbDim != insert.getConnects().size())
			throw new DimensionException("Le nombre de dimension de correspond pas au fait.");
		
		// verif dim relié au fait
		Iterator it = insert.getConnects().keySet().iterator();
		while(it.hasNext()){
			String nomDimension = (String) it.next();
			if(bd.existDimension(nomDimension))
				throw new DimensionException(DimensionException.EXIST);
			if(!bd.factConnectDimension(insert.getNom(),nomDimension))
				throw new DimensionException("La dimension n'est pas connecté au fait.");
		}
		
		// TODO predicat retourne 1 seul enregistrement
		// TODO verif validité des predicats ?
	}

	/**
	 * 
	 * void
	 * @throws DimensionException
	 * @throws AttributException
	 */
	private void analyzeInsert() throws DimensionException, AttributException{
		Insert insert = (Insert) commande;
		
		// nomRelation existe
		if(!bd.existDimension(insert.getNom()))
			throw new DimensionException(DimensionException.EXIST);
		
		// nb valeur correspond
		int nbAttribut = bd.getNumberAttribut(insert.getNom());
		if(nbAttribut != insert.getValues().size())
			throw new AttributException("Le nombre d'attribut ne correspond pas à la dimension.");
		
		// TODO type valeur correspond ?
	}

	/**
	 * void
	 * @throws HierarchyException
	 */
	private void analyzeAlterHierarchy() throws HierarchyException{
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
				// hierarchy deja existante et appartient à la dimension qu'on veut modifier
				Iterator itH = alter.getAttributs().iterator();
				while(itH.hasNext()){
					String nom = (String) itH.next();
					if(!bd.existHierarchy(nom))
						throw new HierarchyException("La hierarchy n'existe pas.");
					if(bd.existHierarchyToDimension(alter.getNom(),nom))
						throw new HierarchyException("La hierarchy n'existe pas pour cette dimension.");
				}
				break;
		}
	}

	/**
	 * void
	 * @throws AttributException
	 * @throws ExistenceFactException
	 * @throws ExistenceDimensionException
	 */
	private void analyzeAlter() throws FactException, DimensionException,
			AttributException{
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
						if(!bd.existAttributToDimension(alter.getNom(),att.getNom()))
							throw new AttributException("L'attribut n'existe pas pour cette dimension.");
					}
					if(alter.getType() == Commande.FACT){
						if(!bd.existAttributToFact(alter.getNom(),att.getNom()))
							throw new AttributException("L'attribut n'existe pas pour ce fait.");
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

	private void analyzeDimensionExist(ArrayList listeDimension)
			throws DimensionException{
		Iterator itDimension = listeDimension.iterator();
		while(itDimension.hasNext()){
			String nomDimension = (String) itDimension.next();
			if(!bd.existDimension(nomDimension)){
				throw new DimensionException(DimensionException.EXIST);
			}
		}
	}

	/**
	 * @return boolean
	 * @throws UniciteDimensionException
	 * @throws UniciteHierarchyException
	 * @throws ExistenceAttributException
	 */
	private void analyzeCreateDimension() throws DimensionException,
			HierarchyException, AttributException{
		CreateDimension dim = (CreateDimension) commande;

		// verifie l'unicite du nom de la dimension
		if(bd.existDimension(dim.getNom())){
			throw new DimensionException(DimensionException.UNIQUE);
		}

		// Verif unicite nom hierarchie
		ArrayList hierarchi = dim.getHierarchys();
		Iterator it = hierarchi.iterator();
		while(it.hasNext()){
			Hierarchy hierar = (Hierarchy) it.next();
			if(bd.existHierarchy(hierar.getNom())){
				throw new HierarchyException(HierarchyException.UNIQUE);
			}

			// verif nom param et attr faible present dans les attributs de la
			// dimension
			ArrayList listeLevel = hierar.getLevels();
			Iterator itLevel = listeLevel.iterator();
			while(itLevel.hasNext()){
				Level level = (Level) it.next();

				// nom param present dans la liste des attributs de la dimension
				if(!dim.existAttributs(level.getNom())){
					throw new AttributException(AttributException.EXIST);
				}

				// attributs faibles present ds liste attributs de la dimension
				ArrayList listeAttributFaible = level.getAttributs();
				Iterator itAttributFaible = listeAttributFaible.iterator();
				while(itAttributFaible.hasNext()){
					String nomAttributFaible = (String) itAttributFaible.next();
					if(!dim.existAttributs(nomAttributFaible)){
						throw new AttributException(AttributException.EXIST);
					}
				}
			}
		}

		// TODO param racine de hierarchi identique pour toutes les hierarchies
	}

	private void analyzeCreateFact() throws FactException, DimensionException{
		CreateFact fait = (CreateFact) commande;

		// verifie l'unicite du nom du fait
		if(bd.existFact(fait.getNom())){
			throw new FactException(FactException.UNIQUE);
		}

		// verifie que les dimensions existe
		analyzeDimensionExist(fait.getConnects());
	}

	public boolean execute(){
		if(commande instanceof CreateFact){
			return executeCreateFact();
		}
		if(commande instanceof CreateDimension){
			return executeCreateDimension();
		}
		if(commande instanceof Drop){
			return executeDrop();
		}
		return false;
	}

	/**
	 * @return
	 */
	private boolean executeDrop(){
		// TODO Auto-generated method stub
		return false;
	}

	private boolean executeCreateFact(){
		// TODO a revoir
		CreateFact fait = (CreateFact) commande;
		ArrayList mesures = new ArrayList();
		ArrayList dimensions = new ArrayList();
		ArrayList attributs = fait.getAttributs();
		Iterator it = attributs.iterator();
		while(it.hasNext()){
			Attribut att = (Attribut) it.next();
			if(att.getType() == Attribut.DATE
					|| att.getType() == Attribut.NUMBER
					|| att.getType() == Attribut.VARCHAR){
				mesures.add(att);
			}
			else{
				if(!att.getNom().equals("CONNECT TO")){
					dimensions.add(att.getNom());
				}
			}
		}
		return bd.createFact(fait.getNom(), mesures, dimensions);
	}

	private boolean executeCreateDimension(){
		CreateDimension dim = (CreateDimension) commande;
		// TODO
		bd.createDimension(dim);
		return false;
	}
}