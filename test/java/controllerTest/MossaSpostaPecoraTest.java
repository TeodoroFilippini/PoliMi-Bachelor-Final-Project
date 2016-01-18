package controllerTest;

import static org.junit.Assert.assertEquals;
import model.Animale;
import model.Casella;
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
import controller.MossaSpostaPecora;

public class MossaSpostaPecoraTest {

	private Regione arrivo;
	private MossaSpostaPecora mossa;
	private Regione partenza;
	private Casella casella;
	private StatoGioco statoGioco;
	private Animale animale;
	private ViewTest view;

	@Before
	public void setUp() throws Exception {
		arrivo = new Regione("regione",TipoTerreno.DESERTO);
		partenza = new Regione("regione",TipoTerreno.FORESTA);
		casella = new Casella("casella",1);
		animale = new Animale(partenza);
		mossa = new MossaSpostaPecora(animale,arrivo);
		SimpleGraph<ElementoMappa, DefaultEdge> grafoMappa = new SimpleGraph<ElementoMappa,DefaultEdge>(DefaultEdge.class);
		grafoMappa.addVertex(arrivo);
		grafoMappa.addVertex(partenza);
		grafoMappa.addVertex(casella);
		grafoMappa.addEdge(partenza, casella);
		grafoMappa.addEdge(casella, arrivo);
		Mappa mappa = Mappa.creaMappa(grafoMappa);
		statoGioco = new StatoGioco(1);
		statoGioco.setMappa(mappa);
		view = new ViewTest(statoGioco);
	}

	@Test
	public void test() {
		mossa.eseguiMossa(view, statoGioco);
		assertEquals(animale.getPosizione(),arrivo);
	}

}
