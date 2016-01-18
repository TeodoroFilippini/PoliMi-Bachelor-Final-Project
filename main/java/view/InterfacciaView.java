package view;

import java.io.Serializable;
import java.util.List;

import model.Animale;
import model.Carta;
import model.CartaInVendita;
import model.Casella;
import model.Giocatore;
import model.Pastore;
import model.Regione;
import model.TipoMossa;
/**
 * Interfaccia utilizzata per la comunicazione controller-view. Nel controller
 * viene creata direttamente come SheeplandView nel caso di gioco offline, mentre
 * viene creata come Server application nel caso di gioco online per gestire la 
 * comunicazione tra il server e i vari client senza dover stravolgere il codice
 * nel controller. Implementa tutti i metodi che devono poter essere chiamati
 * dal controller.
 * 
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public interface InterfacciaView extends Serializable,Runnable{

	/**
	 * Crea una nuova istanza view dell'animale da aggiungere alla regione.
	 * 
	 * @param animale animale di cui si vuole creare una nuova istanza view.
	 * @param regione regione alla quale si vuole aggiungere l'animale.
	 */
	void aggiungiAnimaleView(Animale animale, Regione regione);

	/**
	 * Aggiunge una carta al giocatore.
	 * 
	 * @param carta carta che si vuole aggiungere.
	 * @param giocatore giocatore al quale si vuole aggiungere la carta.
	 */
	void aggiungiCarta(Carta carta, Giocatore giocatore);

	/**
	 * Rimuove le istanze di carte view vuote che non hanno carte corrispondenti.
	 */
	void rimuoviCarteView();

	/**
	 * Viene visualizzato un messaggio informativo con il giocatore corrotto e il corruttore.
	 * 
	 * @param giocatoreCorrotto giocatore che viene corrotto.
	 * @param giocatoreCorruttore giocatore che corrompe.
	 */
	void corrompi(Giocatore giocatoreCorrotto, Giocatore giocatoreCorruttore);

	/**
	 * Disattiva i bottoni mossa.
	 */
	void disabilitaBottoni();

	/**
	 * 
	 * @return giocatore associato alla view.
	 */
	Giocatore getGiocatore();

	/**
	 * Muove l'animale dalla sua posizione corrente alla regione d'arrivo.
	 * 
	 * @param animale animale da muovere.
	 * @param arrivo regione d'arrivo.
	 */
	void muoviAnimale(Animale animale, Regione arrivo);

	/**
	 * Muove il lupo dalla sua posizione corrente alla regione d'arrivo.
	 * 
	 * @param arrivo regione d'arrivo del lupo.
	 * @param dado risultato del dado che ha generato il movimento.
	 */
	void muoviLupo(Regione arrivo, int dado);

	/**
	 * Muove il pastore dalla sua posizione corrente alla casella d'arrivo.
	 * 
	 * @param pastore pastore da muovere.
	 * @param arrivo regione d'arrivo.
	 */
	void muoviPastore(Pastore pastore, Casella arrivo);

	/**
	 * Muove la pecora nera dalla sua posizione corrente alla casella d'arrivo.
	 * 
	 * @param arrivo regione d'arrivo della pecora nera.
	 * @param dado risultato del dado che ha generato il movimento.
	 */
	void muoviPecoraNera(Regione arrivo, int dado);

	/**
	 * Rimuove l'istanza view dell'animale.
	 * 
	 * @param animale animale di cui si vuole rimuovere l'istanza view.
	 */
	void rimuoviAnimaleView(Animale animale);

	/**
	 * Permette all'utente di scegliere un animale da una lista.
	 * 
	 * @param listaAnimali lista degli animali tra cui è possibile scegliere
	 * @return animale scelto.
	 */
	Animale scegliAnimale(List<Animale> listaAnimali);

	/**
	 * Permette all'utente di scegliere una carta da una lista.
	 * 
	 * @param listaCarte lista degli carte tra cui è possibile scegliere
	 * @return carta scelta.
	 */
	Carta scegliCarta(List<Carta> listaCarte);

	/**
	 * Permette all'utente di scegliere una casella da una lista.
	 * 
	 * @param listaCaselle lista delle caselle tra cui è possibile scegliere
	 * @return casella scelta.
	 */
	Casella scegliCasella(List<Casella> listaCaselle);

	/**
	 * Permette all'utente di scegliere un mossa da una lista.
	 * 
	 * @param mosseDisponibili lista delle mosse tra le quali è possibile scegliere
	 * @return mossa scelta.
	 */
	TipoMossa scegliMossa(List<TipoMossa> mosseDisponibili);

	/**
	 * Permette all'utente di scegliere un pastore da una lista.
	 * 
	 * @param listaPastori lista dei pastore tra cui è possibile scegliere
	 * @return pastore scelto.
	 */
	Pastore scegliPastore(List<Pastore> listaPastori);

	/**
	 * Permette all'utente di scegliere una regione da una lista.
	 * 
	 * @param listaRegioni lista delle regioni tra cui è possibile scegliere
	 * @return regione scelta.
	 */
	Regione scegliRegione(List<Regione> listaRegioni);

	/**
	 * Imposta il giocatore corrente a quello specificato.
	 * 
	 * @param giocatore giocatore che si vuole impostare come giocatore corrente.
	 */
	void setGiocatoreCorrente(Giocatore giocatore) ;

	/**
	 * Cambia l'interfaccia giocatore per mostrare i dati del giocatore specificato.
	 * 
	 * @param giocatore giocatore del quale si vuole visualizzare l'interfaccia.
	 */
	void setInterfacciaGiocatore (Giocatore giocatore);

	/**
	 * Stampa un messaggio con la stringa specificata.
	 * 
	 * @param messaggio messaggio da visualizzare.
	 */
	void stampaMessaggio(String messaggio);

	/**
	 * Permette di far impostare all'utente il costo della carta che vuole vendere.
	 * 
	 * @param carta carta di cui si vuole impostare il costo.
	 * @return costo della carta scelto.
	 */
	int scegliCostoCarta(Carta carta);

	/**
	 * Visualizza l'animazione del dado della pecora nera con il risultato specificato.
	 * 
	 * @param dado risultato della faccia del dado da visualizzare.
	 */
	void lanciaDadoPecoraNera(int dado);

	/**
	 * Permette all'utente di scegliere da una lista la carta che desidera vendere.
	 * 
	 * @param carteVendibili lista di carte tra cui scegliere.
	 * @return carta messa in vendita.
	 */
	Carta scegliCartaDavendere(List<Carta> carteVendibili);

	/**
	 * permette all'utente di scegliere da una lista la carta che desidera comprare.
	 * 
	 * @param listaCarteInVendita lista di carte tra cui scegliere.
	 * @return carta in vendita scelta.
	 */
	CartaInVendita scegliCartaDaComprare(List<CartaInVendita> listaCarteInVendita,Giocatore giocatore);

	/**
	 * Crea una nuova istanza view del pastore specificato.
	 * 
	 * @param pastore pastore di cui si desidera creare un'istanza view.
	 */
	void aggiungiPastoreView(Pastore pastore);
}
