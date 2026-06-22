package it.uniroma3.torneidicalcio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Arbitro {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String nome;
    @NotBlank
    private String cognome;
    @NotNull
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

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Arbitro arbitro = (Arbitro) o;
        return Objects.equals(id, arbitro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
