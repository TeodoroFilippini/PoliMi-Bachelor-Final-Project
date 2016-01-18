package model;

/**
 * Classe che implementa l'animale Agnello. 
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */


public class Agnello extends Animale {
	
	private static final long serialVersionUID = 1L;
	private int contatoreTurni;

	/**
	 * Il costruttore della classe Agnello pone il contatore dei suoi
	 * turni di vita a zero. In seguito, ogni turno esso sarà incrementato con
	 * l'apposita funzione.
	 * @param posizione la regione in cui sarà creato l'agnello.
	 */
	public Agnello(Regione posizione) {
		super(posizione);
		contatoreTurni = 0;
	}

	/**
	* @return il numero di turni di vita dell'agnello
	*/
	public int getContatoreTurni(){
		return contatoreTurni;
	}

	/**
	 * incrementa il contatore del numero di turni di vita dell'agnello
	 */
	public void incrementaContatore(){
		contatoreTurni++;
	}

	/**
	 * @return La stringa che descrive l'animale, per stamparla nella classe FinestraMessaggi
	 */
	@Override 
	public String toString(){
		return "Agnello nella regione " + posizione.getNomeRegione();
	}
}
