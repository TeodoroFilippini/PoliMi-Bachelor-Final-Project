
package view;


import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import model.Animale;
import model.Carta;
import model.CartaInVendita;
import model.Casella;
import model.Costanti;
import model.Giocatore;
import model.Lupo;
import model.Pastore;
import model.PecoraNera;
import model.Regione;
import model.StatoGioco;
import model.TipoMossa;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
/**
 * Classe principale di interazione tra il controller e l'interfaccia grafica.
 * Contiene i metodi per la stampa degli oggetti sulla mappa, quelli per
 * prendere gli input dal giocatore e per visualizzare messaggi informativi
 * Siccome il controller istanzia solamente un oggetto view, questa classe viene spesso
 * utilizzata per inoltrare le richieste fatte dal controller ai vari componenti
 * che formano il package view, in maniera da alleggerire e rendere più leggibile il codice
 * nel controller.
 * 
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class SheeplandView implements InterfacciaView{

	private static final long serialVersionUID = 1L;
	private FinestraGioco finestraGioco;
	private Giocatore giocatore;
	private InterfacciaGiocatore interfacciaGiocatore;
	private IstanzeView istanzeView;
	private PannelloBottoni pannelloBottoni;
	private PannelloMappa pannelloMappa;
	private JPanel pannelloOvest;
	private StatoGioco statoGioco;
	private FinestraCarte finestraCarteDaVendere; 

	/**
	 * Carica lo statoGioco, utilizzato per tenere aggiornati i dati da
	 * visualizzare sulla finestra di gioco, crea una classe di istanze view
	 * utilizzata per associare ad ogni entità del model un suo corrispettivo view.
	 * Chiama il metodo creaPannelli() che inizializza la finestra di gioco.
	 * @param statoGioco classe contenente le istanze di gioco.
	 */
	public SheeplandView(StatoGioco statoGioco){
		this.statoGioco = statoGioco;
		istanzeView = new IstanzeView(statoGioco);
		creaPannelli();
	}

	/**
	 * Uguale al costruttore sopra, tranne che viene caricato un giocatore
	 * al quale viene associata l'oggetto view creato. Utile per il gioco online
	 * quando ogni giocatore deve possedere una propria view.
	 * @param statoGioco classe contenente le istanze di gioco.
	 * @param giocatore giocatore a cui viene associata la view.
	 */
	public SheeplandView (StatoGioco statoGioco,Giocatore giocatore){
		this.statoGioco = statoGioco;
		this.giocatore = giocatore;
		istanzeView = new IstanzeView(statoGioco);
		creaPannelli();
	}

	/**
	 * Aggiunge una nuova istanza di animale view ad istanzeView tramite l'apposito metodo.
	 * @param animale animale di cui va creata una nuova istanza di animaleView
	 * @param regione regione alla quale aggiungere la nuova istanza di animaleView 
	 */

	@Override
	public void aggiungiAnimaleView(Animale animale, Regione regione) {
		istanzeView.aggiungiAnimaleView(animale, regione);
	}

	/**
	 * Aggiunge una nuova carta all'interfacciaGiocatore tramite l'apposito metodo.
	 * @param carta carta da aggiungere all'interfaccia
	 * @param giocatore parametro non utilizzato ma utile in altre implementazioni
	 * 		dell'interfacciaView per sapere il giocatore al quale aggiungere la carta.
	 */

	@Override
	public void aggiungiCarta(Carta carta,Giocatore giocatore) {
		interfacciaGiocatore.aggiungiCarta(carta);
	}

	/**
	 * Stampa un messaggio informativo con il giocatore corrotto e il giocatore che corrompe.
	 * @param giocatoreCorrotto giocatore che riceve i danari per la corruzione.
	 * @param giocatoreCorruttore giocatore che paga il giocatore da corrompere.
	 */

	@Override
	public void corrompi(Giocatore giocatoreCorrotto, Giocatore giocatoreCorruttore) {
		stampaMessaggio("Qualcuno ha visto qualcosa! Giocatore "+(giocatoreCorruttore.getNumero())+
				" deve corrompere Giocatore "+(giocatoreCorrotto.getNumero()));
	}

	/**
	 * Crea la finestra di gioco alla quale verranno aggiunti i pannelli. 
	 * Crea il pannello di sfondo tramite l'apposita classe sul quale viene stampata l'immagine di sfondo
	 * che viene utilizzato come contenitore degli altri,utilizzando un BorderLayout.
	 * L'immagine di sfondo resta visibile poichè settiamo tutti gli altri pannelli ad avere
	 * sfondo trasparente. Viene creato il pannelloOvest che è un contenitore a sua volta, sempre con Borderlayout
	 * contenente l'interfaccia del giocatore corrente a nord e il pannello con i danari di tutti i giocatori
	 * a sud. Vengono creati il pannello mappa, contenente la mappa, e il pannello bottoni, contenente i 
	 * bottoni per le varie mosse, tramite le apposite classi. Vengono poi aggiunti tutti al pannello sfondo con
	 * pannelloOvest a ovest, pannelloMappa al centro e pannelloBottoni a est. Il pannello sfondo è dunque
	 * aggiunto alla finestra che viene ridimensionata per contenerlo, centrata rispetto allo schermo e resa visibile.
	 */

	private void creaPannelli() {
		finestraGioco = new FinestraGioco();
		PannelloSfondo pannelloSfondo= new PannelloSfondo(Costanti.PERCORSO_FILE_SFONDO);
		PannelloGiocatori pannelloGiocatori = new PannelloGiocatori(statoGioco.getListaGiocatori());
		pannelloOvest = new JPanel(new BorderLayout());
		pannelloOvest.setOpaque(false);
		pannelloOvest.add(pannelloGiocatori,BorderLayout.SOUTH);
		pannelloMappa = new PannelloMappa(istanzeView,statoGioco);
		pannelloBottoni = new PannelloBottoni();
		pannelloSfondo.add(pannelloMappa, BorderLayout.CENTER);
		pannelloSfondo.add(pannelloBottoni, BorderLayout.EAST);
		pannelloSfondo.add(pannelloOvest, BorderLayout.WEST);
		finestraGioco.add(pannelloSfondo);
		finestraGioco.pack();
		finestraGioco.setLocationRelativeTo(null);
		finestraGioco.setVisible(true);
	}

	/**
	 * Disabilita tutti i bottoni del pannello bottoni tramite l'apposito metodo.
	 */

	@Override
	public void disabilitaBottoni() {
		pannelloBottoni.disabilitaBottoni();
	}

	/**
	 * @return il giocatore alla quale è associata la view
	 */

	@Override
	public Giocatore getGiocatore() {
		return giocatore;
	}



	/**
	 * Lancia l'animazione del lancio del dado da parte del lupo e attende finchè 
	 * il pannello mappa è impegnato in tale azione.
	 * 
	 * @param dadoLupo numero della faccia del dado da visualizzare alla fine dell'animazione
	 */

	private synchronized void lanciaDadoLupo(int dadoLupo){
		pannelloMappa.lanciaDadoLupo(dadoLupo);
		while (pannelloMappa.staLanciandoDadoLupo()) {
			try {
				wait(100);
			} catch (InterruptedException e) {
				new FinestraNotifica("Il thread in attesa della stampa ha terminato la sua attesa inaspettatamente", e);
			}
		}
	}

	/**
	 * Lancia l'animazione del lancio del dado da parte della pecora nera e attende finchè 
	 * il pannello mappa è impegnato in tale azione.
	 * 
	 * @param dadoPecoraNera numero della faccia del dado da visualizzare alla fine dell'animazione
	 */

	@Override
	public synchronized void lanciaDadoPecoraNera(int dadoPecoraNera){
		pannelloMappa.lanciaDadoPecoraNera(dadoPecoraNera);
		while (pannelloMappa.staLanciandoDadoPecoraNera()) {
			try {
				wait(100);
			} catch (InterruptedException e) {
				new FinestraNotifica("Il thread in attesa della stampa ha terminato la sua attesa inaspettatamente", e);
			}
		}
	}

	/**
	 * Muove l'animale dalla sua posizione corrente alla regione d'arrivo tramite l'apposito metodo.
	 * 
	 * @param animale animale da spostare.
	 * @param arrivo regione di arrivo dell'animale da spostare.
	 */

	@Override
	public void muoviAnimale(Animale animale, Regione arrivo) {
		pannelloMappa.muoviAnimale(animale, arrivo);
	}

	/**
	 * Carica l'istanza del lupo dallo statoGioco, 
	 * lancia l'animazione del dado, stampa un messaggio informativo sui movimenti del lupo,
	 * dopodichè muove il lupo sulla mappa nella sua regione d'arrivo tramite l'apposito metodo.
	 * 
	 * @param arrivo regione in cui si deve spostare il lupo.
	 * @param dado numero del dado per lanciare l'animazione.
	 */

	@Override
	public void muoviLupo (Regione arrivo, int dado){
		Lupo lupo = statoGioco.getLupo();
		lanciaDadoLupo(dado);
		stampaMessaggio("Il lupo ha tirato il dado e ottenuto "+dado+". Si muove in "+arrivo.getNomeRegione());
		pannelloMappa.muoviAnimale(lupo, arrivo);
	}

	/**
	 * Muove il pastore di casella in casella lungo il percorso più breve dalla sua 
	 * posizione iniziale fino alla casella d'arrivo. Per fare ciò estraiamo il grafoCaselle
	 * dalla mappa tramite l'apposito metodo, poichè vogliamo che il pastore si muova solamente lungo
	 * le caselle e non attraverso le regioni che fanno invece parte del grafoMappa completo. A questo punto
	 * utilizziamo la classe DijkstraShortestPath per trovare il percorso più breve che viene salvato come
	 * una lista di DefaultEdge. Ora iteriamo su questa lista chiamando il metodo di pannello mappa che muove il pastore 
	 * da una casella all'altra passando ogni volta il pastore, la sua posizione corrente e la prossima casella del percorso.
	 * Inoltre, siccome non sappiamo quale sia la source e il target degli edge dobbiamo controllarli entrambi per trovare 
	 * una casella che differisca dalla posizione corrente del pastore e impostarla come prossima casella d'arrivo.
	 * 
	 *   @param pastore pastore da muovere.
	 *   @param arrivo casella d'arrivo del pastore.
	 */

	@Override
	public void muoviPastore(Pastore pastore, Casella arrivo) {
		SimpleGraph<Casella,DefaultEdge> grafoCaselle = statoGioco.getMappa().getGrafoCaselle();
		Casella posizionePastore = pastore.getPosizione();
		DijkstraShortestPath<Casella, DefaultEdge> dijkstra = new DijkstraShortestPath<Casella, DefaultEdge>(grafoCaselle,posizionePastore,arrivo);
		List<DefaultEdge> percorso = dijkstra.getPathEdgeList();
		Iterator<DefaultEdge> scorriPercorso = percorso.iterator();
		Casella posizioneCorrente = posizionePastore;
		while (scorriPercorso.hasNext()){
			DefaultEdge arco = scorriPercorso.next();
			if (grafoCaselle.getEdgeSource(arco) == posizioneCorrente){
				Casella destinazione = grafoCaselle.getEdgeTarget(arco);
				pannelloMappa.muoviPastore(pastore, posizioneCorrente, destinazione);
				posizioneCorrente = destinazione;
			}else{
				Casella destinazione = grafoCaselle.getEdgeSource(arco);
				pannelloMappa.muoviPastore(pastore, posizioneCorrente, destinazione);
				posizioneCorrente = destinazione;
			}
		}
	}

	/**
	 * Carica l'istanza della pecora nera dallo statoGioco e la muove
	 * nella regione d'arrivo tramite l'apposito metodo.
	 * 
	 * @param arrivo regione d'arrivo della pecora nera
	 * @param dado parametro non utilizzato, ma utile in altre implementazioni
	 * 		dell'InterfacciaView per il gioco online.
	 */

	@Override
	public void muoviPecoraNera(Regione arrivo, int dado){
		PecoraNera pecoraNera = statoGioco.getPecoraNera();
		pannelloMappa.muoviAnimale(pecoraNera, arrivo);
	}


	/**
	 * Rimuove l'istanza view dell'animale passato tramite l'apposito metodo.
	 * 
	 * @param animale animale di cui vuoi rimuovere l'istanza view.
	 */
	@Override
	public void rimuoviAnimaleView(Animale animale) {
		istanzeView.rimuoviAnimaleView(animale);
	}

	
	/**
	 * Permette la scelta di un animale da una lista passata aprendo l'apposita finestraAnimali.
	 * La FinestraAnimali prende come parametro una lista di animaliView, quindi prima di aprirla
	 * cerca le istanze degli animali tramite il metodo di IstanzeView. Quindi crea la finestra animali
	 * e tramite il suo metodo scegliAnimale() si mette in attesa che l'utente  scelga l'animale.
	 * Chiude la finestra e torna l'animale scelto.
	 * 
	 * @param listaAnimali lista di animali tra i quali scegliere.
	 * @return animale animale scelto.
	 */
	@Override
	public Animale scegliAnimale(List<Animale> listaAnimali) {
		List<AnimaleView> listaAnimaliView = new ArrayList<AnimaleView>();
		Iterator<Animale> scorriAnimali = listaAnimali.iterator();
		while (scorriAnimali.hasNext()){
			Animale animale = scorriAnimali.next();
			listaAnimaliView.add(istanzeView.cercaAnimaleView(animale));
		}
		FinestraAnimali finestraAnimali = new FinestraAnimali(listaAnimaliView,finestraGioco);
		Animale animale = finestraAnimali.scegliAnimale();
		finestraAnimali.dispose();
		return animale;
	}

	/**
	 * Permette la scelta di un carta da una lista passata aprendo l'apposita finestraCarte.
	 * La FinestraCarte prende come parametro una lista di CarteView, quindi prima di aprirla
	 * cerca le istanze degli carte tramite il metodo di IstanzeView. Quindi crea la finestra carte
	 * passando come parametro di compravendita false, poichè non siamo nel caso di vendita carte,
	 * e tramite il suo metodo scegliCarta si mette in attesa che l'utente  scelga l'carta.
	 * Chiude la finestra e torna l'carta scelto.
	 * 
	 * @param listaCarte lista di carte tra i quali scegliere.
	 * @return carta scelta.
	 */
	@Override
	public Carta scegliCarta(List<Carta> listaCarte) {
		List<CartaView> listaCarteView = cercaCarteView(listaCarte);
		FinestraCarte finestraCarte = new FinestraCarte(listaCarteView,finestraGioco,false);
		Carta cartaScelta = finestraCarte.scegliCarta();
		finestraCarte.dispose();
		return cartaScelta;
	}

	
	/**
	 * Permette la scelta di una casella da una lista passata come parametro tramite
	 * l'apposito metodo di pannelloMappa.
	 * 
	 * @param listaCaselle lista di caselle tra cui scegliere.
	 * @return casella scelta.
	 */
	@Override
	public Casella scegliCasella(List<Casella> listaCaselle) {
		return pannelloMappa.scegliCasella(listaCaselle);
	}

	
	/**
	 * Permette la scelta di una mossa da una lista passata come parametro tramite
	 * l'apposito metodo di pannelloBottoni.
	 * 
	 * @param mosseDisponibili lista di mosse tra cui scegliere.
	 * @return mossa scelta.
	 */
	@Override
	public TipoMossa scegliMossa(List<TipoMossa> mosseDisponibili){
		return pannelloBottoni.scegliMossa(mosseDisponibili);
	}

	/**
	 * Permette la scelta di un pastore da una lista passata come parametro tramite
	 * l'apposito metodo di pannelloMappa.
	 * 
	 * @param listaPastori lista di pastori tra cui scegliere.
	 * @return pastore scelto.
	 */
	@Override
	public Pastore scegliPastore(List<Pastore> listaPastori) {
		return pannelloMappa.scegliPastore(listaPastori);
	}

	/**
	 * Permette la scelta di una regione da una lista passata come parametro tramite
	 * l'apposito metodo di pannelloMappa.
	 * 
	 * @param listaRegioni lista di regioni tra cui scegliere.
	 * @return regione scelta.
	 */
	@Override
	public Regione scegliRegione(List<Regione> listaRegioni) {
		return pannelloMappa.scegliRegione(listaRegioni);
	}

	/**
	 * Imposta il giocatore passato da parametro ad essere il giocatore corrente
	 * tramite l'apposito metodo di statoGioco.
	 * 
	 * @param giocatore giocatore da impostare come giocatore corrente
	 */
	@Override
	public void setGiocatoreCorrente(Giocatore giocatore) {
		statoGioco.setGiocatoreCorrente(giocatore);
	}

	/**
	 * Rimuove l'interfaccia giocatore, se esistente(ad esempio alla prima chiamata di questo metodo),
	 * quindi ne crea una nuova e la riaggiunge al pannelloOvest in posizione nord. Rivalida il pannello
	 * per visualizzare i cambiamenti.
	 * 
	 * @param giocatore giocatore del quale si desidera visualizzare l'interfaccia.
	 */
	@Override
	public void setInterfacciaGiocatore(Giocatore giocatore) {
		if (interfacciaGiocatore != null) {
			pannelloOvest.remove(interfacciaGiocatore);
		}
		interfacciaGiocatore = new InterfacciaGiocatore(giocatore);
		pannelloOvest.add(interfacciaGiocatore,BorderLayout.NORTH);
		pannelloOvest.revalidate();
	}

	
	/**
	 * Stampa un messaggio a schermo creando l'apposita classe FinestraMessaggi.
	 * 
	 * @param messaggio messaggio da visualizzare.
	 */
	@Override
	public void stampaMessaggio(String messaggio){
		new FinestraMessaggi(messaggio,finestraGioco);
	}

	/**
	 * Crea una istanzaView della carta ricevuta per chiamare il metodo di FinestraCarte
	 * scegliCostoCarta il quale attende che l'utente scelga il costo al quale vendere
	 * la propria carta. Una volta ricevuto l'input chiude la finestra e torna il costo scelto.
	 * 
	 * @param carta carta del quale si vuole scegliere il costo.
	 * @return costo scelto.
	 */
	@Override
	public int scegliCostoCarta(Carta carta) {
		CartaView cartaView = istanzeView.cercaCartaView(carta);
		int costoCarta = finestraCarteDaVendere.scegliCostoCarta(cartaView);
		finestraCarteDaVendere.dispose();
		return costoCarta;
	}

	
	/**
	 * Permette la scelta di un carta da vendere da una lista passata aprendo l'apposita finestraCarte.
	 * La FinestraCarte prende come parametro una lista di CarteView, quindi prima di aprirla
	 * cerca le istanze degli carte tramite il metodo di IstanzeView. Quindi crea la finestra carte
	 * passando come parametro di compravendita true, poichè siamo nel caso di vendita carte,
	 * e tramite il suo metodo scegliCarta si mette in attesa che l'utente  scelga l'carta.
	 * Se la carta scelta è null allora significa che l'utente ha premuto il pulsante di conferma
	 * e la finestra viene chiusa.
	 * 
	 * @param listaCarte lista di carte tra i quali scegliere.
	 * @return carta scelta.
	 */
	@Override
	public Carta scegliCartaDavendere(List<Carta> carteVendibili) {
		List<CartaView> listaCarteView = cercaCarteView(carteVendibili);
		finestraCarteDaVendere = new FinestraCarte(listaCarteView,finestraGioco,true);
		Carta cartaScelta = finestraCarteDaVendere.scegliCarta();
		if (cartaScelta == null) {
			finestraCarteDaVendere.dispose();
		}
		return cartaScelta;
	}

	
	/**
	 * Scorre la lista di carte ricevuta e per ognuna di esse torna il suo corrispettivo
	 * view utilizzando l'apposito metodo di IstanzeView.
	 * 
	 * @param listaCarte lista di carte delle quali si vuole ottenere la view.
	 * @return lista di carteView corrispondente alla lista di carte ricevuta.
	 */
	private List<CartaView> cercaCarteView(List<Carta> listaCarte) {
		List<CartaView> listaCarteView = new ArrayList<CartaView>();
		Iterator<Carta> scorriCarte = listaCarte.iterator();
		while (scorriCarte.hasNext()){
			Carta carta = scorriCarte.next();
			listaCarteView.add(istanzeView.cercaCartaView(carta));
		}
		return listaCarteView;
	}

	/**
	 * Crea una nuova FinestraCarteInVendita con la lista ricevuta da parametro, quindi 
	 * chiama il suo metodo scegliCarta che aspetta che l'utente scelga una carta, quindi
	 * chiude la finestra e torna la carta scelta.
	 * 
	 * @param listaCarteInVendita lista di carte in vendita tra le quali scegliere.
	 * @param giocatore parametro non utilizzato ma utile in altre implementazioni di InterfacciaView
	 * 		per il gioco online.
	 * 
	 * @return la carta in vendita scelta dall'utente.
	 */
	@Override
	public CartaInVendita scegliCartaDaComprare(List<CartaInVendita> listaCarteInVendita, Giocatore giocatore) {
		FinestraCarteInVendita finestraCarteInVendita = new FinestraCarteInVendita(listaCarteInVendita, istanzeView, finestraGioco);
		CartaInVendita cartaScelta = finestraCarteInVendita.scegliCarta();
		finestraCarteInVendita.dispose();
		return cartaScelta;
	}

	/**
	 * Rimuove le carte dall'interfaccia giocatore tramite l'apposito metodo.
	 */
	@Override
	public void rimuoviCarteView() {
		interfacciaGiocatore.rimuoviCarteView();
	}

	/**
	 * Aggiunge una nuova istanza di pastoreView in istanzeView tramite l'apposito metodo.
	 * 
	 * @param pastore pastore di cui si vuole creare una nuova istanza view.
	 */
	@Override
	public void aggiungiPastoreView(Pastore pastore) {
		istanzeView.aggiungiPastoreView(pastore);
	}

	/**
	 * Metodo non utilizzato ma utile in altre implementazioni di InterfacciaView.
	 */
	@Override
	public void run() {
	}

}
