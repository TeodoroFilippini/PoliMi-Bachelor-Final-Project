package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Animale;
import model.Regione;
import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata alla rimozione di un animale da una regione, 
 * quando gli viene sparato con successo, quando viene mangiato
 * dal lupo o quando l'animale è un agnello che invecchia e diventa
 * una pecora o un ariete.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaRimuoviAnimale  implements Mossa{

	private static final long serialVersionUID = 1L;
	Animale animale;

	public MossaRimuoviAnimale(Animale animale) {
		this.animale = animale;
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
		Animale istanzaAnimale = AdattatoreIstanze.cercaIstanzaAnimale(animale, listaAnimaliCompleta);
		Regione regione = istanzaAnimale.getPosizione();
		regione.getListaAnimali().remove(istanzaAnimale);
		view.rimuoviAnimaleView(istanzaAnimale);

	}

}
