package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 * La classe Singleton che implementa la mappa del gioco. Essa sarà descritta 
 * dalla struttura dati del grafo con archi non orientati, i cui 
 * vertici sono regioni e caselle: le caselle sono collegate ad altre 
 * caselle (tramite strade) e alle regioni che suddividono (ogni casella 
 * separa due regioni), mentre le regioni sono collegate solo a caselle,
 * mai ad altre regioni direttamente.
 *  
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class Mappa implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private SimpleGraph<Casella,DefaultEdge> grafoCaselle = new SimpleGraph<Casella,DefaultEdge>(DefaultEdge.class);
	private SimpleGraph<ElementoMappa,DefaultEdge>grafoMappa = new SimpleGraph<ElementoMappa,DefaultEdge>(DefaultEdge.class);
	private List<DefaultEdge> listaArchiCaselle = new ArrayList<DefaultEdge>();
	private List<Casella> listaCaselle = new ArrayList<Casella>();
	private List<Regione> listaRegioni = new ArrayList<Regione>();
	private static Mappa istanza = null;

	/**
	 * Costruttore della classe. Prende come unico argomento
	 * il grafo costruito tramite l'XMLParser e lo trasforma in
	 * una mappa a tutti gli effetti.
	 * @param grafoMappa il grafo ottenuto con parsing dal file Mappa.xml
	 */
	private Mappa(SimpleGraph<ElementoMappa, DefaultEdge> grafoMappa) {
		this.grafoMappa = grafoMappa;
		creaListaCaselle();
		creaListaRegioni();
		creaListaArchiCaselle();
		creaGrafoCaselle();
	}

	/**
	 * Calcola gli elementi adiacenti a quello passato come parametro. Per
	 * farlo, si avvale del metodo elementiAdiacenti.
	 * @param elementoMappa l'elemento del quale trovare le caselle adiacenti.
	 * @return la lista di caselle adiacenti all'elementoMappa.
	 */
	public List<Casella> caselleAdiacenti (ElementoMappa elementoMappa){
		List<Casella> caselleAdiacenti = new ArrayList<Casella>();
		List<ElementoMappa> elementiAdiacenti = elementiAdiacenti(elementoMappa);
		Iterator<ElementoMappa> scorriElementi = elementiAdiacenti.iterator();
		while(scorriElementi.hasNext()){
			ElementoMappa elementoAdiacente = scorriElementi.next();
			if (elementoAdiacente instanceof Casella) {
				caselleAdiacenti.add((Casella) elementoAdiacente);
			}
		}
		return caselleAdiacenti;
	}

	/**
	 * 
	 * @return le caselle non occupate nella mappa.
	 */
	public List<Casella> caselleLibere(){
		List<Casella> caselleLibere = new ArrayList<Casella>();
		Iterator<Casella> scorriCaselle = listaCaselle.iterator();
		while (scorriCaselle.hasNext()){
			Casella casella = scorriCaselle.next();
			if (!casella.isOccupata()) {
				caselleLibere.add(casella);
			}
		}
		return caselleLibere;
	}

	/**
	 * Calcola le caselle libere adiacenti ad un dato elementoMappa.
	 * Il metodo sarà chiamato dal pastore che vorrà sapere in che caselle
	 * può muoversi senza spendere denaro, quindi il paramentro sarà la 
	 * sua casella corrente. Tuttavia il metodo prende il tipo generico
	 * ElementoMappa come input, scelta giustificata dalla volontà di lasciare
	 * spazio ad eventuali implementazioni future.
	 * @param elementoMappa l'elemento del quale si vogliono calcolare le 
	 * 		  caselle libere adiacenti.
	 * @return la lista di caselle libere adiacenti all'elementoMappa. 
	 */
	public List<Casella> caselleLibereAdiacenti(ElementoMappa elementoMappa) {
		List<Casella> caselleAdiacenti = caselleAdiacenti(elementoMappa);
		List<Casella> caselleLibereAdiacenti = new ArrayList<Casella>();
		Iterator<Casella> scorriCaselle = caselleAdiacenti.iterator();
		while(scorriCaselle.hasNext()){
			Casella casella = scorriCaselle.next();
			if (!casella.isOccupata()) {
				caselleLibereAdiacenti.add(casella);
			}
		}
		return caselleLibereAdiacenti;
	}

	/**
	 * Crea porzione del grafo della mappa, contenente solo le 
	 * caselle e le strade che le collegano (archi non orientati).
	 * Il metodo è utilizzato dal Pastore quando si deve muovere
	 * attraverso un percorso tra due caselle.
	 */
	private void creaGrafoCaselle() {
		Iterator<Casella> scorriCaselle = listaCaselle.iterator();
		while(scorriCaselle.hasNext()){
			grafoCaselle.addVertex(scorriCaselle.next());
		}
		Iterator<DefaultEdge> scorriArchiCaselle = listaArchiCaselle.iterator();
		while(scorriArchiCaselle.hasNext()){
			DefaultEdge arco = scorriArchiCaselle.next();
			grafoCaselle.addEdge((Casella)grafoMappa.getEdgeSource(arco),(Casella)grafoMappa.getEdgeTarget(arco),arco);
		}
	}

	/**
	 * @return la lista degli archi che connettono due vertici di tipo 
	 * 		   casella.
	 */
	private List<DefaultEdge> creaListaArchiCaselle() {
		Set<DefaultEdge> listaArchi = grafoMappa.edgeSet();
		Iterator<DefaultEdge> scorriArchi = listaArchi.iterator();
		while (scorriArchi.hasNext()){
			DefaultEdge arco = scorriArchi.next();
			if (grafoMappa.getEdgeSource(arco) instanceof Casella && grafoMappa.getEdgeTarget(arco) instanceof Casella) {
				listaArchiCaselle.add(arco);
			}
		}
		return listaArchiCaselle;
	}
	
	/**
	 * Calcola le caselle da aggiungere alla lista di caselle della mappa,
	 * e le aggiunge.
	 */
	private void creaListaCaselle() {
		Set<ElementoMappa> listaElementiMappa = grafoMappa.vertexSet();
		Iterator<ElementoMappa> scorriElementiMappa = listaElementiMappa.iterator();
		while (scorriElementiMappa.hasNext()){
			ElementoMappa elementoMappa = scorriElementiMappa.next();
			if (elementoMappa instanceof Casella) {
				listaCaselle.add((Casella)elementoMappa);
			}
		}
	}

	/**
	 * Calcola le regioni da aggiungere alla lista delle regioni della 
	 * mappa, e le aggiunge.
	 */
	private void creaListaRegioni() {
		Set<ElementoMappa> listaElementiMappa = grafoMappa.vertexSet();
		Iterator<ElementoMappa> scorriElementiMappa = listaElementiMappa.iterator();
		while (scorriElementiMappa.hasNext()){
			ElementoMappa elementoMappa = scorriElementiMappa.next();
			if (elementoMappa instanceof Regione) {
				listaRegioni.add((Regione)elementoMappa);
			}
		}
	}

	/**
	 * Calcola la lista delle regioni e delle caselle adiacenti ad un
	 * elementoMappa dato, controllando semplicemente se nel grafo 
	 * esiste un arco che li connetta direttamente.
	 * @param elementoMappa l'elementoMappa del quale calcolare la lista
	 * 		  delle adiacenze.
	 * @return la lista delle regioni e caselle adiacenti all'elementoMappa.
	 */
	private List<ElementoMappa> elementiAdiacenti (ElementoMappa elementoMappa){
		List<ElementoMappa> elementiAdiacenti = new ArrayList<ElementoMappa>();
		Set<DefaultEdge> archiUscenti = new HashSet<DefaultEdge>();
		archiUscenti = grafoMappa.edgesOf(elementoMappa);
		Iterator<DefaultEdge> scorriArchi = archiUscenti.iterator();
		while (scorriArchi.hasNext()){
			DefaultEdge arco = scorriArchi.next();
			ElementoMappa elementoAdiacente = grafoMappa.getEdgeSource(arco);
			if (elementoAdiacente.equals(elementoMappa)) {
				elementoAdiacente = grafoMappa.getEdgeTarget(arco);
			}
			elementiAdiacenti.add(elementoAdiacente);
		}
		return elementiAdiacenti;
	}

	/**
	 * 
	 * @return il grafo della mappa con sole caselle al suo interno.
	 */
	public SimpleGraph<Casella, DefaultEdge> getGrafoCaselle() {
		return grafoCaselle;

	}

	/**
	 * 
	 * @return la lista di tutte le caselle nella mappa.
	 */
	public List<Casella> getListaCaselle() {
		return listaCaselle;
	}

	/**
	 * 
	 * @return la lista di tutte le regioni nella mappa.
	 */
	public List<Regione> getListaRegioni() {
		return listaRegioni;
	}

	/**
	 * Calcola le regioni libere adiacenti ad un dato elementoMappa.
	 * Il metodo sarà chiamato da un animale indipendente (lupo o pecora
	 * nera) che vorrà sapere in che regioni può muoversi, quindi il paramentro 
	 * sarà una delle caselle adiacenti alla sua regione corrente, di preciso
	 * quella col numero associato al risultato del tiro del dado dell'animale. 
	 * Tuttavia il metodo prende il tipo generico ElementoMappa come input, 
	 * scelta giustificata dalla volontà di lasciare spazio ad eventuali 
	 * implementazioni future. Al momento l'ElementoMappa non potrà che essere 
	 * una casella, per come la mappa è strutturata.
	 * @param elementoMappa l'elemento del quale si vogliono calcolare le 
	 * 		  regioni libere adiacenti.
	 * @return la lista di regioni libere adiacenti all'elementoMappa. 
	 */
	public List<Regione> regioniConfinanti (ElementoMappa elementoMappa){
		List<Regione> regioniConfinanti = new ArrayList<Regione>();
		List<ElementoMappa> elementiAdiacenti = elementiAdiacenti(elementoMappa);
		Iterator<ElementoMappa> scorriElementi = elementiAdiacenti.iterator();
		while(scorriElementi.hasNext()){
			ElementoMappa elementoAdiacente = scorriElementi.next();
			if (elementoAdiacente instanceof Regione) {
				regioniConfinanti.add((Regione) elementoAdiacente);
			}
		}
		return regioniConfinanti;
	}

	/**
	 * Metodo che si sincera che la Mappa sia un Singleton.
	 * @param grafoMappa il grafo estratto dal file Mappa.xml
	 * @return la Mappa creata, se questa non esiste già.
	 */
	public static Mappa creaMappa(SimpleGraph<ElementoMappa,DefaultEdge> grafoMappa){
		if (istanza == null) {
			return new Mappa(grafoMappa);
		} else {
			return null;
		}
	}
}
