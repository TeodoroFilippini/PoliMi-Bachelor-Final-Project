package controllerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import model.Carta;
import model.Costanti;
import model.Giocatore;
import model.StatoGioco;
import model.TipoTerreno;

import org.junit.Before;
import org.junit.Test;

import viewTest.ViewTest;
import controller.MossaCompraCarta;

public class MossaCompraCartaTest {

	private Giocatore giocatore;
	private Carta carta;
	private MossaCompraCarta mossa;
	private StatoGioco statoGioco;
	private int danari = 20;
	private ViewTest view;

	@Before
	public void setUp() throws Exception {
		giocatore = new Giocatore(danari,1);
		carta = new Carta(TipoTerreno.DESERTO);
		mossa = new MossaCompraCarta(carta,giocatore);
		statoGioco = new StatoGioco(1);
		statoGioco.getListaCarte().add(carta);
		statoGioco.getListaGiocatori().add(giocatore);
		statoGioco.setGiocatoreCorrente(giocatore);
		view = new ViewTest(statoGioco);
	}

	@Test
	public void test() {
		int costoCarta = carta.getCosto();
		mossa.eseguiMossa(view, statoGioco);
		assertTrue(giocatore.getListaCarte().contains(carta));
		assertEquals(giocatore.getDanari(),danari-costoCarta);
		assertEquals(carta.getCosto(),costoCarta+1);
		assertEquals(carta.getCarteDisponibili(),Costanti.NUMERO_CARTE_DISPONIBLI-1);
	}

}
