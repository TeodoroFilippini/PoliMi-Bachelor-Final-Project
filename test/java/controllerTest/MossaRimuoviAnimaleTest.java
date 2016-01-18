package controllerTest;

import static org.junit.Assert.assertFalse;
import model.Animale;
import model.ElementoMappa;
import model.Mappa;
import model.Regione;
import model.StatoGioco;
import model.TipoTerreno;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Before;
import org.junit.Test;

import viewTest.ViewTest;
import controller.MossaRimuoviAnimale;

public class MossaRimuoviAnimaleTest {

	private Animale animale;
	private Regione regione;
	private MossaRimuoviAnimale mossa;
	private StatoGioco statoGioco;
	private ViewTest view;

	@Before
	public void setUp() throws Exception {
		regione = new Regione("regione",TipoTerreno.DESERTO);
		animale = new Animale(regione);
		mossa = new MossaRimuoviAnimale(animale);
		statoGioco = new StatoGioco(2);
		SimpleGraph<ElementoMappa, DefaultEdge> grafoMappa = new SimpleGraph<ElementoMappa,DefaultEdge>(DefaultEdge.class);
		grafoMappa.addVertex(regione);
		Mappa mappa = Mappa.creaMappa(grafoMappa);
		statoGioco.setMappa(mappa);
		view = new ViewTest(statoGioco);
	}

	@Test
	public void test() {
		mossa.eseguiMossa(view, statoGioco);
		assertFalse(regione.getListaAnimali().contains(animale));
	}

}
