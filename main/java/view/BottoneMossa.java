package view;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import model.Costanti;
import model.TipoMossa;
import net.coobird.thumbnailator.Thumbnails;

/**
 * Classe che implementa il bottone associato alla
 * scelta della mossa dentro al PannelloBottoni.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class BottoneMossa extends JButton {

	private static final long serialVersionUID = 1L;
	private BufferedImage immagine;
	private TipoMossa mossa;

	/**
	 * Costruttore della classe.
	 * @param mossa la mossa associata al bottone.
	 */
	public BottoneMossa (TipoMossa mossa){
		super();
		this.mossa = mossa;
		caricaImmagini();
		setIcon(new ImageIcon(immagine));
		setOpaque(false);
		setContentAreaFilled(false);
		setBorder(BorderFactory.createEmptyBorder());
	}

	/**
	 * Metodo che carica da file le immagini associate ai bottoni mossa.
	 */
	private void caricaImmagini() {
		try {
			immagine = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_BOTTONE + mossa.toString() + ".png"));
			immagine = Thumbnails.of(immagine).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura dell'immagine associata all'oggetto \"bottone mossa "+mossa.toString()+"\"", e);
		}
	}

	/**
	 * 
	 * @return l'immagine associata al bottone.
	 */
	public Image getImmagine() {
		return immagine;
	}

	/**
	 * 
	 * @return il tipo di mossa associato al bottone.
	 */
	public TipoMossa getMossa() {
		return mossa;
	}

	/**
	 * @return la mstringa che rappresenta la mossa.
	 */
	@Override
	public String toString(){
		return mossa.toString();
	}

}
