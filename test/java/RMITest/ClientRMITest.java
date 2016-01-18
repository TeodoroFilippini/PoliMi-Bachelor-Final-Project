package RMITest;

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
import view.InterfacciaView;
import viewTest.ViewTest;
import RMIServer.InterfacciaRMI;
import controller.Mossa;
import controller.MossaScelta;

public class ClientRMITest extends UnicastRemoteObject implements Remote{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InterfacciaRMI server;
	private StatoGioco statoGioco;
	private Giocatore giocatore;
	private InterfacciaView view;

	public ClientRMITest() throws RemoteException {
		Registry registry = LocateRegistry.getRegistry("localhost",Costanti.PORTA_SERVER+1);
		try {
			server = (InterfacciaRMI) registry.lookup(Costanti.NOME_SERVER);
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		statoGioco = server.inizializzaGioco();
		int numeroGiocatore = server.getNumeroGiocatore();
		Iterator<Giocatore> scorriGiocatori = statoGioco.getListaGiocatori().iterator();
		while (scorriGiocatori.hasNext()){
			Giocatore giocatoreCorrente = scorriGiocatori.next();
			if (giocatoreCorrente.getNumero() == numeroGiocatore) {
				giocatore = giocatoreCorrente;
			}
		}
		view = new ViewTest(statoGioco);
	}
	
	public void eseguiMossa(Mossa mossa){
		mossa.eseguiMossa(view, statoGioco);
	}

	public void riceviMossa() {
		try {
			Mossa mossa = server.riceviMossa(giocatore);
			if (mossa instanceof MossaScelta){
				eseguiMossaScelta((MossaScelta)mossa);
			}else{
				eseguiMossa(mossa);
			}
				
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void eseguiMossaScelta(MossaScelta mossa) {
		mossa.eseguiMossa(view, statoGioco,server);
		
	}
}
