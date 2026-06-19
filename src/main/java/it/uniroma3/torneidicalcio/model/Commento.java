package it.uniroma3.torneidicalcio.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Commento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String testo;
    private LocalDateTime dataOra;

    @ManyToOne
    private Utente utente;

    @ManyToOne
    private Partita partita;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Partita getPartita() {
        return partita;
    }

    public void setPartita(Partita partita) {
        this.partita = partita;
    }
}
