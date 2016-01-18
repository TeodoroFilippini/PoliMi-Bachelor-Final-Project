package model;

/**
 * Classe che implementa l'animale Pecora. 
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */

public class Pecora extends Animale {

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore dell'oggetto pecora.
	 * @param posizione la posizione in cui generare 
	 * 		  la pecora.
	 */
	public Pecora(Regione posizione) {
		super(posizione);
	}

	/**
	 * 
	 * @return la stringa che identifica la pecora tramite la sua
	 * 		   regione, utilizzata dalla FinestraStampaMessaggi per descriverla
	 * 		   durante il gioco.
	 */
	@Override 
	public String toString(){
		return "Pecora nella regione " + posizione.getNomeRegione();
	}
}
