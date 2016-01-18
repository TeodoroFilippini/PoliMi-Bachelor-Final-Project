package model;

/**
 * Classe che implementa l'animale Ariete. 
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */

public class Ariete extends Animale {

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore dell'oggetto Ariete.
	 * @param posizione la posizione in cui va creato l'ariete.
	 */
	public Ariete(Regione posizione) {
		super(posizione);
	}

	/**
	 * @return la stringa che descrive l'ariete, che sarà usata dalla
	 * 		   classe FinestraMessaggi per descriverlo.
	 */
	@Override 
	public String toString(){
		return "Ariete nella regione " + posizione.getNomeRegione();
	}
}
