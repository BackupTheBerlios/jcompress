
/*
 * Java class "Element.java" generated from Poseidon for UML.
 * Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 * Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
import java.lang.String;


/**
 * <p></p>
 */
public abstract class Element {

  ///////////////////////////////////////
  // attributes


/**
 * <p>Represents ...</p>
 */
    private int frequence; 

/**
 * <p>Represents ...</p>
 */
    private String caractere; 

/**
 * <p>Represents ...</p>
 */
    private Noeud SAD; 

/**
 * <p>Represents ...</p>
 */
    private Noeud SAG; 
    
    public String getCaractere(){
    	return caractere;
    }
    
    public void setCaractere(String c){
    	caractere = c;
    }
    
    public int getFrequence(){
    	return frequence;
    }
    
    public void setFrequence(int f){
    	frequence = f;
    }
    
    public Noeud getSAD(){
    	return SAD;
    }
    
    public void setSAD(Noeud sa){
    	SAD = sa;
    }
    
    public Noeud getSAG(){
    	return SAG;
    }
    
    public void setSAG(Noeud sa){
    	SAG = sa;
    }

 /**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 * @param car 
 */
    public Noeud existeCar(String car) {        
        // your code here
        return null;
    } // end existeCar        

/**
 * <p>Does ...</p>
 * 
 * 
 * @param noeud 
 */
    public void modifier() {        
        // your code here
    } // end modificationArbre        

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 * @param caractere 
 */
    public String getCode(Noeud caractere) {        
        // your code here
        return null;
    } // end getCode        

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 * @param code 
 */
    public Noeud getNoeud(String code) {        
        // your code here
        return null;
    } // end getNoeud        

} // end Element






