package it.uniroma3.torneidicalcio.exception;

import java.time.format.DateTimeFormatter;

public class PartitaDuplicataException extends RuntimeException {

    public PartitaDuplicataException(String squadraCasa, String squadraOspite, String dataOra) {
        super("Esiste già una partita tra " + squadraCasa + " e " + squadraOspite + " in data " + dataOra);
    }
}
