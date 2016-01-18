package controller;

import model.Animale;
import model.Regione;
import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata all'aggiunta di un animale ad una regione.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaAggiungiAnimale implements Mossa{

	private static final long serialVersionUID = 1L;
	Animale animale;
	Regione regione;

	public MossaAggiungiAnimale(Animale animale, Regione regione) {
		this.animale = animale;
		this.regione = regione;
	}

	/**
	 * Esegue la mossa: attraverso la view, mostra i dovuti cambiamenti
	 * che ha effettuato sullo stato del gioco.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa (InterfacciaView view, StatoGioco statoGioco){
		Regione istanzaRegione = AdattatoreIstanze.cercaIstanzaRegione (regione,statoGioco.getMappa().getListaRegioni());
		istanzaRegione.getListaAnimali().add(animale);
		view.aggiungiAnimaleView(animale, istanzaRegione);
	}

}
