package it.uniroma3.torneidicalcio.repository;

import it.uniroma3.torneidicalcio.model.Preferito;
import it.uniroma3.torneidicalcio.model.Utente;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PreferitoRepository extends CrudRepository<Preferito, Long> {

    List<Preferito> findByUtente(Utente utente);

    Optional<Preferito> findByUtenteAndTorneoId(Utente utente, Long torneoId);

    boolean existsByUtenteAndTorneoId(Utente utente, Long torneoId);

    void deleteByUtenteAndTorneoId(Utente utente, Long torneoId);
}