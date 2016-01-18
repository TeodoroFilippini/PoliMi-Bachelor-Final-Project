package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe che descrive la regione nella mappa, intesa come
 * una delle tre porzioni di un tipo di terreno, suddivisa 
 * da strade che collegano le caselle che la circondano. Al suo
 * interno possono esserci soltanto animali, che, se indipendenti,
 * si sposteranno da una regione all'altra tramite una casella,
 * se no (pecore, arieti e agnelli) saranno spostati da un pastore
 * sito su una delle caselle adiacenti alla regione che è loro 
 * posizione. Ogni regione ha un ID univoco che la identifica
 * nell'ordine della sua creazione, e torna comodo per fare confronti 
 * tra oggetti regione serializzati. Il metodo equals() infatti non 
 * è adeguato per la presenza di attributi non primitivi.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */

public class Regione extends ElementoMappa{
	
	private static int contatoreiD = 0;
	private static final long serialVersionUID = 1L;
	private int iD;
	private List<Animale> listaAnimali = new ArrayList<Animale>();
	private String nome; 
	private boolean selezionabile = false;
	private TipoTerreno tipo;

	/**
	 * Costruttore dell'oggetto Regione.
	 * @param nome il nome univoco della regione, utilizzato dal
	 * 		  parser del file Mappa.xml.
	 * @param tipo il tipo di terreno associato alla regione.
	 */
	public Regione(String nome, TipoTerreno tipo) {
		this.iD = contatoreiD;
		contatoreiD ++;
		this.nome = nome;
		this.tipo = tipo;
	}

	/**
	 * Aggiunge un animale alla lista degli animali contenuti
	 * nella regione.
	 * @param animale l'animale da aggiungere.
	 */
	public void aggiungiAnimale(Animale animale){
		listaAnimali.add(animale);
	}

	/**
	 * 
	 * @return true se ci sono arieti nella regione al momento,
	 *         false se no.
	 */
	public boolean contieneArieti(){
		Iterator<Animale> scorriAnimali = listaAnimali.iterator();
		while(scorriAnimali.hasNext()){
			if (scorriAnimali.next() instanceof Ariete) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @return true se c'è il lupo nella regione al momento,
	 * 		   false se no.
	 */
	public boolean contieneLupo() {
		Iterator<Animale> scorriAnimali = listaAnimali.iterator();
		while(scorriAnimali.hasNext()){
			if (scorriAnimali.next() instanceof Lupo) {
				return true;
			}
		}
		return false;	
	}

	/**
	 * 
	 * @return true se c'è la pecora nera nella regione al momento,
	 * 		   false se no.
	 */
	public boolean contienePecoraNera() {
		Iterator<Animale> scorriAnimali = listaAnimali.iterator();
		while(scorriAnimali.hasNext()){
			if (scorriAnimali.next() instanceof PecoraNera) {
				return true;
			}
		}
		return false;	
	}
	
	/**
	 * 
	 * @return true se c'è almeno una pecora nella regione al momento,
	 * 		   false se no.
	 */
	public boolean contienePecore(){
		Iterator<Animale> scorriAnimali = listaAnimali.iterator();
		while(scorriAnimali.hasNext()){
			if (scorriAnimali.next() instanceof Pecora) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @return l'ID univoco della regione.
	 */
	public int getID() {
		return iD;
	}

	/**
	 * 
	 * @return la lista degli agnelli siti nella regione.
	 */
	public List<Agnello> getListaAgnelli(){
		List<Agnello> listaAgnelli = new ArrayList<Agnello>();
		Iterator<Animale> scorriAnimali = listaAnimali.iterator();
		while (scorriAnimali.hasNext()){
			Animale animale = scorriAnimali.next();
			if (animale instanceof Agnello) {
				listaAgnelli.add((Agnello) animale);
			}
		}
		return listaAgnelli;
	}

	
	/**
	 * 
	 * @return la lista degli animali siti nella regione.
	 */
	public List<Animale> getListaAnimali() {
		return listaAnimali;
	}

	/**
	 * 
	 * @return la lista degli animali non indipendenti siti
	 *		   nella regione: arieti, pecore e agnelli.
	 */
	public List<Animale> getListaOvini(){
		List<Animale> listaOvini = new ArrayList<Animale>();
		Iterator<Animale> scorriAnimali = listaAnimali.iterator();
		while (scorriAnimali.hasNext()){
			Animale animale = scorriAnimali.next();
			if (animale instanceof Pecora || animale instanceof Ariete || animale instanceof Agnello) {
				listaOvini.add(animale);
			}
		}
		return listaOvini;
	}

	/**
	 * 
	 * @return la lista degli animali spostabili siti nella regione:
	 * 		   pecore e arieti.
	 */
	public List<Animale> getListaPecoreArieti(){
		List<Animale> listaPecoreArieti = new ArrayList<Animale>();
		Iterator<Animale> scorriAnimali = listaAnimali.iterator();
		while (scorriAnimali.hasNext()){
			Animale animale = scorriAnimali.next();
			if (animale instanceof Pecora || animale instanceof Ariete) {
				listaPecoreArieti.add(animale);
			}
		}
		return listaPecoreArieti;
	}

	/**
	 * 
	 * @return il tipo di terreno associato alla regione.
	 */
	public TipoTerreno getTipo() {
		return tipo;
	}

	/**
	 * 
	 * @return true se la regione è correntemente selezionabile,
	 * 		   false se no.
	 */
	public boolean isSelezionabile (){
		return selezionabile;
	}
	
	/**
	 * Rimuove un animale dalla regione.
	 * @param animale l'animale da rimuovere.
	 */
	public void rimuoviAnimale(Animale animale){
		listaAnimali.remove(animale);
	}

	/**
	 * Imposta la regione selezionabile o meno.
	 * @param selezionabile il boolean che descrive come
	 * 		  va impostata la variabile "selezionabile".
	 */
	public void setSelezionabile(boolean selezionabile) {
		this.selezionabile  = selezionabile;
	}

	/**
	 * Metodo usato da FinestraMessaggi per stampare il nome di una regione
	 * durante il gioco.
	 * @return il tipo della regione scritto in lettere minuscole, tranne
	 * 			la prima, maiuscola.
	 */
	public String getNomeRegione(){
		String nomeRegione = tipo.toString();
		return nomeRegione.substring(0,1).toUpperCase()
				+ nomeRegione.substring(1).toLowerCase();
	}
	
	/**
	 * @return la stringa che descrive univocamente la regione,
	 * 		   utilizzata durante il parsing dall'XMLParser.
	 */
	@Override
	public String toString(){
		return nome;
	}
}
