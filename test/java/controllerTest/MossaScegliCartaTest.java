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
import controller.MossaScegliCarta;

public class MossaScegliCartaTest {

	private ServerSocketTest serverSocket;
	private ObjectOutputStream clientOutputSocket;
	private ObjectInputStream clientInputSocket;
	private Socket clientSocket;
	private ObjectInputStream serverInputSocket;
	private ObjectOutputStream serverOutputSocket;
	private StatoGioco statoGioco;
	private ViewTest view;
	private Carta carta;
	private Carta cartaSceltaRMI;
	private Carta cartaSceltaSocket;
	private ServerRMITest serverRMI;


	@Before
	public void setUp() throws Exception {
		statoGioco = new StatoGioco(1);
		carta = new Carta(TipoTerreno.DESERTO);
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
				return new MossaScegliCarta(statoGioco.getListaCarte());
			}

			@Override
			public void aggiungiOggettoInCoda (Object oggetto){
				cartaSceltaRMI = AdattatoreIstanze.cercaIstanzaCarta(carta, statoGioco.getListaCarte());
			}
		};
	}

	@Test
	public void test() {
		try {
			serverOutputSocket.writeObject(new MossaScegliCarta(statoGioco.getListaCarte()));
			((MossaScegliCarta)clientInputSocket.readObject()).eseguiMossa(view, statoGioco, clientOutputSocket);
			cartaSceltaSocket =AdattatoreIstanze.cercaIstanzaCarta((Carta)serverInputSocket.readObject(),statoGioco.getListaCarte());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(carta,cartaSceltaSocket);	
		try {
			ClientRMITest clientRMI = new ClientRMITest();
			clientRMI.riceviMossa();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		assertEquals(carta,cartaSceltaRMI);
		serverRMI.chiudiServer();
		serverSocket.chiudiServer();
	}

}
