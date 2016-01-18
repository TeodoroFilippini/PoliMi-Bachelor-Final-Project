package controllerTest;

import static org.junit.Assert.assertEquals;
import model.Costanti;
import model.Giocatore;
import model.StatoGioco;

import org.junit.Before;
import org.junit.Test;

import viewTest.ViewTest;
import controller.MossaCorruzione;

public class MossaCorruzioneTest {

	private Giocatore corrotto;
	private Giocatore corruttore;
	private int danari = 20;
	private MossaCorruzione mossa;
	private StatoGioco statoGioco;
	private ViewTest view;

	@Before
	public void setUp() throws Exception {
		corrotto = new Giocatore(danari ,1);
		corruttore = new Giocatore(danari,2);
		mossa = new MossaCorruzione(corrotto,corruttore);
		statoGioco = new StatoGioco(2);
		statoGioco.getListaGiocatori().add(corruttore);
		statoGioco.getListaGiocatori().add(corrotto);
		view = new ViewTest(statoGioco);
	}

	@Test
	public void test() {
		mossa.eseguiMossa(view, statoGioco);
		assertEquals(corrotto.getDanari(), danari+Costanti.COSTO_CORRUZIONE);
		assertEquals(corruttore.getDanari(), danari-Costanti.COSTO_CORRUZIONE);
	}

}
