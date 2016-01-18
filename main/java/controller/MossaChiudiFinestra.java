package controller;

import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata alla chiusura della finestra di vendita delle carte.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaChiudiFinestra implements Mossa{

	private static final long serialVersionUID = 1L;
	/**
	 *Questo metodo non viene utilizzato: la mossachiudifinestra è adoperata
	 *solo come mossa-flag per segnalare al server di chiudere la finestra durante
	 *la mossa vendi carta, che prevede la vendita di zero, una o più carte, costituendo
	 *di fatto la mossa più "libera" del gioco. 
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco) {
	}

}
