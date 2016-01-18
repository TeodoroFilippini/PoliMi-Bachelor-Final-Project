package controllerTest;

import static org.junit.Assert.assertEquals;
import model.Agnello;
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
import controller.MossaInvecchiaAgnelli;

public class MossaInvecchiaAgnelliTest {

	private MossaInvecchiaAgnelli mossa;
	private StatoGioco statoGioco;
	private Agnello agnello;
	private Regione regione;
	private ViewTest view;

	@Before
	public void setUp() throws Exception {
		regione = new Regione("regione",TipoTerreno.DESERTO);
		agnello = new Agnello(regione);
		statoGioco = new StatoGioco(2);
		SimpleGraph<ElementoMappa, DefaultEdge> grafoMappa = new SimpleGraph<ElementoMappa,DefaultEdge>(DefaultEdge.class);
		grafoMappa.addVertex(regione);
		Mappa mappa = Mappa.creaMappa(grafoMappa);
		statoGioco.setMappa(mappa);
		mossa = new MossaInvecchiaAgnelli();
		view = new ViewTest(statoGioco);
	}

	@Test
	public void test() {
		mossa.eseguiMossa(view, statoGioco);
		assertEquals(agnello.getContatoreTurni(),1);
		assertEquals(regione.getListaAgnelli().size(),1);	
		mossa.eseguiMossa(view, statoGioco);
		assertEquals(regione.getListaAgnelli().size(),0);
		assertEquals(regione.getListaAnimali().size(),1);
	}

}
