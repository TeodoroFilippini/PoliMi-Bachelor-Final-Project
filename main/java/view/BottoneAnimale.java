package view;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import model.Animale;

/**
 * Classe che implementa il bottone associato alla
 * scelta di un animale dentro ad una FinestraAnimali
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class BottoneAnimale extends JButton{
	private static final long serialVersionUID = 1L;
	private AnimaleView animaleView;
	private BufferedImage immagine;
	private int numeroAnimali;

	/**
	 * Costruttore della classe. Crea il bottone, che stampa il tipo
	 * dell'animale associato. Al bottone è associato il primo animale 
	 * della lista.
	 * @param listaAnimaliView la lista di animali di un tipo da 
	 * 		  mettere nel bottone
	 */
	public BottoneAnimale(List<AnimaleView> listaAnimaliView) {
		this.animaleView = listaAnimaliView.get(0);
		this.numeroAnimali = listaAnimaliView.size();
		this.immagine = animaleView.getImmagine();
		setContentAreaFilled(false);
		setBorder(BorderFactory.createEmptyBorder());
		setIcon(new ImageIcon(immagine));
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder());
	}

	/**
	 * 
	 * @return l'animale associato al bottone.
	 */
	public Animale getAnimale(){
		return animaleView.getAnimale();
	}

	/**
	 * 
	 * @return l'immagine del bottone.
	 */
	public BufferedImage getImmagine(){
		return immagine;
	}

	/**
	 * 
	 * @return il numero di animali dello stesso tipo
	 * 		   di quello rappresentato dal bottone.
	 */
	public int getNumeroAnimali() {
		return numeroAnimali;
	}
}
