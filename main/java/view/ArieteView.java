package view;

import java.io.IOException;

import javax.imageio.ImageIO;

import model.Ariete;
import model.Costanti;
import net.coobird.thumbnailator.Thumbnails;

/**
 * Classe view associata agli arieti. Contiene semplicemente l'immagine dell'ariete.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class ArieteView extends AnimaleView {
	/**
	 * Chiama il super costruttore animaleView associando l'oggetto all'ariete
	 * preso da parametro. Carica l'immagine dell'ariete.
	 * 
	 * @param ariete ariete associato a questa view.
	 */
	public ArieteView(Ariete ariete) {
		super(ariete);
		try {
			immagine = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_ARIETE));
			immagine = Thumbnails.of(immagine).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura dell'immagine associata all'oggetto \"Ariete\"", e);
		}
	}

}
