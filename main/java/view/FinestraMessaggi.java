package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import model.Costanti;

/**
 * Finestra che stampa il messaggio specificato durante il gioco.
 * Utilizzata per visualizzare informazioni su ciò che avviene durante il gioco.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class FinestraMessaggi extends JDialog implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private Timer timer;

	/**
	 * Crea una nuova finestra con il messaggio specificato.
	 * @param messaggio
	 * @param finestraGioco
	 */
	public FinestraMessaggi(String messaggio, FinestraGioco finestraGioco){
		super(finestraGioco);
		setCursor(StrumentiView.getCursore());
		setLayout(new BorderLayout());
		getContentPane().setBackground(Color.black);
		JLabel label = new JLabel(messaggio);
		label.setForeground(Color.white);
		label.setOpaque(false);
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		add(label,BorderLayout.CENTER);		
		setSize(new Dimension(finestraGioco.getWidth(), Costanti.ALTEZZA_FINESTRA_MESSAGGI));
		setLocationRelativeTo(finestraGioco);
		setUndecorated(true);
		setAutoRequestFocus(false);
		setVisible(true);
		timer = new Timer(Costanti.TEMPO_VISIBILITA_FINESTRA,this);
		timer.start();
		waitSynchronized();
	}

	
	/**
	 * Quando scatta il timer la finestra si chiude automaticamente.
	 * 
	 * @param e evento di timer.
	 */
	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		notify();
		//quest'istruzione impedisce deadlock di wait e notify tra finestre, incontrato in debugging:
		timer.stop(); 
		dispose();
	}

	/**
	 * Aspetta finchè la finestra non si chiude.
	 */
	private synchronized void waitSynchronized(){ 
		try {
			wait();
		} catch (InterruptedException e) {
			new FinestraNotifica("Il thread in attesa di chiudere la finestra ha terminato la sua attesa in maniera inaspettata", e);
		}
	}

}
