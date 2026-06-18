package it.uniroma3.torneidicalcio.exception;

/**
 * Eccezione di dominio per segnalare che una partita ricercata non è stata trovata nel database.
 */
public class PartitaNotFoundException extends RuntimeException {

    private final Long idPartita;

    public PartitaNotFoundException(Long idPartita) {
        super("Partita con id " + idPartita + " non trovata.");
        this.idPartita = idPartita;
    }

    public Long getIdPartita() {
        return idPartita;
    }
}
