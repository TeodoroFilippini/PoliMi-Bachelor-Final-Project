package model;

import java.io.Serializable;

/** La classe Animale è la classe padre di tutti gli animali
 * del gioco. Oltre alla posizione, ha un ID univoco che identifica
 * ogni animale nell'ordine della sua creazione, e torna comodo per
 * fare confronti tra oggetti animale serializzati. Il metodo equals()
 * infatti non è adeguato per la presenza di attributi non primitivi.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */

public class Animale implements Serializable{
	private static int contatoreiD = 0;

	private static final long serialVersionUID = 1L;
	private int iD;
	protected Regione posizione;

	/**
	 * costruttore della classe Animale. Ogniqualvolta viene
	 * creato un animale, il suo contatore (statico) è incrementato
	 * di 1. Così abbiamo un ID univoco per ogni animale.
	 * @param posizione la posizione in cui sarà creato l'animale.
	 */
	public Animale(Regione posizione) {
		this.iD = contatoreiD;
		contatoreiD++;
		if (posizione != null){
			this.posizione = posizione;
			posizione.aggiungiAnimale(this);
		}
	}

	/**
	 * 
	 * @return L'ID dell'animale.
	 */
	public int getID() {
		return iD;
	}

	/**
	 * 
	 * @return la posizione corrente dell'animale.
	 */
	public Regione getPosizione (){
		return posizione;
	}

	/** Il setter, oltre a impostare la nuova posizione,
	 * rimuove l'animale da quella vecchia. 
	 * @param posizione la regione dove va spostato l'animale.
	 */
	public void setPosizione (Regione posizione){
		if (this.posizione != null){
			this.posizione.rimuoviAnimale(this);
		}
		this.posizione = posizione;
		posizione.aggiungiAnimale(this);
	}

}