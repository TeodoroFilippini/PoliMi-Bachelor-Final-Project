package RMIServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import model.Animale;
import model.Carta;
import model.CartaInVendita;
import model.Casella;
import model.Costanti;
import model.Giocatore;
import model.Pastore;
import model.Regione;
import model.StatoGioco;
import model.TipoMossa;
import view.FinestraNotifica;
import view.InterfacciaView;
import controller.AdattatoreIstanze;
import controller.Mossa;
import controller.MossaAggiungiAnimale;
import controller.MossaAggiungiPastoreView;
import controller.MossaCompraCarta;
import controller.MossaCorruzione;
import controller.MossaIniziaTurno;
import controller.MossaLanciaDadoPecoraNera;
import controller.MossaMessaggio;
import controller.MossaMuoviPastore;
import controller.MossaRimuoviAnimale;
import controller.MossaRimuoviCarteView;
import controller.MossaSaltaTurno;
import controller.MossaScegliAnimale;
import controller.MossaScegliCarta;
import controller.MossaScegliCartaDaComprare;
import controller.MossaScegliCartaDaVendere;
import controller.MossaScegliCasella;
import controller.MossaScegliCostoCarta;
import controller.MossaScegliMossa;
import controller.MossaScegliPastore;
import controller.MossaScegliRegione;
import controller.MossaSpostaLupo;
import controller.MossaSpostaPecora;
import controller.MossaSpostaPecoraNera;
import controller.MossaVendiCarta;
import controller.SheeplandController;
/**
 * Interfaccia di comunicazione RMI tra il controller (caricato solo sul server) e
 * le view di tutti i vari client. 
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class ServerApplicationRMI extends UnicastRemoteObject implements InterfacciaRMI,InterfacciaView{

	private static final String THREAD_TERMINATO_INASPETTATAMENTE = "il thread in attesa della mossa ha terminato la sua attesa inaspettatamente";

	private static final long serialVersionUID = 1L;
	private StatoGioco statoGioco;
	private Map<Giocatore,BlockingQueue<Mossa>> mosseInCodaGiocatori = new HashMap<Giocatore,BlockingQueue<Mossa>>();
	private BlockingQueue<Object> listaOggettiInCoda = new ArrayBlockingQueue<Object>(30);
	private int numeroGiocatore = 0;

	/**
	 * Carica lo statoGioco utilizzato per inizializzare il gioco dei client.
	 * 
	 * @param statoGioco classe contenente le istanze di gioco da inoltrare ai client
	 * @throws RemoteException eccezione lanciata in caso di problemi nella creazione dell'interfaccia
	 * 		remota.
	 */
	public ServerApplicationRMI(StatoGioco statoGioco) throws RemoteException{
		this.statoGioco = statoGioco;
	}

	/**
	 * Aspetta che tutti i giocatori siano connessi.
	 * 
	 */
	private synchronized void aspettaConnessioni() {
		while (mosseInCodaGiocatori.size() < statoGioco.getNumeroGiocatori()){
			try {
				wait();
			} catch (InterruptedException e) {
				new FinestraNotifica("il thread in attesa delle connessioni ha terminato la sua esecuzione in maniera inavvertita", e);
			}
		}
	}

	/**
	 * Metodo implementato dall'intefaccia di comunicazione remota InterfacciaRMI. viene chiamata dai client
	 * per ricevere la mossa associata al giocatore che la chiama. Per gestire la coda delle mosse utilizza delle
	 * BlockingQueue associate ai vari giocatori in una HashMap.
	 * 
	 * @param giocatore giocatore che invoca il metodo, al quale va inviata la mossa nella coda mosse
	 * 		corrispondente.
	 * @return la prossima mossa in coda nella coda del giocatore.
	 */
	@Override
	public Mossa riceviMossa(Giocatore giocatore){
		Giocatore istanzaGiocatore = AdattatoreIstanze.cercaIstanzaGiocatore(giocatore, statoGioco.getListaGiocatori());
		Mossa mossa = null;
		try {
			mossa = mosseInCodaGiocatori.get(istanzaGiocatore).take();
		} catch (InterruptedException e) {
			new FinestraNotifica("Il thread in attesa della mossa ha interrotto inaspettatamente la sua esecuzione", e);
			e.printStackTrace();
		}
		return mossa;
	}

	/**
	 * Metodo implementato dall'intefaccia di comunicazione remota InterfacciaRMI. Viene chiamato dai
	 * client per caricare lo statoGioco appena stabilita la connessione.
	 * @return lo stato iniziale del gioco.
	 */
	@Override
	public StatoGioco inizializzaGioco(){
		return statoGioco;
	}

	/**
	 * Metodo implementato dall'intefaccia di comunicazione remota InterfacciaRMI. Viene chiamato
	 * nel momento in cui si stabilisce la connessione per stabilire che numero di giocatore è associato
	 * al client che lo chiama. Tale numero viene aumentato ad ogni chiamata di questo metodo.
	 * 
	 * @return numero di giocatore associato al client che lo chiama.
	 * 
	 * @throws eccezione nel caso di problemi di comunicazione.
	 */
	@Override
	public synchronized int getNumeroGiocatore() throws RemoteException {
		Giocatore giocatore = statoGioco.getListaGiocatori().get(numeroGiocatore);
		numeroGiocatore++;
		mosseInCodaGiocatori.put(giocatore, new ArrayBlockingQueue<Mossa>(30));
		if (numeroGiocatore < statoGioco.getNumeroGiocatori()) {
			mosseInCodaGiocatori.get(giocatore).add(new MossaMessaggio("Aspetta che tutti i giocatori siano connessi"));
		}
		notify();
		return giocatore.getNumero();
	}

	/**
	 * Aggiunge la mossa specificata alla coda di tutti i giocatori.
	 * 
	 * @param mossa mossa da aggiungere in coda.
	 */
	private void aggiungiMossaTuttiGiocatori(Mossa mossa){
		Iterator<BlockingQueue<Mossa>> scorriMappa = mosseInCodaGiocatori.values().iterator();
		while (scorriMappa.hasNext()){
			scorriMappa.next().add(mossa);
		}
	}

	/**
	 * Aggiunge la mossa specificata alla coda del giocatore corrente.
	 * 
	 * @param mossa mossa da aggiungere in coda.
	 */
	private void aggiungiMossaGiocatoreCorrente (Mossa mossa){
		Giocatore giocatore = statoGioco.getGiocatoreCorrente();
		mosseInCodaGiocatori.get(giocatore).add(mossa);
	}

	/**
	 * Rimuove la coda mosse associata al giocatore specificato.
	 * 
	 * @param giocatore giocatore di cui si vuole rimuovere la coda.
	 */
	@Override
	public void rimuoviGiocatore(Giocatore giocatore) {
		mosseInCodaGiocatori.remove(giocatore);
	}

	/**
	 * Aggiunge una nuova mossa aggiungi animale alla coda di tutti i giocatori.
	 * 
	 * @param animale animale di cui si vuole creare una nuova istanza view.
	 * @param regione regione alla quale si vuole aggiungere l'animale.
	 */
	@Override
	public void aggiungiAnimaleView(Animale animale, Regione regione) {
		aggiungiMossaTuttiGiocatori(new MossaAggiungiAnimale(animale, regione));
	}

	/**
	 * Aggiunge una nuova mossa aggiungi carta alla coda di tutti i giocatori.
	 * 
	 * @param carta carta da aggiungere.
	 * @param giocatore giocatore al quale si vuole aggiungere la carta.
	 */
	@Override
	public void aggiungiCarta(Carta carta, Giocatore giocatore) {
		aggiungiMossaTuttiGiocatori(new MossaCompraCarta(carta, giocatore));
	}

	/**
	 * Aggiunge una nuova mossa corruzione alla coda di tutti i giocatori.
	 * 
	 * @param giocatoreCorrotto giocatore che viene corrotto.
	 * @param giocatoreCorruttore giocatore che deve corrompere.
	 */
	@Override
	public void corrompi(Giocatore giocatoreCorrotto, Giocatore giocatoreCorruttore) {
		aggiungiMossaTuttiGiocatori(new MossaCorruzione(giocatoreCorrotto, giocatoreCorruttore));
	}

	/**
	 * Aggiunge una nuova mossa salta turno alla coda di tutti i giocatori.
	 */
	@Override
	public void disabilitaBottoni() {
		aggiungiMossaTuttiGiocatori(new MossaSaltaTurno());
	}

	/**
	 * @return giocatore corrente.
	 */
	@Override 
	public Giocatore getGiocatore() {
		return statoGioco.getGiocatoreCorrente();
	}

	/**
	 * Aggiunge una nuova mossa muovi animale alla coda di tutti i giocatori.
	 * 
	 * @param animale animale da muovere.
	 * @param regione regione in cui si vuole muovere l'animale.
	 */
	@Override
	public void muoviAnimale(Animale animale, Regione arrivo) {
		aggiungiMossaTuttiGiocatori(new MossaSpostaPecora(animale, arrivo));
	}

	/**
	 * Aggiunge una nuova mossa muovi lupo alla coda di tutti i giocatori.
	 * 
	 * @param regione regione d'arrivo del lupo.
	 * @param dado risultato del dado che ha generato il movimento del lupo.
	 */
	@Override
	public void muoviLupo(Regione arrivo, int dado) {
		aggiungiMossaTuttiGiocatori(new MossaSpostaLupo(arrivo, dado));
	}

	/**
	 * Aggiunge una nuova mossa muovi pastore alla coda di tutti i giocatori.
	 * 
	 * @param pastore pastore che si vuole muovere.
	 * @param arrivo casella in cui si vuole muovere il pastore.
	 */
	@Override
	public void muoviPastore(Pastore pastore, Casella arrivo) {
		aggiungiMossaTuttiGiocatori(new MossaMuoviPastore(pastore, arrivo));
	}

	/**
	 * Aggiunge una nuova mossa sposta pecora nera alla coda di tutti i giocatori.
	 * 
	 * @param arrivo regione d'arrivo della pecora nera.
	 * @param dado risultato del dado che ha generato il movimento della pecora nera.
	 */
	@Override
	public void muoviPecoraNera(Regione arrivo, int dado) {
		aggiungiMossaTuttiGiocatori(new MossaSpostaPecoraNera(arrivo, dado));
	}

	/**
	 * Aggiunge una nuova mossa rimuovi animale view alla coda di tutti i giocatori.
	 * 
	 * @param animale animale di cui si vuole rimuovere la corrispondente istanza view.
	 */
	@Override
	public void rimuoviAnimaleView(Animale animale) {
		aggiungiMossaTuttiGiocatori(new MossaRimuoviAnimale(animale));
	}

	/**
	 * Aggiunge una nuova mossa scegli animale al giocatore corrente e aspetta che l'animale 
	 * scelto venga aggiunto alla lista oggetti in coda da parte del client.
	 * 
	 * @param listaAnimali lista di animali tra cui scegliere.
	 * @return animale scelto.
	 */
	@Override
	public Animale scegliAnimale(List<Animale> listaAnimali){
		Animale animale = null;
		aggiungiMossaGiocatoreCorrente(new MossaScegliAnimale(listaAnimali));
		try {
			animale = (Animale)listaOggettiInCoda.take();
		} catch (InterruptedException e) {
			new FinestraNotifica(THREAD_TERMINATO_INASPETTATAMENTE, e);
		}
		return AdattatoreIstanze.cercaIstanzaAnimale(animale, listaAnimali);
	}

	/**
	 * Aggiunge una nuova mossa scegli carta al giocatore corrente e aspetta che la carta 
	 *  scelta venga aggiunta alla lista oggetti in coda da parte del client.
	 *  
	 *  @param listaCarte lista di carte tra cui scegliere.
	 *  @return carta scelta.
	 */
	@Override
	public Carta scegliCarta(List<Carta> listaCarte){
		aggiungiMossaGiocatoreCorrente(new MossaScegliCarta(listaCarte));
		Carta carta = null;
		try {
			carta = (Carta)listaOggettiInCoda.take();
		} catch (InterruptedException e) {
			new FinestraNotifica(THREAD_TERMINATO_INASPETTATAMENTE, e);
		}
		return AdattatoreIstanze.cercaIstanzaCarta(carta, listaCarte);
	}

	/**
	 * Aggiunge una nuova mossa scegli casella al giocatore corrente e aspetta che la casella 
	 *  scelta venga aggiunta alla lista oggetti in coda da parte del client.
	 *  
	 *  @param listaCaselle lista di caselle tra cui scegliere.
	 *  @return casella scelta.
	 */
	@Override
	public Casella scegliCasella(List<Casella> listaCaselle){
		Casella casella = null;
		aggiungiMossaGiocatoreCorrente(new MossaScegliCasella(listaCaselle));
		try {
			casella = (Casella)listaOggettiInCoda.take();
		} catch (InterruptedException e) {
			new FinestraNotifica(THREAD_TERMINATO_INASPETTATAMENTE, e);
		}
		return AdattatoreIstanze.cercaIstanzaCasella(casella, listaCaselle);
	}

	/**
	 * Aggiunge una nuova mossa scegli mossa al giocatore corrente e aspetta che la mossa 
	 *  scelta venga aggiunta alla lista oggetti in coda da parte del client.
	 *  
	 *  @param mosseDisponibli lista di mosse tra cui scegliere.
	 *  @return mossa scelta.
	 */
	@Override
	public TipoMossa scegliMossa(List<TipoMossa> mosseDisponibili){
		TipoMossa mossa = null;
		int azioniRimanenti = statoGioco.getGiocatoreCorrente().getAzioniRimanentiPerTurno();
		aggiungiMossaGiocatoreCorrente(new MossaScegliMossa(mosseDisponibili, azioniRimanenti));
		try {
			mossa = (TipoMossa)listaOggettiInCoda.take();
		} catch (InterruptedException e) {
			new FinestraNotifica(THREAD_TERMINATO_INASPETTATAMENTE, e);
		}
		return mossa;
	}

	/**
	 * Aggiunge una nuova mossa scegli pastore al giocatore corrente e aspetta che il pastore 
	 *  scelto venga aggiunto alla lista oggetti in coda da parte del client.
	 *  
	 *  @param listaPastori lista di pastori tra cui scegliere
	 *  @return pastore scelto.
	 */
	@Override
	public Pastore scegliPastore(List<Pastore> listaPastori) {
		Pastore pastore = null;
		aggiungiMossaGiocatoreCorrente(new MossaScegliPastore(listaPastori));
		try {
			pastore = (Pastore)listaOggettiInCoda.take();
		} catch (InterruptedException e) {
			new FinestraNotifica(THREAD_TERMINATO_INASPETTATAMENTE, e);
		}
		return AdattatoreIstanze.cercaIstanzaPastore(pastore, listaPastori);
	}

	/**
	 * Aggiunge una nuova mossa scegli regione al giocatore corrente e aspetta che la regione 
	 *  scelta venga aggiunta alla lista oggetti in coda da parte del client.
	 *  
	 *  @param listaRegioni lista di regioni tra cui scegliere.
	 *  @return regione scelta.
	 */
	@Override
	public Regione scegliRegione(List<Regione> listaRegioni){
		Regione regione = null;
		aggiungiMossaGiocatoreCorrente(new MossaScegliRegione(listaRegioni));
		try {
			regione = (Regione) listaOggettiInCoda.take();
		} catch (InterruptedException e) {
			new FinestraNotifica(THREAD_TERMINATO_INASPETTATAMENTE, e);
		}
		return AdattatoreIstanze.cercaIstanzaRegione(regione, listaRegioni);
	}

	/**
	 * Aggiorna lo statoGioco con il nuovo giocatore corrente specificato e aggiunge alla
	 * coda di tutti i giocatori una nuova mossa inizia turno.
	 * 
	 * @param giocatore giocatore che si vuole settare come giocatore corrente.
	 */
	@Override
	public void setGiocatoreCorrente(Giocatore giocatore) { 
		statoGioco.setGiocatoreCorrente(giocatore);
		aggiungiMossaTuttiGiocatori(new MossaIniziaTurno(giocatore));
	}

	/**
	 * Questo metodo non viene mai chiamato nel gioco online
	 * visto che l'interfaccia del giocatore non ha bisogno di essere
	 * esplicitamente settata, ma è invece associata ad ogni client in maniera
	 * biunivoca
	 * */
	@Override
	public void setInterfacciaGiocatore(Giocatore giocatore) {

	}

	/**
	 * Aggiunge una nuova mossa stampa messaggio alla coda di tutti i giocatori.
	 * 
	 * @param messaggio messaggio da visualizzare.
	 */
	@Override
	public void stampaMessaggio(String messaggio) {
		aggiungiMossaTuttiGiocatori(new MossaMessaggio(messaggio));
	}

	/**
	 * Aggiunge una nuova mossa scegli costo carta al giocatore corrente e aspetta che il costo 
	 *  scelto venga aggiunto alla lista oggetti in coda da parte del client.
	 *  
	 *  @param carta carta di cui scegliere il costo.
	 *  @return costo scelto.
	 */
	@Override
	public int scegliCostoCarta(Carta carta){
		int costoCarta = -1;
		aggiungiMossaGiocatoreCorrente(new MossaScegliCostoCarta(carta));
		try {
			costoCarta = (int)listaOggettiInCoda.take();
		} catch (InterruptedException e) {
			new FinestraNotifica(THREAD_TERMINATO_INASPETTATAMENTE, e);
		}
		return costoCarta;
	}

	/**
	 * Metodo implementato dall'intefaccia di comunicazione remota InterfacciaRMI.
	 * Viene chiamato dai client che desiderano tornare un oggetto al server, come ad esempio
	 * nelle mosse scelta. L'oggetto specificato viene aggiunto alla lista oggetti in coda.
	 * 
	 * @param oggetto oggetto da aggiungere alla lista oggetti in coda.
	 */
	@Override
	public void aggiungiOggettoInCoda(Object oggetto) {
		listaOggettiInCoda.add(oggetto);
	}

	/**
	 * Aggiunge una nuova mossa lancia dado pecora nera alla coda di tutti i giocatori.
	 * 
	 * @param dado faccia da visualizzare alla fine dell'animazione del dado.
	 */
	@Override
	public void lanciaDadoPecoraNera(int dado) {
		aggiungiMossaTuttiGiocatori(new MossaLanciaDadoPecoraNera(dado));
	}

	/**
	 * Aggiunge una nuova mossa scegli carta da vendere al giocatore corrente e aspetta che la carta scelta
	 *  venga aggiunta alla lista oggetti in coda da parte del client. Viene aggiunta in coda una mossa
	 *  chiudi finestra nel caso si voglia interrompere la vendita delle carte.
	 *  
	 *  @param carteVendibili lista di carte tra cui scegliere.
	 *  @return carta scelta.
	 */
	@Override
	public Carta scegliCartaDavendere(List<Carta> carteVendibili) {
		aggiungiMossaGiocatoreCorrente(new MossaScegliCartaDaVendere(carteVendibili));
		Carta carta = null;
		try {
			Object oggetto = listaOggettiInCoda.take();
			if (oggetto instanceof Carta) {
				carta = (Carta) oggetto;
			} else {
				return null;
			}
		} catch (InterruptedException e) {
			new FinestraNotifica(THREAD_TERMINATO_INASPETTATAMENTE, e);
		}
		return AdattatoreIstanze.cercaIstanzaCarta(carta, carteVendibili);
	}

	/**
	 * Aggiunge una nuova mossa scegli carta da comprare al giocatore corrente e aspetta che la carta in vendita scelta
	 * venga aggiunta alla lista oggetti in coda da parte del client. Viene aggiunta in coda una mossa
	 * chiudi finestra nel caso si voglia interrompere l'acuisto delle carte.
	 *  
	 *  @param listaCarteInVendita lista di carte in vendita tra cui scegliere.
	 *  @param acquirente giocatore che può scegliere la carta da comprare.
	 *  @return carta in vendita scelta.
	 */
	@Override
	public CartaInVendita scegliCartaDaComprare(List<CartaInVendita> listaCarteInVendita, Giocatore acquirente) {
		mosseInCodaGiocatori.get(acquirente).add(new MossaScegliCartaDaComprare(listaCarteInVendita,acquirente));
		CartaInVendita cartaInVendita = null;
		try {
			Object oggetto = listaOggettiInCoda.take();
			if (oggetto instanceof CartaInVendita) {
				cartaInVendita = (CartaInVendita) oggetto;
			} else {
				return null;
			}

		} catch (InterruptedException e) {
			new FinestraNotifica(THREAD_TERMINATO_INASPETTATAMENTE, e);
		}
		aggiungiMossaTuttiGiocatori(new MossaVendiCarta(cartaInVendita,acquirente));
		return cartaInVendita;
	}

	/**
	 * Aggiunge una nuova mossa rimuovi carte view nera alla coda di tutti i giocatori.
	 * 
	 */
	@Override
	public void rimuoviCarteView() {
		aggiungiMossaTuttiGiocatori(new MossaRimuoviCarteView());

	}
	
	/**
	 * Aggiunge una nuova mossa aggiungi pastore view alla coda di tutti i giocatori.
	 * 
	 * @param pastore pastore di cui si desidera creare una nuova istanza view.
	 */
	@Override
	public void aggiungiPastoreView(Pastore pastore) {
		aggiungiMossaTuttiGiocatori(new MossaAggiungiPastoreView(pastore));
	}

	/**
	 * Codice eseguito dal thread del server. Crea il registry e aspetta che tutti i giocatori 
	 * si connettano, dopo di che crea il controller imponendo la sua view ad essere questa interfaccia
	 * di comunicazione (il controller non deve sapere con quale view sta comunicando)
	 */
	@Override
	public void run() {
		try {
			Registry registry = LocateRegistry.createRegistry(Costanti.PORTA_SERVER);
			registry.rebind(Costanti.NOME_SERVER, this);
		} catch (RemoteException e) {
			new FinestraNotifica("Errore di connessione remota", e);
		}
		aspettaConnessioni();		
		SheeplandController.creaController(statoGioco, this);
	}

}
