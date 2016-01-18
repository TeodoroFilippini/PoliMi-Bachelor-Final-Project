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
import model.Mappa;
import model.Pastore;
import model.Pecora;
import model.PecoraNera;
import model.Regione;
import model.StatoGioco;
import model.TipoMossa;
import model.TipoTerreno;
import view.InterfacciaView;

/**
 * Classe che modella il turno, inteso come le tre mosse (più una, se si effettua
 * la vendita tessera) che fanno parte dell'azione effettuata da un giocatore 
 * quando tocca a lui giocare.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class Turno {
	private Giocatore giocatore;
	private Mappa mappa;
	private List <TipoMossa> mosseEffettuate = new ArrayList<TipoMossa>();
	private Pastore pastore;
	private StatoGioco statoGioco;
	private InterfacciaView view;

	/**
	 * Costruttore della classe. Acquisisce il giocatore corrente e la sua view, nonchè
	 * lo stato del gioco corrente; poi fa impostare al giocatore il suo pastore in gioco
	 * (se necessario).
	 * @param statoGioco
	 * @param view
	 * @param giocatore
	 */
	public Turno(StatoGioco statoGioco,InterfacciaView view, Giocatore giocatore){
		this.giocatore = giocatore;
		this.statoGioco = statoGioco;
		this.view = view;
		view.setGiocatoreCorrente(giocatore);
		view.setInterfacciaGiocatore(giocatore);
		int numeroGiocatori = statoGioco.getNumeroGiocatori();
		if (numeroGiocatori == 2){
			view.stampaMessaggio("Giocatore "+giocatore.getNumero()+", scegli il pastore che vuoi utilizzare");
			pastore = view.scegliPastore(giocatore.getListaPastori());
			giocatore.setPastoreInGioco(pastore);
		}else{
			pastore = giocatore.getListaPastori().get(0);
			giocatore.setPastoreInGioco(pastore);
		}
		mappa = statoGioco.getMappa();
		giocatore.setAzioniRimanentiPerTurno(Costanti.AZIONI_PER_TURNO);
		view.stampaMessaggio("Inizio Turno Giocatore "+(giocatore.getNumero()));
	}

	/**
	 * Mossa accoppiamento: se nella regione ci sono almeno una pecora e
	 * almeno un ariete, tirando un dado e ottenendo il numero corrispondente alla
	 * casella dove si trova il pastore, si fa nascere un nuovo agnello; se no,
	 * non succede nulla.
	 * @return true se la mossa ha successo, false se no.
	 */
	private boolean accoppia() {
		Regione regione;
		regione = view.scegliRegione(regioniAccoppiamento());
		int dado = (int) (Math.random() * 6) + 1;
		if (dado == pastore.getPosizione().getNumero()) {
			Agnello agnello = new Agnello(regione);
			view.aggiungiAnimaleView(agnello,regione);
			view.stampaMessaggio("Accoppiamento riuscito! è nato un nuovo Agnello");
			return true;
		}else{
			view.stampaMessaggio("Accoppiamento fallito");
			return false;
		}
	}

	/**
	 * Calcola le mosse disponibili per il giocatore: se siamo all'ultima mossa
	 * e non ha ancora spostato il pastore, deve farlo; se no, può fare tutte le mosse 
	 * consentite dalla sua posizione sulla mappa e dai suoi soldi, tranne quella appena effettuata, 
	 * a meno che questa non sia una mossa sposta pastore.
	 * @return la lista delle mosse effettuabili.
	 */
	private List<TipoMossa> calcolaMosseDisponibili(){
		List<TipoMossa> mosseDisponibili = new ArrayList<TipoMossa>();
		TipoMossa ultimaMossa = null; 
		if (!mosseEffettuate.isEmpty()) {
			ultimaMossa = mosseEffettuate.get(mosseEffettuate.size()-1);
		}
		if (siPuoMuovere()) {
			mosseDisponibili.add(TipoMossa.SPOSTA_PASTORE);
		}
		if (ultimaMossa != TipoMossa.SPOSTA_PECORA && !regioniSpostamentoPecore().isEmpty()) {
			mosseDisponibili.add(TipoMossa.SPOSTA_PECORA);
		}
		if (ultimaMossa != TipoMossa.ACCOPPIA && !regioniAccoppiamento().isEmpty()) {
			mosseDisponibili.add(TipoMossa.ACCOPPIA);
		}
		if (ultimaMossa != TipoMossa.SPARA && !regioniSparatoria().isEmpty()) {
			mosseDisponibili.add(TipoMossa.SPARA);
		}
		if (ultimaMossa != TipoMossa.COMPRA_CARTA && !carteAcquistabili().isEmpty()) {
			mosseDisponibili.add(TipoMossa.COMPRA_CARTA);
		}
		return mosseDisponibili;
	}

	/**
	 * Calcola le carte acquistabili da un giocatore.
	 * @return le carte acquistabili dove il giocatore ha chiamato la 
	 *         mossa compra carta: queste dipendono dala casella del pastore associato 
	 *         al giocatore, dai suoi soldi e dal numero di carte disponibili per il tipo
	 *         di interesse.
	 */
	private List<Carta> carteAcquistabili() {
		List<Carta> carteAcquistabili = new ArrayList<Carta>();
		List<Regione> regioniConfinanti = mappa.regioniConfinanti(pastore.getPosizione());
		Iterator<Regione> scorriRegioni = regioniConfinanti.iterator();
		while (scorriRegioni.hasNext()){
			TipoTerreno tipo = scorriRegioni.next().getTipo();
			Iterator<Carta> scorriCarte = statoGioco.getListaCarte().iterator();
			while(scorriCarte.hasNext()){
				Carta carta = scorriCarte.next();
				if (carta.getTipo() == tipo && carta.getCarteDisponibili() > 0 && carta.getCosto() <= giocatore.getDanari() && !carteAcquistabili.contains(carta)) {
					carteAcquistabili.add(carta);
				}
			}
		}
		return carteAcquistabili;
	}

	/**
	 * Mossa compra carta.
	 */
	private void compraCarta() {
		Carta carta;
		carta = view.scegliCarta(carteAcquistabili());
		giocatore.aggiungiCarta(carta);
		giocatore.decrementaDanari(carta.getCosto());
		view.aggiungiCarta(carta,giocatore);
		carta.aumentaCosto();
		carta.decrementaCarteDisponibili();
	}

	/**
	 * Metodo che fa partire il turno del giocatore: Cicla n volte, dove n è il
	 * numero di azioni per turno, e ad ogni iterazione calcola il numero di mosse effettuabili,
	 * e ne fa scegliere una al giocatore.
	 */
	public void iniziaTurno(){
		int azioniRimanenti;
		List <TipoMossa> mosseDisponibili = new ArrayList<TipoMossa>();
		for (azioniRimanenti = Costanti.AZIONI_PER_TURNO; azioniRimanenti > 0; azioniRimanenti--){
			boolean uscita = false;
			boolean spostatoPastore = mosseEffettuate.contains(TipoMossa.SPOSTA_PASTORE);
			if (azioniRimanenti == 1 & !spostatoPastore){
				mosseDisponibili = new ArrayList<TipoMossa>();
				if (siPuoMuovere()) {
					mosseDisponibili.add(TipoMossa.SPOSTA_PASTORE);
				}else{
					uscita = true;
				}
				/*Le regole del gioco non prevedono la possibilità di saltare il turno, tuttavia esiste la possibilità 
			 che si presenti la situazione in cui il giocatore non possa compiere alcuna mossa generando un deadlock*/ 
			}else{
				mosseDisponibili = calcolaMosseDisponibili();
				if (mosseDisponibili.isEmpty()) {
					uscita = true;
				}
			}
			if (uscita){
				break;
			}
			scegliMossa(mosseDisponibili);
			if (statoGioco.getNumeroRecinti() == 0) {
				statoGioco.setUltimoTurno(true);
			}
			if(statoGioco.getNumeroRecintiFinali() == 0){
				statoGioco.setGiocoFinito(true);
				return;
			}
			giocatore.decrementaAzioniDisponibili();
		}
		mosseDisponibili.clear();
		mosseDisponibili.add(TipoMossa.VENDI_TESSERA);
		mosseDisponibili.add(TipoMossa.SALTA_TURNO);
		scegliMossa(mosseDisponibili);
	}

	/**
	 * 
	 * @return la lista di regioni dove è possibile effettuare
	 * 	       un'azione accoppiamento, cioè sono presenti almeno
	 * 		   una pecora e almeno un ariete.
	 */
	private List<Regione> regioniAccoppiamento() {
		List<Regione> regioniConfinanti = mappa.regioniConfinanti(pastore.getPosizione());
		Iterator<Regione> scorriRegioni = regioniConfinanti.iterator();
		List<Regione> regioniAccoppiamento = new ArrayList<Regione>();
		while (scorriRegioni.hasNext()){
			Regione regioneCorrente = scorriRegioni.next();
			if (regioneCorrente.contieneArieti() & regioneCorrente.contienePecore()) {
				regioniAccoppiamento.add(regioneCorrente);
			}
		}
		return regioniAccoppiamento;
	}

	/**
	 * 
	 * @return le regioni dove è possibile effettuare l'azione sparatoria, cioè
	 * 		   dove c'è almeno una pecora o un ariete, e il giocatore che 
	 * 	       effettuerebbe la mossa ha abbastanza soldi per corrompere tutti
	 * 		   i giocatori che hanno pastori in una casella adiacente alla regione.
	 */
	private List<Regione> regioniSparatoria() {
		List<Regione> regioniSparatoria = new ArrayList<Regione>();
		List<Regione> regioniConfinanti = mappa.regioniConfinanti(pastore.getPosizione());
		int costoCorruzione = 0;
		Iterator<Regione> scorriRegioni = regioniConfinanti.iterator();
		while (scorriRegioni.hasNext()){
			Regione regioneCorrente = scorriRegioni.next();
			if (regioneCorrente.contienePecore() || regioneCorrente.contieneArieti()){
				List<Casella> caselleAdiacenti = mappa.caselleAdiacenti(regioneCorrente);
				Iterator<Casella> scorriCaselle = caselleAdiacenti.iterator();
				while (scorriCaselle.hasNext()){
					Pastore pastoreCorrente = scorriCaselle.next().getPastore();
					if (!giocatore.getListaPastori().contains(pastoreCorrente) && pastoreCorrente != null) {
						costoCorruzione += Costanti.COSTO_CORRUZIONE;
					}
				}
				if (costoCorruzione <= giocatore.getDanari()) {
					regioniSparatoria.add(regioneCorrente);
				}
			}
		}
		return regioniSparatoria;
	}

	/**
	 * 
	 * @return La lista di tutte le regioni intorno alle quali si può effettuare un'azione spostamento,
	 * 		   ovvero dove sono presenti almeno uno tra una pecora, o un ariete, o la pecora nera.
	 */
	private List<Regione> regioniSpostamentoPecore() {
		List<Regione> regioniConfinanti = mappa.regioniConfinanti(pastore.getPosizione());
		Iterator<Regione> scorriRegioni = regioniConfinanti.iterator();
		List<Regione> regioniSpostamento = new ArrayList<Regione>();
		while (scorriRegioni.hasNext()){
			Regione regioneCorrente = scorriRegioni.next();
			if (regioneCorrente.contieneArieti() || regioneCorrente.contienePecore() || regioneCorrente.contienePecoraNera()) {
				regioniSpostamento.add(regioneCorrente);
			}
		}
		return regioniSpostamento;
	}

	/**
	 * Mossa salto turno,inteso sia come vero salto della mossa (in caso di deadlock logico), 
	 * sia come conclusione del proprio turno senza effettuare la mossa, facoltativa, di 
	 * compravendita. non fa che rendere i bottoni associati alla scelta della mossa non 
	 * selezionabili.
	 */
	private void saltaTurno() {	
		view.disabilitaBottoni();
	}

	/**
	 * Fa scegliere al giocatore, tramite la view, la mossa da effettuare.
	 * @param mosseDisponibili la lista delle mosse che il giocatore può
	 * 		  scegliere di effettuare.
	 */
	private void scegliMossa(List<TipoMossa> mosseDisponibili) {
		TipoMossa mossa;
		mossa = view.scegliMossa(mosseDisponibili);
		mosseEffettuate.add(mossa);
		switch (mossa){
		case ACCOPPIA:
			accoppia();
			break;
		case COMPRA_CARTA:
			compraCarta();
			break;
		case SALTA_TURNO:
			saltaTurno();
			break;
		case SPARA:
			spara();
			break;
		case SPOSTA_PASTORE:
			spostaPastore();
			break;
		case SPOSTA_PECORA:
			spostaPecora();
			break;
		case VENDI_TESSERA:
			vendiTessera();
			break;
		default:
			break;
		}
	}

	/**
	 * Mossa vendi tessera: fa scegliere tessere da vendere finchè il giocatore non 
	 * preme sulla finestra conferma, che manda come notifica una carta null, interrompendo 
	 * il ciclo che continua a far scegliere nuove carte da vendere al giocatore. Infine
	 * effettua una mossa salta turno per disabilitare i bottoni, e conclude il turno.
	 */
	private void vendiTessera() {
		List<CartaInVendita> listaCarteInVendita = statoGioco.getListaCarteInVendita();
		List<Carta> carteVendibili = new ArrayList<Carta>();
		carteVendibili.addAll(giocatore.getListaCarte());
		while(true){
			Carta carta = view.scegliCartaDavendere(StrumentiController.getListaCarteSenzaRipetizioni(carteVendibili));
			if (carta == null) {
				break;
			}
			int costoCarta = view.scegliCostoCarta(carta);
			listaCarteInVendita.add(new CartaInVendita(carta, costoCarta, statoGioco.getGiocatoreCorrente()));
			carteVendibili.remove(carta);
		}
		saltaTurno();
	}

	/**
	 * Metodo che ritorna se il pastore può muoversi o no.
	 * @return true se il pastore ha caselle libere adiacenti (il costo dello
	 * 		   spostamento è allora nullo), oppure se il giocatore associato al pastore
	 * 		   ha i soldi necessari a farlo spostare più lontano.
	 * 		   false se no.
	 */
	private boolean siPuoMuovere() {
		List<Casella> caselleLibereAdiacenti = mappa.caselleLibereAdiacenti(pastore.getPosizione());
		if (!caselleLibereAdiacenti.isEmpty()) {
			return true;
		}
		if (giocatore.getDanari() >= Costanti.COSTO_SPOSTAMENTO) {
			return true;
		}
		return false;
	}

	/**
	 * Mosa spara: il giocatore tira il dado, e se ottiene lo stesso numero della casella
	 * sulla quale è posto il pastore che sta usando, uccide l'animale (pecora o ariete)
	 * desiderato; dopodichè, tira il dado una volta per ogni pastore nemico sito in una casella
	 * adiacente alla regione della sparatoria: dovrà corrompere ogni giocatore che tirando il
	 * dado per ognuno dei proprio pastori adiacenti a quella regione ottenga 5 o 6.
	 * @return
	 */
	private boolean spara() {
		Regione regione;
		Animale animaleScelto;
		regione = view.scegliRegione(regioniSparatoria());
		int dado =(int) (Math.random() * 6) + 1;
		if (dado == pastore.getPosizione().getNumero()){
			if (StrumentiController.isListaConUnSoloTipoDiAnimale(regione.getListaPecoreArieti())){
				animaleScelto = regione.getListaPecoreArieti().get(0);
			}else{
				animaleScelto = view.scegliAnimale(regione.getListaPecoreArieti());
			}
			List<Casella> caselleAdiacenti = mappa.caselleAdiacenti(regione);
			Iterator<Casella> scorriCaselle = caselleAdiacenti.iterator();
			boolean necessariaCorruzione = false;
			while (scorriCaselle.hasNext()){
				Pastore pastoreCorrente = scorriCaselle.next().getPastore();
				if (!giocatore.getListaPastori().contains(pastoreCorrente) && pastoreCorrente != null){
					dado = (int) (Math.random() * 6)+ 1;
					if (dado == 5 || dado == 6){
						necessariaCorruzione = true;
						pastoreCorrente.getGiocatore().incrementaDanari(Costanti.COSTO_CORRUZIONE);
						giocatore.decrementaDanari(Costanti.COSTO_CORRUZIONE);
						view.corrompi(pastoreCorrente.getGiocatore(),giocatore);
					}
				}
			}
			regione.rimuoviAnimale(animaleScelto);
			view.rimuoviAnimaleView(animaleScelto);
			view.stampaMessaggio("Un innocente esemplare di " + animaleScelto.toString() + " è stato assassinato");
			if (!necessariaCorruzione) {
				view.stampaMessaggio("Nessuno ha visto niente...");
			}
			return true;
		}else{
			view.stampaMessaggio("Sparatoria fallita");
			return false;
		}
	}

	/**
	 * Mossa sposta pastore: sposta il pastore da una casella ad un'altra.
	 */
	private void spostaPastore() {
		Casella casellaDestinazione;
		List<Casella> caselleLibereAdiacenti = mappa.caselleLibereAdiacenti(pastore.getPosizione());
		if (giocatore.getDanari() >= 1){
			casellaDestinazione = view.scegliCasella(mappa.caselleLibere());
		}else {
			casellaDestinazione = view.scegliCasella(caselleLibereAdiacenti);
		}
		if (!caselleLibereAdiacenti.contains(casellaDestinazione)) {
			giocatore.decrementaDanari(Costanti.COSTO_SPOSTAMENTO);
		}		
		if (statoGioco.getNumeroRecinti() > 0){
			statoGioco.decrementaRecinti();
			pastore.getPosizione().setOccupataRecinto(true);
		}else{
			statoGioco.decrementaRecintiFinali();
			pastore.getPosizione().setOccupataRecintoFinale(true);
		}
		view.muoviPastore(pastore,casellaDestinazione);
		pastore.setPosizione(casellaDestinazione);
	}

	/**
	 * Mossa sposta pecora: sposta una pecora da una regione a quella 
	 * adiacente tramite la casella dove si trova il pastore il cui giocatore 
	 * proprietario sta effettuando la mossa.
	 */
	private void spostaPecora() { 
		List<Regione> regioniSpostamentoPecore = regioniSpostamentoPecore();
		List<Animale> listaAnimali = new ArrayList<Animale>();
		Regione regionePartenza;
		regionePartenza = view.scegliRegione(regioniSpostamentoPecore);
		Iterator<Animale> scorriAnimali = regionePartenza.getListaAnimali().iterator();
		while (scorriAnimali.hasNext()){
			Animale animale = scorriAnimali.next();
			if (animale instanceof Pecora || animale instanceof PecoraNera || animale instanceof Ariete) {
				listaAnimali.add(animale);
			}
		}
		Animale animaleScelto;
		if (!StrumentiController.isListaConUnSoloTipoDiAnimale(listaAnimali)){
			animaleScelto = view.scegliAnimale(listaAnimali);
		}else{
			animaleScelto = listaAnimali.get(0);
		}
		List<Regione> regioniConfinanti = statoGioco.getMappa().regioniConfinanti(pastore.getPosizione());
		Iterator<Regione> scorriRegioni = regioniConfinanti.iterator();
		while (scorriRegioni.hasNext()){
			Regione regioneDestinazione = scorriRegioni.next();
			if (!regioneDestinazione.equals(regionePartenza)){
				view.muoviAnimale(animaleScelto,regioneDestinazione);
				animaleScelto.setPosizione(regioneDestinazione);
			}
		}
	}
}
