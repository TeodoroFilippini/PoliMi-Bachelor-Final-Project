package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JDialog;

import model.Agnello;
import model.Animale;
import model.Ariete;
import model.Pecora;
import model.PecoraNera;
/**
 * Finestra utilizzata per scegliere gli animali da una lista.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class FinestraAnimali extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private Animale animaleScelto;
	private List<AnimaleView> listaAnimaliView;
	private List<BottoneAnimale> listaBottoni = new ArrayList<BottoneAnimale>();
	private FinestraGioco finestraGioco;


	public FinestraAnimali(List<AnimaleView> listaAnimaliView, FinestraGioco finestraGioco){
		super(finestraGioco);
		this.finestraGioco = finestraGioco;
		this.listaAnimaliView = listaAnimaliView;
		creaBottoni();
		creaFinestra();
		setCursor(StrumentiView.getCursore());
	}

	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof BottoneAnimale){
			BottoneAnimale bottoneAnimale = (BottoneAnimale) e.getSource();
			animaleScelto = bottoneAnimale.getAnimale();
			notify();
		}
	}

	private void creaBottoni() {
		List<AnimaleView> listaPecoreView = new ArrayList<AnimaleView>();
		List<AnimaleView> listaArietiView = new ArrayList<AnimaleView>();
		List<AnimaleView> listaAgnelliView = new ArrayList<AnimaleView>();
		List<AnimaleView> pecoraNeraView = new ArrayList<AnimaleView>();
		Iterator<AnimaleView> scorriAnimali = listaAnimaliView.iterator();
		while (scorriAnimali.hasNext()){
			AnimaleView animaleView = scorriAnimali.next();
			Animale animale = animaleView.getAnimale();
			if (animale instanceof Pecora){
				listaPecoreView.add(animaleView);
			}else if (animale instanceof Ariete){
				listaArietiView.add(animaleView);
			}else if (animale instanceof Agnello){
				listaAgnelliView.add(animaleView);
			}else if (animale instanceof PecoraNera) {
				pecoraNeraView.add(animaleView);
			}
		}

		if (!listaPecoreView.isEmpty()){
			BottoneAnimale bottone = new BottoneAnimale(listaPecoreView);
			listaBottoni.add(bottone);
			add(bottone);
			bottone.addActionListener(this);
			add(new Label("X" + bottone.getNumeroAnimali()));
		}
		if (!listaArietiView.isEmpty()){
			BottoneAnimale bottone = new BottoneAnimale(listaArietiView);
			listaBottoni.add(bottone);
			add(bottone);
			bottone.addActionListener(this);
			add(new Label("X" + bottone.getNumeroAnimali()));
		}
		if (!listaAgnelliView.isEmpty()){
			BottoneAnimale bottone = new BottoneAnimale(listaAgnelliView);
			listaBottoni.add(bottone);
			add(bottone);
			bottone.addActionListener(this);
			add(new Label("X" + bottone.getNumeroAnimali()));
		}
		if (!pecoraNeraView.isEmpty()){
			BottoneAnimale bottone = new BottoneAnimale(pecoraNeraView);
			listaBottoni.add(bottone);
			add(bottone);
			bottone.addActionListener(this);
			add(new Label("X" + bottone.getNumeroAnimali()));
		}

	}


	private void creaFinestra() {
		this.getContentPane().setBackground(Color.white);
		setLayout(new GridLayout(5,2));
		setUndecorated(true);
		setAutoRequestFocus(false);
		pack();
		setLocationRelativeTo(finestraGioco);
		setResizable(false);
		setVisible(true);
	}


	public synchronized Animale scegliAnimale(){
		try {
			wait();
		} catch (InterruptedException e) {
			new FinestraNotifica("Il thread in attesa della scelta del giocatore ha terminato la sua attesa in maniera inaspettata", e);
		}
		return animaleScelto;
	}

}
