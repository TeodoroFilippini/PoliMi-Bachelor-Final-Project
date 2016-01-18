package controller;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Pastore;
import model.StatoGioco;
import view.FinestraNotifica;
import view.InterfacciaView;
import RMIServer.InterfacciaRMI;

/**
 * Mossa associata alla scelta di un pastore, necessaria all'inizio del turno
 * nel caso di gioco a due giocatori.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaScegliPastore implements MossaScelta{

	private static final long serialVersionUID = 1L;
	private List<Pastore> listaPastori;

	public MossaScegliPastore(List<Pastore> listaPastori){
		this.listaPastori = listaPastori;
	}

	/**
	 * Mette sullo stream online il pastore scelto.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco, ObjectOutputStream outputStream){
		Pastore pastoreScelto = scegliPastore(view, statoGioco);
		try {
			outputStream.writeObject(pastoreScelto);
		} catch (EOFException e){
			new FinestraNotifica("Fine dello stream associato all'oggetto \"pastore\" raggiunta inaspettatamente", e);
		}catch (IOException e) {
			new FinestraNotifica("Errore durante l'invio dell'oggetto pastore", e);
		} 

	}

	/**
	 * Mette sullo stream online il pastore scelto.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa (InterfacciaView view, StatoGioco statoGioco, InterfacciaRMI server){
		Pastore pastoreScelto = scegliPastore(view, statoGioco);
		try {
			server.aggiungiOggettoInCoda(pastoreScelto);
		} catch (RemoteException e) {
			new FinestraNotifica("Errore di connessione durante l'invio della mossa \"scegli pastore\"", e);
		}
	}

	/**
	 * Sceglie il pastore.
	 * @param view la view del giocatore.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	private Pastore scegliPastore(InterfacciaView view, StatoGioco statoGioco) {
		Iterator<Pastore> scorriPastori = listaPastori.iterator();
		List<Pastore> listaIstanzePastori = new ArrayList<Pastore>();
		while (scorriPastori.hasNext()){
			Pastore pastore = scorriPastori.next();
			listaIstanzePastori.add(AdattatoreIstanze.cercaIstanzaPastore(pastore, statoGioco.getListaPastori()));
		}
		return view.scegliPastore(listaIstanzePastori); 
	}

	/**Le mosse di tipo scelta non prevedono l'implementazione del metodo esegui, offline,
	 * in quanto la loro utilità inizia e finisce nell'ambiente locale del giocatore 
	 * che le effettua. Il loro risultato sarà poi eseguito: a ogni mossa-scelta corrisponde
	 * naturalmente una mossa.*/
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco) {

	}
}
