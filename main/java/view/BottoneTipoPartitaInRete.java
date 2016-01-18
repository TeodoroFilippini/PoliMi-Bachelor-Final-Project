package view;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import model.Costanti;
import model.TipoPartitaInRete;
import net.coobird.thumbnailator.Thumbnails;

/**
 * Classe che implementa il bottone associato alla
 * scelta del tipo di partita in rete (host o client) 
 * utilizzato nel Menu.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class BottoneTipoPartitaInRete extends JButton {

	private static final long serialVersionUID = 1L;
	private TipoPartitaInRete tipoPartita;

	/**
	 * Costruttore della classe, che carica l'immagine associata
	 * e crea il bottone.
	 * @param tipoPartita il tipo di partita desiderato.
	 */
	public BottoneTipoPartitaInRete (TipoPartitaInRete tipoPartita){
		this.tipoPartita = tipoPartita;
		BufferedImage immagine = null;
		try {
			immagine = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_BOTTONE + tipoPartita.toString() + ".png"));
			immagine= Thumbnails.of(immagine).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura dell'immagine associata al bottone di scelta del tipo di partita in rete", e);
		}
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);
		setIcon(new ImageIcon(immagine));
	}

	/**
	 * 
	 * @return il tipo di partita in rete.
	 */
	public TipoPartitaInRete getTipoPartita(){
		return tipoPartita;
	}

}
