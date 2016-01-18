package controller;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Regione;
import model.StatoGioco;
import view.FinestraNotifica;
import view.InterfacciaView;
import RMIServer.InterfacciaRMI;

/**
 * Mossa associata alla scelta di una regione, in caso di mossa sposta pecora,
 * accoppiamento, o spara.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaScegliRegione implements MossaScelta{

	private static final long serialVersionUID = 1L;
	private List<Regione> listaRegioni;

	public MossaScegliRegione(List<Regione> listaRegioni){
		this.listaRegioni = listaRegioni;
	}

	/**
	 * Mette sullo stream online la regione scelta.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco, ObjectOutputStream outputStream){
		Regione regioneScelta = scegliRegione(view, statoGioco);
		try {
			outputStream.writeObject(regioneScelta);
		} catch (EOFException e){
			new FinestraNotifica("Fine dello stream associato all'oggetto \"regione\" raggiunta inaspettatamente", e);
		}catch (IOException e) {
			new FinestraNotifica("Errore durante l'invio dell'oggetto regione", e);
		} 

	}

	/**
	 * Manda alla coda la regione scelta.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco, InterfacciaRMI server){
		Regione regioneScelta = scegliRegione(view, statoGioco);
		try {
			server.aggiungiOggettoInCoda(regioneScelta);
		} catch (RemoteException e) {
			new FinestraNotifica("Errore di connessione durante l'invio della mossa \"scegli regione\"", e);
		}
	}

	/**
	 * Fa scegliere la regione al giocatore tramite la sua view.
	 * @param view la view del giocatore.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	private Regione scegliRegione(InterfacciaView view, StatoGioco statoGioco) {
		Iterator<Regione> scorriRegioni = listaRegioni.iterator();
		List<Regione> listaIstanzeRegioni = new ArrayList<Regione>();
		while (scorriRegioni.hasNext()){
			Regione regione = scorriRegioni.next();
			listaIstanzeRegioni.add(AdattatoreIstanze.cercaIstanzaRegione(regione, statoGioco.getMappa().getListaRegioni()));
		}
		return view.scegliRegione(listaIstanzeRegioni);
	}

	/**Le mosse di tipo scelta non prevedono l'implementazione del metodo esegui, offline,
	 * in quanto la loro utilità inizia e finisce nell'ambiente locale del giocatore 
	 * che le effettua. Il loro risultato sarà poi eseguito: a ogni mossa-scelta corrisponde
	 * naturalmente una mossa.*/
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco) {

	}
}
