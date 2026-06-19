package it.uniroma3.torneidicalcio.exception;

/**
 * Eccezione di dominio per segnalare un errore di business:
 * tentativo di iscrivere una squadra con un nome già esistente nel database.
 */
public class SquadraDuplicataException extends RuntimeException {

    private final String nomeSquadra;

    public SquadraDuplicataException(String nomeSquadra) {
        super("La squadra '" + nomeSquadra + "' è già presente nel sistema.");
        this.nomeSquadra = nomeSquadra;
    }

    public String getNomeSquadra() {
        return nomeSquadra;
    }
}
