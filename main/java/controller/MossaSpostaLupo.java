package controller;

import model.Lupo;
import model.Regione;
import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata al movimento, effettuato dal server e comunicato
 * ai client, del lupo.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaSpostaLupo implements Mossa {

	private static final long serialVersionUID = 1L;
	private Regione arrivo;
	private int dado;

	public MossaSpostaLupo(Regione arrivo, int dado){
		this.arrivo = arrivo;
		this.dado = dado;
	}

	/**
	 * Esegue la mossa: attraverso la view, mostra i dovuti cambiamenti
	 * che ha effettuato sullo stato del gioco.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco){
		Regione istanzaArrivo = AdattatoreIstanze.cercaIstanzaRegione (arrivo,statoGioco.getMappa().getListaRegioni());
		Lupo lupo = statoGioco.getLupo();
		lupo.setPosizione(istanzaArrivo);
		view.muoviLupo(istanzaArrivo, dado);
	}
}
