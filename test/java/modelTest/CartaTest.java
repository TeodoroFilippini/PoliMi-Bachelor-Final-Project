package modelTest;

import static org.junit.Assert.assertEquals;
import model.Carta;
import model.Costanti;
import model.TipoTerreno;

import org.junit.Before;
import org.junit.Test;

public class CartaTest {

	Carta carta;
	TipoTerreno tipoTerreno = TipoTerreno.DESERTO;
	@Before
	public void setUp() throws Exception {
		carta = new Carta(tipoTerreno);
	}

	@Test
	public void testGettersSetters() {
		assertEquals (tipoTerreno,carta.getTipo());
		assertEquals(Costanti.COSTO_CARTA_INIZIALE,carta.getCosto());
		assertEquals (Costanti.NUMERO_CARTE_DISPONIBLI,carta.getCarteDisponibili());
		carta.aumentaCosto();
		assertEquals (carta.getCosto(),Costanti.COSTO_CARTA_INIZIALE+1);
		carta.decrementaCarteDisponibili();
		assertEquals(carta.getCarteDisponibili(),Costanti.NUMERO_CARTE_DISPONIBLI-1);
	}

}
