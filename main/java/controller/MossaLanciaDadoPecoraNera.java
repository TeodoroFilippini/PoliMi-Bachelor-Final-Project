package controller;

import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata al lancio del dado della pecora nera.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaLanciaDadoPecoraNera implements Mossa{

	private static final long serialVersionUID = 1L;
	int dado;

	public MossaLanciaDadoPecoraNera(int dado) {
		this.dado = dado;
	}

	/**
	 * Esegue la mossa: attraverso la view, mostra i dovuti cambiamenti
	 * che ha effettuato sullo stato del gioco.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa (InterfacciaView view,StatoGioco statoGioco){
		view.lanciaDadoPecoraNera(dado);
	}

}
