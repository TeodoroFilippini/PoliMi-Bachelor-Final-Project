package modelTest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import model.Casella;
import model.ElementoMappa;
import model.Mappa;
import model.Regione;
import model.TipoTerreno;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Before;
import org.junit.Test;

public class MappaTest {

	private SimpleGraph<ElementoMappa,DefaultEdge> grafoMappa;
	private Regione regione1 = new Regione("regione1",TipoTerreno.DESERTO);
	private Regione regione2 = new Regione("regione2",TipoTerreno.DESERTO);
	private Casella casella1 = new Casella("casella1",1);
	private Casella casella2 = new Casella("casella2",1);
	private Mappa mappa;

	@Before
	public void setUp() throws Exception {
		grafoMappa = new SimpleGraph<ElementoMappa,DefaultEdge>(DefaultEdge.class);
		grafoMappa.addVertex(regione1);
		grafoMappa.addVertex(regione2);
		grafoMappa.addVertex(casella1);
		grafoMappa.addVertex(casella2);
		grafoMappa.addEdge(regione1, casella1);
		grafoMappa.addEdge(regione1, casella2);
		grafoMappa.addEdge(regione2, casella1);
		grafoMappa.addEdge(casella1,casella2);
		mappa = Mappa.creaMappa(grafoMappa);
	}

	@Test
	public void test() {
		List<Regione> listaRegioni = new ArrayList<Regione>();
		listaRegioni.add(regione1);
		listaRegioni.add(regione2);
		assertEquals(listaRegioni,mappa.getListaRegioni());
		assertEquals(listaRegioni,mappa.regioniConfinanti(casella1));
		List<Casella> listaCaselle = new ArrayList<Casella>();
		listaCaselle.add(casella1);
		listaCaselle.add(casella2);
		assertEquals(listaCaselle,mappa.getListaCaselle());
		assertEquals(listaCaselle,mappa.caselleAdiacenti(regione1));
		assertEquals(listaCaselle,mappa.caselleLibere());
		assertEquals(listaCaselle,mappa.caselleLibereAdiacenti(regione1));
	}

}
