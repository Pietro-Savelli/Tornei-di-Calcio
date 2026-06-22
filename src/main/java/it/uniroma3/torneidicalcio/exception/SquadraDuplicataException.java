package it.uniroma3.torneidicalcio.exception;


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
