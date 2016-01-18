package controller;

import model.Animale;
import model.Lupo;
import model.Regione;
import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata all'azione "mangia" effettuata ogni
 * turno dal lupo appena dopo essersi spostato.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaLupoMangia implements Mossa {

	private static final long serialVersionUID = 1L;

	/**
	 * Esegue la mossa: attraverso la view, mostra i dovuti cambiamenti
	 * che ha effettuato sullo stato del gioco.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco){
		Lupo lupo = statoGioco.getLupo();
		Regione regione = lupo.getPosizione();
		if (regione.contieneArieti() || regione.contienePecore()){
			Animale animale = regione.getListaPecoreArieti().get(0);
			regione.rimuoviAnimale(animale);
			view.stampaMessaggio("il lupo mangia un innocente esemplare di "+animale.toString()+" !");
			view.rimuoviAnimaleView(animale);
		} else {
			view.stampaMessaggio("il lupo, per questo turno, digiuna!");
		}
	}
}
