/**
 * Date 		= 21/01/2005
 * Project		= JCompress
 * File name  	= Application.java
 * @author Bosse Laure/Fauroux claire
 *  
 */

package JCompress;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Cette classe g�re les acc�s (�criture, lecture ...) 
 * aux diff�rents fichiers.
 */
public class Ressources {
    private FileInputStream fichierSource;

    private FileOutputStream fichierDestination;

    private String bufferInput;

    private String bufferOutput;

    /**
     * Constructeur permettant d'ouvrir les fichiers en entr�es et en sortie.
     * 
     * @param Fichier source.
     * @param Fichier destination.
     * @throws Exception lev� si le fichier source est inexistant.
     */
    public Ressources(String source, String destination)
            throws FileNotFoundException {

        // Cr�er les fichiers source et destination
        fichierSource = new FileInputStream(source);
        fichierDestination = new FileOutputStream(destination);

        // Initialise les buffer
        bufferInput = new String();
        bufferOutput = new String();

    }

    ///////////////////////////////////////
    // operations

    /**
     * Retourne le bit suivant du fichier source.
     * 
     * @return Entier repr�sentant un bit du fichier source (0 ou 1).
     * @throws Exception
     *             lev� si un probl�me est survenue lors de l'acc�s au fichier.
     */
    public String lireBit() throws IOException {
        if (bufferInput.length() < 1) {
            int intLu = fichierSource.read();
            String binaireLu = Integer.toBinaryString(intLu);

            if (binaireLu.length() < 8) {
                int cond = 8 - binaireLu.length();
                for (int i = 0; i < cond; i++) {
                    binaireLu = "0" + binaireLu;
                }
            }

            bufferInput = bufferInput + binaireLu;
        }

        Integer res = new Integer(bufferInput.substring(0, 1));
        bufferInput = bufferInput.substring(1, bufferInput.length());
        return res.toString();
    }

    /**
     * Retourne l'octet suivant du fichier source.
     * 
     * @return String repr�sentant le code ascii en binaire de l'octet suivant
     *         du fichier source.
     * @throws Exception
     *             lev� si un probl�me est survenue lors de l'acc�s au fichier.
     */
    public String lireOctet() throws IOException {
        int intLu = fichierSource.read();
        String binaireLu = Integer.toBinaryString(intLu);

        if (binaireLu.length() < 8) {
            int cond = 8 - binaireLu.length();
            for (int i = 0; i < cond; i++) {
                binaireLu = "0" + binaireLu;
            }
        }

        bufferInput = bufferInput + binaireLu;

        String res = bufferInput.substring(0, 8);
        bufferInput = bufferInput.substring(8, bufferInput.length());
        return res;
    }

    /**
     * Permet d'ecrire un suite de bit (pass� en parametre) dans le fichier
     * destination.
     * 
     * @param caractere
     *            Chaine de caractere repr�sentant une suite de bit du caractere
     *            � �crire dans le fichier destination
     * @throws Exception
     *             lev� si un probl�me est survenue lors de l'acc�s au fichier.
     */
    public void ecrireCaractere(String caractere) throws IOException {
        bufferOutput = bufferOutput + caractere;

        if (bufferOutput.length() >= 8) {
            fichierDestination.write(binaireToDecimal(bufferOutput.substring(0,
                    8)));
            if (bufferOutput.length() > 8) {
                bufferOutput = bufferOutput.substring(8, bufferOutput.length());
            } else {
                bufferOutput = "";
            }
        }
    }

    /**
     * Fonction interne utilis� pour convertir une chaine de bit en sa valeur
     * d�cimal.
     * 
     * @param num
     *            Chaine de bit � convertir.
     * @return Valeur de num en d�cimal.
     */
    private int binaireToDecimal(String num) {
        int numDec = 0;
        for (int i = 0; i < num.length(); i++) {
            int j = Integer.parseInt(bufferOutput.substring(i, i + 1));
            numDec = numDec * 2 + j;
        }
        return numDec;
    }

    /**
     * Ecrit le caractere de fin dans le fichier destination.
     * 
     * @param fin
     *            Chaine de bit correspondant au code de fin du fichier.
     * @throws Exception
     *             lev� si un probl�me est survenue lors de l'acc�s au fichier.
     */
    public void finEcrire(String fin) throws IOException {
        ecrireCaractere(fin);

        while (bufferOutput.length() >= 8) {
            ecrireCaractere("");
        }

        if (bufferOutput.length() > 0) {
            int num0 = 8 - bufferOutput.length();
            for (int i = 0; i < num0; i++) {
                bufferOutput = bufferOutput + "0";
            }
            ecrireCaractere("");
        }

        fermer();
    }

    /**
     * Ferme les fichiers source et destination.
     */
    public void fermer() {
        try {
            fichierSource.close();
            fichierDestination.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}