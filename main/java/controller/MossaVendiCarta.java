package controller;

import model.Carta;
import model.CartaInVendita;
import model.Giocatore;
import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata alla vendita di una singola tessera.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaVendiCarta implements Mossa {

	private static final long serialVersionUID = 1L;

	private CartaInVendita cartaInVendita;
	private Giocatore acquirente;
	public MossaVendiCarta(CartaInVendita cartaInVendita, Giocatore acquirente) {
		this.cartaInVendita = cartaInVendita;
		this.acquirente = acquirente;
	}

	/**
	 * Esegue la mossa: attraverso la view, mostra i dovuti cambiamenti
	 * che ha effettuato sullo stato del gioco.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco){
		Giocatore istanzaAcquirente = AdattatoreIstanze.cercaIstanzaGiocatore(acquirente, statoGioco.getListaGiocatori());
		Giocatore istanzaVenditore = AdattatoreIstanze.cercaIstanzaGiocatore(cartaInVendita.getProprietario(),statoGioco.getListaGiocatori());
		int costoCarta = cartaInVendita.getCosto();
		Carta istanzaCarta = AdattatoreIstanze.cercaIstanzaCarta(cartaInVendita.getCarta(),statoGioco.getListaCarte());
		istanzaAcquirente.aggiungiCarta(istanzaCarta);
		istanzaVenditore.rimuoviCarta(istanzaCarta);
		istanzaAcquirente.decrementaDanari(costoCarta);
		istanzaVenditore.incrementaDanari(costoCarta);
		view.aggiungiCarta(istanzaCarta, istanzaAcquirente);

	}
}
