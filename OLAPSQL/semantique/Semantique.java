package semantique;

import java.util.ArrayList;
import java.util.Iterator;

import exception.ExistenceAttributException;
import exception.UniciteDimensionException;
import exception.UniciteFaitException;
import exception.UniciteHierarchyException;

import structure.Commande;
import structure.CreateDimension;
import structure.CreateFact;
import structure.Drop;
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

	public void analyze() throws UniciteFaitException, UniciteDimensionException, UniciteHierarchyException, ExistenceAttributException{
		if(commande instanceof CreateFact){
			analyzeCreateFact();
		}
		if(commande instanceof CreateDimension){
			analyzeCreateDimension();
		}
	}

	/**
	 * @return
	 * boolean
	 * @throws UniciteDimensionException
	 * @throws UniciteHierarchyException
	 * @throws ExistenceAttributException
	 */
	private void analyzeCreateDimension() throws UniciteDimensionException, UniciteHierarchyException, ExistenceAttributException{
		CreateDimension dim = (CreateDimension) commande;
		
		// verifie l'unicite du nom de la dimension
		if(bd.existDimension(dim.getNom())){
			throw new UniciteDimensionException();
		}
		
		// Verif unicite nom hierarchie
		ArrayList hierarchi = dim.getHierarchys();
		Iterator it = hierarchi.iterator();
		while(it.hasNext()){
			Hierarchy hierar = (Hierarchy) it.next();
			if(bd.existHierarchy(hierar.getNom())){
				throw new UniciteHierarchyException();
			}
			
			// verif nom param et attr faible present dans les attributs de la dimension
			ArrayList listeLevel = hierar.getLevels();
			Iterator itLevel = listeLevel.iterator();
			while(itLevel.hasNext()){
				Level level = (Level) it.next();
				
				// nom param present dans la liste des attributs de la dimension
				if(!dim.existAttributs(level.getNom())){
					throw new ExistenceAttributException();
				}
				
				// attributs faibles present ds liste attributs de la dimension
				ArrayList listeAttributFaible = level.getAttributs();
				Iterator itAttributFaible = listeAttributFaible.iterator();
				while(itAttributFaible.hasNext()){
					String nomAttributFaible = (String) itAttributFaible.next();
					if(!dim.existAttributs(nomAttributFaible)){
						throw new ExistenceAttributException();
					}
				}
			}
		}
		
		// TODO param racine de hierarchi identique pour toutes les hierarchies
	}

	private void analyzeCreateFact() throws UniciteFaitException, UniciteDimensionException{
		CreateFact fait = (CreateFact) commande;
		
		// verifie l'unicite du nom du fait
		if(bd.existFact(fait.getNom())){
			throw new UniciteFaitException();
		}
		
		// verifie que les noms de domaine existe
		ArrayList listDim = fait.getConnects();
		Iterator it = listDim.iterator();
		while(it.hasNext()){
			String nomDim = (String) it.next();
			if(bd.existDimension(nomDim)){
				throw new UniciteDimensionException();
			}
		}
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
	private boolean executeDrop() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean executeCreateFact(){
		CreateFact fait = (CreateFact) commande;
		ArrayList mesures = new ArrayList();
		ArrayList dimensions = new ArrayList();
		ArrayList attributs = fait.getAttributs();
		Iterator it = attributs.iterator();
		while(it.hasNext()){
			Attribut att = (Attribut) it.next();
			if(att.getType() == Attribut.DATE || att.getType() == Attribut.NUMBER || att.getType() == Attribut.VARCHAR){
				mesures.add(att);
			}
			else{
				if(!att.getNom().equals("CONNECT TO")){
					dimensions.add(att.getNom());
				}
			}
		}
		return bd.createFact(fait.getNom(),mesures,dimensions);
	}
	
	private boolean executeCreateDimension(){
		CreateDimension dim = (CreateDimension) commande;
		// TODO
		bd.createDimension(dim);
		return false;
	}
}