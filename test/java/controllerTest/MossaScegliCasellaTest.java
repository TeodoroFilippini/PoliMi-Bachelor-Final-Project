package controllerTest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import model.Casella;
import model.Costanti;
import model.ElementoMappa;
import model.Giocatore;
import model.Mappa;
import model.StatoGioco;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Before;
import org.junit.Test;

import viewTest.ViewTest;
import RMITest.ClientRMITest;
import RMITest.ServerRMITest;
import SocketTest.ServerSocketTest;
import controller.AdattatoreIstanze;
import controller.Mossa;
import controller.MossaScegliCasella;

public class MossaScegliCasellaTest {

	private ServerSocketTest serverSocket;
	private StatoGioco statoGioco;
	private ViewTest view;
	private Socket clientSocket;
	private ObjectOutputStream serverOutputSocket;
	private ObjectInputStream serverInputSocket;
	private ObjectInputStream clientInputSocket;
	private ObjectOutputStream clientOutputSocket;
	private ServerRMITest serverRMI;
	private Casella casella;
	private Casella casellaSceltaRMI;
	private Casella casellaSceltaSocket;


	@Before
	public void setUpServer() throws Exception {
		casella = new Casella("casella",1);
		SimpleGraph<ElementoMappa, DefaultEdge> grafoMappa = new SimpleGraph<ElementoMappa,DefaultEdge>(DefaultEdge.class);
		grafoMappa.addVertex(casella);
		Mappa mappa = Mappa.creaMappa(grafoMappa);
		statoGioco = new StatoGioco(1);
		statoGioco.setMappa(mappa);
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
				return new MossaScegliCasella(statoGioco.getMappa().getListaCaselle());
			}

			@Override
			public void aggiungiOggettoInCoda (Object oggetto){
				casellaSceltaRMI = AdattatoreIstanze.cercaIstanzaCasella((Casella)oggetto,statoGioco.getMappa().getListaCaselle());
			}
		};
	}

	@Test
	public void test() {
		try {
			serverOutputSocket.writeObject(new MossaScegliCasella(statoGioco.getMappa().getListaCaselle()));
			((MossaScegliCasella)clientInputSocket.readObject()).eseguiMossa(view, statoGioco, clientOutputSocket);
			casellaSceltaSocket = AdattatoreIstanze.cercaIstanzaCasella((Casella)serverInputSocket.readObject(),statoGioco.getMappa().getListaCaselle());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		assertEquals(casella,casellaSceltaSocket);	
		try {
			ClientRMITest clientRMI = new ClientRMITest();
			clientRMI.riceviMossa();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		assertEquals(casella,casellaSceltaRMI);
		serverRMI.chiudiServer();
		serverSocket.chiudiServer();
	}
}
