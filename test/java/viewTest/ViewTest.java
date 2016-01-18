package viewTest;

import java.util.List;

import model.Animale;
import model.Carta;
import model.CartaInVendita;
import model.Casella;
import model.Giocatore;
import model.Pastore;
import model.Regione;
import model.StatoGioco;
import model.TipoMossa;
import view.InterfacciaView;

public class ViewTest implements InterfacciaView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StatoGioco statoGioco;

	public ViewTest(StatoGioco statoGioco){
		this.statoGioco = statoGioco;
	}
	
	@Override
	public void aggiungiAnimaleView(Animale animale, Regione regione) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aggiungiCarta(Carta carta, Giocatore giocatore) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rimuoviCarteView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void corrompi(Giocatore giocatoreCorrotto,
			Giocatore giocatoreCorruttore) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disabilitaBottoni() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Giocatore getGiocatore() {
		return statoGioco.getGiocatoreCorrente();
	}

	@Override
	public void muoviAnimale(Animale animale, Regione arrivo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void muoviLupo(Regione arrivo, int dado) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void muoviPastore(Pastore pastore, Casella arrivo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void muoviPecoraNera(Regione arrivo, int dado) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rimuoviAnimaleView(Animale animale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Animale scegliAnimale(List<Animale> listaAnimali) {
		return listaAnimali.get(0);
	}

	@Override
	public Carta scegliCarta(List<Carta> listaCarte) {
		return listaCarte.get(0);
	}

	@Override
	public Casella scegliCasella(List<Casella> listaCaselle) {
		return listaCaselle.get(0);
	}

	@Override
	public TipoMossa scegliMossa(List<TipoMossa> mosseDisponibili) {
		return mosseDisponibili.get(0);
	}

	@Override
	public Pastore scegliPastore(List<Pastore> listaPastori) {
		return listaPastori.get(0);
	}

	@Override
	public Regione scegliRegione(List<Regione> listaRegioni) {
		return listaRegioni.get(0);
	}

	@Override
	public void setGiocatoreCorrente(Giocatore giocatore) {
		statoGioco.setGiocatoreCorrente(giocatore);
	}

	@Override
	public void setInterfacciaGiocatore(Giocatore giocatore) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stampaMessaggio(String messaggio) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int scegliCostoCarta(Carta carta) {
		return 1;
	}

	@Override
	public void lanciaDadoPecoraNera(int dado) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Carta scegliCartaDavendere(List<Carta> carteVendibili) {
		return carteVendibili.get(0);
	}

	@Override
	public CartaInVendita scegliCartaDaComprare(
			List<CartaInVendita> listaCarteInVendita, Giocatore giocatore) {
		return listaCarteInVendita.get(0);
	}

	@Override
	public void aggiungiPastoreView(Pastore pastore) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
