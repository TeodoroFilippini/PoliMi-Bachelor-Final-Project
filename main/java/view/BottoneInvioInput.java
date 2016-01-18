package view;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import model.Costanti;
import net.coobird.thumbnailator.Thumbnails;

/**
 * Classe che implementa il bottone associato all'invio
 * di un input, utilizzato nel menu per far scrivere l'indirizzo
 * IP del server ad un giocatore di gioco online che 
 * cerca una partita.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class BottoneInvioInput extends JButton{
	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore della classe.
	 * Apre l'immagine associata al bottone e crea il bottone.
	 */
	public BottoneInvioInput (){
		BufferedImage immagine = null;
		try {
			immagine = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_BOTTONE + "OK" + ".png"));
			immagine= Thumbnails.of(immagine).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura dell'immagine associata al bottone di invio input", e);
		}
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);
		setIcon(new ImageIcon(immagine));
	}
}
