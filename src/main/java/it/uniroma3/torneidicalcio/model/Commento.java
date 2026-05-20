package it.uniroma3.torneidicalcio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Commento {
    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


}
