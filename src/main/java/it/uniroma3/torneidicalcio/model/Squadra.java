package it.uniroma3.torneidicalcio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Squadra {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    private String nome;
    //da aggiugere un vinocolo delle date accettabili
    private Integer annoFondazione;
    @NotBlank
    private String citta;

    @ManyToMany (mappedBy = "squadre")
    private Set<Torneo> tornei = new HashSet<>();

    @OneToMany(mappedBy = "squadra")
    private Set<Giocatore> giocatori = new HashSet<>();

    @OneToMany(mappedBy = "squadraCasa")
    private Set<Partita> partiteGiocateInCasa = new HashSet<>();

    @OneToMany(mappedBy = "squadraOspite")
    private Set<Partita> partiteGiocateInTrasferta = new HashSet<>();

    public Squadra(String citta, Integer annoFondazione, String nome) {
        this.citta = citta;
        this.annoFondazione = annoFondazione;
        this.nome = nome;
    }

    public Squadra() {
    }

    public Set<Partita> getPartiteGiocateInTrasferta() {
        return partiteGiocateInTrasferta;
    }

    public void setPartiteGiocateInTrasferta(Set<Partita> partiteGiocateInTrasferta) {
        this.partiteGiocateInTrasferta = partiteGiocateInTrasferta;
    }

    public Set<Partita> getPartiteGiocateInCasa() {
        return partiteGiocateInCasa;
    }

    public void setPartiteGiocateInCasa(Set<Partita> partiteGiocateInCasa) {
        this.partiteGiocateInCasa = partiteGiocateInCasa;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public Integer getAnnoFondazione() {
        return annoFondazione;
    }

    public void setAnnoFondazione(Integer annoFondazione) {
        this.annoFondazione = annoFondazione;
    }

    public Set<Torneo> getTornei() {
        return tornei;
    }

    public void setTornei(Set<Torneo> tornei) {
        this.tornei = tornei;
    }

    public Set<Giocatore> getGiocatori() {
        return giocatori;
    }

    public void setGiocatori(Set<Giocatore> giocatori) {
        this.giocatori = giocatori;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Squadra squadra = (Squadra) o;
        return id == squadra.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
