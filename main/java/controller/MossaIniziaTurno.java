package controller;

import model.Giocatore;
import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata all'inizio del turno.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaIniziaTurno implements Mossa{

	private static final long serialVersionUID = 1L;
	private Giocatore giocatore;

	public MossaIniziaTurno(Giocatore giocatore){
		this.giocatore = giocatore;
	}

	/**
	 * Esegue la mossa: attraverso la view, mostra i dovuti cambiamenti
	 * che ha effettuato sullo stato del gioco.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco){
		Giocatore istanzaGiocatore = AdattatoreIstanze.cercaIstanzaGiocatore(giocatore,statoGioco.getListaGiocatori());
		view.setGiocatoreCorrente(istanzaGiocatore);
	}

}
