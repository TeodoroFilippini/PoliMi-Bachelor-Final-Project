package controller;

import model.Carta;
import model.Giocatore;
import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata all'acquisto di una carta "tradizionale", ovvero 
 * trovandosi al confine con una regione del tipo corrispondente a quello della carta.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaCompraCarta implements Mossa{

	private static final long serialVersionUID = 1L;
	private Carta carta;
	private Giocatore giocatore;

	public MossaCompraCarta(Carta carta,Giocatore giocatore) {
		this.carta = carta;
		this.giocatore = giocatore;
	}

	/**
	 * Esegue la mossa: attraverso la view, mostra i dovuti cambiamenti
	 * che ha effettuato sullo stato del gioco.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa (InterfacciaView view, StatoGioco statoGioco){
		Giocatore istanzaGiocatore = AdattatoreIstanze.cercaIstanzaGiocatore(giocatore,statoGioco.getListaGiocatori());
		Carta istanzaCarta = AdattatoreIstanze.cercaIstanzaCarta(carta, statoGioco.getListaCarte());
		istanzaGiocatore.aggiungiCarta(istanzaCarta);
		istanzaGiocatore.decrementaDanari(istanzaCarta.getCosto());
		istanzaCarta.aumentaCosto();
		istanzaCarta.decrementaCarteDisponibili();
		if (view.getGiocatore().getNumero() == istanzaGiocatore.getNumero()) {
			view.aggiungiCarta(istanzaCarta, istanzaGiocatore);	
		}
		view.stampaMessaggio("Il giocatore "+statoGioco.getGiocatoreCorrente().getNumero()+" ha comprato una carta");
	}
}
