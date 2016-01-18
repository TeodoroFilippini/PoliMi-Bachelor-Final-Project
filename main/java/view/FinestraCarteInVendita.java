package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Carta;
import model.CartaInVendita;
/**
 * Finestra che visualizza le carte in vendita e permette di 
 * sceglierne una da comprare.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class FinestraCarteInVendita extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private CartaInVendita cartaScelta;
	private JLabel label;
	private JPanel pannelloBottoni;
	private FinestraGioco finestraGioco;
	private IstanzeView istanzeView;
	private List<CartaInVendita> listaCarteInVendita;

	public FinestraCarteInVendita (List<CartaInVendita> listaCarteInVendita,IstanzeView istanzeView,FinestraGioco finestraGioco){
		super (finestraGioco);
		this.finestraGioco = finestraGioco;
		this.istanzeView = istanzeView;
		this.listaCarteInVendita = listaCarteInVendita;
		label = new JLabel("Scegli la carta da comprare");	
		creaBottoni();
		creaFinestra();
		setCursor(StrumentiView.getCursore());
	}

	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof BottoneCarta){
			cartaScelta = cercaCartaInVendita(((BottoneCarta)e.getSource()).getCarta());
			notify();
		}else{
			cartaScelta = null;
			notify();
		}
	}

	private CartaInVendita cercaCartaInVendita(Carta carta) {
		Iterator<CartaInVendita> scorriCarte = listaCarteInVendita.iterator();
		while (scorriCarte.hasNext()){
			CartaInVendita cartaInVendita = scorriCarte.next();
			if (cartaInVendita.getCarta() == carta) {
				return cartaInVendita;
			}
		}
		return null;
	}

	private void creaBottoni() {
		pannelloBottoni = new JPanel(new FlowLayout());
		Iterator<CartaInVendita> scorriCarte = listaCarteInVendita.iterator();
		while(scorriCarte.hasNext()){
			CartaInVendita cartaInVendita = scorriCarte.next();
			BottoneCarta bottoneCarta;
			bottoneCarta = new BottoneCarta(istanzeView.cercaCartaView(cartaInVendita.getCarta()),cartaInVendita.getCosto());
			bottoneCarta.addActionListener(this);
			pannelloBottoni.add(bottoneCarta);
		}
		JButton bottoneConferma = StrumentiView.creaBottoneConferma();
		bottoneConferma.addActionListener(this);
		pannelloBottoni.add(bottoneConferma);
	}

	private void creaFinestra() {
		JPanel pannelloNord = new JPanel(new BorderLayout());
		JPanel contenitore = new JPanel(new BorderLayout());
		pannelloNord.add(label,BorderLayout.NORTH);
		pannelloNord.setPreferredSize(label.getPreferredSize());
		pannelloNord.setOpaque(false);
		pannelloBottoni.setOpaque(false);
		contenitore.setOpaque(false);
		setLayout(new BorderLayout());
		this.getContentPane().setBackground(Color.white);
		contenitore.add(pannelloNord,BorderLayout.NORTH);
		contenitore.add(pannelloBottoni,BorderLayout.SOUTH);
		add(contenitore);
		setLayout(new FlowLayout());
		setUndecorated(true);
		pack();
		setLocationRelativeTo(finestraGioco);
		setAutoRequestFocus(false);
		setResizable(false);
		setVisible(true);
	}

	public synchronized CartaInVendita scegliCarta(){
		cartaScelta = null;
		try {
			wait();
		} catch (InterruptedException e) {
			new FinestraNotifica("Il thread in attesa della scelta della carta ha terminato inaspettatamente la sua esecuzione", e);
		}
		return cartaScelta;
	}
}
