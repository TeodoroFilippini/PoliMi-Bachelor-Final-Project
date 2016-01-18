package view;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import model.Costanti;
import model.TipoRete;
import net.coobird.thumbnailator.Thumbnails;

/**
 * Classe che implementa il bottone associato alla
 * scelta del tipo di rete (RMI o Socket), utilizzato 
 * nel Menu.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class BottoneTipoRete extends JButton{

	private static final long serialVersionUID = 1L;
	private TipoRete tipoRete;

	/**
	 * Costruttore della classe.
	 * @param tipoRete il tipo di rete da stampare, e che il
	 * 		  bottone rappresenterà, tra RMI e Socket.
	 */
	public BottoneTipoRete (TipoRete tipoRete){
		this.tipoRete = tipoRete;
		BufferedImage immagine = null;
		try {
			immagine = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_BOTTONE + tipoRete.toString() + ".png"));
			immagine= Thumbnails.of(immagine).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura dell'immagine associata al bottone di scelta del tipo di connessione di rete", e);
		}
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);
		setIcon(new ImageIcon(immagine));
	}

	public TipoRete getTipoRete(){
		return tipoRete;
	}
}

