package it.uniroma3.torneidicalcio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;


@Entity
public class Partita {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    private String luogo;

    @NotBlank
    private Integer goalsAway;
    @NotBlank
    private Integer goalsHome;
    private java.time.LocalDateTime dataOra;
    @NotBlank
    private Stato stato;

    @ManyToOne
    private Torneo torneo;
    @ManyToOne
    private Arbitro arbitro;

    //Una partita coinvolge due squadre (Casa e Trasferta)
    @ManyToOne
    @NotNull // Rende obbligatoria la presenza della squadra
    private Squadra squadraCasa;

    @ManyToOne
    @NotNull // Rende obbligatoria la presenza della squadra
    private Squadra squadraOspite;


    public Partita(String luogo, Integer goalsAway, Integer goalsHome, LocalDateTime dataOra, Stato stato) {
        this.luogo = luogo;
        this.goalsAway = goalsAway;
        this.goalsHome = goalsHome;
        this.dataOra = dataOra;
        this.stato = stato;
    }

    public Partita() {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Partita partita = (Partita) o;
        return id == partita.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
