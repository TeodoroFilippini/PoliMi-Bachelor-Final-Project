package controller;

import model.PecoraNera;
import model.Regione;
import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata al movimento, effettuato dal server e comunicato
 * ai client, della pecora nera.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaSpostaPecoraNera implements Mossa{

	private static final long serialVersionUID = 1L;
	private Regione arrivo;
	private int dado;

	public MossaSpostaPecoraNera(Regione arrivo, int dado){
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
	public void eseguiMossa(InterfacciaView view,StatoGioco statoGioco){
		PecoraNera pecoraNera = statoGioco.getPecoraNera();
		Regione istanzaArrivo = AdattatoreIstanze.cercaIstanzaRegione(arrivo,statoGioco.getMappa().getListaRegioni());
		pecoraNera.setPosizione(istanzaArrivo);
		view.muoviPecoraNera(istanzaArrivo, dado);
	}

}
