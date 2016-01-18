package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Animale;
import model.Regione;
import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata allo spostamento di un animale da una regione ad un'altra.
 * Nel gioco la mossa si chiama "sposta pecora", tuttavia permette di spostare
 * anche gli arieti e la pecora nera.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaSpostaPecora implements Mossa{

	private static final long serialVersionUID = 1L;
	private Animale animaleSpostato;
	private Regione regioneDestinazione;

	public MossaSpostaPecora (Animale animaleSpostato, Regione regioneDestinazione){
		this.animaleSpostato = animaleSpostato;
		this.regioneDestinazione = regioneDestinazione;
	}

	/**
	 * Esegue la mossa: attraverso la view, mostra i dovuti cambiamenti
	 * che ha effettuato sullo stato del gioco.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa (InterfacciaView view, StatoGioco statoGioco){
		List<Animale> listaAnimaliCompleta = new ArrayList<Animale>();
		Iterator<Regione> scorriRegioni = statoGioco.getMappa().getListaRegioni().iterator();
		while (scorriRegioni.hasNext()){
			Regione regione = scorriRegioni.next();
			listaAnimaliCompleta.addAll(regione.getListaAnimali());
		}
		Animale istanzaAnimale = AdattatoreIstanze.cercaIstanzaAnimale(animaleSpostato,listaAnimaliCompleta);
		Regione istanzaRegioneDestinazione = AdattatoreIstanze.cercaIstanzaRegione(regioneDestinazione, statoGioco.getMappa().getListaRegioni());
		istanzaAnimale.setPosizione(istanzaRegioneDestinazione);
		view.muoviAnimale(istanzaAnimale, istanzaRegioneDestinazione);
	}
}
