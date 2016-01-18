package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Costanti;
import model.Giocatore;
import net.coobird.thumbnailator.Thumbnails;
/**
 * Pannello contenente i dati sui danari dei vari giocatori.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class PannelloGiocatori extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private List<Giocatore> listaGiocatori;
	private BufferedImage immagineDenaro;
	private List<JLabel> listaLabel = new ArrayList<JLabel>();
	/**
	 * Carica la lista di giocatori dei quali vogliamo stampare i denari, fa partire
	 * il timer utilizzato per aggiornare i dati dei giocatori ogni tot millisecondi,
	 * imposta il layout come GridLayout con 3 colonne, una per l'immagine del giocatore,
	 * la seconda per l'immagine dei danari e la terza per la label con l'ammontare di danari per giocatore,
	 * e tante righe quante sono i giocatori. Carica l'immagine dei danari e crea le label da aggiungere
	 * sul pannello. Viene impostato lo sfondo del pannello a trasparente, ridimensionato e impostato il cursore personalizzato
	 * tramite l'apposito metodo di StrumentiView.
	 * @param listaGiocatori
	 */
	public PannelloGiocatori(List<Giocatore> listaGiocatori){
		this.listaGiocatori = listaGiocatori;
		Timer timer = new Timer(50,this);
		timer.start();
		setLayout(new GridLayout(listaGiocatori.size(),3));
		try {
			immagineDenaro = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_DENARO));
			immagineDenaro = Thumbnails.of(immagineDenaro).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura dell'immagine associata al denaro", e);
		}
		creaLabel();
		setOpaque(false);
		setCursor(StrumentiView.getCursore());
		setPreferredSize(new Dimension(Costanti.DIMENSIONE_PANNELLI_LATERALI.width,Costanti.DIMENSIONE_PANNELLI_LATERALI.height/2));
	}

	/**
	 * Per ogni giocatore vengono create 3 label: una con la sua immagine, una con l'immagine
	 * generica dei danari e una con il numero di danari del giocatore. Le label vengono quindi aggiunte
	 * ad al pannello e quelle contenenti il numero di danari ad una lista utilizzata per l'aggiornamento periodico.
	 */
	private void creaLabel() { 
		Iterator<Giocatore> scorriGiocatori = listaGiocatori.iterator();
		while(scorriGiocatori.hasNext()){
			Giocatore giocatore = scorriGiocatori.next();
			BufferedImage immagine = null;
			try {
				immagine = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_PASTORE+Integer.toString(giocatore.getNumero())+".png"));
				immagine = Thumbnails.of(immagine).scale(2*Costanti.FATTORE_SCALA).asBufferedImage();
			} catch (IOException e) {
				new FinestraNotifica("Errore nell'apertura dell'immagine associata alla pedina del giocatore", e);
			}
			add(new JLabel(new ImageIcon(immagine)));
			add(new JLabel(new ImageIcon(immagineDenaro)));
			JLabel label = new JLabel("x"+Integer.toString(giocatore.getDanari()));
			listaLabel .add(label);
			add(label);
		}
	}

	/**
	 * Ogni volta che scatta il timer si scorre la lista delle label da aggiornare e si 
	 * imposta il nuovo valore di danari del giocatore corrispondente.
	 * 
	 * @param e evento di timer che invoca questo metodo.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Iterator<JLabel> scorriLabel = listaLabel.iterator();
		Iterator<Giocatore> scorriGiocatori = listaGiocatori.iterator();
		while (scorriLabel.hasNext() & scorriGiocatori.hasNext()){
			JLabel label = scorriLabel.next();
			label.setText("x"+Integer.toString(scorriGiocatori.next().getDanari()));
		}
	}

}
