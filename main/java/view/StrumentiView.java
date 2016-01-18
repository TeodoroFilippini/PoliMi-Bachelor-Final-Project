package view;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import model.Costanti;
import net.coobird.thumbnailator.Thumbnails;

/**
 * Classe d'appoggio per eseguire ricerche in altre classi
 * della view. i suoi metodi sono, infatti, tutti statici
 * e pubblici.
 *  
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */

public class StrumentiView {
	/**
	 * Costruttore privato utilizzato per nascondere quello pubblico di default,
	 * infatti questa classe non deve poter essere istanziata.
	 */
	private StrumentiView(){
		
	}
	/**
	 * Carica l'immagine del bottone di conferma 
	 * e crea un nuovo JButton senza bordi la cui icona
	 * è l'immagine caricata
	 * @return il bottone creato
	 */
	public static JButton creaBottoneConferma() {
		BufferedImage immagineConferma = null;
		try {
			immagineConferma = ImageIO.read(StrumentiView.class.getResourceAsStream(Costanti.PERCORSO_FILE_BOTTONE_CONFERMA));
			immagineConferma = Thumbnails.of(immagineConferma).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura dell'immagine del bottone \"conferma acquisto\"", e);
		}
		JButton bottoneConferma = new JButton(new ImageIcon(immagineConferma));
		bottoneConferma.setBorder(BorderFactory.createEmptyBorder());
		bottoneConferma.setContentAreaFilled(false);
		return bottoneConferma;
	}
	/**
	 *  Crea un cursore con l'immagine del nostro cursore personalizzato
	 *   
	 * @return il cursore creato
	 */
	public static Cursor getCursore(){
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Point hotSpot = new Point(0,0);
		BufferedImage cursore;
		Cursor cursor = null;
		try {
			cursore = ImageIO.read(StrumentiView.class.getResourceAsStream(Costanti.PERCORSO_FILE_CURSORE));
			cursor = toolkit.createCustomCursor(cursore, hotSpot, "PointingSheep");
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'impostazione del cursore del mouse", e);
		}
		return cursor;
	}
}
