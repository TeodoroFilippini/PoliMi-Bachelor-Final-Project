package controller;

import java.util.Iterator;
import java.util.List;

import model.Animale;
import model.Carta;
import model.Casella;
import model.Giocatore;
import model.Pastore;
import model.Regione;

/**
 * Classe statica ausiliaria utilizzata, nel gioco online, per stabilire 
 * la corrispondenza tra oggetti serializzati.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */

public class AdattatoreIstanze {

	/**
	 * Costruttore privato, che non va mai chiamato.
	 */
	private AdattatoreIstanze(){	
	}
	
	/**
	 * Carca l'istanza locale corrispondente ad un animale dato.
	 * @param animale l'animale fornito
	 * @param listaAnimali la lista (con riferimenti diversi) in cui cercare l'animale.
	 * @return l'istanza locale ricercata.
	 */
	public static Animale cercaIstanzaAnimale(Animale animale, List<Animale> listaAnimali) {
		Iterator<Animale> scorriAnimali = listaAnimali.iterator();
		while (scorriAnimali.hasNext()){
			Animale istanzaAnimale = scorriAnimali.next();
			if (istanzaAnimale.getID() == animale.getID()) {
				return istanzaAnimale;
			}
		}
		return null;
	}

	/**
	 * Cerca l'istanza locale corrispondente ad una carta data.
	 * @param carta la carta fornita
	 * @param listaCarte la lista (con riferimenti diversi) in cui cercare la carta.
	 * @return l'istanza locale ricercata.
	 */
	public static Carta cercaIstanzaCarta(Carta carta, List<Carta> listaCarte) {
		Iterator<Carta> scorriCarte = listaCarte.iterator();
		while (scorriCarte.hasNext()){
			Carta istanzaCarta = scorriCarte.next();
			if (istanzaCarta.getTipo() == carta.getTipo()) {
				return istanzaCarta;
			}
		}
		return null;
	}

	/**
	 * Cerca l'istanza locale corrispondente ad una casella data.
	 * @param casella la casella da cercare.
	 * @param listaCaselle la lista (con riferimenti diversi) in cui cercare la casella.
	 * @return l'istanza locale ricercata.
	 */
	public static Casella cercaIstanzaCasella(Casella casella,List<Casella> listaCaselle) {
		Iterator<Casella> scorriCaselle = listaCaselle.iterator();
		while (scorriCaselle.hasNext()){
			Casella istanzaCasella = scorriCaselle.next();
			if (istanzaCasella.getID() == casella.getID()) {
				return istanzaCasella;
			}
		}
		return null;
	}

	/**
	 * Cerca l'istanza locale corrispondente ad un giocatore dato.
	 * @param giocatore il giocatore da cercare.
	 * @param listaGiocatori la lista (con riferimenti diversi) in cui cercare il giocatore.
	 * @return l'istanza locale ricercata.
	 */
	public static Giocatore cercaIstanzaGiocatore(Giocatore giocatore,List<Giocatore> listaGiocatori) {
		Iterator<Giocatore> scorriGiocatori = listaGiocatori.iterator();
		while (scorriGiocatori.hasNext()){
			Giocatore istanzaGiocatore = scorriGiocatori.next();
			if (istanzaGiocatore.getNumero() == giocatore.getNumero()) {
				return istanzaGiocatore;
			}
		}
		return null;
	}

	/**
	 * Cerca l'istanza locale corrispondente ad un pastore dato.
	 * @param pastore il pastore da cercare.
	 * @param listaPastori la lista (con riferimenti diversi) in cui cercare il pastore.
	 * @return l'istanza locale ricercata.
	 */
	public static Pastore cercaIstanzaPastore(Pastore pastore,List<Pastore> listaPastori) {
		Iterator<Pastore> scorriPastori = listaPastori.iterator();
		while (scorriPastori.hasNext()){
			Pastore istanzaPastore = scorriPastori.next();
			if (istanzaPastore.getID() == pastore.getID()) {
				return istanzaPastore;
			}
		}
		return null;
	}

	/**
	 * Cerca l'istanza locale corrispondente ad una regione data.
	 * @param regione la regione da cercare.
	 * @param listaRegioni la lista (con riferimenti diversi) in cui cercare la regione.
	 * @return l'istanza locale ricercata.
	 */
	public static Regione cercaIstanzaRegione(Regione regione, List<Regione> listaRegioni) {
		Iterator<Regione> scorriRegioni = listaRegioni.iterator();
		while (scorriRegioni.hasNext()){
			Regione istanzaRegione = scorriRegioni.next();
			if (istanzaRegione.getID() == regione.getID()) {
				return istanzaRegione;
			}
		}
		return null;
	}
}
