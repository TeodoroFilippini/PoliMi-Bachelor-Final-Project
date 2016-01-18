package view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Animale;
import model.Casella;
import model.Costanti;
import model.Lupo;
import model.Pastore;
import model.PecoraNera;
import model.Regione;
import model.StatoGioco;
import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.IOUtils;

/**
 * Pannello contenente la mappa e le varie entità che la popolano. 
 * Contiene inoltre anche in alto il numero di carte, recinti e recinti finali
 * disponibili.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class PannelloMappa extends JPanel implements MouseListener,ActionListener{

	
	private static final long serialVersionUID = 1L;
	private Image animazioneDadoLupo;
	private Image animazioneDadoPecoraNera;
	private Point coordinateClick;
	private int dadoLupo;
	private int dadoPecoraNera;
	private BufferedImage immagineCaselle;
	private BufferedImage immagineGruppo;
	private BufferedImage immagineMappa;
	private List<BufferedImage> immaginiDadoLupo = new ArrayList<BufferedImage>();
	private List<BufferedImage> immaginiDadoPecoraNera = new ArrayList<BufferedImage>();
	private IstanzeView istanzeView;
	private boolean lanciaDadoLupo;
	private boolean lanciaDadoPecoraNera;
	private Regione regioneScelta;
	private boolean selezionaCaselle;
	private boolean selezionaRegioni;
	private Point stampaLancioDado;
	private StatoGioco statoGioco;
	private long tempoInizioStampaDadoLupo;
	private long tempoInizioStampaDadoPecoraNera;

	/**
	 * Carica le istanze view e lo statoGioco utilizzate per stampare le informazioni
	 * corrette e le cose al posto giusto, carica le immagini, setta il cursore e fa partire
	 * il timer che chiama il repaint ogni tot millisecondi.
	 * 
	 * @param istanzeView oggetto contenente le istanze view.
	 * @param statoGioco oggetto contenente le istanze di gioco.
	 */
	public PannelloMappa(IstanzeView istanzeView, StatoGioco statoGioco){
		this.istanzeView = istanzeView;
		this.statoGioco = statoGioco;
		caricaImmagini();
		setFocusable(true);
		setOpaque(false);
		addMouseListener(this);
		setPreferredSize(Costanti.DIMENSIONE_MAPPA);
		stampaLancioDado = new Point(immagineMappa.getWidth() / 2 - Costanti.LARGHEZZA_DADO / 2, immagineMappa.getHeight() / 2 - Costanti.ALTEZZA_DADO / 2);
		Timer timer = new Timer(25, this);
		timer.start();
		setCursor(StrumentiView.getCursore());
	}

	/**
	 * Ristampa tutto ogni evento di timer.
	 * 
	 * @param e evento di timer.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	/**
	 * Carica le immagini necessarie.
	 */
	private void caricaImmagini() {
		try {
			animazioneDadoPecoraNera =  Toolkit.getDefaultToolkit().createImage(IOUtils.toByteArray(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_DADO_PECORA_NERA)));
			animazioneDadoLupo =  Toolkit.getDefaultToolkit().createImage(IOUtils.toByteArray(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_DADO_LUPO)));
			immagineMappa = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_MAPPA));
			immagineCaselle = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_MAPPA_CASELLE));
			immagineGruppo = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_GRUPPO));
			immagineMappa = Thumbnails.of(immagineMappa).scale(Costanti.FATTORE_SCALA).asBufferedImage();
			immagineCaselle = Thumbnails.of(immagineCaselle).scale(Costanti.FATTORE_SCALA).asBufferedImage();
			immagineGruppo = Thumbnails.of(immagineGruppo).scale(Costanti.FATTORE_SCALA).asBufferedImage();

			for (int i = 1; i<= 6; i++){
				immaginiDadoLupo .add(ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_DADI+"Lupo"+i+".png")));
				immaginiDadoPecoraNera .add(ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_DADI+"PecoraNera"+i+".png")));
			}
		} catch (IOException e) {
			new FinestraNotifica("Errore durante l'apertura di una o più immagini associate agli oggetti \"dado\", \"mappa\", \"caselle\" o \"gruppo di ovini\"", e);
		}
	}

	/**
	 * Imposta il boolean per l'animazione del lancio del dado a true in maniera tale che il paintComponent
	 * la stampi e salva il tempo di inizio dell'animazione per poterla poi fermare una volta finita.
	 * 
	 * @param dadoLupo numero per decidere la faccia del dado da visualizzare alla fine dell'animazione.
	 */
	public void lanciaDadoLupo(int dadoLupo){
		this.dadoLupo = dadoLupo;
		lanciaDadoLupo = true;
		tempoInizioStampaDadoLupo = System.currentTimeMillis();
	}

	/**
	 * Imposta il boolean per l'animazione del lancio del dado a true in maniera tale che il paintComponent
	 * la stampi e salva il tempo di inizio dell'animazione per poterla poi fermare una volta finita.
	 * 
	 * @param dadoPecoraNera numero per decidere la faccia del dado da visualizzare alla fine dell'animazione.
	 */
	public void lanciaDadoPecoraNera(int dadoPecoraNera) {
		this.dadoPecoraNera = dadoPecoraNera;
		lanciaDadoPecoraNera = true;
		tempoInizioStampaDadoPecoraNera = System.currentTimeMillis();
	}

	/**
	 * Quando si clicca il mouse verifica che si debbano selezionare caselle o regioni utilizzando i rispettivi
	 * boolean. In entrambi i casi si sfrutta la trasparenza delle immagini di caselle e regioni 
	 * per verificare che si sia premuto effettivamente su una di esse e notifica o al metodo scegliCasella o
	 * scegliRegione che erano in attesa che poi torneranno la casella o la regione sulla quale si è cliccato.
	 * 
	 * @param e evento di click del mouse.
	 */
	@Override
	public synchronized void mouseClicked(MouseEvent e) { 
		//se si schiaccia su un punto di trasparenza (non una casella), alpha = 0, e restituiamo coordinate nulle 
		//al metodo di Pannello "selezionaCasella". Se si seleziona una casella già occupata, il problema viene gestito in SheeplandView.
		//clicca su una casella, alpha è in (0, 255]; in seguito, verifichiamo su che casella abbiamo cliccato scorrendole tutte e trovando
		//quella con coordinate più vicine al punto di clic, che è più o meno il centro della casella.
		coordinateClick = e.getPoint();
		int alpha = 0;
		if (selezionaCaselle){
			if (coordinateClick.x < immagineCaselle.getWidth()){
				int pixel = immagineCaselle.getRGB(coordinateClick.x, coordinateClick.y);
				alpha = (pixel >> 24) & 0xff;
				if (alpha != 0){
					notify();
				}
			}
		}else if (selezionaRegioni){
			Iterator<RegioneView> scorriRegioni = istanzeView.getListaRegioniView().iterator();
			while (scorriRegioni.hasNext()){
				RegioneView regioneView = scorriRegioni.next();
				int pixel = regioneView.getImmagine().getRGB(coordinateClick.x, coordinateClick.y);
				alpha = (pixel >> 24) & 0xff;
				if (alpha != 0 & regioneView.getRegione().isSelezionabile()){
					regioneScelta = regioneView.getRegione();
					notify();
				}
			}
		}
	}

	/**
	 * Muove l'animale dalla sua posizione ad una regione d'arrivo. Per farlo muovere imposta un 
	 * numero di passi prefissato che deve compiere e, ogni passo calcola il dx e il dy della retta,
	 * approssimata poichè i pixel sono discreti, che unisce le coordinate correnti dell'animale alle
	 * coordinate della sua destinazione. Dobbiamo ripetere questo calcolo ogni iterazione altrimenti 
	 * abbiamo un errore nella traiettoria dovuto al fatto che i pizel sono discreti.
	 * 
	 * @param animale animale da muovere.
	 * @param arrivo regione di arrivo.
	 */
	public void muoviAnimale(Animale animale, Regione arrivo){
		RegioneView regioneViewArrivo = istanzeView.cercaRegioneView(arrivo);
		AnimaleView animaleView = istanzeView.cercaAnimaleView(animale);
		Point posizioneArrivo;
		Point puntoTemporaneo  = new Point(animaleView.getCoordinate());
		if (animale instanceof Lupo){
			posizioneArrivo = regioneViewArrivo.getCoordinateLupo();
		}else if (animale instanceof PecoraNera){
			posizioneArrivo = regioneViewArrivo.getCoordinatePecoraNera();
		}else {
			animaleView.setInMovimento(true);
			posizioneArrivo = new Point(regioneViewArrivo.getCoordinateOvini());
			posizioneArrivo.x += arrivo.getListaPecoreArieti().size() * Costanti.OFFSET_STAMPA_OVINI;
			posizioneArrivo.y += arrivo.getListaPecoreArieti().size() * Costanti.OFFSET_STAMPA_OVINI;
		}
		for (int i = 1; i < Costanti.PASSI_PER_MOVIMENTO_ANIMALE; i++) {
			int dx = (posizioneArrivo.x - puntoTemporaneo.x)/(Costanti.PASSI_PER_MOVIMENTO_ANIMALE - i);
			int dy = (posizioneArrivo.y - puntoTemporaneo.y)/(Costanti.PASSI_PER_MOVIMENTO_ANIMALE - i);
			puntoTemporaneo.translate(dx, dy);
			animaleView.setCoordinate(puntoTemporaneo);
			try {
				TimeUnit.MILLISECONDS.sleep((int)(Costanti.INTERVALLO_DI_MOVIMENTO_ANIMALE / Costanti.PASSI_PER_MOVIMENTO_ANIMALE));
			} catch (InterruptedException e) {
				new FinestraNotifica("il thread in pausa durante il movimento dell'animale ha terminato l'attesa in maniera inaspettata", e);
			}
		}
		animaleView.setInMovimento(false);
	}

	/**
	 * L'algoritmo di movimento è identico a quello del movimento dell'animale.
	 * 
	 * @param pastore pastore da muovere.
	 * @param partenza casella di partenza.
	 * @param arrivo casella di arrivo.
	 */
	public void muoviPastore (Pastore pastore, Casella partenza, Casella arrivo){
	
		PastoreView pastoreView = istanzeView.cercaPastoreView(pastore);
		CasellaView casellaPartenzaView = istanzeView.cercaCasellaView(partenza);
		CasellaView casellaArrivoView = istanzeView.cercaCasellaView(arrivo);
		Point puntoTemporaneo = new Point(casellaPartenzaView.getCoordinate());
		for (int i = 1; i < Costanti.PASSI_PER_MOVIMENTO_PASTORE; i++) {
			int dx = (casellaArrivoView.getCoordinate().x - puntoTemporaneo.x)/(Costanti.PASSI_PER_MOVIMENTO_PASTORE - i);
			int dy = (casellaArrivoView.getCoordinate().y - puntoTemporaneo.y)/(Costanti.PASSI_PER_MOVIMENTO_PASTORE - i);
			puntoTemporaneo.translate(dx, dy);
			pastoreView.setCoordinate(puntoTemporaneo);
			try {
				TimeUnit.MILLISECONDS.sleep(Costanti.INTERVALLO_DI_MOVIMENTO_PASTORE / Costanti.PASSI_PER_MOVIMENTO_PASTORE);
			} catch (InterruptedException e) {
				new FinestraNotifica("il thread in pausa durante il movimento del pastore ha terminato l'attesa in maniera inaspettata", e);
			}
		}
	}
	
	/**
	 * Stampa i componenti a partire dalle coordinate delle istanze view corrispondenti. In caso
	 * di regioni o caselle selezionabili le evidenzia e, in caso debba farlo, stampa le animazioni 
	 * di lancio dado per lupo e pecora nera.
	 * 
	 *  @param g grafica del componente.
	 * 
	 */
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(immagineMappa, 0, 0, this);
		stampaSfondoCaselle(g);
		g.drawImage(immagineCaselle, 0, 0, this);
		stampaRegioniSelezionabili(g);
		stampaRecinti(g);
		stampaAnimali(g);
		stampaPastori(g);
		stampaCarte(g);
		if (lanciaDadoLupo) {
			stampaDadoLupo(g);
		}
		if (lanciaDadoPecoraNera) {
			stampaDadoPecoraNera(g);
		}
	}

	/**
	 * Imposta le caselle ricevute da parametro ad essere selezionabili e si mette in attessa che l'utente 
	 * clicchi su una di esse. Quando avviene l'evento di click cerchiamo la casella che dista dal punto del click al più un 
	 * diametro dalla casella selezionata tra quelle disponibili. Se non la troviamo,
	 * significa che essa è occupata e l'utente dovrà cliccare nuovamente. Prima di tornare le caselle vengono
	 * nuovamente impostate come non selezionabili.
	 * 
	 * @param listaCaselle lista tra le quali scegliere.
	 * @return casella scelta.
	 */
	public synchronized Casella scegliCasella(List<Casella> listaCaselle) {
		selezionaCaselle = true;
		Casella casellaScelta = null;
		coordinateClick = null;
		Iterator<Casella> scorriCaselle = listaCaselle.iterator();
		while (scorriCaselle.hasNext()){
			scorriCaselle.next().setSelezionabile(true);
		}

		do{
			try {
				wait();
			} catch (InterruptedException e) {
				new FinestraNotifica("il thread in attesa della scelta della casella ha terminato l'attesa in maniera inaspettata", e);
			}
			scorriCaselle = listaCaselle.iterator();
			while(scorriCaselle.hasNext()){
				Casella casella = scorriCaselle.next();
				CasellaView casellaView = istanzeView.cercaCasellaView(casella);
				Point coordinateCasella = casellaView.getCoordinate();
				if (coordinateClick != null){
					int distanza = Math.abs(coordinateClick.x - coordinateCasella.x) + Math.abs(coordinateClick.y - coordinateCasella.y);
					if (distanza < Costanti.DIAMETRO_CASELLA*Costanti.MARGINE_ERRORE_COORDINATE_CASELLA) {
						casellaScelta = casellaView.getCasella();
					}
				}
			}
		}
		while(casellaScelta == null);

		scorriCaselle = listaCaselle.iterator();
		while (scorriCaselle.hasNext()){
			scorriCaselle.next().setSelezionabile(false);
		}
		selezionaCaselle = false;
		return casellaScelta;
	}

	/**
	 * Sfrutta lo scegli casella. Infatti prendiamo la posizione dei pastori e le passiamo allo
	 * scegli casella. Una volta scelta la casella torna il pastore che la occupa.
	 * 
	 * @param listaPastori lista di pastori tra cui scegliere.
	 * @return pastore scelto.
	 */
	public Pastore scegliPastore(List<Pastore> listaPastori) {
		Pastore pastoreScelto = null;
		Iterator<Pastore> scorriPastori = listaPastori.iterator();
		List<Casella> caselleSelezionabili = new ArrayList<Casella>();
		while(scorriPastori.hasNext()){
			Pastore pastore = scorriPastori.next();
			pastore.getPosizione().setSelezionabile(true);
			caselleSelezionabili.add(pastore.getPosizione());
		}
		Casella casella = scegliCasella(caselleSelezionabili);
		scorriPastori = listaPastori.iterator();
		while(scorriPastori.hasNext()){
			Pastore pastore = scorriPastori.next();
			if(casella == pastore.getPosizione()){
				pastoreScelto = pastore;
			}
			pastore.getPosizione().setSelezionabile(false);
		}
		return pastoreScelto;	
	}

	/**
	 * Imposta le regioni ricevute da parametro ad essere selezionabili e si mette in attessa che l'utente 
	 * clicchi su una di esse. Quando avviene l'evento di click l'action listener imposta la regione scelta e
	 * notifica l'avvenuta scelta. Prima di tornare la regione scelta le regioni vengono impostate nuovamente 
	 * non selezionabili.
	 * 
	 * @param listaRegioni lista di regioni tra le quali scegliere.
	 * @return regione scelta.
	 */
	public synchronized Regione scegliRegione(List<Regione> listaRegioni) {
		regioneScelta = null;
		selezionaRegioni = true;
		Iterator<Regione> scorriRegioni = listaRegioni.iterator();
		while(scorriRegioni.hasNext()){
			Regione regione = scorriRegioni.next();
			RegioneView regioneView = istanzeView.cercaRegioneView(regione);
			regioneView.getRegione().setSelezionabile(true);
		}
		try {
			wait();
		} catch (InterruptedException e) {
			new FinestraNotifica("il thread in attesa della scelta della regione ha terminato l'attesa in maniera inaspettata", e);
		}
		scorriRegioni = listaRegioni.iterator();
		while(scorriRegioni.hasNext()){
			Regione regione = scorriRegioni.next();
			regione.setSelezionabile(false);
		}
		selezionaRegioni = false;
		return regioneScelta;	
	}

	/**
	 * 
	 * @return boolean che determina se l'animazione del lancio del dado del lupo è in corso.
	 */
	public boolean staLanciandoDadoLupo() {
		return lanciaDadoLupo;
	}

	/**
	 * 
	 * @return boolean che determina se l'animazione del lancio del dado della pecora nera è in corso.
	 */
	public boolean staLanciandoDadoPecoraNera() {
		return lanciaDadoPecoraNera;
	}

	/**
	 * Stampa gli animali con l'immagine e le coordinate delle loro corrispondenti istanze view.
	 * Nel caso di ovini verifica che la regione non contenga più del massimo consentito, allora stampa
	 * l'immagine del gruppo di animali. Inoltre calcola l'offset col quale devono essere stampati gli ovini
	 * in maniera tale che non si sovrappongano.
	 * 
	 * @param g grafica del componente.
	 */
	private void stampaAnimali (Graphics g){
		Iterator<RegioneView> scorriRegioni = istanzeView.getListaRegioniView().iterator();
		while (scorriRegioni.hasNext()){
			boolean stampaGruppo = false;
			RegioneView regioneView = scorriRegioni.next();
			int offset = 0;
			List<Animale> listaOvini = regioneView.getRegione().getListaOvini();
			if (listaOvini.size() >= Costanti.NUMERO_ANIMALI_GRUPPO){
				stampaGruppo = true;
				g.drawImage(immagineGruppo,regioneView.getCoordinateOvini().x, regioneView.getCoordinateOvini().y,this);
			}
			Iterator<Animale> scorriOvini = listaOvini.iterator();
			while (scorriOvini.hasNext()){
				Animale ovino = scorriOvini.next();
				AnimaleView ovinoView = istanzeView.cercaAnimaleView(ovino);
				if (ovinoView.isInMovimento()){
					g.drawImage(ovinoView.getImmagine(),ovinoView.getCoordinate().x,ovinoView.getCoordinate().y,this);
				}else if (!stampaGruppo){
					g.drawImage(ovinoView.getImmagine(), regioneView.getCoordinateOvini().x + offset, regioneView.getCoordinateOvini().y + offset, this);
					offset += Costanti.OFFSET_STAMPA_OVINI;
				}
			}

			if(regioneView.getRegione().contienePecoraNera() || regioneView.getRegione().contieneLupo()){
				Iterator<Animale> scorriAnimali = regioneView.getRegione().getListaAnimali().iterator();
				while (scorriAnimali.hasNext()){
					Animale animale = scorriAnimali.next();
					if (animale instanceof PecoraNera){
						PecoraNeraView pecoraNeraView = (PecoraNeraView) istanzeView.cercaAnimaleView(animale);
						g.drawImage(pecoraNeraView.getImmagine(), pecoraNeraView.getCoordinate().x, pecoraNeraView.getCoordinate().y, this);
					}else if (animale instanceof Lupo){
						LupoView lupoView = (LupoView) istanzeView.cercaAnimaleView(animale);
						g.drawImage(lupoView.getImmagine(), lupoView.getCoordinate().x, lupoView.getCoordinate().y, this);

					}
				}
			}
		}
	}

	/**
	 * Stampa sul bordo superiore le carte con una stringa contenente il numero di carte 
	 * disponibili per tipo.
	 * 
	 * @param g grafica del componente.
	 */
	private void stampaCarte (Graphics g) {
		Iterator<CartaView> scorriCarte = istanzeView.getListaCarteView().iterator();
		Point coordinateCarta = new Point (9 * Costanti.RAGGIO_RECINTO + 4 * Costanti.DISTANZA_BOTTONI_CARTA, 0);
		int offset = 0;
		while (scorriCarte.hasNext()){
			CartaView cartaView = scorriCarte.next();
			g.drawImage(cartaView.getImmagine(), coordinateCarta.x + offset, coordinateCarta.y, this);
			g.drawString("x"+cartaView.getCarta().getCarteDisponibili(), coordinateCarta.x + offset, coordinateCarta.y + Costanti.LARGHEZZA_CARTA + Costanti.ALTEZZA_FONT / 2);
			offset += Costanti.LARGHEZZA_CARTA * 5 / 4;
		}
	}

	/**
	 * Stampa l'animazione del dado del lupo a partire da tempoInizioStampaDado, quindi verifica
	 * che il tempo non sia scaduto e stampa l'immagine del risultato del dado. Quando anche questo 
	 * tempo è scaduto l'animazione sparisce.
	 * 
	 * @param g grafica del componente.
	 */
	private void stampaDadoLupo(Graphics g) {
		if (System.currentTimeMillis() - tempoInizioStampaDadoLupo < Costanti.TEMPO_VISIBILITA_DADO){
			g.drawImage(animazioneDadoLupo, stampaLancioDado.x, stampaLancioDado.y, this);
		}else if (System.currentTimeMillis() - tempoInizioStampaDadoLupo >= Costanti.TEMPO_VISIBILITA_DADO + Costanti.TEMPO_VISIBILITA_RISULTATO_DADO){
			lanciaDadoLupo = false;
		}else if (System.currentTimeMillis() - tempoInizioStampaDadoLupo >= Costanti.TEMPO_VISIBILITA_DADO){
			g.drawImage(immaginiDadoLupo.get(dadoLupo-1), stampaLancioDado.x, stampaLancioDado.y, this);
		}
	}

	/**
	 * Stampa l'animazione del dado della pecora nera a partire da tempoInizioStampaDado, quindi verifica
	 * che il tempo non sia scaduto e stampa l'immagine del risultato del dado. Quando anche questo 
	 * tempo è scaduto l'animazione sparisce.
	 * 
	 * @param g grafica del componente.
	 */
	private void stampaDadoPecoraNera(Graphics g) {
		if (System.currentTimeMillis() - tempoInizioStampaDadoPecoraNera < Costanti.TEMPO_VISIBILITA_DADO) {
			g.drawImage(animazioneDadoPecoraNera, stampaLancioDado.x,
					stampaLancioDado.y, this);
		}else if (System.currentTimeMillis() - tempoInizioStampaDadoPecoraNera >= Costanti.TEMPO_VISIBILITA_DADO + Costanti.TEMPO_VISIBILITA_RISULTATO_DADO){
			lanciaDadoPecoraNera = false;
		}else if (System.currentTimeMillis()- tempoInizioStampaDadoPecoraNera >= Costanti.TEMPO_VISIBILITA_DADO) {
			g.drawImage(immaginiDadoPecoraNera.get(dadoPecoraNera - 1),
					stampaLancioDado.x, stampaLancioDado.y, this);
		}


	}

	/**
	 * Stampa i pastori con l'immagine e le coordinate dell'istanza view corrispondente.
	 * 
	 * @param g grafica del componente.
	 */
	private void stampaPastori(Graphics g) { 
		Iterator<PastoreView> scorriPastori = istanzeView.getListaPastoriView().iterator();
		while (scorriPastori.hasNext()){
			PastoreView pastoreView = scorriPastori.next();
			Point offsetPoint = new Point();
			BufferedImage immaginePastore;
			immaginePastore = pastoreView.getImmagine();
			offsetPoint.x = Costanti.LARGHEZZA_PASTORE / 2;
			offsetPoint.y = Costanti.ALTEZZA_PASTORE / 2;
			g.drawImage(immaginePastore, pastoreView.getCoordinate().x -offsetPoint.x, pastoreView.getCoordinate().y-offsetPoint.y, this);
		}
	}

	/**
	 * Stampa i recinti sulle coordinate della casella corrispondente, nonchè l'immagine
	 * dei recinti in alto per indicare il numero di recinti e recinti finali disponibili.
	 * 
	 * @param g grafica del componente.
	 */
	private void stampaRecinti(Graphics g) {
		Iterator<CasellaView> scorriCaselle = istanzeView.getListaCaselleView().iterator();
		while (scorriCaselle.hasNext()){
			CasellaView casellaView = scorriCaselle.next();
			if (casellaView.getCasella().isOccupataRecinto()){
				BufferedImage immagineRecinto = casellaView.getRecinto();
				int x = casellaView.getCoordinate().x - (Costanti.RAGGIO_RECINTO);
				int y = casellaView.getCoordinate().y - (Costanti.RAGGIO_RECINTO);
				g.drawImage(immagineRecinto, x, y, this);
			}else if (casellaView.getCasella().isOccupataRecintoFinale()){
				BufferedImage immagineRecintoFinale = casellaView.getRecintoFinale();
				int x = casellaView.getCoordinate().x - (Costanti.RAGGIO_RECINTO);
				int y = casellaView.getCoordinate().y - (Costanti.RAGGIO_RECINTO);
				g.drawImage(immagineRecintoFinale, x, y, this);
			}
		}
		//stampa l'indicatore di recinti, il doppio più grandi di quelli che appaiono sulla mappa
		BufferedImage recinto = null;
		BufferedImage recintoFinale = null;
		try {
			recinto = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_RECINTO));
			recinto = Thumbnails.of(recinto).scale(Costanti.FATTORE_SCALA * 2).asBufferedImage();
			recintoFinale = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_RECINTO_FINALE));
			recintoFinale = Thumbnails.of(recintoFinale).scale(Costanti.FATTORE_SCALA * 2).asBufferedImage();
		} catch (IOException e) { 
			new FinestraNotifica("Errore durante l'apertura di una o più immagini associate agli oggetti \"recinto\" e \"recinto finale\"", e);
		}
		g.drawImage(recinto, 0, 0, this);
		g.drawString("x"+Integer.toString(statoGioco.getNumeroRecinti()), 2 * Costanti.RAGGIO_RECINTO, 4 * Costanti.RAGGIO_RECINTO + Costanti.ALTEZZA_FONT / 2);
		g.drawImage(recintoFinale, Costanti.RAGGIO_RECINTO * 5, 0, this);
		g.drawString("x"+Integer.toString(statoGioco.getNumeroRecintiFinali()), 6 * Costanti.RAGGIO_RECINTO, 4 * Costanti.RAGGIO_RECINTO + Costanti.ALTEZZA_FONT / 2);
	}

	/**
	 * Nel caso le regioni siano selezionabili stampa l'immagine dell'istanza view corrispondente.
	 * 
	 * @param g grafica del componente.
	 */
	private void stampaRegioniSelezionabili(Graphics g) {
		Iterator<RegioneView> scorriRegioni = istanzeView.getListaRegioniView().iterator();
		while (scorriRegioni.hasNext()){
			RegioneView regioneView  = scorriRegioni.next();
			if (regioneView.getRegione().isSelezionabile()){
				g.drawImage(regioneView.getImmagine(), 0, 0, this);
			}
		}
	}

	/**
	 * Nel caso le caselle siano selezionabili stampa l'immagine dell'istanza view corrispondente.
	 * 
	 * @param g grafica del componente.
	 */
	private void stampaSfondoCaselle(Graphics g) {
		Iterator<CasellaView> scorriCaselle = istanzeView.getListaCaselleView().iterator();
		while (scorriCaselle.hasNext()){
			CasellaView casellaView = scorriCaselle.next();
			if (casellaView.getCasella().isSelezionabile()){
				Image immagineCasella = casellaView.getSfondoCasella();
				int x = casellaView.getCoordinate().x - Costanti.GRANDEZZA_SFONDO_CASELLA / 2;
				int y = casellaView.getCoordinate().y - Costanti.GRANDEZZA_SFONDO_CASELLA / 2;
				g.drawImage(immagineCasella, x, y, this);
			}
		}
	}

	/**
	 * Metodo non utilizzato: per implementare l'interfaccia
	 * del mouse listener è obbligatorio fare Overriding
	 * di tutti i suoi metodi. Questo, e gli altri vuoti,
	 * non si sono rivelati utili per i nostri scopi, dunque
	 * sono stati lasciati in bianco.
	*/
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	/**
	 * Metodo non utilizzato: per implementare l'interfaccia
	 * del mouse listener è obbligatorio fare Overriding
	 * di tutti i suoi metodi. Questo, e gli altri vuoti,
	 * non si sono rivelati utili per i nostri scopi, dunque
	 * sono stati lasciati in bianco.
	*/
	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	/**
	 * Metodo non utilizzato: per implementare l'interfaccia
	 * del mouse listener è obbligatorio fare Overriding
	 * di tutti i suoi metodi. Questo, e gli altri vuoti,
	 * non si sono rivelati utili per i nostri scopi, dunque
	 * sono stati lasciati in bianco.
	*/
	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	/**
	 * Metodo non utilizzato: per implementare l'interfaccia
	 * del mouse listener è obbligatorio fare Overriding
	 * di tutti i suoi metodi. Questo, e gli altri vuoti,
	 * non si sono rivelati utili per i nostri scopi, dunque
	 * sono stati lasciati in bianco.
	*/
	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

}
