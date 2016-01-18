package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Agnello;
import model.Animale;
import model.Ariete;
import model.Carta;
import model.CartaInVendita;
import model.Casella;
import model.Costanti;
import model.Giocatore;
import model.Lupo;
import model.Mappa;
import model.Pastore;
import model.Pecora;
import model.PecoraNera;
import model.Regione;
import model.StatoGioco;
import model.TipoTerreno;
import view.InterfacciaView;

/**
 * La classe Singleton SheeplandController rappresenta la classe principale 
 * del package "controller", che gestisce, appena dopo l'inizializzazione
 * del gioco, il flusso di tutta la partita. In caso di gioco online,
 * solo il server avrà un controller. 
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */

public class SheeplandController {
	
	private Mappa mappa;
	private StatoGioco statoGioco;
	private InterfacciaView view;
	private static SheeplandController istanza = null;

	/**
	 * Costruttore privato dell classe, che presi la view e
	 * lo stato del gioco, fa iniziare la partita.
	 * @param statoGioco stato del gioco appena creato 
	 * 		   dall'inizializzaGioco.
	 * @param view interfaccia di vista con cui il 
	 * 		  controller comunica, che poi implementerà 
	 * 		  la vista online o offline.
	 */
	private SheeplandController(StatoGioco statoGioco,InterfacciaView view) {
		this.statoGioco = statoGioco;
		this.view = view;
		mappa = statoGioco.getMappa();
		loopGioco();
	}

	/**
	 * Metodo che impedisce la creazione di più di un istanza
	 * di questa classe, rendendola "Singleton".
	 * @param statoGioco lo stato del gioco creato in inizializzazione.
	 * @param view la vista creata in inizializzazione.
	 * @return lo sheeplandcontroller creato se non ne esisteva già
	 * 		   uno; null se no.
	 */
	public static SheeplandController creaController(StatoGioco statoGioco, InterfacciaView view) {
		if (istanza == null){
			return new SheeplandController(statoGioco, view);
		}else{
			return null;
		}
	}

	/**
	 * Calcola il vincitore della partita. Applicando le regole del gioco, una semplice somma
	 * permette di scoprire chi avrà più denari alla fine. In caso di pareggio, i pareggianti
	 * avranno vinto a pari merito.
	 * @return la lista dei vincitori, di grandezza maggiore di 1 solo in caso di pareggio.
	 */
	private List<Giocatore> calcolaVincitore() {
		Iterator<Giocatore> scorriGiocatori = statoGioco.getListaGiocatori().iterator();
		Giocatore vincitore = null;
		List<Giocatore> listaVincitori = new ArrayList<Giocatore>();
		while (scorriGiocatori.hasNext()){
			Giocatore giocatore = scorriGiocatori.next();
			Iterator<Carta> scorriCarte = giocatore.getListaCarte().iterator();
			while (scorriCarte.hasNext()){
				TipoTerreno tipo = scorriCarte.next().getTipo();
				giocatore.incrementaDanari(StrumentiController.getNumeroPecoreTerreno(tipo, mappa.getListaRegioni()));
			}
			if (vincitore == null){
				vincitore = giocatore;
				listaVincitori.add(vincitore);
			}else if (giocatore.getDanari() > vincitore.getDanari()){
				listaVincitori.clear();
				vincitore = giocatore;
				listaVincitori.add(vincitore);
			}else if (giocatore.getDanari() == vincitore.getDanari()){
				listaVincitori.add(giocatore);
			}
		}
		return listaVincitori;
	}

