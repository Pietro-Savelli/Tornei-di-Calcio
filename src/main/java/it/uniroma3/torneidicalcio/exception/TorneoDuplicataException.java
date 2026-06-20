package it.uniroma3.torneidicalcio.exception;

public class TorneoDuplicataException extends RuntimeException {
    private final String nome;
    private final Integer anno;

    public TorneoDuplicataException(String nome, Integer anno) {

        super("Il torneo '" + nome + "' (" + anno + ") è già presente nel sistema.");
        this.nome = nome;
        this.anno = anno;

    }
    public String getNomeTorneo() {
        return nome;
    }
    public Integer getAnnoTorneo() {return anno;}
}
