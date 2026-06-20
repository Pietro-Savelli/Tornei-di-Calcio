package it.uniroma3.torneidicalcio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "torneo", indexes = {
    @Index(name = "idx_torneo_anno", columnList = "anno")
})
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String nome;
    //da aggiugere un vinocolo delle date accettabili
    private Integer anno;
    @NotBlank  //da aggiungere un vincolo per la lunghezza massima
    private String descrizione;

    @ManyToMany
    private Set<Squadra> squadre = new HashSet<>();

    @OneToMany(mappedBy = "torneo", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Partita> partite = new HashSet<>();

    public Torneo(Integer anno, String descrizione, String nome) {
        this.anno = anno;
        this.descrizione = descrizione;
        this.nome = nome;
    }

    public Torneo() {
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Squadra> getSquadre() {
        return squadre;
    }

    public void setSquadre(Set<Squadra> squadre) {
        this.squadre = squadre;
    }

    public Set<Partita> getPartite() {
        return partite;
    }

    public void setPartite(Set<Partita> partite) {
        this.partite = partite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Torneo torneo = (Torneo) o;
        return Objects.equals(id, torneo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}