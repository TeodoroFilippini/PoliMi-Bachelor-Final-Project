package controller;

import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata alla stampa a video di un messaggio, effettuata
 * localmente dalla classe FinestraMessaggi.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaMessaggio implements Mossa {

	private static final long serialVersionUID = 1L;
	private String messaggio;

	/**
	 * Costruttore della classe.
	 * @param messaggio il messaggio da stampare.
	 */
	public MossaMessaggio(String messaggio){
		this.messaggio = messaggio;
	}

	/**
	 * Esegue la mossa: attraverso la view, stampa il messaggio.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco){
		view.stampaMessaggio(messaggio);
	}

}
