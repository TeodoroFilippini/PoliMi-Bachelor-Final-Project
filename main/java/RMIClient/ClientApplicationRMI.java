package RMIClient;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;

import model.Costanti;
import model.Giocatore;
import model.StatoGioco;
import view.FinestraNotifica;
import view.InterfacciaView;
import view.SheeplandView;
import RMIServer.InterfacciaRMI;
import controller.Mossa;
import controller.MossaScelta;

/**
 * Classe che implementa il client per la connessione di tipo RMI.
 * Dopo aver impostato la connessione, fa partire un ciclo infinito di
 * ricezione oggetti Mossa, inviatigli dal server.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class ClientApplicationRMI extends UnicastRemoteObject implements Remote{

	private static final long serialVersionUID = 1L;
	private StatoGioco statoGioco;
	private InterfacciaRMI server;
	private InterfacciaView view;
	private Giocatore giocatore;

	/**
	 * Costruttore della classe; inizia con la creazione del registry associato
	 * al server, che gli permette di stabilire la connessione. Dopodichè, il server 
	 * gli manda il suo numero di giocatore e lo stato del gioco, al che 
	 * il client può creare la sua view di tipo sheeplandView, ed essere utilizzato 
	 * dall'user.
	 * @param indirizzoIP
	 */
	public ClientApplicationRMI(String indirizzoIP) throws RemoteException{
		try {
			Registry registry = LocateRegistry.getRegistry(indirizzoIP,Costanti.PORTA_SERVER);
			server = (InterfacciaRMI) registry.lookup(Costanti.NOME_SERVER);
			statoGioco = server.inizializzaGioco();
			int numeroGiocatore = server.getNumeroGiocatore();
			Iterator<Giocatore> scorriGiocatori = statoGioco.getListaGiocatori().iterator();
			while (scorriGiocatori.hasNext()){
				Giocatore giocatoreCorrente = scorriGiocatori.next();
				if (giocatoreCorrente.getNumero() == numeroGiocatore) {
					giocatore = giocatoreCorrente;
				}
			}
			view = new SheeplandView(statoGioco,giocatore);
			view.setInterfacciaGiocatore(giocatore);
		} catch (NotBoundException e) {
			new FinestraNotifica("Il server "+Costanti.NOME_SERVER+" non è connesso", e);
		}
	}
	
	/**
	 * ciclo infinito di attesa di ricezione di mosse inviate dal server.
	 */
	public void iniziaRicezioneMosse(){
		while (true){
			Mossa mossa = null;
			try {
				mossa = server.riceviMossa(giocatore);
			} catch (RemoteException e) {
				new FinestraNotifica("Connessione con il server interrotta",e);
				System.exit(0);
			}
			if (mossa instanceof MossaScelta) {
				eseguiMossaScelta((MossaScelta)mossa);
			} else {
				eseguiMossa(mossa);
			}
		}
	}
	
	/**
	 * Metodo che esegue le mosse ricevute.
	 * @param mossa la mossa ricevuta.
	 */
	private void eseguiMossa(Mossa mossa) {
		mossa.eseguiMossa(view, statoGioco);
	}

	/**
	 * Metodo che esegue le mosse scelta ricevute.
	 * @param mossa la mossa scelta ricevuta.
	 */
	private void eseguiMossaScelta(MossaScelta mossaScelta) {
		mossaScelta.eseguiMossa(view, statoGioco, server);
	}
}