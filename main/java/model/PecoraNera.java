package model;

/**
 * Classe che descrive l'animale Pecora Nera, 
 * che si muove casualmente nella mappa se un 
 * ostacolo non glielo impedisce, e alla fine del
 * gioco vale il doppio dei punti di una pecora 
 * qualsiasi.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */

public class PecoraNera extends Animale{

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore dell'oggetto Pecora Nera.
	 * @param posizione la posizione iniziale della pecora nera.
	 */
	public PecoraNera(Regione posizione) {
		super(posizione);
	}

	/**
	 * 
	 * @return la stringa che descrive la pecora nera (unica)
	 * e la sua posizione.
	 */
	@Override 
	public String toString(){
		return "PecoraNera nella regione " + posizione.getNomeRegione();
	}
}
