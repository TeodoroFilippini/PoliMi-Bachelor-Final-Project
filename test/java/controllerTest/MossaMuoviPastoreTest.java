package controllerTest;

import static org.junit.Assert.assertEquals;
import model.Casella;
import model.Costanti;
import model.ElementoMappa;
import model.Giocatore;
import model.Mappa;
import model.Pastore;
import model.StatoGioco;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Before;
import org.junit.Test;

import viewTest.ViewTest;
import controller.MossaMuoviPastore;

public class MossaMuoviPastoreTest {

	Giocatore giocatore;
	Pastore pastore;
	Casella arrivo;
	MossaMuoviPastore mossa;
	private int danari = 20;
	private Casella partenza;
	private Casella casellaIntermedia;
	private StatoGioco statoGioco;
	private MossaMuoviPastore mossa2;
	private ViewTest view;

	@Before
	public void setUp() throws Exception {
		giocatore = new Giocatore(danari ,1);
		pastore = giocatore.creaNuovoPastore();
		partenza = new Casella("casella",1);
		casellaIntermedia = new Casella("casellaIntermedia",2);
		arrivo = new Casella("arrivo",3);
		pastore.setPosizione(partenza);
		SimpleGraph<ElementoMappa, DefaultEdge> grafoMappa = new SimpleGraph<ElementoMappa,DefaultEdge>(DefaultEdge.class);
		grafoMappa.addVertex(arrivo);
		grafoMappa.addVertex(partenza);
		grafoMappa.addVertex(casellaIntermedia);
		grafoMappa.addEdge(partenza, casellaIntermedia);
		grafoMappa.addEdge(casellaIntermedia, arrivo);
		Mappa mappa = Mappa.creaMappa(grafoMappa);
		statoGioco = new StatoGioco(2);
		statoGioco.setMappa(mappa);
		statoGioco.getListaGiocatori().add(giocatore);
		statoGioco.getListaPastori().add(pastore);
		mossa = new MossaMuoviPastore(pastore,arrivo);
		mossa2 = new MossaMuoviPastore(pastore, casellaIntermedia);
		view = new ViewTest(statoGioco);
	}

	@Test
	public void test() {	
		mossa.eseguiMossa(view, statoGioco);
		assertEquals(pastore.getPosizione(),arrivo);
		assertEquals(giocatore.getDanari(),danari - Costanti.COSTO_SPOSTAMENTO);
		int danariIntermedi = giocatore.getDanari();
		try{
			mossa2.eseguiMossa(null, statoGioco);
		}catch (NullPointerException e){

		}
		assertEquals(giocatore.getDanari(),danariIntermedi);
	}

}
