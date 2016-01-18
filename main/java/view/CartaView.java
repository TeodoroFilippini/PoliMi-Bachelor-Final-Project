package view;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Carta;
import model.Costanti;
import net.coobird.thumbnailator.Thumbnails;

public class CartaView {

	private Carta carta;
	BufferedImage immagine; 

	public CartaView(Carta carta){
		this.carta = carta;
		try {
			immagine = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_TESSERE+carta.toString()+".png"));
			immagine = Thumbnails.of(immagine).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura dell'immagine associata all'oggetto \"Carta\"", e);
		}
	}

	public Carta getCarta() {
		return carta;
	}

	public BufferedImage getImmagine() {
		return immagine;
	}
}
