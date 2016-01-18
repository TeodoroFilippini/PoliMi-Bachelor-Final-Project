package view;

import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Casella;
import model.Costanti;
import model.Mappa;
import model.Regione;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
/**
 * Classe per il parsing del file XML contenente i dati della mappa utili per 
 * la stampa degi vari componenti che popolano il campo di gioco.
 * 
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class XMLParserView {
	private InputStream inputStream;
	private List<CasellaView> listaCaselleView = new ArrayList<CasellaView>();
	private List<RegioneView> listaRegioniView = new ArrayList<RegioneView>();
	private Mappa mappa;
	private Element radice;
	/**
	 * Viene caricata la mappa dalla quale prendiamo le caselle e le regioni di cui
	 * vogliamo creare la corrispettiva classe View.
	 * Viene caricato il file XML da cui prendiamo tutti i dati che ci interessano.
	 * Vengono chiamati i metodi per la creazione delle varie classi RegioneView e CasellaView
	 * @param fileString path del file XML
	 * @param mappa mappa usata per associare ad ogni elemento il suo corrispettivo view
	 */
	public XMLParserView(String fileString,Mappa mappa){
		this.mappa = mappa;
		inputStream = getClass().getResourceAsStream(fileString);
		readFileXML();
		Element grafo = radice.getChild("grafo");
		Element vertici = grafo.getChild("vertici");
		Element elencoRegioni = vertici.getChild("regioni");
		Element elencoCaselle = vertici.getChild("caselle");
		List<Element> regioni = elencoRegioni.getChildren();
		List<Element> caselle = elencoCaselle.getChildren();
		creaCaselleView(caselle);
		creaRegioniView(regioni);
	}
	/**
	 * Cerca una casella con il nome passato da parametro all'interno della lista
	 * di caselle della mappa.
	 * @param nomeSource nome della casella.
	 * @return casella con il nome passato.
	 */
	private Casella cercaInCaselle(String nomeSource) {
		Iterator<Casella> iterator = mappa.getListaCaselle().iterator();
		while (iterator.hasNext()){
			Casella casellaCorrente = iterator.next();
			if (nomeSource.equals(casellaCorrente.toString())) {
				return casellaCorrente;
			}
		}
		return null;
	}
	/**
	 * Cerca una regione con il nome passato da parametro all'interno della lista
	 * di regioni della mappa.
	 * @param nomeSource nome della regione.
	 * @return regione con il nome passato.
	 */
	private Regione cercaInRegioni(String nomeSource) {
		Iterator<Regione> scorriRegioni = mappa.getListaRegioni().iterator();
		while (scorriRegioni.hasNext()){
			Regione regione = scorriRegioni.next();
			if (nomeSource.equals(regione.toString())) {
				return regione;
			}
		}
		return null;
	}
	/**
	 * Itera sulla lista di caselle passata come parametro e per ognuna di esse
	 * crea la corrispettiva CasellaView associata ad ogni casella con un riferimento alla casella
	 * stessa e le coordinate di stampa prese dal file XML e scalate per l'opportuno fattore di scala.
	 * 
	 * @param listaCaselle lista di caselle di cui bisogna creare la CasellaView corrispettiva.
	 */
	private void creaCaselleView(List<Element> listaCaselle) {
		Iterator<Element> scorriCaselle = listaCaselle.iterator();
		while (scorriCaselle.hasNext()){
			Element elementoCorrente = scorriCaselle.next();
			String nome = elementoCorrente.getAttributeValue("nome");
			int x = (int) (Integer.parseInt(elementoCorrente.getChildText("x")) * Costanti.FATTORE_SCALA);
			int y = (int) (Integer.parseInt(elementoCorrente.getChildText("y")) * Costanti.FATTORE_SCALA);
			Casella casella = cercaInCaselle(nome);
			listaCaselleView .add(new CasellaView(casella, new Point (x,y)));
		}
	}
	/**
	 * Itera sulla lista di regioni passata come parametro e per ognuna di esse
	 * crea la corrispettiva RegioneView associata ad ogni regione con un riferimento alla regione
	 * stessa e le coordinate di stampa per gli ovini, il lupo e la pecora nera
	 * prese dal file XML e scalate per l'opportuno fattore di scala.
	 * 
	 * @param listaregioni lista di regioni di cui bisogna creare la RegioneView corrispettiva.
	 */
	private void creaRegioniView(List<Element> regioni) {
		Iterator<Element> iterator = regioni.iterator();
		while (iterator.hasNext()){
			Element elementoCorrente = iterator.next();
			String nome = elementoCorrente.getAttributeValue("nome");
			int xLupo = (int) (Integer.parseInt(elementoCorrente.getChildText("xLupo")) * Costanti.FATTORE_SCALA);
			int yLupo = (int) (Integer.parseInt(elementoCorrente.getChildText("yLupo")) * Costanti.FATTORE_SCALA);
			int xPecoraNera = (int) (Integer.parseInt(elementoCorrente.getChildText("xPecoraNera")) * Costanti.FATTORE_SCALA);
			int yPecoraNera = (int) (Integer.parseInt(elementoCorrente.getChildText("yPecoraNera")) * Costanti.FATTORE_SCALA);
			int xOvini = (int) (Integer.parseInt(elementoCorrente.getChildText("xOvini")) * Costanti.FATTORE_SCALA);
			int yOvini = (int) (Integer.parseInt(elementoCorrente.getChildText("yOvini")) * Costanti.FATTORE_SCALA);
			Point coordinateLupo = new Point (xLupo,yLupo);
			Point coordinatePecoraNera = new Point (xPecoraNera,yPecoraNera);
			Point coordinateOvini = new Point (xOvini, yOvini);
			Regione regione = cercaInRegioni(nome);
			listaRegioniView.add(new RegioneView(regione,coordinateLupo,coordinatePecoraNera,coordinateOvini));
		}
	}
	/**
	 * 
	 * @return lista delle CaselleView.
	 */
	public List<CasellaView> getListaCaselleView() {
		return listaCaselleView;
	}
	/**
	 * 
	 * @return lista delle RegioniView
	 */
	public List<RegioneView> getListaRegioniView() {
		return listaRegioniView;
	}
	/**
	 * Apre il file XML per estrarre l'elemento iniziale(radice) che viene salvato
	 * come field della classe.
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
