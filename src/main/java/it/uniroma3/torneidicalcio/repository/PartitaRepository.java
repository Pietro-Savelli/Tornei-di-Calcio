package it.uniroma3.torneidicalcio.repository;

import it.uniroma3.torneidicalcio.model.Partita;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PartitaRepository extends CrudRepository<Partita, Long> {
    Optional<Partita> findFirstByTorneoIdAndDataOraAfterOrderByDataOraAsc(Long torneoId, LocalDateTime dataOra);

    @Query("SELECT DISTINCT p FROM Partita p " +
            "LEFT JOIN FETCH p.squadraCasa " +
            "LEFT JOIN FETCH p.squadraOspite " +
            "WHERE p.torneo.id = :torneoId AND p.eliminata = false " +
            "ORDER BY p.dataOra")
    List<Partita> findCalendarioByTorneoId(@Param("torneoId") Long torneoId);

}
