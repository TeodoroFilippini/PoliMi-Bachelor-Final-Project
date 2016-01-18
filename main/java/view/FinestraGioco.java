package view;

import javax.swing.JFrame;
/**
 * Finestra di gioco. Contiene tutti i vari componenti
 * che verranno poi aggiunti o modificati durante il gioco.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 *
 */
public class FinestraGioco extends JFrame{

	private static final long serialVersionUID = 1L;

	/**
	 * Crea la finestra di gioco.
	 */
	public FinestraGioco(){
		super("S H E E P L A N D");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setCursor(StrumentiView.getCursore());
		setResizable(false);
	}
}
