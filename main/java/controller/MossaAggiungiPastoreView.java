package controller;

import model.Casella;
import model.Pastore;
import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata all'aggiunta di un pastore ad una casella, nella fase iniziale del gioco.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaAggiungiPastoreView implements Mossa{

	private static final long serialVersionUID = 1L;
	Pastore pastore;
	
	
	public MossaAggiungiPastoreView(Pastore pastore) {
		this.pastore = pastore;
	}

	/**
	 * Esegue la mossa: attraverso la view, mostra i dovuti cambiamenti
	 * che ha effettuato sullo stato del gioco.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco) {
		Pastore istanzaPastore = AdattatoreIstanze.cercaIstanzaPastore(pastore, statoGioco.getListaPastori());
		Casella istanzaPosizione = AdattatoreIstanze.cercaIstanzaCasella(pastore.getPosizione(), statoGioco.getMappa().getListaCaselle());
		istanzaPastore.setPosizione(istanzaPosizione);
		view.aggiungiPastoreView(istanzaPastore);
	}

	
	
}
