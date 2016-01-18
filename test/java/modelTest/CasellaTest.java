package modelTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import model.Casella;
import model.Giocatore;
import model.Pastore;

import org.junit.Before;
import org.junit.Test;

public class CasellaTest {
	Casella casella;
	int danari = 30;
	Giocatore giocatore;
	private String nomeCasella = "casellaTest";
	private int numeroCasella = 3;
	Pastore pastore;

	@Before
	public void setUp() throws Exception {
		casella = new Casella(nomeCasella, numeroCasella);
	}

	@Test
	public void testGettersSetters() {
		assertEquals (numeroCasella, casella.getNumero());
		assertEquals (nomeCasella, casella.toString());
		assertNull (casella.getPastore());
		assertFalse (casella.isOccupataRecinto());
		casella.setOccupataRecinto(true);
		assertTrue(casella.isOccupataRecinto());
		casella.setOccupataRecintoFinale(true);
		assertTrue(casella.isOccupataRecintoFinale());
	}

}
