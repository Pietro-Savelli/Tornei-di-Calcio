package it.uniroma3.torneidicalcio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Giocatore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    private String nome;
    @NotBlank
    private String cognome;
    @NotNull
    private java.time.LocalDate dataDiNascita;
    @NotBlank
    @Enumerated(EnumType.STRING) // serve per dire al db di salvarlo come una stringa(serve per la scalabilita' del cosdice)
    private Ruolo ruolo;
    private Integer altezza;

    @ManyToOne
    private Squadra squadra;

    public Giocatore(String nome, String cognome, LocalDate dataDiNascita, Ruolo ruolo, Integer altezza) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataDiNascita = dataDiNascita;
        this.ruolo = ruolo;
        this.altezza = altezza;
    }

    public Giocatore() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Squadra getSquadra() {
        return squadra;
    }

    public void setSquadra(Squadra squadra) {
        this.squadra = squadra;
    }

    public Integer getAltezza() {
        return altezza;
    }

    public void setAltezza(Integer altezza) {
        this.altezza = altezza;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public LocalDate getDataDiNascita() {
        return dataDiNascita;
    }

    public void setDataDiNascita(LocalDate dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Giocatore giocatore = (Giocatore) o;
        return id == giocatore.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
