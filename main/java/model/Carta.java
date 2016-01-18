package model;

import java.io.Serializable;

/** La classe carta prevede di creare un oggetto diverso per ogni
 * TIPO di carta: ha quindi gli attributi costo e CarteDisponibili
 * che sono tipici del gruppo di carte di un tipo. A parte la creazione
 * iniziale, nessuna carta di crea né si distrugge. Quando invece aggiungiamo
 * una carta al giocatore, esso acquisisce un puntatore a quel TIPO di carta.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
*/

public class Carta implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int carteDisponibili;
	private int costo;
	private TipoTerreno terreno;

	/**
	 * costruttore della classe Carta: chiamato solo una
	 * volta durante l'inizializzazione del gioco. In questa classe
	 * non serve un ID per riconoscere l'oggetto serializzato: infatti,
	 * il tipo di terreno è univoco, e quindi utilizzabile come ID.
	 * @param terreno il tipo di terreno che la carta rappresenta.
	 */
	public Carta(TipoTerreno terreno) {
		this.terreno = terreno;
		costo = Costanti.COSTO_CARTA_INIZIALE;
		carteDisponibili = Costanti.NUMERO_CARTE_DISPONIBLI;
	}

	/**
	 * aumenta il costo della prossima carta acquistabile
	 * associata all'oggetto carta del tipoTerreno corrente.
	 */
	public void aumentaCosto() {
		costo ++;
	}

	/**
	 * decrementa di uno il numero di carte acquistabili disponibili
	 * associate all'oggetto carta del tipoTerreno corrente.
	 */
	public void decrementaCarteDisponibili(){
		carteDisponibili --;
	}

	/**
	 * @return il numero di carte acquistabili disponibili
	 *		   associate all'oggetto. 
	 */
	public int getCarteDisponibili() {
		return carteDisponibili;
	}

	/**
	 * 
	 * @return il costo della corrente carta acquistabile
	 * 		   associata all'oggetto carta del tipoTerreno corrente.
	 */
	public int getCosto() {
		return costo;
	}

	/**
	 * 
	 * @return il tipo di terreno associato all'oggetto carta.
	 */
	public TipoTerreno getTipo() {
		return terreno;
	}

	/**
	 * @return la stringa che descrive il terreno associato alla carta.
	 */
	@Override
	public String toString(){
		return terreno.toString();
	}

}
