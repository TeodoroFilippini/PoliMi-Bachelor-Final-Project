package controller;

import java.io.ObjectOutputStream;

import model.StatoGioco;
import view.InterfacciaView;
import RMIServer.InterfacciaRMI;

/**
 * Mossa associata ad un qualsiasi tipo di scelta, che dunque verrà eseguita puramente in ambito locale.
 * I suoi risultati (ad esempio l'animale scelto) saranno poi propagati agi altri giocatori.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public interface MossaScelta extends Mossa{
	/**
	 * Esegue la mossa in questione. Questo metodo
	 * sarà sempre Overridato (versione di rete per Socket)
	*/
	void eseguiMossa(InterfacciaView view,StatoGioco statoGioco, ObjectOutputStream objectOutputStream);

	/**
	 * Esegue la mossa in questione. Questo metodo
	 * sarà sempre Overridato (versione di rete per RMI)
	*/
	void eseguiMossa(InterfacciaView view, StatoGioco statoGioco, InterfacciaRMI server);

}
