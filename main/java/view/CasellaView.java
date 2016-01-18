package view;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Casella;
import model.Costanti;
import net.coobird.thumbnailator.Thumbnails;

public class CasellaView{
	private Casella casella;
	private Point coordinate;
	private BufferedImage recinto;
	private BufferedImage recintoFinale;
	private BufferedImage sfondoCasella;

	public CasellaView (Casella casella, Point coordinate){
		this.casella = casella;
		this.coordinate = coordinate;
		try {
			sfondoCasella = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_SFONDOCASELLA));
			sfondoCasella = Thumbnails.of(sfondoCasella).scale(Costanti.FATTORE_SCALA).asBufferedImage();
			recinto = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_RECINTO));
			recintoFinale = ImageIO.read(getClass().getResourceAsStream(Costanti.PERCORSO_FILE_RECINTO_FINALE));
			recinto = Thumbnails.of(recinto).scale(Costanti.FATTORE_SCALA).asBufferedImage();
			recintoFinale = Thumbnails.of(recintoFinale).scale(Costanti.FATTORE_SCALA).asBufferedImage();
		} catch (IOException e) {
			new FinestraNotifica("Errore nell'apertura dell'immagine \"sfondo casella\", \"recinto\" o \"recinto finale\"", e);
		}

	}

	public Casella getCasella() {
		return casella;
	}

	public Point getCoordinate() {
		return coordinate;
	}

	public BufferedImage getRecinto() {
		return recinto;
	}

	public BufferedImage getRecintoFinale() {
		return recintoFinale;
	}

	public BufferedImage getSfondoCasella() {
		return sfondoCasella;
	}
	@Override
	public String toString(){
		return casella.toString();
	}

}
