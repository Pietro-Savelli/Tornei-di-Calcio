package it.uniroma3.torneidicalcio.repository;

import it.uniroma3.torneidicalcio.model.Giocatore;
import org.springframework.data.repository.CrudRepository;

public interface GiocatoreRepository  extends CrudRepository<Giocatore, Long> {
}
