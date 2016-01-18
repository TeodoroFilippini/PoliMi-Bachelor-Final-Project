package modelTest;

import static org.junit.Assert.assertEquals;
import model.Carta;
import model.CartaInVendita;
import model.Giocatore;
import model.TipoTerreno;

import org.junit.Before;
import org.junit.Test;

public class CartaInVenditaTest {

	private CartaInVendita cartaInVendita;
	private Carta carta;
	private int costo;
	private Giocatore proprietario;

	@Before
	public void setUp() throws Exception {
		carta = new Carta(TipoTerreno.DESERTO);
		proprietario = new Giocatore(30,1);
		cartaInVendita = new CartaInVendita(carta,costo,proprietario);
	}

	@Test
	public void testGetters() {
		assertEquals(carta,cartaInVendita.getCarta());
		assertEquals(costo,cartaInVendita.getCosto());
		assertEquals(proprietario,cartaInVendita.getProprietario());
	}

}
