package view;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Costanti;
import model.Regione;
import net.coobird.thumbnailator.Thumbnails;
/**
 * Classe view associata alle regioni. Utilizzata per stampare la sua immagine in caso
 * possa essere selezionata e per stampare gli animali nelle coordinate corrette.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class RegioneView {

	private Point coordinateLupo;
	private Point coordinateOvini;
	private Point coordinatePecoraNera;
	private BufferedImage immagineSelezionabile;
	private Regione regione;
	/**
	 * Crea una nuova istanza di regione view associandogli una regione.
	 * Vengono inoltre caricate le coordinate per la stampa di lupo, pecora nera e ovini
	 * e caricata l'immagine della regione, quando selezionabile, usando il nome della regione associata.
	 * Tale immagine è trasparente ovunque se non sulla regione che appare rossa.
	 * 
	 * @param regione regione associata a questa view.
	 * @param coordinateLupo coordinate di stampa per il lupo.
	 * @param coordinatePecoraNera coordinate di stampa per la pecora nera.
	 * @param coordinateOvini coordinate di stampa per gli ovini.
	 */
	public RegioneView(Regione regione,Point coordinateLupo, Point coordinatePecoraNera,Point coordinateOvini) {
		this.coordinateLupo = coordinateLupo;
		this.coordinatePecoraNera = coordinatePecoraNera;
		this.coordinateOvini = coordinateOvini;
		this.regione = regione;
		try {
			immagineSelezionabile = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_REGIONI+this.toString()+".png"));
			immagineSelezionabile = Thumbnails.of(immagineSelezionabile).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura dell'immagine selezionabile di uno degli oggetti \"regione\"", e);
		}
	}

	/**
	 * 
	 * @return coordinate di stampa del lupo.
	 */
	public Point getCoordinateLupo() {
		return coordinateLupo;
	}

	/**
	 * 
	 * @return coordinate di stampa per gli ovini.
	 */
	public Point getCoordinateOvini() {
		return coordinateOvini;
	}

	/**
	 * 
	 * @return cooridnate di stampa per la pecora nera.
	 */
	public Point getCoordinatePecoraNera() {
		return coordinatePecoraNera;
	}

	/**
	 * 
	 * @return immagine della regione nel caso sia selezionabile.
	 */
	public BufferedImage getImmagine() {
		return immagineSelezionabile;
	}

	/**
	 * 
	 * @return regione associata a questa istanza view.
	 */
	public Regione getRegione(){
		return regione;
	}
	
	/**
	 * @return toString della regione associata
	 */
	@Override
	public String toString(){
		return regione.toString();
	}

}
