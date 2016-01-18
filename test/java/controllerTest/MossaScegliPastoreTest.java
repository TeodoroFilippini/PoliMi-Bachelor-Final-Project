package controllerTest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import model.Costanti;
import model.Giocatore;
import model.Pastore;
import model.StatoGioco;
import model.TipoMossa;

import org.junit.Before;
import org.junit.Test;

import viewTest.ViewTest;
import RMITest.ClientRMITest;
import RMITest.ServerRMITest;
import SocketTest.ServerSocketTest;
import controller.AdattatoreIstanze;
import controller.Mossa;
import controller.MossaScegliPastore;

public class MossaScegliPastoreTest {

	private ServerSocketTest serverSocket;
	private StatoGioco statoGioco;
	private ViewTest view;
	private Socket clientSocket;
	private ObjectOutputStream serverOutputSocket;
	private ObjectInputStream serverInputSocket;
	private ObjectInputStream clientInputSocket;
	private ObjectOutputStream clientOutputSocket;
	private ServerRMITest serverRMI;
	List<TipoMossa> mosseDisponibili = new ArrayList<TipoMossa>();
	private Pastore pastoreSceltoRMI;
	private Pastore pastoreSceltoSocket;
	private Giocatore giocatore;
	private Pastore pastore;

	@Before
	public void setUpServer() throws Exception {
		giocatore = new Giocatore(20,1);
		pastore = giocatore.creaNuovoPastore();
		statoGioco = new StatoGioco(1);
		statoGioco.getListaGiocatori().add(giocatore);
		statoGioco.setGiocatoreCorrente(giocatore);
		statoGioco.getListaPastori().add(pastore);
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
				return new MossaScegliPastore(statoGioco.getListaPastori());
			}

			@Override
			public void aggiungiOggettoInCoda (Object oggetto){
				pastoreSceltoRMI = AdattatoreIstanze.cercaIstanzaPastore((Pastore)oggetto,statoGioco.getListaPastori());
			}
		};
	}

	@Test
	public void test() {
		try {
			serverOutputSocket.writeObject(new MossaScegliPastore(statoGioco.getListaPastori()));
			((MossaScegliPastore)clientInputSocket.readObject()).eseguiMossa(view, statoGioco, clientOutputSocket);
			pastoreSceltoSocket = AdattatoreIstanze.cercaIstanzaPastore((Pastore)serverInputSocket.readObject(), statoGioco.getListaPastori());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		assertEquals(pastoreSceltoSocket,pastore);	
		try {
			ClientRMITest clientRMI = new ClientRMITest();
			clientRMI.riceviMossa();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		assertEquals(pastoreSceltoRMI,pastore);
		serverRMI.chiudiServer();
		serverSocket.chiudiServer();
	}
}
