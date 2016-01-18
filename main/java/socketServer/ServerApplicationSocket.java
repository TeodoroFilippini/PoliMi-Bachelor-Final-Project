package socketServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
 * Interfaccia di comunicazione Socket tra il controller, caricato
 * solo sul server, e le view, caricate sui vari client.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class ServerApplicationSocket implements InterfacciaView{
	private static final String CLASSE_OGGETTO_NON_RICONOSCIUTA = "classe dell'oggetto arrivato dal client non riconosciuta";
	private static final long serialVersionUID = 1L;
	private static final String ERRORE_INVIO_MOSSA = "errore durante l'invio della mossa al client";
	private boolean aspettandoConnessioni = true;
	private List<Socket> listaClientSocket = new ArrayList<Socket>();
	private List<ObjectInputStream> listaInput = new ArrayList<ObjectInputStream>();
	private List<ObjectOutputStream> listaOutput = new ArrayList<ObjectOutputStream>();
	private StatoGioco statoGioco;
	private int numeroGiocatori;

	/**
	 * Costruttore della classe.
	 * @param statoGioco lo stato del gioco attuale.
	 */
	public ServerApplicationSocket(StatoGioco statoGioco){
		this.statoGioco = statoGioco;
		this.numeroGiocatori = statoGioco.getNumeroGiocatori();
	}

	/**
	 * Invia la mossa effettuata in broadcast a tutti 
	 * i client.
	 * @param animale l'animale da aggiungere alla regione.
	 * @param regione la regione cui aggiungere l'animale.
	 */
	@Override
	public void aggiungiAnimaleView(Animale animale, Regione regione) {
		inviaOggettoBroadcast(new MossaAggiungiAnimale(animale,regione));
	}

	/**
	 * Invia la mossa effettuata in broadcast a tutti 
	 * i client.
	 * @param carta la carta da aggiungere.
	 * @param giocatore il giocatore cui assegnare la carta.
	 */
	@Override
	public void aggiungiCarta(Carta carta,Giocatore giocatore) {
		inviaOggettoBroadcast(new MossaCompraCarta(carta,giocatore));
	}

	/**
	 * Invia la mossa effettuata in broadcast a tutti 
	 * i client.
	 * @param giocatoreCorrotto giocatore che prende i soldi della corruzione
	 * 	      al'altro.
	 * @param giocatoreCorruttore giocatore che ha effettuato la mossa sparatoria
	 * 		  e deve corrompere l'altro.
	 */
	@Override
	public void corrompi(Giocatore giocatoreCorrotto, Giocatore giocatoreCorruttore) {
		inviaOggettoBroadcast(new MossaCorruzione(giocatoreCorrotto,giocatoreCorruttore));
	}

	/**
	 * Invia la mossa effettuata in broadcast a tutti 
	 * i client.
	 */
	@Override
	public void disabilitaBottoni() {
		inviaOggettoBroadcast(new MossaSaltaTurno());
	}

	/**
	 * @return il giocatore corrente, preso dallo stato del gioco
	 */
	@Override
	public Giocatore getGiocatore() {
		return statoGioco.getGiocatoreCorrente();
	}

	/**
	 * Metodo che invia in broadcast l'oggetto ricevuto: scorre la
	 * lista degli outputstream e manda su ognuno di essi l'oggetto
	 * in questione, dopon aver resettato lo stream, azione rivelatasi necessaria
	 * per cancellare i riferimenti a vecchi oggetti inviati.
	 * @param oggetto l'oggetto da inviare in broadcast ai client.
	 */
	private void inviaOggettoBroadcast(Object oggetto){
		Iterator<ObjectOutputStream> scorriOutput = listaOutput.iterator();
		while(scorriOutput.hasNext()){
			ObjectOutputStream output = scorriOutput.next();
			try {
				output.reset();
				output.writeObject(oggetto);
			} catch (IOException e) {
				new FinestraNotifica("Errore durante l'invio di oggetti ai client", e);
			}
		}
	}

	/**
	 * Invia la mossa effettuata in broadcast a tutti 
	 * i client.
	 * @param animale l'animale da spostare.
	 * @param arrivo la regione in cui spostare l'animale,
	 */
	@Override
	public void muoviAnimale(Animale animale, Regione arrivo) {
		inviaOggettoBroadcast(new MossaSpostaPecora(animale,arrivo));
	}

	/**
	 * Invia la mossa effettuata in broadcast a tutti 
	 * i client.
	 * @param regione la regione in cui arriverà il lupo. 
	 * @param dado il valore dell'esito del dado, da 1 a 6.
	 */
	@Override
	public void muoviLupo(Regione regione, int dado) {
		inviaOggettoBroadcast(new MossaSpostaLupo(regione,dado));
	}

	/**
	 * Invia la mossa effettuata in broadcast a tutti 
	 * i client.
	 * @param pastore il pastore da spostare.
	 * @param arrivo la casella in cui spostare il pastore.
	 */
	@Override
	public void muoviPastore(Pastore pastore, Casella arrivo) {
		inviaOggettoBroadcast(new MossaMuoviPastore(pastore,arrivo));
	}

	/**
	 * Invia la mossa effettuata in broadcast a tutti 
	 * i client.
	 * @param regione la regione dove spostare la pecora nera.
	 * @param dado il valore dell'esito del dado, da 1 a 6.
	 */
	@Override
	public void muoviPecoraNera(Regione regione, int dado) {
		inviaOggettoBroadcast(new MossaSpostaPecoraNera(regione,dado));
	}

	/**
	 * Invia la mossa effettuata in broadcast a tutti 
	 * i client.
	 * @param animale l'animale da eliminare.
	 */
	@Override
	public void rimuoviAnimaleView(Animale animale) {
		inviaOggettoBroadcast(new MossaRimuoviAnimale(animale));
	}

	/**
	 * Esegue la mossa di scelta animale.
	 * @param listaAnimali la lista tra cui scegliere.
	 * @return l'animale da eliminare.
	 */
	@Override
	public Animale scegliAnimale(List<Animale> listaAnimali) {
		int numeroGiocatore = statoGioco.getGiocatoreCorrente().getNumero();
		ObjectOutputStream output = listaOutput.get(numeroGiocatore - 1);
		ObjectInputStream input = listaInput.get(numeroGiocatore - 1);
		try {
			output.reset();
			output.writeObject(new MossaScegliAnimale(listaAnimali));
			Animale animale = (Animale)input.readObject();
			return AdattatoreIstanze.cercaIstanzaAnimale(animale, listaAnimali);
		} catch (IOException e) {
			new FinestraNotifica(ERRORE_INVIO_MOSSA, e);
		} catch (ClassNotFoundException e) {
			new FinestraNotifica(CLASSE_OGGETTO_NON_RICONOSCIUTA, e);
		}
		return null;
	}

	/**
	 * Esegue la mossa di scelta carta.
	 * @param listaCarte la lista tra cui scegliere la carta.
	 * @return la carta scelta.
	 */
	@Override
	public Carta scegliCarta(List<Carta> listaCarte) {
		int numeroGiocatore = statoGioco.getGiocatoreCorrente().getNumero();
		ObjectOutputStream output = listaOutput.get(numeroGiocatore - 1);
		ObjectInputStream input = listaInput.get(numeroGiocatore - 1);
		try {
			output.reset();
			output.writeObject(new MossaScegliCarta(listaCarte));
			Carta carta = (Carta)input.readObject();
			return AdattatoreIstanze.cercaIstanzaCarta(carta, listaCarte);
		} catch (IOException e) {
			new FinestraNotifica(ERRORE_INVIO_MOSSA, e);
		} catch (ClassNotFoundException e) {
			new FinestraNotifica(CLASSE_OGGETTO_NON_RICONOSCIUTA, e);
		}
		return null;
	}

	/**
	 * Esegue la mossa di scelta casella.
	 * @param listaCasella la lista tra cui scegliere la casella.
	 * @return la casella scelta.
	 */
	@Override
	public Casella scegliCasella(List<Casella> listaCaselle) {
		int numeroGiocatore = statoGioco.getGiocatoreCorrente().getNumero();
		ObjectOutputStream output = listaOutput.get(numeroGiocatore - 1);
		ObjectInputStream input = listaInput.get(numeroGiocatore - 1);
		try {
			output.reset();
			output.writeObject(new MossaScegliCasella(listaCaselle));
			Casella casella = (Casella)input.readObject();
			return AdattatoreIstanze.cercaIstanzaCasella(casella, listaCaselle);
		} catch (IOException e) {
			new FinestraNotifica(ERRORE_INVIO_MOSSA, e);
		} catch (ClassNotFoundException e) {
			new FinestraNotifica(CLASSE_OGGETTO_NON_RICONOSCIUTA, e);
		}
		return null;
	}

	/**
	 * Esegue la mossa di scelta mossa.
	 * @param mosseDisponibili la lista tra cui scegliere la mossa.
	 * @return la mossa scelta.
	 */
	@Override
	public TipoMossa scegliMossa(List<TipoMossa> mosseDisponibili) {
		int numeroGiocatore = statoGioco.getGiocatoreCorrente().getNumero();
		int azioniRimanenti = statoGioco.getGiocatoreCorrente().getAzioniRimanentiPerTurno();
		ObjectOutputStream output = listaOutput.get(numeroGiocatore - 1);
		ObjectInputStream input = listaInput.get(numeroGiocatore - 1);
		try {
			output.reset();
			output.writeObject(new MossaScegliMossa(mosseDisponibili, azioniRimanenti));
			return (TipoMossa)input.readObject();
		} catch (IOException e) {
			new FinestraNotifica(ERRORE_INVIO_MOSSA, e);
		} catch (ClassNotFoundException e) {
			new FinestraNotifica(CLASSE_OGGETTO_NON_RICONOSCIUTA, e);
		}
		return null;
	}

	/**
	 * Esegue la mossa di scelta pastore.
	 * @param listaPastori la lista tra cui scegliere il pastore.
	 * @return il pastore scelto.
	 */
	@Override
	public Pastore scegliPastore(List<Pastore> listaPastori) {
		int numeroGiocatore = statoGioco.getGiocatoreCorrente().getNumero();
		ObjectOutputStream output = listaOutput.get(numeroGiocatore - 1);
		ObjectInputStream input = listaInput.get(numeroGiocatore - 1);
		try {
			output.writeObject(new MossaScegliPastore(listaPastori));
			Pastore pastore = (Pastore)input.readObject();
			return AdattatoreIstanze.cercaIstanzaPastore(pastore, listaPastori);
		} catch (IOException e) {
			new FinestraNotifica(ERRORE_INVIO_MOSSA, e);
		} catch (ClassNotFoundException e) {
			new FinestraNotifica(CLASSE_OGGETTO_NON_RICONOSCIUTA, e);
		}
		return null;
	}

	/**
	 * Esegue la mossa di scelta regione.
	 * @param listaRegioni la lista tra cui scegliere la regione.
	 * @return la regione scelta.
	 */
	@Override
	public Regione scegliRegione(List<Regione> listaRegioni) {
		int numeroGiocatore = statoGioco.getGiocatoreCorrente().getNumero();
		ObjectOutputStream output = listaOutput.get(numeroGiocatore - 1);
		ObjectInputStream input = listaInput.get(numeroGiocatore - 1);
		try {
			output.reset();
			output.writeObject(new MossaScegliRegione(listaRegioni));
			Regione regione = (Regione)input.readObject();
			return AdattatoreIstanze.cercaIstanzaRegione(regione, listaRegioni);
		} catch (IOException e) {
			new FinestraNotifica(ERRORE_INVIO_MOSSA, e);
		} catch (ClassNotFoundException e) {
			new FinestraNotifica(CLASSE_OGGETTO_NON_RICONOSCIUTA, e);
		}
		return null;
	}

	/**
	 * Esegue la mossa di scelta costo carta.
	 * @param carta la carta di cui scegliere il costo.
	 * @return il costo selezionato per la carta.
	 */
	@Override
	public int scegliCostoCarta(Carta carta) {
		int numeroGiocatore = statoGioco.getGiocatoreCorrente().getNumero();
		ObjectOutputStream output = listaOutput.get(numeroGiocatore - 1);
		ObjectInputStream input = listaInput.get(numeroGiocatore - 1);
		try {
			output.reset();
			output.writeObject(new MossaScegliCostoCarta(carta));
			return (int)input.readObject();
		} catch (IOException e) {
			new FinestraNotifica(ERRORE_INVIO_MOSSA, e);
		} catch (ClassNotFoundException e) {
			new FinestraNotifica(CLASSE_OGGETTO_NON_RICONOSCIUTA, e);
		}
		return -1;
	}

	/**
	 * Imposta il giocatore corrente.
	 * @param giocatore il giocatore da impostare come corrente.
	 */
	@Override
	public void setGiocatoreCorrente(Giocatore giocatore) {
		statoGioco.setGiocatoreCorrente(giocatore);
		inviaOggettoBroadcast(new MossaIniziaTurno(giocatore));
	}

	/**Questo metodo non viene mai chiamato nel gioco online
	 * visto che l'interfaccia del giocatore non ha bisogno di essere
	 * esplicitamente settata, ma è invece associata ad ogni client in maniera
	 * biunivoca.
	*/
	@Override
	public void setInterfacciaGiocatore(Giocatore giocatore){
	}

	/**
	 * 
	 * @return true se il server è in attesa di connessioni da parte
	 *         di clients, false se no.
	 */
	public boolean staAspettandoConnessioni() {
		return aspettandoConnessioni;
	}

	/**
	 * Invia la mossa effettuata in broadcast a tutti 
	 * i client.
	 * @param messaggio il messaggio da stampare a schermo.
	 */
	@Override
	public void stampaMessaggio(String messaggio) {
		inviaOggettoBroadcast(new MossaMessaggio(messaggio));
	}

	/**
	 * Invia la mossa effettuata in broadcast a tutti 
	 * i client.
	 * @param dado il risultato del lancio del dado della pecora nera.
	 */
	@Override
	public void lanciaDadoPecoraNera(int dado) {
		inviaOggettoBroadcast(new MossaLanciaDadoPecoraNera(dado));
	}

	/**
	 * Effettua la mossa scegli carta da vendere.
	 * @param carteVendibili la lista delle carte che si possono vendere.
	 * @return la carta scelta, oppure una nuova mossaChiudiFinestra se si 
	 * 		   sceglie di chiudere la finestra senza vendere altre carte.
	 */
	@Override
	public Carta scegliCartaDavendere(List<Carta> carteVendibili) {
		int numeroGiocatore = statoGioco.getGiocatoreCorrente().getNumero();
		ObjectOutputStream output = listaOutput.get(numeroGiocatore - 1);
		ObjectInputStream input = listaInput.get(numeroGiocatore - 1);
		try {
			output.reset();
			output.writeObject(new MossaScegliCartaDaVendere(carteVendibili));
			Object oggetto = input.readObject();
			if (oggetto instanceof Carta){
				Carta carta = (Carta)oggetto;
				return AdattatoreIstanze.cercaIstanzaCarta(carta, carteVendibili);
			}
		} catch (IOException e) {
			new FinestraNotifica(ERRORE_INVIO_MOSSA, e);
		} catch (ClassNotFoundException e) {
			new FinestraNotifica(CLASSE_OGGETTO_NON_RICONOSCIUTA, e);
		}
		return null;
	}

	/**
	 * Effettua la mossa scegli carta da comprare.
	 * @param carteInVendita la lista delle carte che si possono vendere.
	 * @param acquirente il giocatore che sta acquistando la carta.
	 * @return la carta scelta, oppure una nuova mossaChiudiFinestra se si 
	 * 		   sceglie di chiudere la finestra senza vendere altre carte.
	 */
	@Override
	public CartaInVendita scegliCartaDaComprare(List<CartaInVendita> listaCarteInVendita, Giocatore acquirente) {
		int numeroGiocatore = acquirente.getNumero();
		ObjectOutputStream output = listaOutput.get(numeroGiocatore - 1);
		ObjectInputStream input = listaInput.get(numeroGiocatore - 1);
		CartaInVendita cartaInVendita = null;
		try {
			output.reset();
			output.writeObject(new MossaScegliCartaDaComprare(listaCarteInVendita, acquirente));
			Object oggetto = input.readObject();
			if (oggetto instanceof CartaInVendita){
				cartaInVendita = (CartaInVendita)oggetto;
				inviaOggettoBroadcast(new MossaVendiCarta(cartaInVendita, acquirente));
				return cartaInVendita;
			}
		} catch (IOException e) {
			new FinestraNotifica(ERRORE_INVIO_MOSSA, e);
		} catch (ClassNotFoundException e) {
			new FinestraNotifica(CLASSE_OGGETTO_NON_RICONOSCIUTA, e);
		}
		return null;
	}

	/**
	 * Invia la rimozione delle carte view in broadcast a tutti 
	 * i client.
	 */
	@Override
	public void rimuoviCarteView() {
		inviaOggettoBroadcast(new MossaRimuoviCarteView());

	}

	/**
	 * Invia la mossa effettuata in broadcast a tutti 
	 * i client.
	 * @param pastore il pastore aggiunto.
	 */
	@Override
	public void aggiungiPastoreView(Pastore pastore) {
		inviaOggettoBroadcast(new MossaAggiungiPastoreView(pastore));
	}

	/**
	 * Metodo che fa partire l'esecuzione del thread. Crea il socket associato
	 * al server e una coppia objectoutputstream-objectinputstream per ogni client.
	 * Infine procede nella creazione del controller.
	 */
	@Override
	public void run() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(Costanti.PORTA_SERVER);
			for (int i = 1; i <= numeroGiocatori ; i++){
				Socket socket = serverSocket.accept();
				ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
				output.writeObject(i);
				output.writeObject(statoGioco);
				if (i != numeroGiocatori) {
					output.writeObject(new MossaMessaggio("Aspetta che tutti i giocatori siano connessi"));
				}
				listaOutput.add(output);
				listaInput.add(input);
				listaClientSocket.add(socket);
			}
		} catch (IOException e) {
			new FinestraNotifica("Errore di connessione ad un client", e);
		}
		SheeplandController.creaController(statoGioco, this);
	}
}