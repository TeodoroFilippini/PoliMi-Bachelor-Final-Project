package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import model.Costanti;
import model.TipoMossa;
/**
 * Pannello contenente i bottoni con le possibili mosse che può compiere il giocatore.
 * 
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class PannelloBottoni extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private List<BottoneMossa> listaBottoni = new ArrayList<BottoneMossa>();
	private TipoMossa mossaScelta;
	/**
	 * Crea un JPanel con GridLayout da 1 colonna e tante righe quante sono le mosse possibili.
	 * Viene impostato lo sfondo trasparente e ridimensionato, quindi per ogni possibile mossa
	 * creato un nuovo BottoneMossa utilizzando l'apposita classe alla quale passiamo il tipoMossa.
	 * I bottoni vengono aggiunti al pannello, all'opportuna lista, disabilitati e gli viene aggiunto 
	 * l'action listener implementato da questa classe per catturare gli eventi di click sui vari bottoni. 
	 */
	public PannelloBottoni(){
		setLayout(new GridLayout(TipoMossa.values().length,1));
		setOpaque(false);
		setPreferredSize(Costanti.DIMENSIONE_PANNELLI_LATERALI);
		for (int i = 0; i < TipoMossa.values().length; i++){
			BottoneMossa bottone = new BottoneMossa(TipoMossa.values()[i]);
			listaBottoni.add(bottone);
			add(bottone);
			bottone.setEnabled(false);
			bottone.addActionListener(this);
		}
	}

	/**
	 * Cattura l'evento di click sul bottone, imposta l'attributo mossaScelta
	 * alla mossa associata al bottono che ha generato l'evento e notifica lo scegli mossa 
	 * che era in attesa.
	 * 
	 * @param e evento di click sul bottone.
	 */
	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof BottoneMossa){
			BottoneMossa bottoneMossa = (BottoneMossa)e.getSource();
			mossaScelta = bottoneMossa.getMossa();
			notify();
		}
	}
	/**
	 * Scorre la lista di bottoni e li disabilita.
	 */
	public void disabilitaBottoni() {
		Iterator<BottoneMossa> scorriBottoni = listaBottoni.iterator();
		while (scorriBottoni.hasNext()){
			scorriBottoni.next().setEnabled(false);
		}
	}
	/**
	 * Abilita tutti i bottoni associati ad una mossa presente nella lista di mosse
	 * ricevuta da parametro e disabilita tutti gli altri, quindi si mette in attesa 
	 * che un giocatore clicchi su un bottone. Una volta notificata la scelta dell'utente
	 * ritorna la mossa scelta.
	 * 
	 * @param mosseDisponibili lista di mosse che è possibile effettuare.
	 * @return mossa scelta.
	 */
	public synchronized TipoMossa scegliMossa(List<TipoMossa> mosseDisponibili){
		Iterator<BottoneMossa> scorriBottoni = listaBottoni.iterator();
		mossaScelta = null;
		while (scorriBottoni.hasNext()){
			BottoneMossa bottone = scorriBottoni.next();
			if (mosseDisponibili.contains(bottone.getMossa())){
				bottone.setEnabled(true);
			}else{
				bottone.setEnabled(false);
			}
		}
		try {
			wait();
		} catch (InterruptedException e) {
			new FinestraNotifica("Il thread in attesa della scelta della mossa ha terminato la sua attesa in maniera inaspettata", e);
		}
		return mossaScelta;
	}
}