	/**
	 * Incrementa il contatore dell'età di tutti gli agnelli nel gioco, e se questo è
	 * maggiore di un numero di turni stabilito, l'agnello corrispondente sarà rimosso
	 * e verrà creato al suo posto una pecora o un ariete.
	 */
	private void invecchiaAgnelli() {
		Iterator<Regione> scorriRegioni = mappa.getListaRegioni().iterator();
		while(scorriRegioni.hasNext()){
			Regione regione = scorriRegioni.next();
			Iterator<Agnello> scorriAgnelli = regione.getListaAgnelli().iterator();
			while (scorriAgnelli.hasNext()){
				Agnello agnello = scorriAgnelli.next();
				agnello.incrementaContatore();
				if (agnello.getContatoreTurni() == Costanti.NUMERO_TURNI_CRESCITA_AGNELLO){
					agnelloEvolve(agnello);
				}
			}
		}
	}
	/**
	 * Traforma un agnello in pecora o ariete con probabilità
	 * 1/2, rimuovendolo dalla regione e creando nella stessa 
	 * regione un animale di tipo pecora/ariete.
	 * @param agnello l'agnello da trsformare
	 */
	private void agnelloEvolve(Agnello agnello) {
		Regione regione = agnello.getPosizione();
		regione.rimuoviAnimale(agnello);
		view.rimuoviAnimaleView(agnello);
		String messaggio = agnello.toString() +" è cresciuto: ";
		if (Math.random() > 0.5){
			Pecora pecora = new Pecora(regione);
			view.aggiungiAnimaleView(pecora, regione);
			view.stampaMessaggio(messaggio+ "è diventata una bellissima pecora");

		}else{
			Ariete ariete = new Ariete(regione);
			view.aggiungiAnimaleView(ariete, regione);
			view.stampaMessaggio(messaggio+"è diventato un bell'ariete");

		}
	}

	/**
	 * Il ciclo di gioco: inizialmente si posizionano i pastori; dopodichè,
	 * ha inizio la prima fase di gioco, che fa fare turni ai giocatori 
	 * finchè il numero di recinti è meggiore di zero. Al finire dei recinti,
	 * parte l'ultimo turno, che sarà diverso dal turno normale solo nel fatto 
	 * che i recinti posizionate durante questo turno saranno recinti finali.
	 * Infine, viene calcolato il vincitore/i vincitori della partita.
	 */
	private void loopGioco() {
		int i = 0;
		posizionaPastori();
		while (statoGioco.getNumeroRecinti() > 0){
			for (i = 0; i < statoGioco.getNumeroGiocatori() & statoGioco.getNumeroRecinti() > 0; i++){
				Turno turno = new Turno(statoGioco, view, statoGioco.getListaGiocatori().get(i));
				turno.iniziaTurno();
				spostaPecoraNera();
				spostaLupo();
				lupoMangia();
				invecchiaAgnelli();
			}
			if (!statoGioco.getListaCarteInVendita().isEmpty()){
				turnoCompraVendita();
			}
		}
		/*
		 * Quando finiscono i recinti inzia il turno finale a partire dal giocatore i-esimo.
		 * in questo ciclo j va da 0 a numero giocatori e prendiamo il giocatore |j-i|, ossia l'i-esimo
		 * alla prima iterazione, l'i+1-esimo alla seconda e così via.
		 */
		view.stampaMessaggio("HA INIZIO IL TURNO FINALE!");

		for (int j = 0; j < statoGioco.getNumeroGiocatori() & statoGioco.getNumeroRecintiFinali() > 0; j++){
			new Turno(statoGioco,view,statoGioco.getListaGiocatori().get(Math.abs(j - i)));
			spostaPecoraNera();
			spostaLupo();
			lupoMangia();
			invecchiaAgnelli();
		}
		view.stampaMessaggio("Gioco Finito");
		List<Giocatore> listaVincitori = calcolaVincitore();
		if (listaVincitori.size() == 1){
			view.stampaMessaggio("vince il Giocatore "+(listaVincitori.get(0).getNumero()));
		}else{
			view.stampaMessaggio("Vincono a pari merito i Giocatori "+listaVincitori.toString());
		}
	}

