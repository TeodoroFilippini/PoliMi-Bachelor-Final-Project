package controller;

import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata al salto del turno, inteso sia come vero salto della mossa
 * (in caso di deadlock logico), sia come conclusione del proprio turno senza
 * effettuare la mossa, facoltativa, di compravendita.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaSaltaTurno implements Mossa {

	private static final long serialVersionUID = 1L;

	/**
	 * La mossa sarà vuota; l'unica cosa che fa è rendere tutti i bottoni
	 * non selezionabili per la view del giocatore, il quale ha concluso il suo turno.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco){
		view.disabilitaBottoni();
	}
}
