package controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import model.Ariete;
import model.Carta;
import model.Costanti;
import model.Giocatore;
import model.Lupo;
import model.Mappa;
import model.Pecora;
import model.PecoraNera;
import model.Regione;
import model.StatoGioco;
import model.TipoGioco;
import model.TipoPartitaInRete;
import model.TipoRete;
import model.TipoTerreno;
import socketClient.ClientApplicationSocket;
import socketServer.ServerApplicationSocket;
import view.FinestraNotifica;
import view.InterfacciaView;
import view.Menu;
import view.SheeplandView;
import RMIClient.ClientApplicationRMI;
import RMIServer.ServerApplicationRMI;

/**
 * Classe, contenente il metono principale, che fa partire il gioco, e 
 * attuando le scelte effettuate tramite il menu (una sorta di view temporanea),
 * inizializza la partita stessa.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class InizializzaGioco {

	private TipoGioco tipoGioco;
	private int numeroGiocatori;
	private TipoRete tipoRete;
	private TipoPartitaInRete tipoPartitaInRete;
	private InterfacciaView view;
	private Mappa mappa;
	private StatoGioco statoGioco;
	private String indirizzoIP;

	/**
	 * metodo main, che fa partire il costruttore di questa stessa classe.
	 * @param args paramentro non utilizzato
	 */
	public static void main(String[] args) {
		new InizializzaGioco();	
	}

	/**
	 * Costruttore della classe. Crea il menu e attraverso di esso
	 * determina, con l'input del giocatore, il tipo di gioco, il numero di 
	 * giocatori, e, se il gioco è online, le modalità con cui si svolge.
	 * Dopodichè, crea il nuovo StatoGioco e chiama la vera e propria 
	 * inizializzazione del gioco, che sfrutterà i parametri appena ricevuti.
	 */
	public InizializzaGioco(){
		Menu menu = new Menu();
		tipoGioco = menu.scegliTipoGioco();
		numeroGiocatori = menu.getNumeroGiocatori();
		if (tipoGioco == TipoGioco.ONLINE){
			tipoRete = menu.scegliTipoRete();
			tipoPartitaInRete = menu.scegliHostOClient();
			if (tipoPartitaInRete == TipoPartitaInRete.CLIENT){
				indirizzoIP = menu.digitaIP();
			}else{
				try {
					indirizzoIP = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
					new FinestraNotifica("Host sconosciuto", e);
				}
			}
		}
		menu.dispose();
		statoGioco = new StatoGioco(numeroGiocatori);
		inizializzaGioco();
		try {
			scegliTipoPartita();
		} catch (UnknownHostException e) {
			new FinestraNotifica("Host Sconosciuto", e);
		}
	}

	/**
	 * Fa partire il tipo di partita corrispondente alle
	 * scelte fatte precedentemente.
	 * @throws UnknownHostException in caso di gioco online, se la 
	 *		   connessione non è stata stabilita.
	 */
	private void scegliTipoPartita() throws UnknownHostException {
		if (tipoGioco == TipoGioco.ONLINE){
			if (tipoRete == TipoRete.RMI){
				iniziaPartitaRMI();
			}else{
				iniziaPartitaSocket();
			}
		}else{
			iniziaPartitaOffline();
		}
	}

	/**
	 * Fa partire il gioco in locale, creando la view e il controller,
	 * e impostando l'interfaccia del giocatore corrente.
	 */
	private void iniziaPartitaOffline() {
		view = new SheeplandView(statoGioco);
		view.setInterfacciaGiocatore(statoGioco.getGiocatoreCorrente());
		SheeplandController.creaController(statoGioco,view);
	}

	/**
	 * Fa partire il gioco online in Socket. Se si è scelto di ospitare la
	 * partita, parte il server, oltre che un client; se no, solo un client.
	 * @throws UnknownHostException in caso di errore di rete.
	 */
	private void iniziaPartitaSocket() throws UnknownHostException {
		ClientApplicationSocket client;
		if (tipoPartitaInRete == TipoPartitaInRete.HOST){
			indirizzoIP = InetAddress.getLocalHost().getHostAddress();
			view = new ServerApplicationSocket(statoGioco);
			Thread serverThread = new Thread(view);
			serverThread.start();
			client = new ClientApplicationSocket(indirizzoIP);
			new FinestraNotifica("Indirizzo IP del server: "+InetAddress.getLocalHost().getHostAddress());
			client.iniziaRicezioneMosse();
		}
		else{
			client = new ClientApplicationSocket(indirizzoIP);
			client.iniziaRicezioneMosse();
		}
	}

	/**
	 * Fa partire il gioco online in Socket. Se si è scelto di ospitare la
	 * partita, parte il server, oltre che un client; se no, solo un client.
	 * @throws UnknownHostException in caso di errore di rete.
	 */
	private void iniziaPartitaRMI() throws UnknownHostException {
		ClientApplicationRMI client;
		try {
			if (tipoPartitaInRete == TipoPartitaInRete.HOST){
				indirizzoIP = InetAddress.getLocalHost().getHostAddress();
				view = new ServerApplicationRMI(statoGioco);
				Thread serverThread = new Thread(view);
				serverThread.start();
				client = new ClientApplicationRMI(indirizzoIP);
				new FinestraNotifica("Indirizzo IP del server: "+ indirizzoIP);
				client.iniziaRicezioneMosse();
			}
			else{
				client = new ClientApplicationRMI(indirizzoIP);
				client.iniziaRicezioneMosse();
			}
		} catch (RemoteException e) {
			new FinestraNotifica("Creazione connessione RMI fallita: l'indirizzo " + indirizzoIP +" non è quello del server "
					+ "o il server non è ancora stato creato", e);
		}
	}

	/**
	 * La vera e propria inizializzazione del gioco: viene creata la mappa,
	 * estraendola dal file XML, con tutti i suoi attributi: carte, giocatori,
	 * animali. Infine viene impostato il giocatore 1 come giocatore corrente.
	 */
	private void inizializzaGioco(){
		XMLParser parser = new XMLParser(Costanti.PERCORSO_FILE_XML);
		mappa = parser.getMappa();
		//creiamo la mappa estratta dal file XML tramite il parser
		statoGioco.setMappa(mappa); 
		creaCarte();
		creaGiocatori(numeroGiocatori);
		distribuisciCartaIniziale();
		posizionaPecoreArieti();
		posizionaLupo();
		posizionaPecoraNera();
		statoGioco.setGiocatoreCorrente(statoGioco.getListaGiocatori().get(0));
	}

	/**
	 * posiziona la pecora nera.
	 */
	private void posizionaPecoraNera() {
		Iterator<Regione> iterator = mappa.getListaRegioni().iterator();
		while (iterator.hasNext()){
			Regione regioneCorrente = iterator.next();
			if (regioneCorrente.getTipo() == TipoTerreno.SHEEPSBURG){
				statoGioco.setPecoraNera(new PecoraNera(regioneCorrente));
				return;
			}
		}
	}

	/**
	 * posiziona pecore e arieti.
	 */
	private void posizionaPecoreArieti() {
		Iterator<Regione> iterator = mappa.getListaRegioni().iterator();
		while (iterator.hasNext()){
			Regione regioneCorrente = iterator.next();
			double numeroCasuale = Math.random();
			if (regioneCorrente.getTipo() != TipoTerreno.SHEEPSBURG) {
				if (numeroCasuale < 0.5 ){ 
					//creiamo una pecora o un ariete con probabilità 1/2
					new Pecora(regioneCorrente);
				}else{
					new Ariete(regioneCorrente);
				}
			}
		}
	}

	/**
	 * posiziona il lupo.
	 */
	private void posizionaLupo() {
		int numeroCasuale = (int) (Math.random() * mappa.getListaRegioni().size());
		Regione regione = mappa.getListaRegioni().get(numeroCasuale);
		statoGioco.setLupo(new Lupo(regione));
	}

	/**
	 * crea le carte (una per tipo di terreno: vedi classe Carta).
	 */
	private void creaCarte() {
		for (int i = 0; i < Costanti.NUMERO_TERRENI; i++){
			statoGioco.getListaCarte().add(new Carta(TipoTerreno.values()[i]));
		}
	}

	/**
	 * crea i giocatori: se sono due, avranno due pastori a testa, se no
	 * solo uno.
	 * @param numeroGiocatori il numero di giocatori da creare.
	 */
	private void creaGiocatori(int numeroGiocatori){
		if (numeroGiocatori == 2){
			for (int i = 1; i <= numeroGiocatori; i++){
				Giocatore giocatore = new Giocatore(Costanti.DANARI_INIZIALI_2_GIOCATORI,i);
				statoGioco.getListaGiocatori().add(giocatore);
				for (int j = 0; j < Costanti.PASTORI_PER_2_GIOCATORI; j++) {
					statoGioco.getListaPastori().add(giocatore.creaNuovoPastore());
				}
			}
		}else{
			for (int i = 1; i <= numeroGiocatori; i++){
				Giocatore giocatore = new Giocatore(Costanti.DANARI_INIZIALI,i);
				statoGioco.getListaPastori().add(giocatore.creaNuovoPastore());
				statoGioco.getListaGiocatori().add(giocatore);
			}
		}
	}

	/**
	 * distribuisci ad ogni giocatore una carta gratuita, estratta a caso.
	 */
	private void distribuisciCartaIniziale() {
		Iterator<Giocatore> iterator = statoGioco.getListaGiocatori().iterator();
		Set<Carta> carteDistribuite = new HashSet<Carta>();
		while (iterator.hasNext()){
			Giocatore giocatoreCorrente = iterator.next();
			int numeroCasuale;
			do{
				numeroCasuale = (int) (Math.random() * statoGioco.getListaCarte().size());
			}
			while (carteDistribuite.contains(statoGioco.getListaCarte().get(numeroCasuale)));
			carteDistribuite.add(statoGioco.getListaCarte().get(numeroCasuale));
			giocatoreCorrente.aggiungiCarta(statoGioco.getListaCarte().get(numeroCasuale));
		}
	}
}
