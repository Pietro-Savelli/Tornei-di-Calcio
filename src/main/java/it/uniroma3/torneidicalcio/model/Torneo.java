package it.uniroma3.torneidicalcio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
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

    /*
     * ATTENZIONE: orphanRemoval rimosso di proposito.
     * Con il soft delete (eliminata = true) non cancelliamo mai fisicamente le partite
     * dalla collezione JPA, quindi orphanRemoval causerebbe cancellazioni fisiche
     * indesiderate ogni volta che JPA ricarica la collezione senza tutte le partite.
     * CascadeType.REMOVE rimane: se si cancella fisicamente un Torneo, le sue partite
     * vengono cancellate fisicamente anche loro (comportamento corretto).
     */
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
        return Objects.equals(id, torneo.id); // FIX: Long è un oggetto, == confronta riferimenti non valori
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}