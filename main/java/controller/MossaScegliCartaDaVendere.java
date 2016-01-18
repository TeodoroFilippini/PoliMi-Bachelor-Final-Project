package controller;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Carta;
import model.StatoGioco;
import view.FinestraNotifica;
import view.InterfacciaView;
import RMIServer.InterfacciaRMI;

/**
 * Mossa associata alla scelta della carta da vendere durante la mossa vendi carta.
 * Se non se ne vuole vendere nessuna, o smettere di venderne, l'esegui
 * mossa riceve una carta nulla, che corrisponde ad una mossa chiudi 
 * finestra.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaScegliCartaDaVendere implements MossaScelta{

	private static final long serialVersionUID = 1L;
	private List<Carta> carteVendibili;

	public MossaScegliCartaDaVendere(List<Carta> carteVendibili){
		this.carteVendibili = carteVendibili;
	}

	/**
	 * Mette sullo stream online la carta scelta.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco,ObjectOutputStream output){
		Carta cartaScelta = scegliCarta(view, statoGioco);
		try {
			if (cartaScelta != null) {
				output.writeObject(cartaScelta);
			} else {
				output.writeObject(new MossaChiudiFinestra());
			}
		} catch (EOFException e){
			new FinestraNotifica("Fine dello stream associato all'oggetto \"carta\" raggiunta inaspettatamente", e);
		}catch (IOException e) {
			new FinestraNotifica("Errore durante l'invio dell'oggetto carta", e);
		} 
	}

	/**
	 * Manda in coda la mossa associata alla carta scelta.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco,InterfacciaRMI server){
		Carta cartaScelta = scegliCarta(view, statoGioco);
		try {
			if (cartaScelta != null) {
				server.aggiungiOggettoInCoda(cartaScelta);
			} else {
				server.aggiungiOggettoInCoda(new MossaChiudiFinestra());
			}
		} catch (RemoteException e) {
			new FinestraNotifica("Errore di connessione durante l'invio della mossa \"vendi carta\"", e);
		}
	}

	/**
	 * Fa scegliere la carta al giocatore tramite la sua view.
	 * @param view la view del giocatore.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	private Carta scegliCarta(InterfacciaView view, StatoGioco statoGioco) {
		List<Carta> listaIstanzeCarte = new ArrayList<Carta>();
		Iterator<Carta> scorriCarte = carteVendibili.iterator();
		while (scorriCarte.hasNext()){
			listaIstanzeCarte.add(AdattatoreIstanze.cercaIstanzaCarta(scorriCarte.next(), statoGioco.getListaCarte()));
		}
		return view.scegliCartaDavendere(listaIstanzeCarte);
	}

	/**Le mosse di tipo scelta non prevedono l'implementazione del metodo esegui, offline,
	 * in quanto la loro utilità inizia e finisce nell'ambiente locale del giocatore 
	 * che le effettua. Il loro risultato sarà poi eseguito: a ogni mossa-scelta corrisponde
	 * naturalmente una mossa.*/
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco) {

	}
}
