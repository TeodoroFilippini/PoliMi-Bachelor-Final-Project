package socketClient;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.Costanti;
import model.Giocatore;
import model.StatoGioco;
import view.FinestraNotifica;
import view.SheeplandView;
import controller.Mossa;
import controller.MossaScelta;

/**
 * Classe che implementa il client per la connessione di tipo Socket.
 * Dopo aver impostato la connessione, fa partire un ciclo infinito di
 * ricezione oggetti Mossa, inviatigli dal server.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */
public class ClientApplicationSocket {
	Socket clientSocket;
	StatoGioco statoGioco;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private SheeplandView view;

	/**
	 * Costruttore della classe; inizia con la creazione del socket associato
	 * al client, e della coppia inputstream+outputstream col server. Dopodichè,
	 * il server gli manda il suo numero di giocatore e lo stato del gioco, al che 
	 * il client può creare la sua view di tipo sheeplandView, ed essere utilizzato 
	 * dall'user.
	 * @param indirizzoIP l'indirizzo IP del server
	 */
	public ClientApplicationSocket(String indirizzoIP){
		try {
			clientSocket = new Socket(indirizzoIP,Costanti.PORTA_SERVER);
			input = new ObjectInputStream(clientSocket.getInputStream());
			output = new ObjectOutputStream(clientSocket.getOutputStream());
			int numeroGiocatore =  (int) input.readObject();
			statoGioco = (StatoGioco) input.readObject();
			Giocatore giocatore = statoGioco.getListaGiocatori().get(numeroGiocatore-1);
			view = new SheeplandView(statoGioco,giocatore);
			view.setInterfacciaGiocatore(giocatore);
			view.stampaMessaggio("Ha inizio il tuo gioco, giocatore " + giocatore.getNumero());
		}catch (EOFException e){	
			new FinestraNotifica("Raggiunta inaspettatamente la fine dello stream associato "
					+ "all'oggetto mossa in arrivo dal server", e);
		}catch (ClassNotFoundException e){	
			new FinestraNotifica("Errore nella lettura dell'oggetto inviato dal server", e);
		} catch (IOException e) {
			new FinestraNotifica("Creazione connessione Socket fallita: l'indirizzo " + indirizzoIP +" non è quello del server "
					+ "o il server non è ancora stato creato.",e);
		}
	}

	/**
	 * ciclo infinito di attesa di ricezione di mosse inviate dal server.
	 */
	public void iniziaRicezioneMosse(){
		while(true){
			Mossa mossa = null;
			try {
				mossa = (Mossa) input.readObject();
			} catch (ClassNotFoundException | IOException e) {
				new FinestraNotifica("Connessione con il server interrotta",e);
				System.exit(0);
			}
			if (mossa instanceof MossaScelta){
				((MossaScelta) mossa).eseguiMossa(view, statoGioco, output);
			}else{
				mossa.eseguiMossa(view, statoGioco);
			}
		}
	}
}
