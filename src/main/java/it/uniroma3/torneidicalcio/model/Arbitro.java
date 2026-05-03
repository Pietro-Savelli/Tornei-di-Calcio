package it.uniroma3.torneidicalcio.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Arbitro {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String nome;
    private String cognome;
    private  String codiceArbitrale;

    @OneToMany(mappedBy = "arbitro")
    private Set<Partita> partite = new HashSet<>();

    public Arbitro(String nome, String cognome, String codiceArbitrale) {
        this.nome = nome;
        this.cognome = cognome;
        this.codiceArbitrale = codiceArbitrale;
    }

    public Arbitro() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCongome() {
        return cognome;
    }

    public void setCongome(String congome) {
        this.cognome = congome;
    }

    public String getCodiceArbitrale() {
        return codiceArbitrale;
    }

    public void setCodiceArbitrale(String codiceArbitrale) {
        this.codiceArbitrale = codiceArbitrale;
    }

    public Set<Partita> getPartite() {
        return partite;
    }

    public void setPartite(Set<Partita> partite) {
        this.partite = partite;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Arbitro arbitro = (Arbitro) o;
        return id == arbitro.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
