package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Animale;
import model.Carta;
import model.CartaInVendita;
import model.Costanti;
import model.Regione;
import model.TipoTerreno;

/** Classe d'appoggio per eseguire ricerche in altre classi
 * del controller. i suoi metodi sono, infatti, tutti statici
 * e pubblici. 
 */

public class StrumentiController {

	/**
	 * Costruttore privato che impedisca la creazione
	 * di un oggetto del tipo di questa classe.
	 */
	private StrumentiController(){
	}

	/**
	 * Cerca la carta corrispondente alla cartaInVendita fornita.
	 * @param cartaInVendita la cartaInVendita fornita.
	 * @param listaCarte la lista di tutte le carte, in cui cercare
	 * 		  la carta da ritornare.
	 * @return la carta corrispondente alla cartaInVendita fornita.
	 */
	public static CartaInVendita trovaCartaInVendita(CartaInVendita cartaInVendita, List<CartaInVendita> listaCarte) {
		Iterator<CartaInVendita> scorriCarteInVendita = listaCarte.iterator();
		while (scorriCarteInVendita.hasNext()){
			CartaInVendita carta = scorriCarteInVendita.next();
			if (carta.equals(cartaInVendita)) {
				return carta;
			}
		}
		return null;
	}

	/**
	 * Metodo che calcola il numero di pecore presenti nelle regioni di un tipo.
	 * @param tipo il tipo di terreno.
	 * @param listaRegioni la lista di tutte le regioni del gioco.
	 * @return la somma del numero di pecore presente nelle regioni del tipo passato.
	 */
	public static int getNumeroPecoreTerreno(TipoTerreno tipo, List<Regione> listaRegioni) {
		int numeroPecoreTerreno = 0;
		Iterator<Regione> scorriRegioni = listaRegioni.iterator();
		while (scorriRegioni.hasNext()){
			Regione regione = scorriRegioni.next();
			if (regione.getTipo() == tipo){
				numeroPecoreTerreno += regione.getListaPecoreArieti().size();
				if (regione.contienePecoraNera()) {
					numeroPecoreTerreno += Costanti.VALORE_PECORA_NERA;
				}
			}
		}
		return numeroPecoreTerreno;
	}

	/**
	 * Metodo che verifica se in una lista c'è un solo tipo di
	 * animale oppure no.
	 * @param listaAnimali una lista di animali.
	 * @return true se la lista contiene animali di un solo tipo, 
	 * 		   false se no.
	 */
	public static boolean isListaConUnSoloTipoDiAnimale(List<Animale> listaAnimali) {
		Iterator<Animale> scorriAnimali = listaAnimali.iterator();
		Animale animaleIniziale = listaAnimali.get(0);
		while(scorriAnimali.hasNext()){
			Animale animale = scorriAnimali.next();
			if (!animaleIniziale.getClass().isAssignableFrom(animale.getClass())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Fornisce una lista di carte senza ripetizioni di tipoTerreno.
	 * @param listaCarte una lista di carte, tipicamente quelle di un giocatore.
	 * @return la stessa lista in input senza ripetizioni di tipo.
	 */
	public static List<Carta> getListaCarteSenzaRipetizioni(List<Carta> listaCarte) {
		List<Carta> listaCarteSenzaRipetizioni = new ArrayList<Carta>();
		Iterator<Carta> scorriCarte = listaCarte.iterator();
		while (scorriCarte.hasNext()){
			boolean cartaPresente = false;
			Carta carta = scorriCarte.next();
			Iterator<Carta> scorriCarteSenzaRipetizioni = listaCarteSenzaRipetizioni.iterator();
			while(scorriCarteSenzaRipetizioni.hasNext()){
				Carta cartaUnica = scorriCarteSenzaRipetizioni.next();
				if (cartaUnica.getTipo() == carta.getTipo()){
					cartaPresente = true;
				}
			}
			if (!cartaPresente){
				listaCarteSenzaRipetizioni.add(carta);
			}
		}
		return listaCarteSenzaRipetizioni;
	}
}
