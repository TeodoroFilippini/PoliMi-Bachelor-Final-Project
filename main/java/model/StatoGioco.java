package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che tiene traccia dello stato corrente di tutti gli oggetti del gioco.
 * è la classe più importante del model e funge da grande contenitore di oggetti.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */

public class StatoGioco implements Serializable{

	private static final long serialVersionUID = 1L;
	private Giocatore giocatoreCorrente;
	private boolean giocoFinito = false;
	private List<Carta> listaCarte = new ArrayList<Carta>();
	private List<Giocatore> listaGiocatori = new ArrayList<Giocatore>();
	private List<Pastore> listaPastori = new ArrayList<Pastore>();
	private Lupo lupo;
	private Mappa mappa;
	private int numeroGiocatori;
	private int numeroRecinti = Costanti.NUMERO_RECINTI;
	private int numeroRecintiFinali = Costanti.NUMERO_RECINTI_FINALI;
	private PecoraNera pecoraNera;
	private TipoGioco tipoGioco;
	private boolean ultimoTurno = false;
	private List<CartaInVendita> listaCarteInVendita = new ArrayList<CartaInVendita>();

	/**
	 * Costruttore dello statoGioco.
	 * @param numeroGiocatori il numero di giocatori della partita.
	 */
	public StatoGioco(int numeroGiocatori){
		this.numeroGiocatori = numeroGiocatori;
	}

	/**
	 * Diminuisce di uno il numero di recinti disponibili.
	 */
	public void decrementaRecinti(){
		numeroRecinti--;
	}

	/**
	 * Diminuisce di uno il numero di recinti finali disponibili.
	 */
	public void decrementaRecintiFinali(){
		numeroRecintiFinali--;
	}

	/**
	 * 
	 * @return il giocatore correntemente impegnato nel gioco: quello 
	 * 		   del quale uno dei pastori sta effettuando o deve effettuare una
	 * 		   mossa.
	 */
	public Giocatore getGiocatoreCorrente() {
		return giocatoreCorrente;
	}

	/**
	 * 
	 * @return la lista di tutte le carte presenti nel gioco.
	 */
	public List<Carta> getListaCarte() {
		return listaCarte;
	}

	/**
	 * 
	 * @return la lista di tutti i giocatori presenti nel gioco.
	 */
	public List<Giocatore> getListaGiocatori() {
		return listaGiocatori;
	}

	/**
	 * 
	 * @return la lista di tutti i pastori presenti nel gioco.
	 */
	public List<Pastore> getListaPastori() {
		return listaPastori;
	}

	/**
	 * 
	 * @return il lupo.
	 */
	public Lupo getLupo() {
		return lupo;
	}

	/**
	 * 
	 * @return la mappa.
	 */
	public Mappa getMappa() {
		return mappa;
	}

	/**
	 * 
	 * @return il numero di giocatori della partita corrente.
	 */
	public int getNumeroGiocatori() {
		return numeroGiocatori;
	}

	/**
	 * 
	 * @return il numero di recinti non ancora posizionati, quindi
	 * 		   disponibili.
	 */
	public int getNumeroRecinti() {
		return numeroRecinti;
	}

	/**
	 * 
	 * @return il numero di recinti finali non ancora posizionati, 
	 * 		   quindi disponibili.
	 */
	public int getNumeroRecintiFinali() {
		return numeroRecintiFinali;
	}

	/**
	 * 
	 * @return la pecora nera.
	 */
	public PecoraNera getPecoraNera() {
		return pecoraNera;
	}

	/**
	 * 
	 * @return il tipo di gioco del gioco corrente (offline o online).
	 */
	public TipoGioco getTipoGioco(){
		return tipoGioco;
	}

	/**
	 * 
	 * @return true se l'ultimo turno è stato concluso, 
	 * 		   false se no.
	 */
	public boolean isGiocoFinito() {
		return giocoFinito;
	}

	/**
	 * 
	 * @return true se l'ultimo turno è in corso,
	 * 		   false se no.
	 */
	public boolean isUltimoTurno() {
		return ultimoTurno;
	}

	/**
	 * Imposta il giocatore correntemente impegnato in un turno.
	 * @param giocatoreCorrente il giocatore che deve giocare adesso.
	 */
	public void setGiocatoreCorrente(Giocatore giocatoreCorrente) {
		this.giocatoreCorrente = giocatoreCorrente;
	}

	/**
	 * Imposta la variabile boolean che descrive la fine del gioco: true
	 * se il gioco è finito, false se no.
	 * @param giocoFinito il boolean che descrive la fine o meno del gioco.
	 */
	public void setGiocoFinito(boolean giocoFinito) {
		this.giocoFinito = giocoFinito;
	}

	/**
	 * Imposta la mappa.
	 * @param mappa la mappa di gioco.
	 */
	public void setMappa(Mappa mappa){
		this.mappa = mappa;
	}

	/**
	 * Imposta il tipo di gioco della partita corrente (online o offline)
	 * @param tipoGioco il tipo di gioco desiderato.
	 */
	public void setTipoGioco(TipoGioco tipoGioco) {
		this.tipoGioco = tipoGioco;
	}

	/**
	 * Imposta la variabile che descrive se si sta giocando l'ultimo
	 * turno (true) o meno (false).
	 * @param ultimoTurno il boolean che comunica se si sta giocando 
	 * 		  l'ultimo turno o no.
	 */
	public void setUltimoTurno(boolean ultimoTurno) {
		this.ultimoTurno = ultimoTurno;
	}

	/**
	 * 
	 * @return la lista delle carte messe in vendita da tutti i
	 * 		   giocatori.
	 */
	public List<CartaInVendita> getListaCarteInVendita() {
		return listaCarteInVendita ;
	}

	/**
	 * Fornisce alla classe il riferimento all'oggetto lupo.
	 * @param lupo il lupo.
	 */
	public void setLupo(Lupo lupo) {
		this.lupo = lupo;
	}

	/**
	 * Fornisce alla classe il riferimento all'oggetto pecora
	 * nera.
	 * @param pecoraNera la pecora nera.
	 */
	public void setPecoraNera(PecoraNera pecoraNera) {
		this.pecoraNera = pecoraNera;
	}
}
