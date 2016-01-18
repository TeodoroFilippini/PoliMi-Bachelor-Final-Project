package view;

import java.io.IOException;

import javax.imageio.ImageIO;

import model.Agnello;
import model.Costanti;
import net.coobird.thumbnailator.Thumbnails;
/**
 * Classe view associata agli agnelli. Contiene semplicemente l'immagine dell'agnello.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class AgnelloView extends AnimaleView {
	/**
	 * Chiama il super costruttore animaleView associando l'oggetto all'agnello
	 * preso da parametro. Carica l'immagine dell'agnello.
	 * 
	 * @param agnello agnello associato a questa view.
	 */
	public AgnelloView(Agnello agnello) {
		super(agnello);
		try {
			immagine = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_AGNELLO));
			immagine = Thumbnails.of(immagine).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura dell'immagine associata all'oggetto \"Agnello\"", e);
		}
	}
}
