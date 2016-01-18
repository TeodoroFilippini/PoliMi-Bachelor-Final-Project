package view;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Agnello;
import model.Animale;
import model.Ariete;
import model.Carta;
import model.Casella;
import model.Costanti;
import model.Lupo;
import model.Mappa;
import model.Pastore;
import model.Pecora;
import model.PecoraNera;
import model.Regione;
import model.StatoGioco;
/**
 * Classe utilizzata per creare, contenere e ricercare le istanze view associate
 * alle varie classi di model.
 * 
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class IstanzeView {

	private List<AnimaleView> listaAnimaliView = new ArrayList<AnimaleView>();
	private List<CartaView> listaCarteView = new ArrayList<CartaView>();
	private List<CasellaView> listaCaselleView = new ArrayList<CasellaView>();
	private List<PastoreView> listaPastoriView = new ArrayList<PastoreView>();
	private List<RegioneView> listaRegioniView = new ArrayList<RegioneView>();
	private Mappa mappa;
	private StatoGioco statoGioco;

	/**
	 * Riceve lo statoGioco e crea le istanze view di tutte le istanze presenti in esso.
	 * @param statoGioco oggetto contenente tutte le istanze di cui vogliamo creare la view associata.
	 */
	public IstanzeView(StatoGioco statoGioco){
		this.statoGioco = statoGioco;
		mappa = statoGioco.getMappa();
		creaIstanze();
	}
	
	/**
	 * Riceve un animale e la regione alla quale aggiungerlo. Verifica 
	 * il tipo di animale e a crea una nuova istanza view con i dati relativi
	 * a quel tipo di animale (tipo di istanza view e coordinate della regione view) e
	 * lo aggiunge alla apposita lista di animali view.
	 * 
	 * @param animale animale di cui si vuole creare una nuova istanza.
	 * @param regione regione alla quale si vuole aggiungere il nuovo animale.
	 */
	public void aggiungiAnimaleView(Animale animale,Regione regione) {
		RegioneView regioneView = cercaRegioneView(regione);
		int offset = Costanti.OFFSET_STAMPA_OVINI*(regione.getListaAnimali().size()-1);
		int xOvini = regioneView.getCoordinateOvini().x + offset;
		int yOvini = regioneView.getCoordinateOvini().y+offset;
		if (animale instanceof Pecora){
			PecoraView pecoraView = new PecoraView ((Pecora)animale);
			pecoraView.setCoordinate(new Point(xOvini,yOvini));
			listaAnimaliView.add(pecoraView);
		}
		if (animale instanceof Agnello){
			AgnelloView agnelloView = new AgnelloView ((Agnello)animale);
			agnelloView.setCoordinate(new Point(xOvini,yOvini));
			listaAnimaliView.add(agnelloView);
		}
		if (animale instanceof Ariete){
			ArieteView arieteView = new ArieteView ((Ariete)animale);
			arieteView.setCoordinate(new Point(xOvini,yOvini));
			listaAnimaliView.add(arieteView);
		}
		if (animale instanceof Lupo){
			LupoView lupoView = new LupoView ((Lupo)animale);
			lupoView.setCoordinate(regioneView.getCoordinateLupo());
			listaAnimaliView.add(lupoView);
		}
		if (animale instanceof PecoraNera){
			PecoraNeraView pecoraNeraView = new PecoraNeraView ((PecoraNera)animale);
			pecoraNeraView.setCoordinate(regioneView.getCoordinatePecoraNera());
			listaAnimaliView.add(pecoraNeraView);
		}
	}

	/**
	 * Crea una nuova istanza view del pastore ricevuto da parametro, quindi imposta
	 * le sue coordinate a quelle della casella view corrispondente alla sua posizione e 
	 * lo aggiunge alla lista di pastori view.
	 * 
	 * @param pastore pastore di cui si vuole creare una nuova istanza view.
	 */
	public void aggiungiPastoreView(Pastore pastore){
		PastoreView pastoreView = new PastoreView(pastore);
		CasellaView casellaView = cercaCasellaView(pastore.getPosizione());
		pastoreView.setCoordinate(casellaView.getCoordinate());
		listaPastoriView.add(pastoreView);
	}

	/**
	 * Cerca nella lista di animaliView quello associato all'animale ricevuto da parametro.
	 * 
	 * @param animale animale di cui si vuole ottenere l'istanza view.
	 * @return istanza view associata all'animale.
	 */
	public AnimaleView cercaAnimaleView(Animale animale) {
		Iterator<AnimaleView> scorriAnimali = listaAnimaliView.iterator();
		while(scorriAnimali.hasNext()){
			AnimaleView animaleView = scorriAnimali.next();
			if (animaleView.getAnimale() == animale) {
				return animaleView;
			}
		}
		return null;
	}

	/**
	 * Cerca nella lista di carteView quella associata alla carta ricevuta da parametro.
	 * 
	 * @param carta carta di cui si vuole ottenere l'istanza view.
	 * @return istanza view associata alla carta.
	 */
	public CartaView cercaCartaView(Carta carta) {
		Iterator<CartaView> scorriCarte = listaCarteView.iterator();
		while(scorriCarte.hasNext()){
			CartaView cartaView = scorriCarte.next();
			if (cartaView.getCarta() == carta) {
				return cartaView;
			}
		}
		return null;
	}

	/**
	 * Cerca nella lista di caselleView quella associata alla casella ricevuta da parametro.
	 * 
	 * @param casella casella di cui si vuole ottenere l'istanza view.
	 * @return istanza view associata alla casella.
	 */
	public CasellaView cercaCasellaView(Casella casella) {
		Iterator<CasellaView> scorriCaselle = listaCaselleView.iterator();
		while(scorriCaselle.hasNext()){
			CasellaView casellaView = scorriCaselle.next();
			if (casellaView.getCasella() == casella) {
				return casellaView;
			}
		}
		return null;
	}

	/**
	 * Cerca nella lista di pastoriView quello associato al pastore ricevuto da parametro.
	 * 
	 * @param pastore pastore di cui si vuole ottenere l'istanza view.
	 * @return istanza view associata al pastore.
	 */
	public PastoreView cercaPastoreView(Pastore pastore) {
		Iterator<PastoreView> scorriPastori = listaPastoriView.iterator();
		while(scorriPastori.hasNext()){
			PastoreView pastoreView = scorriPastori.next();
			if (pastoreView.getPastore() == pastore) {
				return pastoreView;
			}
		}
		return null;
	}

	/**
	 * Cerca nella lista di regioniView quella associata alla regione ricevuta da parametro.
	 * 
	 * @param regione regione di cui si vuole ottenere l'istanza view.
	 * @return istanza view associata alla regione.
	 */
	public RegioneView cercaRegioneView(Regione regione) {
		Iterator<RegioneView> scorriRegioni = listaRegioniView.iterator();
		while(scorriRegioni.hasNext()){
			RegioneView regioneView = scorriRegioni.next();
			if (regioneView.getRegione() == regione) {
				return regioneView;
			}
		}
		return null;
	}

	/**
	 * Crea un XMLParserView che crea le istanze delle regioni view e delle caselle view.
	 * A questo punto per ogni animale contenuto nelle regioni chiama il metodo aggiungiAnimaleView
	 * che crea una nuova istanza view dell'animale, poi per ogni carta contenuta nello statoGioco
	 * crea una corrispettiva carta view.
	 */
	private void creaIstanze(){
		XMLParserView nuovoXMLParserView = new XMLParserView(Costanti.PERCORSO_FILE_XML,mappa);
		listaRegioniView =  nuovoXMLParserView.getListaRegioniView();
		listaCaselleView = nuovoXMLParserView.getListaCaselleView();
		Iterator<Regione> scorriRegioni = mappa.getListaRegioni().iterator();
		while (scorriRegioni.hasNext()){
			Regione regione = scorriRegioni.next();
			Iterator<Animale> scorriAnimali = regione.getListaAnimali().iterator();
			while (scorriAnimali.hasNext()){
				Animale animale = scorriAnimali.next();
				aggiungiAnimaleView(animale, regione);
			}
		}
		Iterator<Carta> scorriCarte = statoGioco.getListaCarte().iterator();
		while (scorriCarte.hasNext()){
			Carta carta = scorriCarte.next();
			listaCarteView.add(new CartaView(carta));
		}
	}

	/**
	 * 
	 * @return lista degli animali view.
	 */
	public List<AnimaleView> getListaAnimaliView() {
		return listaAnimaliView;
	}

	/**
	 * 
	 * @return lista delle carte view.
	 */
	public List<CartaView> getListaCarteView() {
		return listaCarteView;
	}

	/**
	 * 
	 * @return lista delle caselle view.
	 */
	public List<CasellaView> getListaCaselleView() {
		return listaCaselleView;
	}

	/**
	 * 
	 * @return lista dei pastori view.
	 */
	public List<PastoreView> getListaPastoriView() {
		return listaPastoriView;
	}

	/**
	 * 
	 * @return lista delle regioni view.
	 */
	public List<RegioneView> getListaRegioniView() {
		return listaRegioniView;
	}

	/**
	 * Cerca l'animale view corrispondente all'animale passato da parametro e 
	 * lo rimuove dalla lista di animali view.
	 * @param animale animale di cui si vuole rimuovere la view corrispondente.
	 */
	public void rimuoviAnimaleView(Animale animale) {
		AnimaleView animaleView = cercaAnimaleView(animale);
		listaAnimaliView.remove(animaleView);
	}

}
