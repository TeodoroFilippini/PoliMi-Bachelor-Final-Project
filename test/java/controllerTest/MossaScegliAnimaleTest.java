package controllerTest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import model.Animale;
import model.Costanti;
import model.ElementoMappa;
import model.Giocatore;
import model.Mappa;
import model.Regione;
import model.StatoGioco;
import model.TipoTerreno;

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
import controller.MossaScegliAnimale;

public class MossaScegliAnimaleTest {

	private ServerSocketTest serverSocket;
	private StatoGioco statoGioco;
	private Animale animale;
	private Regione regione;
	private ViewTest view;
	private Animale animaleSceltoSocket;
	private Animale animaleSceltoRMI;
	private Socket clientSocket;
	private ObjectOutputStream serverOutputSocket;
	private ObjectInputStream serverInputSocket;
	private ObjectInputStream clientInputSocket;
	private ObjectOutputStream clientOutputSocket;
	private ServerRMITest serverRMI;

	@Before
	public void setUpServer() throws Exception {
		regione = new Regione("regione",TipoTerreno.DESERTO);
		animale = new Animale(regione);
		SimpleGraph<ElementoMappa, DefaultEdge> grafoMappa = new SimpleGraph<ElementoMappa,DefaultEdge>(DefaultEdge.class);
		grafoMappa.addVertex(regione);
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
				return new MossaScegliAnimale(regione.getListaAnimali());
			}

			@Override
			public void aggiungiOggettoInCoda (Object oggetto){
				animaleSceltoRMI = AdattatoreIstanze.cercaIstanzaAnimale((Animale)oggetto,regione.getListaAnimali());
			}
		};
	}

	@Test
	public void test() {
		try {
			serverOutputSocket.writeObject(new MossaScegliAnimale(regione.getListaAnimali()));
			((MossaScegliAnimale)clientInputSocket.readObject()).eseguiMossa(view, statoGioco, clientOutputSocket);
			animaleSceltoSocket =AdattatoreIstanze.cercaIstanzaAnimale((Animale)serverInputSocket.readObject(),regione.getListaAnimali());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(animale,animaleSceltoSocket);	
		try {
			ClientRMITest clientRMI = new ClientRMITest();
			clientRMI.riceviMossa();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		assertEquals(animale,animaleSceltoRMI);
		serverRMI.chiudiServer();
		serverSocket.chiudiServer();
	}
}
