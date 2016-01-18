package controllerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import model.Animale;
import model.Ariete;
import model.Carta;
import model.CartaInVendita;
import model.Casella;
import model.Costanti;
import model.Giocatore;
import model.Pastore;
import model.Pecora;
import model.PecoraNera;
import model.Regione;
import model.TipoTerreno;

import org.junit.Before;
import org.junit.Test;

import controller.StrumentiController;

public class StrumentiControllerTest {
	Casella casella;
	List<Casella> listaCaselle = new ArrayList<Casella>();
	Animale animale;
	List<Animale> listaAnimali = new ArrayList<Animale>();
	Pastore pastore;
	List<Pastore> listaPastori = new ArrayList<Pastore>();
	Carta carta;
	List<Carta> listaCarte = new ArrayList<Carta>();
	CartaInVendita cartaInVendita;
	List<CartaInVendita> listaCarteInVendita = new ArrayList<CartaInVendita>();
	Regione regione;
	List<Regione> listaRegioni = new ArrayList<Regione>();	

	@Before
	public void setUp() throws Exception {
		animale = new Animale(new Regione("regione prova", TipoTerreno.PALUDE));
		casella = new Casella("casella prova", 1);
		pastore = new Pastore(new Giocatore(30, 1));
		carta = new Carta(TipoTerreno.FORESTA);
		cartaInVendita = new CartaInVendita(carta, 1, new Giocatore(30, 1));
		regione = new Regione("regione prova 2", TipoTerreno.GRANO);
	}

	@Test
	public void test() {
		listaCarteInVendita.add(cartaInVendita);
		assertEquals(cartaInVendita, StrumentiController.trovaCartaInVendita(cartaInVendita, listaCarteInVendita));
		listaCarteInVendita.remove(cartaInVendita);
		assertEquals(null, StrumentiController.trovaCartaInVendita(cartaInVendita, listaCarteInVendita));
		new Pecora(regione);
		new PecoraNera(regione);
		listaRegioni.add(regione);
		listaRegioni.remove(regione);
		assertEquals(0, StrumentiController.getNumeroPecoreTerreno(regione.getTipo(), listaRegioni));
		listaAnimali.add(new Pecora(regione));
		assertEquals(true, StrumentiController.isListaConUnSoloTipoDiAnimale(listaAnimali));
		listaAnimali.add(new Ariete(new Regione("regione prova 3", TipoTerreno.FORESTA)));
		assertEquals(false, StrumentiController.isListaConUnSoloTipoDiAnimale(listaAnimali));
		listaCarte.add(new Carta(TipoTerreno.PALUDE));
		assertEquals(listaCarte, StrumentiController.getListaCarteSenzaRipetizioni(listaCarte));
		listaCarte.add(new Carta(TipoTerreno.PALUDE));
		assertNotEquals(listaCarte, StrumentiController.getListaCarteSenzaRipetizioni(listaCarte));
	}

}
