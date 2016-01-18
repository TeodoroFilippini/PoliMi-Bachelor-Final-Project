package RMITest;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import model.Costanti;
import model.Giocatore;
import model.StatoGioco;
import RMIServer.InterfacciaRMI;
import controller.Mossa;

public class ServerRMITest extends UnicastRemoteObject implements InterfacciaRMI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StatoGioco statoGioco;
	private int numeroGiocatore;
	private Registry registry;
	private static int contatoreGiocatore = 0;
	
	
	public ServerRMITest(StatoGioco statoGioco) throws RemoteException {
		this.statoGioco = statoGioco;
		registry = LocateRegistry.createRegistry(Costanti.PORTA_SERVER+1);
		registry.rebind(Costanti.NOME_SERVER, this);
	}

	@Override
	public Mossa riceviMossa(Giocatore giocatore) throws RemoteException {
		//Va fatto l'override nei singoli test
		return null;
	}

	@Override
	public StatoGioco inizializzaGioco() throws RemoteException {
		return statoGioco;
	}

	@Override
	public int getNumeroGiocatore() throws RemoteException {
		numeroGiocatore = contatoreGiocatore ;
		contatoreGiocatore++;
		return numeroGiocatore;
	}

	@Override
	public void rimuoviGiocatore(Giocatore giocatore) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void aggiungiOggettoInCoda(Object oggetto) throws RemoteException {
		//va fatto l'override nei singoli test
	}

	public void chiudiServer() {
		try {
			registry.unbind(Costanti.NOME_SERVER);
			unexportObject(registry, true);
			unexportObject(this, true);
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
