package controllerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import model.Ariete;
import model.Carta;
import model.Casella;
import model.ElementoMappa;
import model.Giocatore;
import model.Mappa;
import model.Pastore;
import model.Pecora;
import model.Regione;
import model.StatoGioco;
import model.TipoTerreno;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Before;
import org.junit.Test;

import view.InterfacciaView;
import viewTest.ViewTest;
import controller.Turno;

public class TurnoTest {

	private StatoGioco statoGioco;
	private ViewTest view;
	private int numeroGiocatori = 2;
	private int danari;
	private Giocatore giocatore;
	private Turno turno;
	private Pastore pastore;
	private Casella casella;
	private Regione regione;
	private Carta carta;
	private SimpleGraph<ElementoMappa, DefaultEdge> grafoMappa;
	private Regione regione2;
	
	@Before
	public void setUp() throws Exception {
		statoGioco = new StatoGioco(numeroGiocatori);
		view = new ViewTest(statoGioco);
		giocatore = new Giocatore(danari, 1);
		pastore = giocatore.creaNuovoPastore();
		statoGioco.getListaPastori().add(pastore);
		statoGioco.getListaGiocatori().add(giocatore);
		regione = new Regione("regione",TipoTerreno.DESERTO);
		regione2 = new Regione("regione2",TipoTerreno.DESERTO);
		casella = new Casella("casella",1);
		pastore.setPosizione(casella);
		grafoMappa = new SimpleGraph<ElementoMappa,DefaultEdge>(DefaultEdge.class);
		grafoMappa.addVertex(regione);
		grafoMappa.addVertex(regione2);
		grafoMappa.addVertex(casella);
		grafoMappa.addEdge(regione, casella);
		grafoMappa.addEdge(regione2, casella);
		Mappa mappa = Mappa.creaMappa(grafoMappa);
		statoGioco.setMappa(mappa);
		Constructor<Turno> costruttore = Turno.class.getDeclaredConstructor(StatoGioco.class,InterfacciaView.class,Giocatore.class);
		turno = costruttore.newInstance(statoGioco,view,giocatore);
	}

	@Test
	public void testAccoppia() {
		new Pecora(regione);
		new Ariete(regione);
		boolean accoppiamentoRiuscito = false;

		try {
			Method accoppia = turno.getClass().getDeclaredMethod("accoppia", new Class[0]);
			accoppia.setAccessible(true);
			accoppiamentoRiuscito = (boolean) accoppia.invoke(turno);
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}

		if (accoppiamentoRiuscito){
			assertEquals(1,regione.getListaAgnelli().size());
		}
		else{
			assertEquals(0,regione.getListaAgnelli().size());
		}
	}

	@Test
	public void testCompraCarta(){
		carta = new Carta(TipoTerreno.DESERTO);
		statoGioco.getListaCarte().add(carta);

		try {
			Method compraCarta = turno.getClass().getDeclaredMethod("compraCarta", new Class[0]);
			compraCarta.setAccessible(true);
			compraCarta.invoke(turno);
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		assertTrue(giocatore.getListaCarte().contains(carta));
	}

	@Test
	public void testSpara(){
		int numeroAnimaliRegione = regione.getListaPecoreArieti().size();
		int numeroAnimaliRegione2 = regione2.getListaPecoreArieti().size();
		if (numeroAnimaliRegione == 0){
			new Pecora(regione);
			numeroAnimaliRegione++;
		}
		if (numeroAnimaliRegione2 == 0){
			new Pecora(regione2);
			numeroAnimaliRegione2++;
		}
		
		boolean sparatoriaRiuscita = false;
		try {
			Method spara = turno.getClass().getDeclaredMethod("spara", new Class[0]);
			spara.setAccessible(true);
			sparatoriaRiuscita = (boolean) spara.invoke(turno);
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		if (sparatoriaRiuscita){
			assertTrue(numeroAnimaliRegione - 1 == regione.getListaPecoreArieti().size()|
					numeroAnimaliRegione2 - 1 == regione2.getListaPecoreArieti().size());
		}
		else{
			assertEquals(numeroAnimaliRegione,regione.getListaPecoreArieti().size());
			assertEquals(numeroAnimaliRegione2,regione2.getListaPecoreArieti().size());
		}
	}
	@Test
	public void spostaPecoraTest (){
		int numeroPecoreArietiRegione = regione.getListaPecoreArieti().size();
		int numeroPecoreArietiRegione2 = regione2.getListaPecoreArieti().size();
		if (numeroPecoreArietiRegione == 0){
			new Pecora(regione);
			numeroPecoreArietiRegione++;
		}
		if (numeroPecoreArietiRegione2 == 0){
			new Pecora(regione2);
			numeroPecoreArietiRegione2++;
		}
		
		try {
			Method spostaPecora = turno.getClass().getDeclaredMethod("spostaPecora", new Class[0]);
			spostaPecora.setAccessible(true);
			spostaPecora.invoke(turno);
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(numeroPecoreArietiRegione - 1 == regione.getListaPecoreArieti().size() |
				numeroPecoreArietiRegione2 - 1 == regione2.getListaPecoreArieti().size());
		assertTrue(numeroPecoreArietiRegione + 1 == regione.getListaPecoreArieti().size() |
				numeroPecoreArietiRegione2 + 1 == regione2.getListaPecoreArieti().size());
	}
}
