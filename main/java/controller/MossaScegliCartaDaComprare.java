package controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import model.Carta;
import model.CartaInVendita;
import model.Giocatore;
import model.StatoGioco;
import view.FinestraNotifica;
import view.InterfacciaView;
import RMIServer.InterfacciaRMI;

/**
 * Mossa associata alla scelta della carta da comprare durante il
 * turno di compravendita. Se non se ne vuole comprare nessuna, l'esegui
 * mossa riceve una carta nulla, che corrisponde ad una mossa chiudi 
 * finestra.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaScegliCartaDaComprare implements MossaScelta{

	private static final long serialVersionUID = 1L;
	List<CartaInVendita> listaCarteInVendita;
	private Giocatore giocatore;

	public MossaScegliCartaDaComprare(List<CartaInVendita> listaCarteInVendita, Giocatore giocatore) {
		this.listaCarteInVendita = listaCarteInVendita;
		this.giocatore = giocatore;
	}

	/**
	 * Mette sullo stream online la carta scelta.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco,ObjectOutputStream output){
		CartaInVendita cartaInVendita = scegliCartaDaComprare(view, statoGioco);
		try {
			if (cartaInVendita != null) {
				output.writeObject(cartaInVendita);
			} else {
				output.writeObject(new MossaChiudiFinestra());
			}
		} catch (IOException e) {
			new FinestraNotifica("Errore di connessione associato all'oggetto \"carta\"", e);
		}
	}

	/**
	 * Manda in coda la mossa associata alla carta scelta.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco,InterfacciaRMI server){
		CartaInVendita cartaInVendita = scegliCartaDaComprare(view, statoGioco);
		try {
			if (cartaInVendita != null){
				server.aggiungiOggettoInCoda(cartaInVendita);
			}else{
				server.aggiungiOggettoInCoda(new MossaChiudiFinestra());
			}
		}catch (RemoteException e) {
			new FinestraNotifica("Eccezione di connessione durante l'invio della mossa \"compra carta\"", e);
		}
	}

	/**
	 * Esegue la mossa scegli carta da comprare, sulla view del del giocatore.
	 * @param view la view del giocatore.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	private CartaInVendita scegliCartaDaComprare(InterfacciaView view,StatoGioco statoGioco) {
		Iterator<CartaInVendita> scorriCarte = listaCarteInVendita.iterator();
		Giocatore istanzaAcquirente = AdattatoreIstanze.cercaIstanzaGiocatore(giocatore, statoGioco.getListaGiocatori());
		while (scorriCarte.hasNext()){
			CartaInVendita cartaInVendita = scorriCarte.next();
			Carta istanzaCarta = AdattatoreIstanze.cercaIstanzaCarta(cartaInVendita.getCarta(), statoGioco.getListaCarte());
			Giocatore istanzaVenditore = AdattatoreIstanze.cercaIstanzaGiocatore(cartaInVendita.getProprietario(), statoGioco.getListaGiocatori());
			if (istanzaVenditore.getNumero() != istanzaAcquirente.getNumero() && cartaInVendita.getCosto() <= istanzaVenditore.getDanari()) {
				statoGioco.getListaCarteInVendita().add(new CartaInVendita(istanzaCarta,cartaInVendita.getCosto(),istanzaVenditore));
			}
		}
		CartaInVendita cartaInVendita = view.scegliCartaDaComprare(statoGioco.getListaCarteInVendita(), istanzaAcquirente);
		statoGioco.getListaCarteInVendita().remove(cartaInVendita);
		return cartaInVendita;
	}

	/**Le mosse di tipo scelta non prevedono l'implementazione del metodo esegui, offline,
	 * in quanto la loro utilità inizia e finisce nell'ambiente locale del giocatore 
	 * che le effettua. Il loro risultato sarà poi eseguito: a ogni mossa-scelta corrisponde
	 * naturalmente una mossa.*/
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco) {

	}
}