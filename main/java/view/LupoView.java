package view;

import java.io.IOException;

import javax.imageio.ImageIO;

import model.Costanti;
import model.Lupo;
import net.coobird.thumbnailator.Thumbnails;
/**
 * Classe view associata ai lupi. Contiene semplicemente l'immagine del lupo.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class LupoView extends AnimaleView {
	/**
	 * Chiama il super costruttore animaleView associando l'oggetto al
	 * lupo presa da parametro. Carica l'immagine del lupo.
	 * 
	 * @param lupo lupo associato a questa view.
	 */
	public LupoView(Lupo lupo) {
		super(lupo);
		try {
			immagine = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_LUPO));
			immagine = Thumbnails.of(immagine).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura dell'immagine associata all'oggetto \"Lupo\"", e);
		}
	}
}
