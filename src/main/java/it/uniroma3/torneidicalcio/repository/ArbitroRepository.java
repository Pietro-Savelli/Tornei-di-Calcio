package it.uniroma3.torneidicalcio.repository;

import it.uniroma3.torneidicalcio.model.Arbitro;
import it.uniroma3.torneidicalcio.model.Commento;
import it.uniroma3.torneidicalcio.model.Partita;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArbitroRepository extends CrudRepository<Arbitro, Long> {
    List<Arbitro> findAll();
}
