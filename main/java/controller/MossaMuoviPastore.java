package controller;

import java.util.List;

import model.Casella;
import model.Costanti;
import model.Mappa;
import model.Pastore;
import model.StatoGioco;
import view.InterfacciaView;

/**
 * Mossa associata al movimento di un pastore.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class MossaMuoviPastore implements Mossa {

	private static final long serialVersionUID = 1L;
	private Casella arrivo;
	private Pastore pastore;

	public MossaMuoviPastore(Pastore pastore, Casella arrivo) {
		this.pastore = pastore;
		this.arrivo = arrivo;
	}

	/**
	 * Esegue la mossa: attraverso la view, mostra i dovuti cambiamenti
	 * che ha effettuato sullo stato del gioco.
	 * @param view l'interfaccia tramite la quale server e client comunicano.
	 * @param statoGioco lo stato del gioco al momento della chiamata della mossa.
	 */
	@Override
	public void eseguiMossa(InterfacciaView view, StatoGioco statoGioco){
		Mappa mappa = statoGioco.getMappa();
		Pastore istanzaPastore = AdattatoreIstanze.cercaIstanzaPastore(pastore, statoGioco.getListaPastori());
		Casella istanzaArrivo = AdattatoreIstanze.cercaIstanzaCasella(arrivo, mappa.getListaCaselle());
		Casella posizionePastore = istanzaPastore.getPosizione();
		List<Casella> caselleLibereAdiacenti = mappa.caselleLibereAdiacenti(posizionePastore);
		if (!caselleLibereAdiacenti.contains(istanzaArrivo)) {
			istanzaPastore.getGiocatore().decrementaDanari(Costanti.COSTO_SPOSTAMENTO);
		}		
		if (statoGioco.getNumeroRecinti() > 0){
			statoGioco.decrementaRecinti();
			posizionePastore.setOccupataRecinto(true);
		}else{
			statoGioco.decrementaRecintiFinali();
			posizionePastore.setOccupataRecintoFinale(true);
		}
		view.muoviPastore(istanzaPastore, istanzaArrivo);
		istanzaPastore.setPosizione(istanzaArrivo);
	}
}
