package controller;

import model.Agnello;
import model.Regione;
import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata all'accoppiamento di una pecora con un ariete.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaAccoppiamento implements Mossa{

	private static final long serialVersionUID = 1L;
	private Regione regione;
	private boolean riuscito;

	/**
	 * Costruttore
	 * @param regione
	 * @param riuscito
	 */
	public MossaAccoppiamento(Regione regione, boolean riuscito) {
		this.regione = regione;
		this.riuscito = riuscito;
	}

	/**
	 * Esegue la mossa: attraverso la view, mostra i dovuti cambiamenti
	 * che ha effettuato sullo stato del gioco.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco) {
		Regione istanzaRegione = AdattatoreIstanze.cercaIstanzaRegione(regione, statoGioco.getMappa().getListaRegioni());
		if(riuscito){
			Agnello agnello = new Agnello(istanzaRegione);
			view.aggiungiAnimaleView(agnello,istanzaRegione);
			view.stampaMessaggio("Accoppiamento riuscito! è nato un nuovo Agnello");
			return;
		}
		view.stampaMessaggio("Accoppiamento fallito");
	}
}
