package it.uniroma3.torneidicalcio.repository;

import it.uniroma3.torneidicalcio.model.Partita;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PartitaRepository extends CrudRepository<Partita, Long> {

    Optional<Partita> findFirstByTorneoIdAndDataOraAfterOrderByDataOraAsc(Long torneoId, LocalDateTime dataOra);
}
