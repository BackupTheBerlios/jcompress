package IHM;

import java.text.ParseException;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

/**
 * Cette classe nous permet de v�rifier si le taux de compression saisi est valide.
 */
public class Verifier extends InputVerifier{

	/**
	 * V�rifie que le composant passer en parametre contient une donn�e valide.
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
