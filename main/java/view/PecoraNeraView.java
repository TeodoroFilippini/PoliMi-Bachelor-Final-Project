package view;

import java.io.IOException;

import javax.imageio.ImageIO;

import model.Costanti;
import model.PecoraNera;
import net.coobird.thumbnailator.Thumbnails;
/**
 * Classe view associata alle pecoreNere. Contiene semplicemente l'immagine della pecoraNera.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class PecoraNeraView extends AnimaleView {
	/**
	 * Chiama il super costruttore animaleView associando l'oggetto alla 
	 * pecora nera presa da parametro. Carica l'immagine della pecora nrea.
	 * 
	 * @param pecoraNera pecora nera associata a questa view.
	 */
	public PecoraNeraView(PecoraNera pecoraNera) {
		super(pecoraNera);
		try {
			immagine = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_PECORA_NERA));
			immagine = Thumbnails.of(immagine).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura dell'immagine associata all'oggetto \"pecora\"", e);
		}
	}

}
