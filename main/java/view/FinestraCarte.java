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
import model.Costanti;
/**
 * Finestra utilizzata per scegliere le carte da comprare durante il normale svolgimento
 * del gioco(non quelle in vendita di altri giocatori), quelle che si desidera vendere e il 
 * costo al quale si desidera venderle.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class FinestraCarte extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private Carta cartaScelta;
	private List<CartaView> listaCarteView;
	private JLabel label;
	private JPanel pannelloBottoni;
	private FinestraGioco finestraGioco;
	private boolean compravendita;
	private int costoCarta;

	public FinestraCarte(List<CartaView> listaCarteView,FinestraGioco finestraGioco,boolean compravendita){
		super (finestraGioco);
		this.finestraGioco = finestraGioco;
		this.listaCarteView = listaCarteView;
		this.compravendita = compravendita;
		if (compravendita){
			if (listaCarteView.isEmpty()){
				label = new JLabel ("Premi \"FINE\" per proseguire");
			}else{
				label = new JLabel ("Scegli la carta da vendere");
			}
		}else{
			label = new JLabel("Scegli la carta da comprare");
		}
		creaBottoni();
		creaFinestra();
		setCursor(StrumentiView.getCursore());
	}

	public FinestraCarte(List<CartaView> listaCarteView, String testoLabel, FinestraGioco finestraGioco){
		super(finestraGioco);
		this.finestraGioco = finestraGioco;
		this.listaCarteView = listaCarteView;
		label = new JLabel (testoLabel);
		creaBottoni();
		creaFinestra();
	}

	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof BottoneCarta){
			cartaScelta = ((BottoneCarta)e.getSource()).getCarta();
			costoCarta = ((BottoneCarta)e.getSource()).getCostoCarta();
			notify();
		}else{
			cartaScelta = null;
			notify();
		}
	}

	private void creaBottoni() {
		Iterator<CartaView> scorriCarte = listaCarteView.iterator();
		pannelloBottoni = new JPanel(new FlowLayout());
		while(scorriCarte.hasNext()){
			CartaView cartaView = scorriCarte.next();
			BottoneCarta bottoneCarta;
			if (compravendita){
				bottoneCarta = new BottoneCarta(cartaView,0);
			}else{
				bottoneCarta = new BottoneCarta(cartaView,cartaView.getCarta().getCosto());
			}
			bottoneCarta.addActionListener(this);
			pannelloBottoni.add(bottoneCarta);
		}
		if (compravendita){
			JButton bottoneConferma = StrumentiView.creaBottoneConferma();
			bottoneConferma.addActionListener(this);
			pannelloBottoni.add(bottoneConferma);
		}
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

	public synchronized Carta scegliCarta(){
		cartaScelta = null;
		try {
			wait();
		} catch (InterruptedException e) {
			new FinestraNotifica("Il thread in attesa della scelta della carta ha interrotto la sua attesa inaspettatamente", e);
		}
		return cartaScelta;
	}

	public synchronized int scegliCostoCarta(CartaView cartaView) {
		label.setText("Scegli costo carta");
		pannelloBottoni.removeAll();
		for (int i = 1; i <= Costanti.COSTO_MASSIMO_CARTA; i ++){
			BottoneCarta bottone = new BottoneCarta(cartaView,i);
			bottone.addActionListener(this);
			pannelloBottoni.add(bottone);
		}
		pannelloBottoni.revalidate();
		pack();
		setLocationRelativeTo(finestraGioco);
		try {
			wait();
		} catch (InterruptedException e) {
			new FinestraNotifica("Il thread in attesa della scelta del costo della carta ha interrotto la sua attesa inaspettatamente", e);
		}
		return costoCarta;
	}

}
