package controller;

import model.Animale;
import model.Regione;
import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata all'azione sparatoria.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaSpara implements Mossa {

	private static final long serialVersionUID = 1L;
	private Animale animale;
	private boolean necessariaCorruzione;
	private Regione regione;
	private boolean riuscito;

	public MossaSpara(Animale animale, Regione regione,boolean riuscito,boolean necessariaCorruzione){
		this.animale = animale;
		this.regione = regione;
		this.riuscito = riuscito;
		this.necessariaCorruzione = necessariaCorruzione;
	}
	
	/**
	 * Esegue la mossa: attraverso la view, mostra i dovuti cambiamenti
	 * che ha effettuato sullo stato del gioco.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco){
		if (riuscito){
			Regione istanzaRegione = AdattatoreIstanze.cercaIstanzaRegione(regione,statoGioco.getMappa().getListaRegioni());
			Animale istanzaAnimale = AdattatoreIstanze.cercaIstanzaAnimale(animale,istanzaRegione.getListaAnimali());
			istanzaRegione.rimuoviAnimale(istanzaAnimale);
			view.rimuoviAnimaleView(istanzaAnimale);
			if (!necessariaCorruzione) {
				view.stampaMessaggio("Nessuno ha visto niente...");
			}
			view.stampaMessaggio("Un innocente esemplare di " + istanzaAnimale.toString() + " è stato assassinato");
		} else {
			view.stampaMessaggio("Sparatoria Fallita");
		}
	}

}
