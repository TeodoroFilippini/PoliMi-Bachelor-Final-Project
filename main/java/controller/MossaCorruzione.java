package controller;

import model.Costanti;
import model.Giocatore;
import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata alla corruzione che avviene se, durante una Mossa Spara,
 * uno dei giocatori ha "visto" compiere la sparatoria, cioè se uno dei pastori associato
 * ad un altro giocatore era sito su una casella confinante alla regione dell'animale assassinato,
 * e questo giocatore, tirando il dado, ha ottenuto 5 oppure 6.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaCorruzione implements Mossa {

	private static final long serialVersionUID = 1L;
	private Giocatore giocatoreCorrotto;
	private Giocatore giocatoreCorruttore;

	public MossaCorruzione (Giocatore giocatoreCorrotto, Giocatore giocatoreCorruttore){
		this.giocatoreCorrotto = giocatoreCorrotto;
		this.giocatoreCorruttore = giocatoreCorruttore;
	}

	/**
	 * Esegue la mossa: attraverso la view, mostra i dovuti cambiamenti
	 * che ha effettuato sullo stato del gioco.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view,StatoGioco statoGioco){
		Giocatore istanzaGiocatoreCorrotto = AdattatoreIstanze.cercaIstanzaGiocatore (giocatoreCorrotto,statoGioco.getListaGiocatori());
		Giocatore istanzaGiocatoreCorruttore = AdattatoreIstanze.cercaIstanzaGiocatore (giocatoreCorruttore,statoGioco.getListaGiocatori());
		istanzaGiocatoreCorruttore.decrementaDanari(Costanti.COSTO_CORRUZIONE);
		istanzaGiocatoreCorrotto.incrementaDanari(Costanti.COSTO_CORRUZIONE);
		view.stampaMessaggio ("Qualcuno ha visto qualcosa! Giocatore "+(istanzaGiocatoreCorruttore.getNumero()+1)+
				" deve corrompere Giocatore "+(istanzaGiocatoreCorrotto.getNumero()+1));
	}

}
