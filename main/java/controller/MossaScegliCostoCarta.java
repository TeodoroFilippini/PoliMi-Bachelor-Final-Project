package controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;

import model.Carta;
import model.StatoGioco;
import view.FinestraNotifica;
import view.InterfacciaView;
import RMIServer.InterfacciaRMI;

/**
 * Mossa associata alla scelta del prezzo da assegnare ad una carta
 * nel momento in cui la si pone in vendita.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaScegliCostoCarta implements MossaScelta{

	private static final long serialVersionUID = 1L;
	private Carta carta;

	public MossaScegliCostoCarta(Carta carta){
		this.carta = carta;
	}

	/**
	 * Mette sullo stream online il costo della carta dopo averlo fatto scegliere.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco, ObjectOutputStream output){
		Carta istanzaCarta = AdattatoreIstanze.cercaIstanzaCarta(carta, statoGioco.getListaCarte());
		int costoCarta = view.scegliCostoCarta(istanzaCarta);
		try {
			output.writeObject(costoCarta);
		}catch (IOException e) {
			new FinestraNotifica("Errore durante l'invio dell'oggetto \"carta con costo associato\"", e);
		} 
	}

	/**
	 * Mette nella coda online il costo della carta dopo averlo fatto scegliere.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco, InterfacciaRMI server){
		Carta istanzaCarta = AdattatoreIstanze.cercaIstanzaCarta(carta, statoGioco.getListaCarte());
		int costoCarta = view.scegliCostoCarta(istanzaCarta);
		try {
			server.aggiungiOggettoInCoda(costoCarta);
		} catch (RemoteException e) {
			new FinestraNotifica("Errore di connessione durante l'invio della mossa \"scegli costo carta\"", e);
		}
	}

	/**Le mosse di tipo scelta non prevedono l'implementazione del metodo esegui, offline,
	 * in quanto la loro utilità inizia e finisce nell'ambiente locale del giocatore 
	 * che le effettua. Il loro risultato sarà poi eseguito: a ogni mossa-scelta corrisponde
	 * naturalmente una mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco) {

	}

}
