package model;

import java.io.Serializable;


/**
 * Classe che implementa il pastore. ogni pastore è associato
 * univocamente ad un giocatore. C'è un ID univoco che identifica
 * ogni pastore nell'ordine della sua creazione, e torna comodo per
 * fare confronti tra oggetti pastore serializzati. Il metodo equals()
 * infatti non è adeguato per la presenza di attributi non primitivi.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class Pastore implements Serializable{

	private static int contatoreiD = 0;
	private static final long serialVersionUID = 1L;
	private Giocatore giocatore;
	private int iD;
	private Casella posizione;

	/**
	 * Costruttore della classe. Imposta il giocatore
	 * associato al pastore e l'ID del pastore stesso.
	 * Poi incrementa l'ID (statico).
	 * @param giocatore il giocatore che manovra il pastore.
	 */
	public Pastore (Giocatore giocatore){
		this.giocatore = giocatore;
		this.iD = contatoreiD;
		contatoreiD++;
	}
	
	/**
	 * 
	 * @return il giocatore al qual il pastore è associato.
	 */
	public Giocatore getGiocatore(){
		return giocatore;
	}

	/**
	 * 
	 * @return l'ID univoco del pastore.
	 */
	public int getID() {
		return iD;
	}

	/**
	 * 
	 * @return la posizione corrente del pastore.
	 */
	public Casella getPosizione() {
		return posizione;
	}
	
	/**
	 * Imposta la posizione del pastore uguale quella di input.
	 * Il controllo che la posizione corrente non sia nulla è dovuta 
	 * al fatto che, prima di spostare il pastore, viene cancellata
	 * la sua presenza nella vecchia casella; Alla prima chiamata di
	 * questo metodo, tuttavia, il pastore ancora non ha una posizione.
	 * @param posizione la nuova posizione dove il pastore va spostato.
	 */
	public void setPosizione(Casella posizione) {
		if (this.posizione != null){
			this.posizione.setPastore(null);
		}
		this.posizione = posizione;
		posizione.setPastore(this);
	}

	/**
	 * @return la stringa che identifica il pastore stampandone
	 * 		   la posizione corrente.
	 */
	@Override
	public String toString(){
		String string;
		if (posizione != null)
			string = "posizione = " + posizione.toString();
		else
			string = "da posizionare";
		return string;
	}
}