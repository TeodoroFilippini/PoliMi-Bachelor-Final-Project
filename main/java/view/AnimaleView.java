package view;

import java.awt.Point;
import java.awt.image.BufferedImage;

import model.Animale;
/**
 * Classe padre di tutti le classi view degli animali.
 * Contiene l'animale associato alla view, l'immagine che verrà 
 * caricata dalle varie estensioni e le coordinate da utilizzare per stampare
 * l'animale.
 * 
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class AnimaleView{
	private Animale animale;
	private Point coordinate;
	protected BufferedImage immagine;
	private boolean inMovimento = false; 

	/**
	 * Crea un nuovo oggetto animale view associato all'animale 
	 * passato da parametro.
	 * @param animale animale associato alla view.
	 */
	public AnimaleView(Animale animale) {
		this.animale = animale;
	}

	/**
	 * @return animale associato alla view.
	 */
	public Animale getAnimale() {
		return animale;
	}
	/**
	 * 
	 * @return le coordinate per stampare l'animale.
	 */
	public Point getCoordinate() {
		return coordinate;
	}
	/**
	 * 
	 * @return l'immagine dell'animale.
	 */
	public BufferedImage getImmagine(){
		return immagine;
	}
	/**
	 * Imposta le coordinate dell'animale.
	 * 
	 * @param coordinate coordinate che dovrà assumere l'animale.
	 */
	public void setCoordinate(Point coordinate){
		this.coordinate = coordinate;
	}
	
	/**
	 * Imposta il boolean che determina se l'animale è in movimento o meno.
	 * 
	 * @param inMovimento boolean che determina se l'animale è in movimento.
	 */
	public void setInMovimento(boolean inMovimento){
		this.inMovimento = inMovimento;
	}
	/**
	 * 
	 * @return boolean che determina se l'animale è in movimento.
	 */
	public boolean isInMovimento(){
		return inMovimento;
	}
}
