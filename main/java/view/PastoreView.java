package view;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Costanti;
import model.Pastore;
import net.coobird.thumbnailator.Thumbnails;

/**
 * Classe view associata ai pastori.
 * Contiene il pastore associato alla view, l'immagine del pastore
 * e le coordinate da utilizzare per stampare il pastore.
 * 
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class PastoreView {

	private Point coordinate;
	private BufferedImage immagine;
	private boolean inMovimento = false;
	private Pastore pastore;

	/**
	 * Crea un nuvo oggetto associato al pastore preso da parametro.
	 * Carica l'immagine del pastore.
	 * @param pastore pastore associato alla view.
	 */
	public PastoreView (Pastore pastore) {
		this.pastore = pastore;
		int numeroGiocatore = pastore.getGiocatore().getNumero();
		try {
			immagine = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_PASTORE + Integer.toString(numeroGiocatore)+".png"));
			immagine = Thumbnails.of(immagine).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura dell'immagine associata all'oggetto \"pastore\"", e);
		}
	}

	/**
	 * 
	 * @return coordinate di stampa del pastore.
	 */
	public Point getCoordinate() {
		return coordinate;
	}
	
	/**
	 * Imposta le coordinate di stampa del pastore.
	 * 
	 * @param coordinate coordinate di stampa del pastore.
	 */
	public void setCoordinate(Point coordinate) {
		this.coordinate = coordinate;
	}
	
	/**
	 * 
	 * @return immagine del pastore.
	 */
	public BufferedImage getImmagine(){
		return immagine;
	}
	
	/**
	 * 
	 * @return pastore associato ala view.
	 */
	public Pastore getPastore() {
		return pastore;
	}
	
	/**
	 * Imposta il boolean che determina se l'animale è in movimento o meno.
	 * 
	 * @param inMovimento boolean che determina se l'animale è in movimento.
	 */
	public void setInMovimento(boolean inMovimento){
		this.inMovimento = inMovimento;
	}
	
	/**
	 * 
	 * @return boolean che determina se l'animale è in movimento.
	 */
	public boolean isInMovimento(){
		return inMovimento;
	}
}