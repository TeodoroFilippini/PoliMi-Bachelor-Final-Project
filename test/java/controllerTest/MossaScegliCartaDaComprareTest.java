package controllerTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import model.Carta;
import model.CartaInVendita;
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
import controller.MossaScegliCartaDaComprare;

public class MossaScegliCartaDaComprareTest {

	private ServerSocketTest serverSocket;
	private ObjectOutputStream clientOutputSocket;
	private ObjectInputStream clientInputSocket;
	private Socket clientSocket;
	private ObjectInputStream serverInputSocket;
	private ObjectOutputStream serverOutputSocket;
	private StatoGioco statoGioco;
	private ViewTest view;
	private CartaInVendita cartaInVendita;
	private Carta carta;
	private CartaInVendita cartaVenditaRMI;
	private CartaInVendita cartaVenditaSocket;
	private Giocatore acquirente;
	private Giocatore venditore;
	private int danari = 20;
	private int costoCarta = 2;
	private ServerRMITest serverRMI;

	@Before
	public void setUp() throws Exception {
		venditore = new Giocatore(danari,1);
		acquirente = new Giocatore(danari,2);
		carta = new Carta(TipoTerreno.DESERTO);
		cartaInVendita = new CartaInVendita(carta,costoCarta,venditore);
		statoGioco = new StatoGioco(1);
		statoGioco.getListaGiocatori().add(acquirente);
		statoGioco.getListaGiocatori().add(venditore);
		statoGioco.getListaCarteInVendita().add(cartaInVendita);
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
				return new MossaScegliCartaDaComprare(statoGioco.getListaCarteInVendita(),acquirente);
			}

			@Override
			public void aggiungiOggettoInCoda (Object oggetto){
				cartaVenditaRMI = (CartaInVendita) oggetto;
			}
		};
	}

	@Test
	public void test() {
		try {
			serverOutputSocket.writeObject(new MossaScegliCartaDaComprare(statoGioco.getListaCarteInVendita(),acquirente));
			((MossaScegliCartaDaComprare)clientInputSocket.readObject()).eseguiMossa(view, statoGioco, clientOutputSocket);
			cartaVenditaSocket = (CartaInVendita)serverInputSocket.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Carta cartaSceltaSocket = AdattatoreIstanze.cercaIstanzaCarta(cartaVenditaSocket.getCarta(), statoGioco.getListaCarte());
		Giocatore venditoreSocket = AdattatoreIstanze.cercaIstanzaGiocatore(cartaVenditaSocket.getProprietario(), statoGioco.getListaGiocatori());
		assertEquals(venditoreSocket,cartaInVendita.getProprietario());
		assertEquals(cartaSceltaSocket,cartaInVendita.getCarta());
		assertEquals(cartaVenditaSocket.getCosto(),cartaInVendita.getCosto());
		
		
		try {
			ClientRMITest clientRMI = new ClientRMITest();
			clientRMI.riceviMossa();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		Carta cartaSceltaRMI = AdattatoreIstanze.cercaIstanzaCarta(cartaVenditaRMI.getCarta(), statoGioco.getListaCarte());
		Giocatore venditoreRMI = AdattatoreIstanze.cercaIstanzaGiocatore(cartaVenditaRMI.getProprietario(), statoGioco.getListaGiocatori());
		assertEquals(venditoreRMI,cartaInVendita.getProprietario());
		assertEquals(cartaSceltaRMI,cartaInVendita.getCarta());
		assertEquals(cartaVenditaRMI.getCosto(),cartaInVendita.getCosto());
		serverRMI.chiudiServer();
		serverSocket.chiudiServer();
	}

}
