package it.uniroma3.torneidicalcio.repository;

import it.uniroma3.torneidicalcio.model.Commento;
import it.uniroma3.torneidicalcio.model.Partita;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentoRepository extends CrudRepository<Commento, Long> {
    List<Commento> findByPartita(Partita partita);
}
