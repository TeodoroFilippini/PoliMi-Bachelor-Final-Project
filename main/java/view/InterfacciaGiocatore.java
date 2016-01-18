package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Carta;
import model.Costanti;
import model.Giocatore;
import net.coobird.thumbnailator.Thumbnails;
import controller.StrumentiController;
/**
 * Pannello utilizzato per visualizzari i dati del giocatore alla quale
 * è associata l'interfaccia.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class InterfacciaGiocatore extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private Point coordinateAzioniRimanenti;
	private Point coordinateCarte;
	private Point coordinateDenaro;
	private Point coordinateRiquadro = new Point(0,0);
	private Point coordinateScritta;
	private Giocatore giocatore;
	private BufferedImage immagineDenaro;
	private BufferedImage immagineRiquadro;
	private List<Carta> listaCarte;
	private List<CartaView> listaCarteView = new ArrayList<CartaView>();
	private Point offsetStampaCarte;

	/**
	 * Carica la lista di carte del giocatore e setta le impostazioni iniziali del pannello.
	 * Fa partire il timer per il repaint.
	 * 
	 * @param giocatore giocatore del quale si vuole visualizzare i dati.
	 */
	public InterfacciaGiocatore(Giocatore giocatore) {
		super();
		listaCarte = StrumentiController.getListaCarteSenzaRipetizioni(giocatore.getListaCarte());
		creaListaCarteView();
		Timer timer = new Timer(100,this);
		timer.start();
		this.giocatore = giocatore;
		setCursor(StrumentiView.getCursore());
		setBorder(BorderFactory.createEmptyBorder());
		setPreferredSize(new Dimension(Costanti.DIMENSIONE_PANNELLI_LATERALI.width,Costanti.DIMENSIONE_PANNELLI_LATERALI.height/2));
		setOpaque(false);
		caricaImmagini();
		inizializzaCoordinate();
		calcolaOffsetStampaCarte();
	}

	/**
	 * Calcola la distanza alla quale vengono stampate le carte a seconda della loro quantità-
	 */
	private void calcolaOffsetStampaCarte() {
		Point offset;
		switch (StrumentiController.getListaCarteSenzaRipetizioni(giocatore.getListaCarte()).size()){
		case 1:
		case 2:
			offset = new Point(50,30);
			break;
		case 3:
		case 4:
			offset = new Point(35, 20);
			break;
		case 5:
		case 6:
		default:
			offset = new Point(25, 15);
			break;
		}
		offsetStampaCarte = new Point((int)(offset.x * Costanti.FATTORE_SCALA), (int)(offset.y * Costanti.FATTORE_SCALA));
	}

	/**
	 * Carica le immagini.
	 */
	private void caricaImmagini() {
		try {
			immagineRiquadro = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_RIQUADRO_GIOCATORE));
			immagineDenaro = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_DENARO));
			immagineRiquadro = Thumbnails.of(immagineRiquadro).scale(Costanti.FATTORE_SCALA).asBufferedImage();
			immagineDenaro = Thumbnails.of(immagineDenaro).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura di una o più immagini associate all'interfaccia grafica del giocatore", e);
		}
	}

	/**
	 * Inizializza le coordinate di stampa dei vari componenti.
	 */
	private void inizializzaCoordinate() {
		coordinateDenaro = new Point(coordinateRiquadro.x + Costanti.ALTEZZA_FONT, 
				coordinateRiquadro.y + Costanti.ALTEZZA_FONT * 2 + Costanti.OFFSET_IMMAGINE_PERGAMENA);
		coordinateCarte = new Point(coordinateDenaro.x + Costanti.LARGHEZZA_DENARO, coordinateDenaro.y - Costanti.ALTEZZA_FONT / 2);
		coordinateScritta = new Point(coordinateRiquadro.x + Costanti.ALTEZZA_RIQUADRO_GIOCATORE / 3, 
				coordinateRiquadro.y + Costanti.OFFSET_IMMAGINE_PERGAMENA + Costanti.ALTEZZA_FONT / 2);
		coordinateAzioniRimanenti = new Point(coordinateRiquadro.x + Costanti.ALTEZZA_RIQUADRO_GIOCATORE / 4, 
				coordinateRiquadro.y + Costanti.ALTEZZA_FONT + Costanti.OFFSET_IMMAGINE_PERGAMENA);
	}

	/**
	 * Aggiunge una carta qualora questa non venga già stampata e ricalcola l'offset.
	 * 
	 * @param carta carta che si vuole aggiungere.
	 */
	public void aggiungiCarta(Carta carta) {
		Iterator<Carta> scorriCarte = listaCarte.iterator();
		while(scorriCarte.hasNext()){
			Carta cartaCorrente = scorriCarte.next();	
			if (cartaCorrente.getTipo() == carta.getTipo()) {
				return;
			}
		}
		listaCarteView.add(new CartaView(carta));
		listaCarte.add(carta);
		calcolaOffsetStampaCarte();
	}

	/**
	 * Rimuove le carte view nel caso in cui la carta corrispondente non faccia più
	 * parte della lista carte del giocatore.
	 */
	public void rimuoviCarteView() {
		List<CartaView> listaAggiornata = new ArrayList<CartaView>();
		Iterator<CartaView> scorriCarteView = listaCarteView.iterator();
		while (scorriCarteView.hasNext()){
			CartaView cartaView = scorriCarteView.next();
			if (giocatore.getNumeroCarte(cartaView.getCarta().getTipo()) > 0) {
				listaAggiornata.add(cartaView);
			}
		}
		listaCarteView = listaAggiornata;
	}

	/**
	 * Cerca le carte view della lista carte del giocatore.
	 */
	private void creaListaCarteView() {
		Iterator<Carta> scorriCarte = listaCarte.iterator();
		while (scorriCarte.hasNext()){
			Carta carta = scorriCarte.next();
			listaCarteView.add(new CartaView(carta));
		}
	}

	/**
	 * Stampa tutti i componenti nelle cooridnate che sono state inizializzate.
	 * 
	 * @param g grafica del componente.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//stampiamo, in ordine: riquadro, stringhe con le informazioni, denaro e carte
		g.drawImage(immagineRiquadro, coordinateRiquadro.x, coordinateRiquadro.y, this);
		g.drawString("GIOCATORE "+Integer.toString(giocatore.getNumero()), coordinateScritta.x, coordinateScritta.y);
		g.drawString("AZIONI RIMANENTI: "+giocatore.getAzioniRimanentiPerTurno(), coordinateAzioniRimanenti.x, coordinateAzioniRimanenti.y);
		g.drawImage(immagineDenaro, coordinateDenaro.x, coordinateDenaro.y, this);
		g.drawString(Integer.toString(giocatore.getDanari())+" Denari", coordinateDenaro.x, coordinateDenaro.y);
		stampaCarteGiocatore(g);
	}

	/**
	 * Stampa le carte nelle coordinate calcolate in precedenza.
	 * 
	 * @param g grafica del componente
	 */
	private void stampaCarteGiocatore(Graphics g) {
		Iterator<CartaView> scorriCarte = listaCarteView.iterator(); 
		while(scorriCarte.hasNext()){
			CartaView cartaView = scorriCarte.next();
			g.drawImage(cartaView.getImmagine(), coordinateCarte.x, coordinateCarte.y, this);
			g.drawString("x"+ Integer.toString(giocatore.getNumeroCarte(cartaView.getCarta().getTipo())), coordinateCarte.x + Costanti.LARGHEZZA_CARTA* 2/3, coordinateCarte.y);
			coordinateCarte.x += offsetStampaCarte.x;
			coordinateCarte.y += offsetStampaCarte.y;
		}
		coordinateCarte.x -= offsetStampaCarte.x * listaCarteView.size();
		coordinateCarte.y -= offsetStampaCarte.y * listaCarteView.size();
		//sottraggo l'offset totale (offset singolo * numero di interazioni del ciclo) alle coordinate delle carte, per la prossima stampa.
	}

	/**
	 * ristampa il componente ogni volta che scatta il timer.
	 * 
	 * @param e evento di timer.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}


}
