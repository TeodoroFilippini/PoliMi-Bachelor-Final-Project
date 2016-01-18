package modelTest;

import static org.junit.Assert.assertEquals;
import model.Animale;
import model.Regione;
import model.TipoTerreno;

import org.junit.Before;
import org.junit.Test;

public class AnimaleTest {

	Animale animale;
	Regione regione;
	@Before
	public void setUp() throws Exception {
		regione = new Regione("RegioneTest",TipoTerreno.DESERTO);
		animale = new Animale(regione);
	}

	@Test
	public void testGettersSetters() {
		assertEquals (regione,animale.getPosizione());
		Regione regione2 = new Regione("RegioneTest",TipoTerreno.FORESTA);
		animale.setPosizione(regione2);
		assertEquals (regione2,animale.getPosizione());
	}

}
