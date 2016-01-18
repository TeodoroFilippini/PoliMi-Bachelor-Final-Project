package controller;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.List;

import model.StatoGioco;
import model.TipoMossa;
import view.FinestraNotifica;
import view.InterfacciaView;
import RMIServer.InterfacciaRMI;

/**
 * Mossa associata alla scelta della mossa da effettuare, tra quelle disponibili,
 * durante il turno.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaScegliMossa implements MossaScelta{

	private static final long serialVersionUID = 1L;
	private int azioniRimanenti;
	private List<TipoMossa> listaMosse;

	public MossaScegliMossa(List<TipoMossa> listaMosse, int azioniRimanenti){
		this.listaMosse = listaMosse;
		this.azioniRimanenti = azioniRimanenti;
	}

	/**
	 * Mette sullo stream online la mossa dopo averla fatta scegliere al giocatore.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco, ObjectOutputStream outputStream){
		view.getGiocatore().setAzioniRimanentiPerTurno(azioniRimanenti);
		TipoMossa mossaScelta = view.scegliMossa(listaMosse);
		try {
			outputStream.writeObject(mossaScelta);
		} catch (EOFException e){
			new FinestraNotifica("Fine dello stream associato all'oggetto \"mossa\" raggiunta inaspettatamente", e);
		}catch (IOException e) {
			new FinestraNotifica("Errore durante l'invio dell'oggetto mossa", e);
		} 
	}

	/**
	 * Mette nella coda online la mossa dopo averla fatta scegliere al giocatore.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco, InterfacciaRMI server){
		view.getGiocatore().setAzioniRimanentiPerTurno(azioniRimanenti);
		TipoMossa mossaScelta = view.scegliMossa(listaMosse);
		try {
			server.aggiungiOggettoInCoda(mossaScelta);
		} catch (RemoteException e) {
			new FinestraNotifica("Errore di connessione durante l'invio della mossa \"scegli mossa\"", e);
		}
	}

	/**Le mosse di tipo scelta non prevedono l'implementazione del metodo esegui, offline,
	 * in quanto la loro utilità inizia e finisce nell'ambiente locale del giocatore 
	 * che le effettua. Il loro risultato sarà poi eseguito: a ogni mossa-scelta corrisponde
	 * naturalmente una mossa.*/
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco) {

	}
}
