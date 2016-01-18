package RMIServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

import model.Giocatore;
import model.StatoGioco;
import controller.Mossa;

public interface InterfacciaRMI extends Remote{

	Mossa riceviMossa(Giocatore giocatore) throws RemoteException;

	StatoGioco inizializzaGioco() throws RemoteException;

	int getNumeroGiocatore() throws RemoteException;

	void rimuoviGiocatore (Giocatore giocatore)throws RemoteException;

	void aggiungiOggettoInCoda(Object oggetto)throws RemoteException;

}
