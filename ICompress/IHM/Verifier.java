/*
 * Created on 16 févr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package IHM;

import java.text.ParseException;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

/**
 * @author lalo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Verifier extends InputVerifier{

	/* (non-Javadoc)
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
