package controllerTest;

import static org.junit.Assert.assertEquals;
import model.Ariete;
import model.ElementoMappa;
import model.Mappa;
import model.Pecora;
import model.Regione;
import model.StatoGioco;
import model.TipoTerreno;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Before;
import org.junit.Test;

import viewTest.ViewTest;
import controller.MossaAccoppiamento;

public class MossaAccoppiamentoTest {

	MossaAccoppiamento mossa; 
	StatoGioco statoGioco;
	Regione regione;
	private ViewTest view;
	@Before
	public void setUp() throws Exception {
		regione = new Regione("regione",TipoTerreno.DESERTO);
		regione.aggiungiAnimale(new Pecora(regione));
		regione.aggiungiAnimale(new Ariete(regione));
		mossa = new MossaAccoppiamento(regione, true);
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
		assertEquals(regione.getListaAgnelli().size(),1);
	}

}
