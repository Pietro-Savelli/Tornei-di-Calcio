package it.uniroma3.torneidicalcio.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "preferiti", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"utente_id", "torneo_id"})
})
public class Preferito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "utente_id")
    private Utente utente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "torneo_id")
    private Torneo torneo;

    public Preferito() {}

    public Preferito(Utente utente, Torneo torneo) {
        this.utente = utente;
        this.torneo = torneo;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }
    public Torneo getTorneo() { return torneo; }
    public void setTorneo(Torneo torneo) { this.torneo = torneo; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Preferito preferito = (Preferito) o;
        return Objects.equals(id, preferito.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}