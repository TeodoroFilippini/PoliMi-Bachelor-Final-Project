package controllerTest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import model.Animale;
import model.Carta;
import model.Casella;
import model.Giocatore;
import model.Pastore;
import model.Regione;
import model.TipoTerreno;

import org.junit.Before;
import org.junit.Test;

import controller.AdattatoreIstanze;

public class AdattatoreIstanzeTest {
	Casella casella;
	List<Casella> listaCaselle = new ArrayList<Casella>();
	Animale animale;
	List<Animale> listaAnimali = new ArrayList<Animale>();
	Pastore pastore;
	List<Pastore> listaPastori = new ArrayList<Pastore>();
	Carta carta;
	List<Carta> listaCarte = new ArrayList<Carta>();
	Giocatore giocatore;
	List<Giocatore> listaGiocatori = new ArrayList<Giocatore>();
	Regione regione;
	List<Regione> listaRegioni = new ArrayList<Regione>();	

	@Before
	public void setUp() throws Exception {
		animale = new Animale(new Regione("regione prova", TipoTerreno.PALUDE));
		casella = new Casella("casella prova", 1);
		pastore = new Pastore(new Giocatore(30, 1));
		carta = new Carta(TipoTerreno.FORESTA);
		giocatore = new Giocatore(30, 2);
		regione = new Regione("regione prova 2", TipoTerreno.GRANO);
	}

	@Test
	public void test() {
		listaAnimali.add(animale);
		assertEquals(animale, AdattatoreIstanze.cercaIstanzaAnimale(animale, listaAnimali));
		listaAnimali.remove(animale);
		assertEquals(null, AdattatoreIstanze.cercaIstanzaAnimale(animale, listaAnimali));
		listaCaselle.add(casella);
		assertEquals(casella, AdattatoreIstanze.cercaIstanzaCasella(casella, listaCaselle));
		listaCaselle.remove(casella);
		assertEquals(null, AdattatoreIstanze.cercaIstanzaCasella(casella, listaCaselle));
		listaPastori.add(pastore);
		assertEquals(pastore, AdattatoreIstanze.cercaIstanzaPastore(pastore, listaPastori));
		listaPastori.remove(pastore);
		assertEquals(null, AdattatoreIstanze.cercaIstanzaPastore(pastore, listaPastori));
		listaCarte.add(carta);
		assertEquals(carta, AdattatoreIstanze.cercaIstanzaCarta(carta, listaCarte));
		listaCarte.remove(carta);
		assertEquals(null, AdattatoreIstanze.cercaIstanzaCarta(carta, listaCarte));
		listaGiocatori.add(giocatore);
		assertEquals(giocatore, AdattatoreIstanze.cercaIstanzaGiocatore(giocatore, listaGiocatori));
		listaGiocatori.remove(giocatore);
		assertEquals(null, AdattatoreIstanze.cercaIstanzaGiocatore(giocatore, listaGiocatori));
		listaRegioni.add(regione);
		assertEquals(regione, AdattatoreIstanze.cercaIstanzaRegione(regione, listaRegioni));
		listaRegioni.remove(regione);
		assertEquals(null, AdattatoreIstanze.cercaIstanzaRegione(regione, listaRegioni));
	}

}
