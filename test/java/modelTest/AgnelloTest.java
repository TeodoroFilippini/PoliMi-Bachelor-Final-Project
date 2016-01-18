package modelTest;

import static org.junit.Assert.assertEquals;
import model.Agnello;
import model.Regione;
import model.TipoTerreno;

import org.junit.Before;
import org.junit.Test;

public class AgnelloTest {

	Agnello agnello;
	Regione regione;

	@Before
	public void setUp() throws Exception{
		regione = new Regione("RegioneTest", TipoTerreno.MONTAGNA);
		agnello = new Agnello(regione);
	}

	@Test
	public void test() {
		assertEquals(agnello.getPosizione(), regione);
		assertEquals(agnello.getContatoreTurni(), 0);
		agnello.incrementaContatore();
		assertEquals(agnello.getContatoreTurni(), 1);
		assertEquals(agnello.toString(), "Agnello nella regione " + agnello.getPosizione().getNomeRegione());
	}

}
