package controller;

import java.io.Serializable;

import model.StatoGioco;
import view.InterfacciaView;

/**
 * Interfaccia che rappresenta gli oggetti Mossa che sono spediti in rete in un
 * gioco online. Essi incapsulano le azioni che un giocatore ha effettuato 
 * tramite la sua view, e che il server metterà in atto.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */

public interface Mossa extends Serializable{

	/**
	 * Metodo che esegue la mossa specificata, mettendola cioè in atto.
	 * @param view la view associata al giocatore che compie la mossa.
	 * @param statoGioco lo stato del gioco corrente.
	 */
	void eseguiMossa(InterfacciaView view, StatoGioco statoGioco);

}
