package controllerTest;

import static org.junit.Assert.assertTrue;
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
import controller.MossaAggiungiAnimale;

public class MossaAggiungiAnimaleTest {

	private Animale animale;
	private Regione regione;
	private MossaAggiungiAnimale mossa;
	private StatoGioco statoGioco;
	private ViewTest view;

	@Before
	public void setUp() throws Exception {
		regione = new Regione("regione",TipoTerreno.DESERTO);
		animale = new Animale(regione);
		mossa = new MossaAggiungiAnimale(animale, regione);
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
		assertTrue(regione.getListaAnimali().contains(animale));
	}

}
