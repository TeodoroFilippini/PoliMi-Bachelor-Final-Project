package model;

import java.io.Serializable;

/**
 * Classe padre che accomuna Caselle e Regioni,
 * necessaria per via dell'incapacità della libreria 
 * JGraphT di gestire due tipi di vertice in un grafo.
 * I singoli elementiMappa verranno sempre istanziati
 * come caselle o regioni.
 * @author Luca Teodoro Filippini
 * @author Massimiliano Iacono
 */

public abstract class ElementoMappa implements Serializable{

	private static final long serialVersionUID = 1L;
}
