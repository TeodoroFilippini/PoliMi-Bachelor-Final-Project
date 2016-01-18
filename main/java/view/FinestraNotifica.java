package view;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
/**
 * Finestra utilizzata per notificare eccezioni o messaggi informativi.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class FinestraNotifica {
	
	/**
	 * Crea una finestra in caso di eccezione con il messaggio specificato + il messaggio
	 * originale relativo all'eccezione.
	 * 
	 * @param messaggio messaggio da visualizzare.
	 * @param e eccezione che è stata catturata di cui si vuole stampare il messaggio originale.
	 */
	public FinestraNotifica(String messaggio, Exception e){
		JDialog dialog = new JDialog();
		JOptionPane.showMessageDialog(dialog,
			    "Eccezione catturata: "+messaggio+ "\n\n Messaggio originale: "+e.getMessage(),
			    "ERRORE",
			    JOptionPane.ERROR_MESSAGE);
		Logger.getGlobal().log(Level.SEVERE, messaggio, e);
	}
	
	/**
	 * Crea una finestra di notifica generica con il messaggio specificato.
	 * 
	 * @param messaggio messaggio da visualizzare.
	 */
	public FinestraNotifica(String messaggio){
		JDialog dialog = new JDialog();
		JOptionPane.showMessageDialog(dialog,
			    messaggio,
			    "Attenzione",
			    JOptionPane.INFORMATION_MESSAGE);
	}
}

