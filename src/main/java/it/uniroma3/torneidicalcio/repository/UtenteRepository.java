package it.uniroma3.torneidicalcio.repository;

import it.uniroma3.torneidicalcio.model.Utente;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UtenteRepository extends CrudRepository<Utente, Long> {
    Optional<Utente> findByUsername(String username);
}
