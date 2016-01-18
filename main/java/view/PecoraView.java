package view;

import java.io.IOException;

import javax.imageio.ImageIO;

import model.Costanti;
import model.Pecora;
import net.coobird.thumbnailator.Thumbnails;
/**
 * Classe view associata alle pecore. Contiene semplicemente l'immagine della pecora.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class PecoraView extends AnimaleView {
	/**
	 * Chiama il super costruttore animaleView associando l'oggetto alla 
	 * pecora presa da parametro. Carica l'immagine della pecora.
	 * 
	 * @param pecora pecora associata a questa view.
	 */
	public PecoraView(Pecora pecora) {
		super(pecora);
		try {
			immagine = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_PECORA));
			immagine = Thumbnails.of(immagine).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura dell'immagine associata all'oggetto \"pecora\"", e);
		}
	}

}
