package model;

import java.io.Serializable;

/**
 * A differenza dell'oggetto Carta, l'oggetto CartaInVendita
 * rappresenta una singola carta, non un gruppo di carte dello 
 * stesso tipo. Questa scelta nasce dalla necessità di associare
 * costi diversi a carte dello stesso tipo, e in generale (per 
 * la struttura del turno di compravendita delle carte) a una maggiore
 * comodità di gestione per la singola cartaInVendita.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class CartaInVendita implements Serializable{

	private static final long serialVersionUID = 1L;
	private int costo;
	private Giocatore proprietario;
	private Carta carta;

	/**
	 * Costruttore dell'oggetto 
	 * @param carta la carta a cui è associata la cartaInVendita da creare.
	 * @param costo il costo associato alla cartaInVendita da creare, scelto
	 * 		  dal giocatore che la sta mettendo in vendita.
	 * @param proprietario il giocatore che sta vendendo questa carta.
	 */
	public CartaInVendita(Carta carta, int costo, Giocatore proprietario){
		this.carta = carta;
		this.costo = costo;
		this.proprietario = proprietario;
	}

	/**
	 * 
	 * @return la carta corrispondente alla cartaInVendita corrente.
	 */
	public Carta getCarta() {
		return carta;
	}

	/**
	 * 
	 * @return il costo corrispondente alla cartaInVendita corrente.
	 */
	public int getCosto() {
		return costo;
	}

	/**
	 * 
	 * @return il giocatore (proprietario) corrispondente alla cartaInVendita corrente.
	 */
	public Giocatore getProprietario() {
		return proprietario;
	}

}
