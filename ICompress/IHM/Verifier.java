package IHM;

import java.text.ParseException;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

/**
 * Cette classe nous permet de vérifier si le taux de compression saisi est valide.
 */
public class Verifier extends InputVerifier{

	/**
	 * Vérifie que le composant passer en parametre contient une donnée valide.
	 * @see javax.swing.InputVerifier#verify(javax.swing.JComponent)
	 */
	public boolean verify(JComponent input){
		if(input instanceof JFormattedTextField){
			JFormattedTextField ftf = (JFormattedTextField) input;
			JFormattedTextField.AbstractFormatter formatter = ftf.getFormatter();
			if(formatter != null){
				String text = ftf.getText();
				try{
					formatter.stringToValue(text);
					return true;
				}
				catch(ParseException e){
					return false;
				}
			}
		}
		return true;
	}

}
