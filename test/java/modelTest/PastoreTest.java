package modelTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import model.Casella;
import model.Giocatore;
import model.Pastore;

import org.junit.Before;
import org.junit.Test;

public class PastoreTest {

	private Casella casella;
	private int danari = 30;
	private Giocatore giocatore;
	private int numeroGiocatore = 1;
	private Pastore pastore;

	@Before
	public void setUp() throws Exception {
		giocatore = new Giocatore(danari , numeroGiocatore );
		pastore = giocatore.creaNuovoPastore();
		casella = new Casella("CasellaTest",2);
	}

	@Test
	public void testGettersSetters() {
		assertNull (pastore.getPosizione());
		pastore.setPosizione(casella);
		assertEquals (casella, pastore.getPosizione());
		assertEquals (giocatore,pastore.getGiocatore());
	}

}
