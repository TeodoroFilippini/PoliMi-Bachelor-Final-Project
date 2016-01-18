package model;

/** La classe Casella rappresenta la casella numerata nella mappa del 
 * gioco. Il numero sopra di essa rappresenta il numero che il tiro del 
 * dado di un animale indipendente (lupo o pecora nera) deve ottenere 
 * per attraversarla. Una casella può essere vuota, occupata da un pastore,
 * oppure occupata da un recinto (normale o finale). Una volta che viene 
 * occupata da un pastore, sarà subito dopo occupata da un recinto quindi
 * non tornerà più libera. Ogniqualvolta la casella potrà essere selezionabile 
 * (quando muoviamo, selezioniamo o posizioniamo un pastore), un boolean 
 * associato diventerà true. 
 * La classe dispone di un ID univoco che identifica ogni casella 
 * nell'ordine della sua creazione, e torna comodo per fare confronti 
 * tra oggetti animale serializzati. Il metodo equals() infatti non è 
 * adeguato per la presenza di attributi non primitivi.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */

public class Casella extends ElementoMappa{
	
	private static int contatoreiD = 0;
	private static final long serialVersionUID = 1L;
	private int iD;
	private int numero;
	private boolean occupataRecinto;
	private boolean occupataRecintoFinale;
	private Pastore pastore;
	private boolean selezionabile;
	private String nome;

	/**
	 * Costruttore della classe Casella. Setta i parametri e incrementa
	 * l'ID statico.
	 * @param numero numero non univoco della casella sulla mappa (da 1 a 6).
	 */
	public Casella (String nome, int numero){
		this.nome = nome;
		this.numero = numero;
		this.iD = contatoreiD;
		contatoreiD++;
	}

	/**
	 * 
	 * @return l'ID univoco della casella.
	 */
	public int getID() {
		return iD;
	}
	
	/**
	 * 
	 * @return il numero della casella sulla mapp (1-2-3-4-5-6).
	 */
	public int getNumero() {
		return numero;
	}

	/**
	 * 
	 * @return il pastore presente sulla casella; ritorna null
	 * 		   se la casella non ha un pastore sopra di sè.
	 */
	public Pastore getPastore() {
		return pastore;
	}

	/**
	 * 
	 * @return true se la casella è occupata, false se è libera.
	 */
	public boolean isOccupata(){
		if (pastore != null || occupataRecinto || occupataRecintoFinale) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return true se la casella è occupata da un recinto, false se no.
	 */
	public boolean isOccupataRecinto(){
		return occupataRecinto;
	}

	/**
	 * 
	 * @return true se la casella è occupata da un recinto finale, false se no.
	 */
	public boolean isOccupataRecintoFinale(){
		return occupataRecintoFinale;
	}

	/**
	 * 
	 * @return true se la casella è selezionabile, false se no.
	 */
	public boolean isSelezionabile(){
		return selezionabile;
	}

	/**
	 * 
	 * @param occupataRecinto il boolean che descrive il posizionamento
	 * 		  di un recinto. Tipicamente ugule a true.
	 */
	public void setOccupataRecinto(boolean occupataRecinto) {
		this.occupataRecinto = occupataRecinto;
	}

	/**
	 * 
	 * @param occupataRecintoFinale il boolean che descrive il posizionamento
	 * 		  di un recinto finale. Tipicamente ugule a true.
	 */
	public void setOccupataRecintoFinale(boolean occupataRecintoFinale) {
		this.occupataRecintoFinale = occupataRecintoFinale;		
	}

	/**
	 * 
	 * @param pastore il pastore ora presente sulla casella.
	 */
	public void setPastore(Pastore pastore) {
		this.pastore = pastore;
	}

	/**
	 * 
	 * @param selezionabile lo stato da impostare riguardo alla
	 * 		  possibilità di selezionare la casella.
	 */
	public void setSelezionabile(boolean selezionabile) {
		this.selezionabile = selezionabile;
	}

	/**
	 * @return la stringa che descrive univocamente la casella, 
	 * 		   stampandone l'ID preceduto dalla parola "casella".
	 */
	@Override
	public String toString(){
		return nome;
	}

}
