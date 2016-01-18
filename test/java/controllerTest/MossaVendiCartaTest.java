package controllerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import model.Carta;
import model.CartaInVendita;
import model.Giocatore;
import model.StatoGioco;
import model.TipoTerreno;

import org.junit.Before;
import org.junit.Test;

import viewTest.ViewTest;
import controller.MossaVendiCarta;

public class MossaVendiCartaTest {

	private Carta carta;
	private int costo = 2;
	private Giocatore proprietario;
	CartaInVendita cartaInVendita;
	private int danari = 20;
	private Giocatore acquirente;
	private StatoGioco statoGioco;
	private MossaVendiCarta mossa;
	private ViewTest view;

	@Before
	public void setUp() throws Exception {
		carta = new Carta(TipoTerreno.DESERTO);
		proprietario = new Giocatore(danari ,1);
		proprietario.aggiungiCarta(carta);
		acquirente = new Giocatore(danari,2);
		cartaInVendita = new CartaInVendita(carta,costo,proprietario);
		statoGioco = new StatoGioco(2);
		statoGioco.getListaGiocatori().add(acquirente);
		statoGioco.getListaGiocatori().add(proprietario);
		statoGioco.getListaCarte().add(carta);
		mossa = new MossaVendiCarta(cartaInVendita,acquirente);
		view = new ViewTest(statoGioco);
	}

	@Test
	public void test() {
		mossa.eseguiMossa(view, statoGioco);
		assertEquals(proprietario.getDanari(),danari + cartaInVendita.getCosto());
		assertEquals(acquirente.getDanari(),danari - cartaInVendita.getCosto());
		assertFalse(proprietario.getListaCarte().contains(carta));
		assertTrue(acquirente.getListaCarte().contains(carta));
	}

}
