package view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import model.Carta;
import model.Costanti;
import net.coobird.thumbnailator.Thumbnails;

/**
 * Classe che implementa il bottone associato alla
 * scelta di una carta dentro ad una FinestraCarte
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class BottoneCarta extends JButton{
	private static final long serialVersionUID = 1L;
	private CartaView cartaView;
	private BufferedImage immagineCarta;
	private int costoCarta;

	/**
	 * Costruttore della classe.
	 * @param cartaView la carta view associata al bottone.
	 * @param costoCarta il costo della carta da stampare.
	 */
	public BottoneCarta(CartaView cartaView, int costoCarta) {
		this.cartaView = cartaView;
		this.immagineCarta = cartaView.getImmagine();
		this.costoCarta = costoCarta;	
		if (costoCarta > 0) {
			setIcon(new ImageIcon(creaImmagineCartaConCosto()));
		} else {
			setIcon(new ImageIcon(immagineCarta));
		}
		setContentAreaFilled(false);
		setBorder(BorderFactory.createEmptyBorder());
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);
	}

	/**
	 *  Metodo che crea l'immagine della carta e ci stampa sopra il costo
	 *  associato.
	 * @return l'immagine creata.
	 */
	private BufferedImage creaImmagineCartaConCosto(){
		BufferedImage immagineCosto = null;
		BufferedImage immagineCartaConCosto = new BufferedImage(immagineCarta.getWidth(),immagineCarta.getHeight(), BufferedImage.TYPE_INT_ARGB );
		Graphics2D g = immagineCartaConCosto.createGraphics();
		try {
			immagineCosto = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_DENARO_CARTA+Integer.toString(costoCarta)+".png"));
			immagineCosto = Thumbnails.of(immagineCosto).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura dell'immagine associata all'oggetto \"Costo della carta\"", e);
		}
		g.drawImage(immagineCarta,0,0,null);
		g.drawImage(immagineCosto,0,0,null);
		g.dispose();
		return immagineCartaConCosto;
	}

	/**
	 * 
	 * @return la carta associata al bottone.
	 */
	public Carta getCarta(){
		return cartaView.getCarta();
	}

	/**
	 * 
	 * @return il costo della carta associata al bottone.
	 */
	public int getCostoCarta(){
		return costoCarta;
	}
}
