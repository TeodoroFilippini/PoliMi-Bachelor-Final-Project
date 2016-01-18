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
import model.StatoGioco;
import model.TipoMossa;

import org.junit.Before;
import org.junit.Test;

import viewTest.ViewTest;
import RMITest.ClientRMITest;
import RMITest.ServerRMITest;
import SocketTest.ServerSocketTest;
import controller.Mossa;
import controller.MossaScegliMossa;

public class MossaScegliMossaTest {

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
	private TipoMossa mossaSceltaRMI;
	private TipoMossa mossaSceltaSocket;
	private TipoMossa mossa;
	private Giocatore giocatore;

	@Before
	public void setUpServer() throws Exception {
		mossa = TipoMossa.ACCOPPIA;
		mosseDisponibili.add(mossa);
		giocatore = new Giocatore(20,1);
		statoGioco = new StatoGioco(1);
		statoGioco.getListaGiocatori().add(giocatore);
		statoGioco.setGiocatoreCorrente(giocatore);
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
				return new MossaScegliMossa(mosseDisponibili,1);
			}

			@Override
			public void aggiungiOggettoInCoda (Object oggetto){
				mossaSceltaRMI = (TipoMossa)oggetto;
			}
		};
	}

	@Test
	public void test() {
		try {
			serverOutputSocket.writeObject(new MossaScegliMossa(mosseDisponibili,1));
			((MossaScegliMossa)clientInputSocket.readObject()).eseguiMossa(view, statoGioco, clientOutputSocket);
			mossaSceltaSocket = (TipoMossa)serverInputSocket.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		assertEquals(mossaSceltaSocket,mossa);	
		try {
			ClientRMITest clientRMI = new ClientRMITest();
			clientRMI.riceviMossa();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		assertEquals(mossaSceltaRMI,mossa);
		serverRMI.chiudiServer();
		serverSocket.chiudiServer();
	}
}
