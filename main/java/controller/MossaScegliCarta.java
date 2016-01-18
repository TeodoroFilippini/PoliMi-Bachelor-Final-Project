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
 * Mossa associata alla scelta di una carta, prima di effettuare
 * la mossa compra carta.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaScegliCarta implements MossaScelta{
	private static final long serialVersionUID = 1L;
	private List<Carta> listaCarte;

	public MossaScegliCarta(List<Carta> listaCarte){
		this.listaCarte = listaCarte;
	}

	/**
	 * Manda sullo stream onlinela carta scelta.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco, ObjectOutputStream outputStream){
		Carta cartaScelta = scegliCarta(view, statoGioco);
		try {
			outputStream.writeObject(cartaScelta);
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
	public void eseguiMossa(InterfacciaView view,StatoGioco statoGioco, InterfacciaRMI server){
		Carta cartaScelta = scegliCarta(view,statoGioco);
		try {
			server.aggiungiOggettoInCoda(cartaScelta);
		} catch (RemoteException e) {
			new FinestraNotifica("Errore durante l'invio al server della mossa \"scegli carta\"", e);
		}
	}

	/**
	 * Fa scegliere la carta al giocatore.
	 * @param view la view del giocatore.
	 * @param statoGioco lo stato corrente del gioco.
	 * @return la carta scelta.
	 */
	private Carta scegliCarta(InterfacciaView view, StatoGioco statoGioco) {
		Iterator<Carta> scorriCarte = listaCarte.iterator();
		List<Carta> listaIstanzeCarte = new ArrayList<Carta>();
		while (scorriCarte.hasNext()){
			Carta carta = scorriCarte.next();
			listaIstanzeCarte.add(AdattatoreIstanze.cercaIstanzaCarta(carta, statoGioco.getListaCarte()));
		}
		return view.scegliCarta(listaIstanzeCarte);
	}

	/**
	 *Le mosse di tipo scelta non prevedono l'implementazione del metodo esegui, offline,
	 * in quanto la loro utilità inizia e finisce nell'ambiente locale del giocatore 
	 * che le effettua. Il loro risultato sarà poi eseguito: a ogni mossa-scelta corrisponde
	 * naturalmente una mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco) {
	}

}
