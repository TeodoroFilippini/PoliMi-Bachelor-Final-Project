package modelTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import model.Ariete;
import model.Pecora;
import model.PecoraNera;
import model.Regione;
import model.TipoTerreno;

import org.junit.Before;
import org.junit.Test;

public class RegioneTest {

	private String nomeRegione = "foresta1";
	private Regione regione;
	private TipoTerreno tipoTerreno = TipoTerreno.FORESTA;

	@Before
	public void setUp() throws Exception {
		regione = new Regione(nomeRegione ,tipoTerreno);
	}

	@Test
	public void test() {
		assertEquals (tipoTerreno,regione.getTipo());
		assertEquals (nomeRegione, regione.toString());
		assertFalse(regione.isSelezionabile());
		regione.setSelezionabile(true);
		assertTrue (regione.isSelezionabile());
	}

	@Test
	public void testAnimali(){
		assertFalse(regione.contieneArieti());
		assertFalse(regione.contienePecoraNera());
		assertFalse(regione.contienePecore());
		assertEquals (0,regione.getListaAnimali().size());
		Pecora pecora = new Pecora(regione);
		assertFalse(regione.contieneArieti());
		assertFalse(regione.contienePecoraNera());
		assertTrue(regione.contienePecore());
		assertEquals(pecora,regione.getListaAnimali().get(0));
		assertEquals(pecora,regione.getListaPecoreArieti().get(0));
		regione.rimuoviAnimale(pecora);
		Ariete ariete = new Ariete(regione);
		assertTrue(regione.contieneArieti());
		assertFalse(regione.contienePecoraNera());
		assertFalse(regione.contienePecore());
		assertEquals(ariete,regione.getListaAnimali().get(0));
		assertEquals(ariete,regione.getListaPecoreArieti().get(0));
		regione.rimuoviAnimale(ariete);
		PecoraNera pecoraNera = new PecoraNera(regione);
		assertFalse(regione.contieneArieti());
		assertTrue(regione.contienePecoraNera());
		assertFalse(regione.contienePecore());
		assertEquals(pecoraNera,regione.getListaAnimali().get(0));
	}
}
