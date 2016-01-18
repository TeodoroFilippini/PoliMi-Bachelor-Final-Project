package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Agnello;
import model.Ariete;
import model.Costanti;
import model.Pecora;
import model.Regione;
import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata all'invecchiamento di tutti gli agnelli
 * presenti nel gioco.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaInvecchiaAgnelli implements Mossa{

	private static final long serialVersionUID = 1L;

	/**
	 * Esegue la mossa: attraverso la view, mostra i dovuti cambiamenti
	 * che ha effettuato sullo stato del gioco.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco){
		List<Regione> listaRegioni = statoGioco.getMappa().getListaRegioni();
		Iterator<Regione> scorriRegioni = listaRegioni.iterator();
		List<Agnello> listaAgnelliDaEvolvere = new ArrayList<Agnello>();
		while(scorriRegioni.hasNext()){
			Regione regione = scorriRegioni.next();
			Iterator<Agnello> scorriAgnelli = regione.getListaAgnelli().iterator();
			while (scorriAgnelli.hasNext()){
				Agnello agnello = scorriAgnelli.next();
				agnello.incrementaContatore();
				if (agnello.getContatoreTurni() == Costanti.NUMERO_TURNI_CRESCITA_AGNELLO){
					listaAgnelliDaEvolvere.add(agnello);
				}
			}
		}

		Iterator<Agnello> scorriAgnelli = listaAgnelliDaEvolvere.iterator();
		while (scorriAgnelli.hasNext()){
			Agnello agnello = scorriAgnelli.next();
			Regione regione = agnello.getPosizione();
			String messaggio = "L'agnello in "+regione.toString()+" è cresciuto: ";
			if (Math.random() > 0.5){
				Pecora pecora = new Pecora(regione);
				view.aggiungiAnimaleView(pecora, regione);
				view.stampaMessaggio(messaggio+"è diventata una bellissima pecora");
			}else{
				Ariete ariete = new Ariete(regione);
				view.aggiungiAnimaleView(ariete, regione);
				view.stampaMessaggio(messaggio+"è diventato un bell'ariete");
			}
			regione.rimuoviAnimale(agnello);
			view.rimuoviAnimaleView(agnello);
		}
	}
}
