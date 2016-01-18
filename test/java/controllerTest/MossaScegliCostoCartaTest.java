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
import controller.Mossa;
import controller.MossaScegliCostoCarta;

public class MossaScegliCostoCartaTest {

	private ServerSocketTest serverSocket;
	private StatoGioco statoGioco;
	private ViewTest view;
	private Socket clientSocket;
	private ObjectOutputStream serverOutputSocket;
	private ObjectInputStream serverInputSocket;
	private ObjectInputStream clientInputSocket;
	private ObjectOutputStream clientOutputSocket;
	private ServerRMITest serverRMI;
	private int costoCartaSocket;
	private int costoCartaRMI;
	private Carta carta;

	@Before
	public void setUpServer() throws Exception {
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
				return new MossaScegliCostoCarta(carta);
			}

			@Override
			public void aggiungiOggettoInCoda (Object oggetto){
				costoCartaRMI = (int)oggetto;
			}
		};
	}

	@Test
	public void test() {
		try {
			serverOutputSocket.writeObject(new MossaScegliCostoCarta(carta));
			((MossaScegliCostoCarta)clientInputSocket.readObject()).eseguiMossa(view, statoGioco, clientOutputSocket);
			costoCartaSocket = (int)serverInputSocket.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		assertEquals(costoCartaSocket,1);	
		try {
			ClientRMITest clientRMI = new ClientRMITest();
			clientRMI.riceviMossa();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		assertEquals(costoCartaRMI,1);
		serverRMI.chiudiServer();
		serverSocket.chiudiServer();
	}
}
