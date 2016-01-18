package view;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.Costanti;
import net.coobird.thumbnailator.Thumbnails;
/**
 * Classe utilizzata per creare un pannello di sfondo alle finestre da utilizzare
 * come contenitore per altri componenti da aggiungere, i quali, perchè lo sfondo
 * possa essere visibile, dovranno avere sfondo trasparente.
 * 
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class PannelloSfondo extends JPanel{

	private static final long serialVersionUID = 1L;
	BufferedImage immagineSfondo;

	/**
	 * Crea un nuovo pannello con l'immagine di sfondo che ha il path passato 
	 * come parametro. Viene creato un JPanel con BorderLayout, settato il cursore
	 * con l'apposito metodo di StrumentiView e caricata l'immagine di sfondo.
	 * 
	 * @param percorsoImmagineSfondo path dell'immagine di sfondo da caricare.
	 */
	public PannelloSfondo(String percorsoImmagineSfondo){
		super(new BorderLayout());
		setCursor(StrumentiView.getCursore());
		try {
			immagineSfondo = ImageIO.read(getClass().getResourceAsStream(percorsoImmagineSfondo));
			immagineSfondo = Thumbnails.of(immagineSfondo).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura dell'immagine dello sfondo", e);
		}
	}
	
	/**
	 *Stampa l'immagine di sfondo in 0,0 chiamando l'apposito metodo di JPanel e graphics.
	 *
	 * @param g grafica del componente.
	 */
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(immagineSfondo,0,0,this);
	}

}
