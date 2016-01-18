package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * La classe Giocatore rappresenta l'User vero e proprio. 
 * I pastori che controlla sono un suo attributo, oltre ai
 * denari e alla lista delle sue carte. Ogni giocatore ha
 * associato un numero che lo identifica, da 1 a 4. Se in futuro
 * il numero di giocatori salisse, il codice per gestirli rimarrebbe
 * perfettamente uguale, fatte delle minime modifiche.
 * La variabile azionirimanentiperturno rappresenta infine il numero
 * di mosse che il giocatore ha ancora a disposizione nel turno corrente.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */

public class Giocatore implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int azioniRimanentiPerTurno;
	private int danari;
	private List<Carta> listaCarte = new ArrayList<Carta>();
	private List<Pastore> listaPastori = new ArrayList<Pastore>();
	private int numeroGiocatore;
	private Pastore pastoreInGioco;

	/**
	 * Costruttore della classe Giocatore. Se stiamo costruendo
	 * il giocatore 1, settiamo le sue azioni al numero di azioni
	 * per nuovo turno; infatti, il giocatore 1 è il primo a cominciare
	 * a giocare.
	 * @param danari il numero di denari iniziali del giocatore.
	 * @param numeroGiocatore il numero associato al giocatore, da 1 a 4.
	 */
	public Giocatore(int danari, int numeroGiocatore) {
		this.danari = danari;
		this.numeroGiocatore = numeroGiocatore;
		if (numeroGiocatore == 1) {
			azioniRimanentiPerTurno = Costanti.AZIONI_PER_TURNO;
		}
	}

	/**
	 * aggiunge una carta alla lista delle carte del giocatore.
	 * @param carta la carta da aggiungere.
	 */
	public void aggiungiCarta(Carta carta){
		listaCarte.add(carta);
	}

	/**
	 * crea un nuovo pastore e lo assiunge alla lista dei pastori
	 * del giocatore.
	 * @return il pastore da aggiungere alla lista di quelli del giocatore.
	 */
	public Pastore creaNuovoPastore(){
		Pastore pastore = new Pastore(this);
		listaPastori.add(pastore);
		return pastore;
	}

	/**
	 * decrementa di un'unità le azioni disponibili per il turno corrente.
	 */
	public void decrementaAzioniDisponibili() {
		azioniRimanentiPerTurno--;
	}

	/**
	 * decrementa i danari del giocatore.
	 * @param decremento il numero di danari da togliere al giocatore.
	 */
	public void decrementaDanari(int decremento){
		danari -= decremento;
	}

	/**
	 *
	 * @return il numero di azioni rimanenti per il turno corrente
	 * 		   del giocatore.
	 */
	public int getAzioniRimanentiPerTurno() {
		return azioniRimanentiPerTurno;
	}

	/**
	 * 
	 * @return il numero di denari del giocatore.
	 */
	public int getDanari() {
		return danari;
	}

	/**
	 * 
	 * @return la lista delle carte del giocatore.
	 */
	public List<Carta> getListaCarte() {
		return listaCarte;
	}

	/**
	 * 
	 * @return la lista dei pastori associata al giocatore.
	 */
	public List<Pastore> getListaPastori() {
		return listaPastori;
	}

	/**
	 * 
	 * @return il numero associato al giocatore, da 1 a 4.
	 */
	public int getNumero() {
		return numeroGiocatore;
	}

	/**
	 * 
	 * @param terreno il tipo di terreno del quale voglio sapere
	 * 		  il numero di carte posseduto dal giocatore.
	 * @return il numero di carte, tra quelle possedute dal giocatore,
	 * 		  che sono del tipo passato.
	 */
	public int getNumeroCarte(TipoTerreno terreno){
		int numeroCarte = 0;
		Iterator<Carta> scorriCarte = listaCarte.iterator();
		while (scorriCarte.hasNext()){
			Carta carta = scorriCarte.next();
			if (carta.getTipo() == terreno) {
				numeroCarte ++;
			}
		}
		return numeroCarte;
	}

	/**
	 * Fornisce il pastore attualmente in gioco.
	 * Il metodo è utile nel gioco a due giocatori, dove
	 * a ogni giocatore è associato più di un pastore (2).
	 * @return il pastore attualmente selezionato.
	 */
	public Pastore getPastoreInGioco (){
		return pastoreInGioco; 
	}

	/**
	 * incrementa i denari del giocatore.
	 * @param incremento quantità di denari dell'incremento.
	 */
	public void incrementaDanari(int incremento) {
		danari += incremento;
	}

	/**
	 * imposta le azioni rimanenti per il turno corrente.
	 * @param azioniRimanentiPerTurno il numero di azioni per
	 * 		  turno.
	 */
	public void setAzioniRimanentiPerTurno(int azioniRimanentiPerTurno) {
		this.azioniRimanentiPerTurno = azioniRimanentiPerTurno;
	}

	/**
	 * imposta il pastore attualmente selezionabile per il giocatore.
	 * @param pastoreInGioco il pastore che può eseguire le mosse del turno
	 * 		  corrente.
	 */
	public void setPastoreInGioco(Pastore pastoreInGioco) {
		this.pastoreInGioco = pastoreInGioco;
	}

	/**
	 * rimuove la carta dalla lista delle carte del giocatore.
	 * @param carta la carta da rimuovere.
	 */
	public void rimuoviCarta(Carta carta) {
		listaCarte.remove(carta);
	}
	
	/**
	 * 
	 * @return la stringa che descrive il numero di denari e di pastori
	 * 		   associati al giocatore.
	 */
	@Override
	public String toString(){
		return "danari = " + Integer.toString(danari) + listaPastori.toString();
	}

}
