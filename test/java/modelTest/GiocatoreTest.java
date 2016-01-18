package modelTest;

import static org.junit.Assert.assertEquals;
import model.Carta;
import model.Casella;
import model.Giocatore;
import model.Pastore;
import model.TipoTerreno;

import org.junit.Before;
import org.junit.Test;

public class GiocatoreTest {

	private Carta carta;
	private Casella casella;
	private int danari = 30;
	private Giocatore giocatore;
	private int numeroGiocatore = 1;
	private Pastore pastore;


	@Before
	public void setUp() throws Exception {
		giocatore = new Giocatore(danari,numeroGiocatore);
		pastore = giocatore.creaNuovoPastore();
		casella = new Casella("casella",1);
		pastore.setPosizione(casella);
		carta = new Carta(TipoTerreno.DESERTO);
	}

	@Test
	public void testGettersSetters() {
		assertEquals (danari,giocatore.getDanari());
		assertEquals (pastore,giocatore.getListaPastori().get(0));
		giocatore.aggiungiCarta(carta);
		assertEquals(carta,giocatore.getListaCarte().get(0));
		int deltaDanari = 2;
		giocatore.decrementaDanari(deltaDanari);
		assertEquals (danari - deltaDanari,giocatore.getDanari());
		giocatore.incrementaDanari(deltaDanari);
		assertEquals (danari,giocatore.getDanari());
	}

}
