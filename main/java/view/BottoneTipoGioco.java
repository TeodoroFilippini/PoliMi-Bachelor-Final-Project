package view;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import model.Costanti;
import model.TipoGioco;
import net.coobird.thumbnailator.Thumbnails;

public class BottoneTipoGioco extends JButton{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TipoGioco tipoGioco;

	/**
	 * Costruttore della classe.
	 * @param tipoGioco il tipo di gioco (online o offline)
	 * 		  associato al bottone.
	 */
	public BottoneTipoGioco (TipoGioco tipoGioco){
		this.tipoGioco = tipoGioco;
		BufferedImage immagine = null;
		try {
			immagine = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_BOTTONE + tipoGioco.toString() + ".png"));
			immagine= Thumbnails.of(immagine).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura dell'immagine associata al bottone di scelta del tipo di gioco", e);
		}
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);
		setIcon(new ImageIcon(immagine));
	}

	/**
	 * 
	 * @return il tipo di gioco associato al bottone.
	 */
	public TipoGioco getTipoGioco(){
		return tipoGioco;
	}
}

