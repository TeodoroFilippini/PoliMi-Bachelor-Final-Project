package controller;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Animale;
import model.Regione;
import model.StatoGioco;
import view.FinestraNotifica;
import view.InterfacciaView;
import RMIServer.InterfacciaRMI;

/**
 * Mossa associata alla scelta di una pecora, di un ariete o della pecora
 * nera se li si vuole muovere da una regione all'altra.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaScegliAnimale implements MossaScelta{

	private static final long serialVersionUID = 1L;
	private List<Animale> listaAnimali;

	public MossaScegliAnimale (List<Animale> listaAnimali){
		this.listaAnimali = listaAnimali;
	}

	/**
	 * Manda sullo stream online l'animale scelto.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco, ObjectOutputStream outputStream){
		Animale animaleScelto = scegliAnimale(view, statoGioco);
		try {
			outputStream.writeObject(animaleScelto);
		} catch (EOFException e){
			new FinestraNotifica("Fine dello stream associato all'oggetto \"animale\" raggiunta inaspettatamente", e);
		}catch (IOException e) {
			new FinestraNotifica("Errore durante l'invio dell'oggetto animale", e);
		} 
	}

	/**
	 * Mette in coda la mossa che comunica l'animale scelto.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view,StatoGioco statoGioco, InterfacciaRMI server){
		Animale animaleScelto = scegliAnimale(view,statoGioco);
		try {
			server.aggiungiOggettoInCoda(animaleScelto);
		} catch (RemoteException e) {
			new FinestraNotifica("Errore durante l'invio al server della mossa \"scegli animale\"", e);
		}
	}
	
	/**
	 * Effettua, tramite la view del giocatore, la scelta dell'animale.
	 * @param view la view del giocatore.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	private Animale scegliAnimale(InterfacciaView view, StatoGioco statoGioco) {
		List<Animale> listaIstanzeAnimali = new ArrayList<Animale>();
		List<Animale> listaAnimaliCompleta = new ArrayList<Animale>();
		Iterator<Regione> scorriRegioni = statoGioco.getMappa().getListaRegioni().iterator();
		while (scorriRegioni.hasNext()){
			Regione regione = scorriRegioni.next();
			listaAnimaliCompleta.addAll(regione.getListaAnimali());
		}
		Iterator<Animale> scorriAnimali = listaAnimali.iterator();
		while (scorriAnimali.hasNext()){
			Animale animale = scorriAnimali.next();
			listaIstanzeAnimali.add(AdattatoreIstanze.cercaIstanzaAnimale(animale, listaAnimaliCompleta));
		}
		return view.scegliAnimale(listaIstanzeAnimali);
	}
	
	/**Le mosse di tipo scelta non prevedono l'implementazione del metodo esegui, offline,
	 * in quanto la loro utilità inizia e finisce nell'ambiente locale del giocatore 
	 * che le effettua. Il loro risultato sarà poi eseguito: a ogni mossa-scelta corrisponde
	 * naturalmente una mossa.*/
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco) {

	}
}