	/**
	 * Effettua il turno di compravendita. Il metodo viene chiamato dal loop di gioco solo se 
	 * esistono carte in vendita nel turno corrente, e propone ad ogni giocatore la lista delle 
	 * carte che può acquistare. Questo può acquistarne una oppure saltare l'acquisto. Alla fine,
	 * la lista delle eventuali carte in vendita rimaste viene svuotata, visto che va creata nuova 
	 * ad ogni turno.
	 */
	private void turnoCompraVendita() {
		view.stampaMessaggio("Inizia il turno di compravendita");
		Iterator<Giocatore> scorriGiocatori = statoGioco.getListaGiocatori().iterator();
		while (scorriGiocatori.hasNext()){
			List<CartaInVendita> listaCarteAcquistabili = new ArrayList<CartaInVendita>();
			Giocatore acquirente = scorriGiocatori.next();
			Iterator<CartaInVendita> scorriCarte = statoGioco.getListaCarteInVendita().iterator();
			while (scorriCarte.hasNext()){
				CartaInVendita cartaVendibile = scorriCarte.next();
				if (cartaVendibile.getProprietario().getNumero() != acquirente.getNumero() && cartaVendibile.getCosto() <= acquirente.getDanari()){
					listaCarteAcquistabili.add(cartaVendibile);
				}
			}
			CartaInVendita cartaInVendita = null;
			if (!listaCarteAcquistabili.isEmpty()){
				view.setInterfacciaGiocatore(acquirente);
				view.stampaMessaggio("Tocca al Giocatore "+acquirente.getNumero());
				cartaInVendita = view.scegliCartaDaComprare(listaCarteAcquistabili,acquirente);
			}else{
				view.stampaMessaggio("Il Giocatore "+acquirente.getNumero()+ " non ha nessuna carta da poter comprare");
			}
			if (cartaInVendita != null){
				CartaInVendita cartaVenduta = StrumentiController.trovaCartaInVendita(cartaInVendita, statoGioco.getListaCarteInVendita());
				statoGioco.getListaCarteInVendita().remove(cartaVenduta); 
				Giocatore venditore = cartaInVendita.getProprietario();
				int costoCarta = cartaInVendita.getCosto();
				Carta carta = cartaInVendita.getCarta();
				acquirente.aggiungiCarta(carta);
				venditore.rimuoviCarta(carta);
				acquirente.decrementaDanari(costoCarta);
				venditore.incrementaDanari(costoCarta);
				view.rimuoviCarteView();
			}
		}
		statoGioco.getListaCarteInVendita().clear();
	}

	/**
	 * Fa mangiare il lupo: se ci sono pecore o arieti nella regione, ne mangia uno (il
	 * primo della listaPecoreEArieti della regione). Se no digiuna.
	 */
	private void lupoMangia() {
		Regione regione = statoGioco.getLupo().getPosizione();
		if (regione.contieneArieti() || regione.contienePecore()){
			Animale animale = regione.getListaPecoreArieti().get(0);
			view.stampaMessaggio("il lupo mangia un innocente esemplare di "+animale.toString()+" !");
			view.rimuoviAnimaleView(animale);
			regione.rimuoviAnimale(animale);
		}else{
			view.stampaMessaggio("il lupo, per questo turno, digiuna!");
		}
	}

	/**
	 * Posiziona i pastori nella fase iniziale del loop di gioco. Nel caso
	 * di gioco in due giocatori, fa posizionare un pastore, a turno, ai due 
	 * giocatori.
	 */
	private void posizionaPastori() {
		Iterator<Giocatore> scorriGiocatori = statoGioco.getListaGiocatori().iterator();
		int numeroPastore = 0;
		while (scorriGiocatori.hasNext()){
			Giocatore giocatore = scorriGiocatori.next();
			if (giocatore.getListaPastori().size() < numeroPastore + 1)
				break;
			statoGioco.setGiocatoreCorrente(giocatore);
			view.stampaMessaggio("Giocatore " + giocatore.getNumero() + ", posiziona il tuo pastore");
			Casella casella = view.scegliCasella(mappa.caselleLibere());
			Pastore pastore = giocatore.getListaPastori().get(numeroPastore);
			pastore.setPosizione(casella);
			view.aggiungiPastoreView(pastore);
			if (!scorriGiocatori.hasNext()){
				scorriGiocatori = statoGioco.getListaGiocatori().iterator();
				numeroPastore++;
			}
		}
	}

