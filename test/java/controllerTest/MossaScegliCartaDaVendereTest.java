package controllerTest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import model.Carta;
import model.Costanti;
import model.Giocatore;
import model.StatoGioco;
import model.TipoTerreno;

import org.junit.Before;
import org.junit.Test;

import viewTest.ViewTest;
import RMITest.ClientRMITest;
import RMITest.ServerRMITest;
import SocketTest.ServerSocketTest;
import controller.AdattatoreIstanze;
import controller.Mossa;
import controller.MossaScegliCartaDaVendere;

public class MossaScegliCartaDaVendereTest {

	private ServerSocketTest serverSocket;
	private ObjectOutputStream clientOutputSocket;
	private ObjectInputStream clientInputSocket;
	private Socket clientSocket;
	private ObjectInputStream serverInputSocket;
	private ObjectOutputStream serverOutputSocket;
	private StatoGioco statoGioco;
	private ViewTest view;
	private Carta carta;
	private Carta cartaSceltaSocket;
	private Carta cartaSceltaRMI;
	private ServerRMITest serverRMI;

	
	@Before
	public void setUp() throws Exception {
		carta = new Carta(TipoTerreno.DESERTO);
		statoGioco = new StatoGioco(1);
		statoGioco.getListaCarte().add(carta);
		view = new ViewTest(statoGioco);
		serverSocket = new ServerSocketTest();
		Thread serverThread = new Thread(serverSocket);
		serverThread.start();
		clientSocket = new Socket("localhost",Costanti.PORTA_SERVER);
		clientInputSocket = new ObjectInputStream(clientSocket.getInputStream());
		clientOutputSocket = new ObjectOutputStream(clientSocket.getOutputStream());
		while (serverInputSocket == null | serverOutputSocket == null){
			serverOutputSocket = serverSocket.getOutput();
			serverInputSocket = serverSocket.getInput();
		}
		serverRMI = new ServerRMITest(statoGioco){

			private static final long serialVersionUID = 1L;

			@Override
			public Mossa riceviMossa(Giocatore giocatore){
				return new MossaScegliCartaDaVendere(statoGioco.getListaCarte());
			}

			@Override
			public void aggiungiOggettoInCoda (Object oggetto){
				cartaSceltaRMI = (Carta) oggetto;
			}
		};
	}

	@Test
	public void test() {
		try {
			serverOutputSocket.writeObject(new MossaScegliCartaDaVendere(statoGioco.getListaCarte()));
			((MossaScegliCartaDaVendere)clientInputSocket.readObject()).eseguiMossa(view, statoGioco, clientOutputSocket);
			cartaSceltaSocket = (Carta)serverInputSocket.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		cartaSceltaSocket = AdattatoreIstanze.cercaIstanzaCarta(cartaSceltaSocket, statoGioco.getListaCarte());
		assertEquals(cartaSceltaSocket,carta);
		try {
			ClientRMITest clientRMI = new ClientRMITest();
			clientRMI.riceviMossa();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		cartaSceltaRMI = AdattatoreIstanze.cercaIstanzaCarta(cartaSceltaRMI, statoGioco.getListaCarte());
		assertEquals(cartaSceltaRMI,carta);
		serverRMI.chiudiServer();
		serverSocket.chiudiServer();
	}
}
