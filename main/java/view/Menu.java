package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Costanti;
import model.TipoGioco;
import model.TipoPartitaInRete;
import model.TipoRete;
/**
 * Menu iniziale. Permette la scelta del tipo di gioco(Online o offline), la scelta del numero di
 * giocatori e nel caso di gioco online la scelta del tipo di rete(socket o RMI) e se essere server o client.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class Menu extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private static final String IP_PATTERN = 
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	private JComboBox<Integer> comboBox;
	private JLabel label;
	private JPanel pannelloBottoni;
	private JPanel pannelloInput;
	private JFormattedTextField indirizzoIP;
	private TipoGioco tipoGioco;
	private TipoPartitaInRete tipoPartita;
	private TipoRete tipoRete;
	private PannelloSfondo pannelloSfondo;
	/**
	 * Crea i pannelli, crea la finestra e vi aggiunge i pannelli, imposta il cursore
	 * personalizzato tramite l'apposito metodo di StrumentiView.
	 */
	public Menu(){
		creaPannelli();
		creaFinestra();
		setCursor(StrumentiView.getCursore());
	}

	/**
	 * Cattura gli eventi di click sui bottoni, verifica il tipo di bottone
	 * che è stato cliccato, quindi agisce di conseguenza prendendo il parametro
	 * corrispondente e notificando i metodi che erano in attesa.
	 * 
	 * @param e evento di click sul bottone.
	 */
	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof BottoneTipoGioco){
			tipoGioco = ((BottoneTipoGioco)e.getSource()).getTipoGioco();
			notify();
		}
		if (e.getSource() instanceof BottoneTipoRete){
			tipoRete = ((BottoneTipoRete)e.getSource()).getTipoRete();
			notify();
		}
		if (e.getSource() instanceof BottoneTipoPartitaInRete){
			tipoPartita = ((BottoneTipoPartitaInRete)e.getSource()).getTipoPartita();
			notify();
		}
		if (e.getSource() instanceof BottoneInvioInput){
			notify();
		}
	}

	/**
	 * Crea i componenti necessari alla scelta del numero di giocatori: 
	 * una label informativa e una combobox con i numeri di giocatori possibili.
	 */
	private void creaComponentiSceltaNumeroGiocatori() {
		label = new JLabel();
		label.setFont(label.getFont().deriveFont(30f));
		label.setBackground(Color.black);
		label.setForeground(Color.white);
		label.setText(" Seleziona numero di giocatori ");
		label.setOpaque(true);
		comboBox = new JComboBox<Integer>();
		for (int i = 2; i <= Costanti.NUMERO_GIOCATORI_MASSIMO; i++){
			comboBox.addItem(i);
		}
		comboBox.setVisible(true);

	}

	/**
	 * Crea la finestra grande quanto sarà poi la mappa durante il gioco,
	 * la centra rispetto allo schermo, impone l'uscita dal gioco con la chiusura della finestra, 
	 * la impone non ridimensionabile e visibile.
	 */
	private void creaFinestra() {
		setSize(Costanti.DIMENSIONE_MAPPA);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}

	/**
	 * Crea un pannello sfondo da usare come contenitore con lo sfondo dell'immagine del menu,
	 * un pannello bottoni che conterra i vari bottoni che viene aggiunto a sud e un pannello input
	 * con le componenti necessarie per la scelta del numero di giocatori a nord. Infine il pannello di
	 * sfondo è aggiunto alla finestra.
	 */
	private void creaPannelli() {
		pannelloSfondo = new PannelloSfondo(Costanti.PERCORSO_FILE_MENU);
		pannelloBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER,Costanti.DISTANZA_BOTTONI_MENU,Costanti.DISTANZA_INFERIORE_BOTTONI_MENU));
		pannelloBottoni.setOpaque(false);
		for (int i = 0; i < TipoGioco.values().length; i++){
			BottoneTipoGioco bottone = new BottoneTipoGioco(TipoGioco.values()[i]);
			bottone.addActionListener(this);
			pannelloBottoni.add(bottone);
		}
		pannelloInput = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,Costanti.DIMENSIONE_MAPPA.height/2));
		pannelloInput.setOpaque(false);
		creaComponentiSceltaNumeroGiocatori();
		pannelloInput.add(label);
		pannelloInput.add(comboBox);
		pannelloSfondo.add(pannelloBottoni,BorderLayout.SOUTH);
		pannelloSfondo.add(pannelloInput,BorderLayout.CENTER);
		add(pannelloSfondo);
	}

	/**
	 * 
	 * @return il valore di numero di giocatori selezionato nella combobox.
	 */
	public int getNumeroGiocatori() {
		return (int) comboBox.getSelectedItem();
	}

	/**
	 * Attende che un bottone venga cliccatl. Quando ciò avviene torna il tipo di gioco scelto.
	 * @return tipo di gioco scelto (offline o online)
	 */
	public synchronized TipoGioco scegliTipoGioco(){
		tipoGioco = null;
		try {
			wait();
		} catch (InterruptedException e) {
			new FinestraNotifica("Il thread in attesa della scelta del tipo di gioco ha terminato la sua attesa inaspettatamente", e);
		}
		return tipoGioco;
	}

	/**
	 * Cambia il pannello per permettere all'utente di scegliere il tipo di rete, quindi
	 * si mette in attesa che un bottone venga cliccato, Quando ciò avviene torna il tipo di rete scelto.
	 * 
	 * @return tipo di rete scelto (socket o RMI).
	 */
	public synchronized TipoRete scegliTipoRete() {
		cambiaPannelloPerRete();
		tipoRete = null;
		try {
			wait();
		} catch (InterruptedException e) {
			new FinestraNotifica("Il thread in attesa della scelta del tipo di rete ha terminato la sua attesa in maniera inaspettata", e);
		}
		return tipoRete;
	}

	/**
	 * Cambia il pannello per permettere all'utente di scegliere il tipo di partita in rete, quindi
	 * si mette in attesa che un bottone venga cliccato, Quando ciò avviene torna il tipo di partita scelto.
	 * 
	 * @return tipo di partita in rete scelta (host o client)
	 */
	public synchronized TipoPartitaInRete scegliHostOClient() {
		cambiaPannelloPerTipoPartitaInRete();
		tipoPartita = null;
		try {
			wait();
		} catch (InterruptedException e) {
			new FinestraNotifica("Il thread in attesa della scelta del tipo di partita in rete ha terminato la sua attesa in maniera inaspettata", e);
		}
		return tipoPartita;
	}

	/**
	 * Cambia il pannello per permettere all'utente di inserire l'indirizzo IP, quindi
	 * si mette in attesa che il bottone di conferma venga cliccato e, finchè l'IP digitato
	 * non è di un formato valido lo fa reinserire. Quindi torn l'IP digitato.
	 * 
	 * @return indirizzo IP digitato. 
	 */
	public synchronized String digitaIP(){
		cambiaPannelloPerInserimentoIP();
		String ip = null;
		do {
			try {
				wait();
			} catch (InterruptedException e) {
				new FinestraNotifica("Il thread in attesa della scelta del tipo di partita in rete ha terminato la sua attesa in maniera inaspettata", e);
			}
			ip = indirizzoIP.getText();
			if (!ipValido(ip)){
				new FinestraNotifica("L'indirizzo IP immesso non è valido.");
			}
		}while(!ipValido(ip));
		return ip;
	}

	/**
	 * Rimuove tutti i bottoni presenti nel pannello bottoni e aggiunge quelli per
	 * scegliere il tipo di partita in rete.
	 */
	private void cambiaPannelloPerTipoPartitaInRete() {
		pannelloBottoni.removeAll();
		for (int i = 0; i < TipoPartitaInRete.values().length; i++){
			BottoneTipoPartitaInRete bottone = new BottoneTipoPartitaInRete(TipoPartitaInRete.values()[i]);
			bottone.addActionListener(this);
			pannelloBottoni.add(bottone);
		}
		pannelloSfondo.revalidate();
		pannelloBottoni.revalidate();
	}

	/**
	 * Rimuove tutti i bottoni presenti nel pannello bottoni e aggiunge quelli per
	 * scegliere il tipo di rete. Rimuove i componenti del pannello input e lo nasconde.
	 */
	private void cambiaPannelloPerRete() {
		pannelloBottoni.removeAll();
		for (int i = 0; i < TipoRete.values().length; i++){
			BottoneTipoRete bottone = new BottoneTipoRete(TipoRete.values()[i]);
			bottone.addActionListener(this);
			pannelloBottoni.add(bottone);
		}
		pannelloInput.removeAll();
		pannelloInput.setVisible(false);
		pannelloInput.revalidate();
		pannelloSfondo.revalidate();
		pannelloBottoni.revalidate();
	}
	
	/**
	 * Rimuove tutti i bottoni presenti nel pannello bottoni e aggiunge quello di conferma.
	 * Aggiunge al pannello input una label infrmativa più la casella di testo dove digitare l'indirizzo.
	 */
	private void cambiaPannelloPerInserimentoIP() {
		pannelloBottoni.removeAll();
		BottoneInvioInput bottone = new BottoneInvioInput();
		bottone.addActionListener(this);
		pannelloBottoni.add(bottone);
		pannelloBottoni.revalidate();
		pannelloBottoni.repaint();
		indirizzoIP = new JFormattedTextField(IP_PATTERN);
		indirizzoIP.setText("192.168.1.1");
		label.setText(" Inserisci l'indirizzo IP del server ");
		pannelloInput.add(label);
		pannelloInput.add(indirizzoIP);
		pannelloInput.setVisible(true);
		pannelloInput.revalidate();
		pannelloSfondo.revalidate();
		pannelloBottoni.revalidate();
	}

	/**
	 * Torna true se l'ip passato da parametro è di un formato valido, false altrimenti.
	 * 
	 * @param indirizzoIP indirizzo da verificare.
	 * @return boolean che indica se l'indirizzo ha un formato valido o meno.
	 */
	private boolean ipValido(String indirizzoIP){
		Pattern pattern = Pattern.compile(IP_PATTERN);
		Matcher matcher = pattern.matcher(indirizzoIP);
		return matcher.matches();             
	}

}