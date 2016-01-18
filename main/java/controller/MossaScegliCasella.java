package controller;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Casella;
import model.StatoGioco;
import view.FinestraNotifica;
import view.InterfacciaView;
import RMIServer.InterfacciaRMI;

/**
 * Mossa associata alla scelta della casella, in qualunque caso in cui essa sia
 * selezionabile: a inizio gioco per posizionare i pastori, per scegliere i pastori nel
 * gioco in due giocatori, e per spostare il pastore.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaScegliCasella implements MossaScelta{

	private static final long serialVersionUID = 1L;
	private List<Casella> listaCaselle;

	public MossaScegliCasella(List<Casella> listaCaselle){
		this.listaCaselle = listaCaselle;
	}

	/**
	 * Mette sullo stream online la casella scelta.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco, ObjectOutputStream outputStream){
		Casella casellaScelta = scegliCasella(view, statoGioco);
		try {
			outputStream.writeObject(casellaScelta);
		} catch (EOFException e){
			new FinestraNotifica("Fine dello stream associato all'oggetto \"casella\" raggiunta inaspettatamente", e);
		}catch (IOException e) {
			new FinestraNotifica("Errore durante l'invio dell'oggetto casella", e);
		} 
	}

	/**
	 * Mette nella coda online la casella scelta.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco, InterfacciaRMI server){
		Casella casellaScelta = scegliCasella(view, statoGioco);
		try {
			server.aggiungiOggettoInCoda(casellaScelta);
		} catch (RemoteException e) {
			new FinestraNotifica("Errore di connessione durante l'invio della mossa \"scegli casella\"", e);
		}
	}

	/**
	 * Fa scegliere la casella al giocatore.
	 * @param view la view del giocatore.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	private Casella scegliCasella(InterfacciaView view, StatoGioco statoGioco) {
		Iterator<Casella> scorriCaselle = listaCaselle.iterator();
		List<Casella> listaIstanzeCaselle = new ArrayList<Casella>();
		while (scorriCaselle.hasNext()){
			Casella casella = scorriCaselle.next();
			listaIstanzeCaselle.add(AdattatoreIstanze.cercaIstanzaCasella(casella, statoGioco.getMappa().getListaCaselle()));
		}
		return view.scegliCasella(listaIstanzeCaselle);
	}

	/**Le mosse di tipo scelta non prevedono l'implementazione del metodo esegui, offline,
	 * in quanto la loro utilità inizia e finisce nell'ambiente locale del giocatore 
	 * che le effettua. Il loro risultato sarà poi eseguito: a ogni mossa-scelta corrisponde
	 * naturalmente una mossa.*/
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco) {

	}
}