	/**
	 * Sposta il lupo: Se ha caselle libere attorno, tira il dado fino
	 * a ottenere il numero corrispondente ad una casella libera; se invece è 
	 * circondato, scavalca l'ostacolo corrispondente alla casella del primo tiro del 
	 * suo dado. Quindi il lupo si muove ad ogni turno.
	 */
	private void spostaLupo() {
		Lupo lupo = statoGioco.getLupo();
		int dado = 0;
		Casella casellaSpostamento = null;
		List<Casella> caselleLibereAdiacenti = mappa.caselleLibereAdiacenti(lupo.getPosizione());
		boolean scavalca = false;

		if (caselleLibereAdiacenti.size() == 1){
			//caso in cui c'è una sola casella libera: il lupo deve passare da qua per forza
			dado = caselleLibereAdiacenti.get(0).getNumero();
			casellaSpostamento = caselleLibereAdiacenti.get(0);
		}else if (caselleLibereAdiacenti.isEmpty()){
			//nessuna casella libera: tocca scavalcare
			scavalca = true;
			view.stampaMessaggio("Il lupo è circondato! Dovrà scavalcare un ostacolo...");
		}

		while (scavalca){
			List<Casella> caselleAdiacenti = mappa.caselleAdiacenti(lupo.getPosizione());
			dado = (int) (Math.random() * 6) + 1;
			Iterator<Casella> scorriCaselle = caselleAdiacenti.iterator();
			while (scorriCaselle.hasNext()) {
				Casella casella = scorriCaselle.next();
				if (casella.getNumero() == dado) {
					casellaSpostamento = casella;
					scavalca = false;
				}
			}
		}
		while (casellaSpostamento == null){ 
			//siamo nel caso in cui ci sia più di una casella libera per il passaggio:
			//tiriamo il dado finchè non troviamo una casella libera
			dado = (int)(Math.random() * 6) + 1;
			Iterator<Casella> scorriCaselle = caselleLibereAdiacenti.iterator();
			while (scorriCaselle.hasNext()){
				Casella casella = scorriCaselle.next();
				if (casella.getNumero() == dado){
					casellaSpostamento = casella;
				}
			}	
		}

		List<Regione> regioniConfinanti = mappa.regioniConfinanti(casellaSpostamento);
		Iterator<Regione> scorriRegioni = regioniConfinanti.iterator();
		while (scorriRegioni.hasNext()){
			Regione regione = scorriRegioni.next();
			if (regione != lupo.getPosizione()){
				view.muoviLupo(regione,dado);
				lupo.setPosizione(regione);
				return;
			}
		}
	}

	/**
	 * Sposta la pecora nera: se tirando il dado ottiene il numero
	 * di una casella libera, si muove attraverso di essa, se la casella
	 * invece è occupata la pecora no si muove.
	 */
	private void spostaPecoraNera() {
		PecoraNera pecoraNera = statoGioco.getPecoraNera();
		List<Casella> caselleAdiacenti = mappa.caselleLibereAdiacenti(pecoraNera.getPosizione());
		Regione regione = null;
		int dado = (int)(Math.random() * 6) + 1;
		view.lanciaDadoPecoraNera(dado);
		Iterator<Casella> scorriCaselle = caselleAdiacenti.iterator();
		Casella casella = null;
		while (scorriCaselle.hasNext()){
			casella = scorriCaselle.next();
			if (casella.getNumero() == dado){
				break;
			}
		}
		List<Regione> regioniConfinanti = mappa.regioniConfinanti(casella);
		Iterator<Regione> scorriRegioni = regioniConfinanti.iterator();
		while (scorriRegioni.hasNext()){
			regione = scorriRegioni.next();
			if (regione != pecoraNera.getPosizione()){
				view.stampaMessaggio("La pecora nera ha tirato il dado e ottenuto "+dado+". Si muove in "+regione.getNomeRegione());
				view.muoviPecoraNera(regione,dado);
				pecoraNera.setPosizione(regione);
				return;
			}
		}	
		view.stampaMessaggio("La pecora nera ha tirato il dado e ha ottenuto "+ dado+". Strada Sbarrata!");
	}

}
