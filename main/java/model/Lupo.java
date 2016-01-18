package model;

/**
 * Classe che descrive l'animale Lupo, 
 * che si muove casualmente nella mappa e mangia
 * gli altri animali, ad eccezione di agnelli e della
 * pecora nera.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */

public class Lupo extends Animale{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Costruttore della classe. 
	 * @param posizione la posizione iniziale del lupo.
	 */
	public Lupo(Regione posizione) {
		super(posizione);
	}
	
	/**
	 * 
	 *  @return stringa che descrive il lupo (unico) e la sua posizione.
	 */
	@Override 
	public String toString(){
		return "Lupo nella regione " + posizione.getNomeRegione();
	}
}
