package model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.UIManager;

import view.FinestraNotifica;

/**
 * Classe statica che raccoglie tutte le costanti utilizzate nel gioco. 
 * il metodo "static" serve a creare costanti a compile time: lo usiamo 
 * per caricare il font e dimensionare la finestra del gioco ad una 
 * grandezza di poco inferiore al full screen. Di qui, calcoliamo la 
 * fondamentale costante "FATTORE_SCALA", che scalerà tutte le componenti 
 * grafiche del gioco di conseguenza.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public final class Costanti {
	
	public static final int ALTEZZA_MAPPA = 1515;
	public static final Point CENTRO_SCHERMO;
	public static final double FATTORE_SCALA;
	public static final int LARGHEZZA_MAPPA = 1410;
	private static Font FONT_PERSONALIZZATO;

	static{
		 //moltiplichiamo per 0.9 per avere un margine rispetto alla grandezza dello schermo								
		FATTORE_SCALA = (double) Toolkit.getDefaultToolkit().getScreenSize().height / (double)ALTEZZA_MAPPA *0.9 ;
		CENTRO_SCHERMO = new Point (Toolkit.getDefaultToolkit().getScreenSize().width /2,Toolkit.getDefaultToolkit().getScreenSize().height /2);
		/*Apriamo il font customizzato incluso nel pacchetto, e poi lo settiamo come predefinito per bottonie  labels*/
		try {
			FONT_PERSONALIZZATO = Font.createFont(Font.TRUETYPE_FONT, Costanti.class.getResourceAsStream("/varie/font.ttf"));
			GraphicsEnvironment ge = 
					GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(FONT_PERSONALIZZATO);
			FONT_PERSONALIZZATO = FONT_PERSONALIZZATO.deriveFont(19f);
		} catch (IOException|FontFormatException e) {	
			new FinestraNotifica("Errore nel caricamento del font", e);
		}
		UIManager.put("Button.font", FONT_PERSONALIZZATO);
		UIManager.put("Label.font", FONT_PERSONALIZZATO);
		UIManager.put("Panel.font", FONT_PERSONALIZZATO);
		UIManager.put("Label.background", Color.BLACK);
	}
	
	public static final int ALTEZZA_BOTTONE = (int)(150 * FATTORE_SCALA);
	//dado non scalabile, poichè gif
	public static final int ALTEZZA_DADO = 263; 
	public static final int ALTEZZA_DENARO = (int) (117 * FATTORE_SCALA);
	public static final int ALTEZZA_FINESTRA_ANIMALI = (int)(300 * FATTORE_SCALA);
	public static final int ALTEZZA_FINESTRA_CARTE = (int)(200 * FATTORE_SCALA);
	public static final int ALTEZZA_FINESTRA_MESSAGGI = 50;
	public static final int ALTEZZA_FONT = (int)(60 * FATTORE_SCALA);
	public static final int ALTEZZA_PASTORE = (int) (75 * FATTORE_SCALA);
	public static final int ALTEZZA_PASTORE_SELEZIONABILE = (int) (103 * FATTORE_SCALA);
	public static final int ALTEZZA_RIQUADRO_GIOCATORE = (int)(450 * FATTORE_SCALA);
	public static final int ALTEZZA_TOTALE_BOTTONI = 7 * ALTEZZA_BOTTONE;
	public static final int AZIONI_PER_TURNO = 3;
	public static final int COSTO_CARTA_INIZIALE = 0;
	public static final int COSTO_CORRUZIONE = 2;
	public static final int COSTO_MASSIMO_CARTA = 4;
	public static final int COSTO_SPOSTAMENTO = 1;
	public static final int DANARI_INIZIALI = 20;
	public static final int DANARI_INIZIALI_2_GIOCATORI = 30;
	public static final int DIAMETRO_CASELLA = (int) (50 * FATTORE_SCALA);
	public static final Dimension DIMENSIONE_MAPPA = new Dimension((int)(LARGHEZZA_MAPPA*FATTORE_SCALA),(int)(ALTEZZA_MAPPA*FATTORE_SCALA));
	public static final Dimension DIMENSIONE_PANNELLI_LATERALI = new Dimension((int)(500 * FATTORE_SCALA) ,DIMENSIONE_MAPPA.height);
	public static final Dimension DIMENZIONE_COMBO_BOX_GIOCATORI = new Dimension(100,20);
	public static final int DISTANZA_BOTTONI_CARTA = (int)(20 * FATTORE_SCALA);
	public static final int DISTANZA_BOTTONI_MENU = 100;
	public static final int GRANDEZZA_SFONDO_CASELLA = (int) (80* FATTORE_SCALA);
	//intervalli in millisecondi
	public static final long INTERVALLO_DI_MOVIMENTO_ANIMALE = 1000; 
	public static final int INTERVALLO_DI_MOVIMENTO_PASTORE = 500; 
	public static final int LARGHEZZA_BOTTONE = (int)(300 * FATTORE_SCALA);
	public static final int LARGHEZZA_CARTA = (int)(125 * FATTORE_SCALA);
	//non scalabile, poichè gif
	public static final int LARGHEZZA_DADO = 168; 
	public static final int LARGHEZZA_DENARO = (int) (162 * FATTORE_SCALA);
	public static final int LARGHEZZA_FINESTRA_ANIMALI = (int)(150 * FATTORE_SCALA) ;
	public static final int LARGHEZZA_FINESTRA_CARTE = (int)(300 * FATTORE_SCALA);
	public static final int LARGHEZZA_PASTORE = (int) (69 * FATTORE_SCALA);
	public static final int LARGHEZZA_PASTORE_SELEZIONABILE = (int) (98 * FATTORE_SCALA);
	public static final int LARGHEZZA_RIQUADRO_GIOCATORE = (int)(500 * FATTORE_SCALA);
	//quanto siamo stati imprecisi nel prendere il centro della casella: 1 = massima imprecisione (diametro), 0.5 = massima precisione (raggio)
	public static final double MARGINE_ERRORE_COORDINATE_CASELLA = 0.55; 
	public static final int NUMERO_ANIMALI_GRUPPO = 3;
	public static final int NUMERO_CARTE_DISPONIBLI = 5;
	public static final int NUMERO_GIOCATORI_MASSIMO = 4;
	public static final int NUMERO_RECINTI = 20;
	public static final int NUMERO_RECINTI_FINALI = 12;
	//a numero di terreni si sottrae 1 perchè l'ultimo tipo di terreno è Sheepsburg
	public static final int NUMERO_TERRENI = TipoTerreno.values().length-1; 
	public static final int NUMERO_TURNI_CRESCITA_AGNELLO = 2;
	public static final int OFFSET_IMMAGINE_PERGAMENA = (int)(75 * FATTORE_SCALA);
	public static final int OFFSET_STAMPA_OVINI = (int)(30 * FATTORE_SCALA);
	public static final int PASSI_PER_MOVIMENTO_ANIMALE = 40;
	public static final int PASSI_PER_MOVIMENTO_PASTORE = 20;
	public static final int PASTORI_PER_2_GIOCATORI = 2;

	public static final String PERCORSO_FILE_AGNELLO = "/pedine/agnello.png";
	public static final String PERCORSO_FILE_ARIETE = "/pedine/ariete.png";
	public static final String PERCORSO_FILE_BOTTONE = "/bottoni/bottone";
	public static final String PERCORSO_FILE_BOTTONE_CONFERMA = "/bottoni/bottoneCONFERMA.png";
	public static final String PERCORSO_FILE_BOTTONE_NON_SELEZIONABILE = "/bottoni/bottoneNonSelezionabile";
	public static final String PERCORSO_FILE_CURSORE = "/varie/cursore.png";
	public static final String PERCORSO_FILE_DADI = "/dado/dado";
	public static final String PERCORSO_FILE_DADO_LUPO = "/dado/dadoLupo.gif";
	public static final String PERCORSO_FILE_DADO_PECORA_NERA = "/dado/dadoPecoraNera.gif";
	public static final String PERCORSO_FILE_DENARO = "/interfacciaGiocatore/denaro.png";
	public static final String PERCORSO_FILE_DENARO_CARTA = "/tessere/costoCarta";
	public static final String PERCORSO_FILE_GRUPPO = "/pedine/gruppo.png";
	public static final String PERCORSO_FILE_LUPO = "/pedine/lupo.png";
	public static final String PERCORSO_FILE_MAPPA = "/mappa/mappa.png";
	public static final String PERCORSO_FILE_MAPPA_CASELLE = "/mappa/caselle.png";
	public static final String PERCORSO_FILE_MENU = "/varie/menu.png";
	public static final String PERCORSO_FILE_PASTORE = "/pedine/pastori/pastore";
	public static final String PERCORSO_FILE_PECORA = "/pedine/pecora.png";
	public static final String PERCORSO_FILE_PECORA_NERA = "/pedine/pecoraNera.png";
	public static final String PERCORSO_FILE_RECINTO = "/mappa/recinto.png";
	public static final String PERCORSO_FILE_RECINTO_FINALE = "/mappa/recintoFinale.png";
	public static final String PERCORSO_FILE_REGIONI = "/regioni/";
	public static final String PERCORSO_FILE_RIQUADRO_GIOCATORE = "/interfacciaGiocatore/riquadroGiocatore.png";
	public static final String PERCORSO_FILE_SFONDO = "/mappa/sfondo.png";
	public static final String PERCORSO_FILE_SFONDOCASELLA = "/mappa/sfondoCasella.png";
	public static final String PERCORSO_FILE_TESSERE = "/tessere/";
	public static final String PERCORSO_FILE_XML = "/mappa/mappa.xml";
	public static final Point POSIZIONE_BOTTONE_OFFLINE = new Point((int)(LARGHEZZA_MAPPA * FATTORE_SCALA * 1/5),(int)(ALTEZZA_MAPPA * FATTORE_SCALA *2/3));
	public static final Point POSIZIONE_BOTTONE_ONLINE = new Point((int)(LARGHEZZA_MAPPA * FATTORE_SCALA * 2/3),(int)(ALTEZZA_MAPPA * FATTORE_SCALA *2/3));
	public static final int RAGGIO_RECINTO = (int) (34 * FATTORE_SCALA);
	//tutti i tempi sono in millisecondi
	public static final long TEMPO_VISIBILITA_DADO = 2000; 
	public static final int TEMPO_VISIBILITA_FINESTRA = 2000; 
	public static final long TEMPO_VISIBILITA_MENU = 2000; 
	public static final long TEMPO_VISIBILITA_RISULTATO_DADO = 1200; 
	public static final int VALORE_PECORA_NERA = 2;
	public static final int DISTANZA_INFERIORE_BOTTONI_MENU = 80;
	public static final String NOME_SERVER = "SheeplandServer";
	public static final int PORTA_SERVER = 7777;
	public static final int OFFSET_MOVIMENTO_SFONDO = 10;
	//velocità in millisecondi per pixel
	public static final int VELOCITA_MOVIMENTO_SFONDO = 750; 

	/**
	 * Costruttore privato vuoto, inserito per non rendere la
	 * classe costruibile.
	 */
	private Costanti(){
	}
}
