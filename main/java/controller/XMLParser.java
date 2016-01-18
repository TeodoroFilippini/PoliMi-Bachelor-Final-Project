package controller;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Casella;
import model.ElementoMappa;
import model.Mappa;
import model.Regione;
import model.TipoTerreno;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import view.FinestraNotifica;

/**
 * Classe utilizzata per importare i dati contenuti nel file Mappa.xml
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class XMLParser {
	private InputStream inputStream;
	private SimpleGraph<ElementoMappa,DefaultEdge>grafoMappa = new SimpleGraph<ElementoMappa,DefaultEdge>(DefaultEdge.class);
	private List<Casella> listaCaselle = new ArrayList<Casella>();
	private List<Regione> listaRegioni = new ArrayList<Regione>();
	private Mappa mappa;
	private Element radice;

	/**
	 * Costruttore della classe parser: legge il file xml
	 * e crea la mappa dai dati estratti.
	 * @param fileString nome del file da aprire.
	 */
	public XMLParser(String fileString){
		inputStream = getClass().getResourceAsStream(fileString);
		readFileXML();
		creaMappa();
	}

	/**
	 * Metodo che cerca tra le caselle quella con un nome dato.
	 * @param nomeSource il nome della casella da cercare.
	 * @return la casella cercata, se esiste;
	 * 		   null se no.
	 */
	private Casella cercaInCaselle(String nomeSource) {
		Iterator<Casella> iterator = listaCaselle.iterator();
		while (iterator.hasNext()){
			Casella casellaCorrente = iterator.next();
			if (nomeSource.equals(casellaCorrente.toString())) {
				return casellaCorrente;
			}
		}
		return null;
	}

	/**
	 * Metodo che cerca tra le regioni quella con un nome dato.
	 * @param nomeSource il nome della regione da cercare.
	 * @return la regione cercata, se esiste;
	 * 		   null se no.
	 */
	private Regione cercaInRegioni(String nomeSource) {
		Iterator<Regione> iterator = listaRegioni.iterator();
		while (iterator.hasNext()){
			Regione regioneCorrente = iterator.next();
			if (nomeSource.equals(regioneCorrente.toString())) {
				return regioneCorrente;
			}
		}
		return null;
	}

	/**
	 * aggiunge alla mappa, basandosi su quanto legge nel file xml, gli 
	 * archi con cui connettere i vertici del grafo.
	 * @param archi una lista di archi.
	 */
	private void creaArchi(List<Element> archi) {
		Iterator<Element> iterator = archi.iterator();
		while (iterator.hasNext()){
			Element elementoCorrente = iterator.next();
			String nomeSource = elementoCorrente.getAttributeValue("source");
			String nomeTarget = elementoCorrente.getAttributeValue("target");
			ElementoMappa source;
			ElementoMappa target;
			String nomeCasella = "casella";
			if (nomeSource.startsWith(nomeCasella)) {
				source = cercaInCaselle(nomeSource);
			} else {
				source = cercaInRegioni(nomeSource);
			}
			if (nomeTarget.startsWith(nomeCasella)) {
				target = cercaInCaselle(nomeTarget);
			} else {
				target = cercaInRegioni(nomeTarget);
			}
			grafoMappa.addEdge(source, target);
		}
	}

	/**
	 * crea, basandosi su quanto legge nel file xml, la lista
	 * delle caselle, aggiungendole come vertici al grafo della mappa.
	 * @param caselle le caselle delle quali trovar la corrispondenza
	 * 		  nel file xml.
	 */
	private void creaCaselle(List<Element> caselle) {
		Iterator<Element> scorriCaselle = caselle.iterator();
		while (scorriCaselle.hasNext()){
			Casella casellaCorrente;
			Element elementoCorrente = scorriCaselle.next();
			String numero = elementoCorrente.getChildText("numero");
			String nome = elementoCorrente.getAttributeValue("nome");
			casellaCorrente = new Casella(nome,Integer.parseInt(numero));
			grafoMappa.addVertex(casellaCorrente);
			listaCaselle.add(casellaCorrente);
		}
	}

	/**
	 * Crea la mappa: consultando il file xml, ottiene la lista delle
	 * regioni, delle caselle e degli archi che le connettono, generando
	 * un grafo. Infine genera un oggetto Mappa, passandogli il grafo come 
	 * argomento.
	 */
	private void creaMappa(){
		Element grafo = radice.getChild("grafo");
		Element vertici = grafo.getChild("vertici");
		Element elencoArchi = grafo.getChild("archi");
		Element elencoRegioni = vertici.getChild("regioni");
		Element elencoCaselle = vertici.getChild("caselle");
		List<Element> regioni = elencoRegioni.getChildren();
		List<Element> caselle = elencoCaselle.getChildren();
		List<Element> archi = elencoArchi.getChildren();
		creaRegioni(regioni);
		creaCaselle(caselle);
		creaArchi(archi);
		mappa = Mappa.creaMappa(grafoMappa);
	}

	/**
	 * Crea, consultando il file xml, la lista delle regioni.
	 * @param regioni lista di regioni ottenute dall'xml.
	 */
	private void creaRegioni(List<Element> regioni) {
		Iterator<Element> iterator = regioni.iterator();
		while (iterator.hasNext()){
			Regione regioneCorrente;
			Element elementoCorrente = iterator.next();
			String tipo = elementoCorrente.getChildText("tipo");
			String nome = elementoCorrente.getAttributeValue("nome");
			regioneCorrente = new Regione(nome,TipoTerreno.valueOf(tipo));
			grafoMappa.addVertex(regioneCorrente);
			listaRegioni.add(regioneCorrente);
		}
	}

	/**
	 * 
	 * @return il grafo associato alla mappa.
	 */
	public SimpleGraph<ElementoMappa, DefaultEdge> getGrafoMappa() {
		return grafoMappa;
	}

	/**
	 * 
	 * @return la lista delle caselle presenti.
	 */
	public List<Casella> getListaCaselle() {
		return listaCaselle;
	}

	/**
	 * 
	 * @return la lista delle regioni.
	 */
	public List<Regione> getListaRegioni() {
		return listaRegioni;
	}

	/**
	 * 
	 * @return la mappa.
	 */
	public Mappa getMappa() {
		return mappa;
	}

	/**
	 * Apre il file xml e imposta il nodo radice del documento, 
	 * scritto come un albero, dal quale ricaverà tutti i dati.
	 */
	private void readFileXML(){
		try{
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(inputStream);
			radice = document.getRootElement();
		}catch (IOException e){
			new FinestraNotifica("Errore durante l'apertura del file XML", e);
		}catch (JDOMException e){	
			new FinestraNotifica("Errore durante la lettura del file XML", e);
		}
	}

}

