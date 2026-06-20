package it.uniroma3.torneidicalcio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "partita", indexes = {
    @Index(name = "idx_partita_torneo", columnList = "torneo_id"),
    @Index(name = "idx_partita_stato", columnList = "stato"),
    @Index(name = "idx_partita_data_ora", columnList = "data_ora"),
    @Index(name = "idx_partita_eliminata", columnList = "eliminata")
})
public class Partita {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long  id;

    private String luogo;

    private Integer goalsAway;
    private Integer goalsHome;
    private java.time.LocalDateTime dataOra;
    @Enumerated(EnumType.STRING)
    private Stato stato;

    @NotNull // una partita deve sempre appartenere a un torneo
    @ManyToOne(fetch = FetchType.LAZY)
    private Torneo torneo;
    @ManyToOne(fetch = FetchType.LAZY)
    private Arbitro arbitro;

    //Una partita coinvolge due squadre (Casa e Trasferta)
    @NotNull // la squadra di casa è obbligatoria
    @ManyToOne(fetch = FetchType.LAZY)
    private Squadra squadraCasa;

    @NotNull // la squadra ospite è obbligatoria
    @ManyToOne(fetch = FetchType.LAZY)
    private Squadra squadraOspite;

    @OneToMany(mappedBy = "partita", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Commento> commenti;

    @Column(nullable = false)
    private boolean eliminata = false;


    public Partita(String luogo, Integer goalsAway, Integer goalsHome, LocalDateTime dataOra, Stato stato) {
        this.luogo = luogo;
        this.goalsAway = goalsAway;
        this.goalsHome = goalsHome;
        this.dataOra = dataOra;
        this.stato = stato;
    }

    public Partita() {
    }
    public Long  getId() {
        return id;
    }

    public void setId(Long  id) {
        this.id = id;
    }
    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public Integer getGoalsAway() {
        return goalsAway;
    }

    public void setGoalsAway(Integer goalsAway) {
        this.goalsAway = goalsAway;
    }

    public Integer getGoalsHome() {
        return goalsHome;
    }

    public void setGoalsHome(Integer goalsHome) {
        this.goalsHome = goalsHome;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public Stato getStato() {
        return stato;
    }

    public void setStato(Stato stato) {
        this.stato = stato;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public Arbitro getArbitro() {
        return arbitro;
    }

    public void setArbitro(Arbitro arbitro) {
        this.arbitro = arbitro;
    }

    public Squadra getSquadraCasa() {
        return squadraCasa;
    }

    public void setSquadraCasa(Squadra squadraCasa) {
        this.squadraCasa = squadraCasa;
    }

    public Squadra getSquadraOspite() {
        return squadraOspite;
    }

    public void setSquadraOspite(Squadra squadraOspite) {
        this.squadraOspite = squadraOspite;
    }

    public List<Commento> getCommenti() {
        return commenti;
    }

    public void setCommenti(List<Commento> commenti) {
        this.commenti = commenti;
    }

    public boolean isEliminata() { return eliminata; }
    public void setEliminata(boolean eliminata) { this.eliminata = eliminata; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Partita partita = (Partita) o;
        return Objects.equals(id, partita.id); // FIX: Long è un oggetto, == confronta riferimenti non valori
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}