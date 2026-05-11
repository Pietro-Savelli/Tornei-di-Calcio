package it.uniroma3.torneidicalcio.repository;

import it.uniroma3.torneidicalcio.model.Partita;
import it.uniroma3.torneidicalcio.model.Torneo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TorneoRepository extends CrudRepository<Torneo, Long> {

    @Query("SELECT t FROM Torneo t ORDER BY t.anno desc")
    List<Torneo> findAllOrdinatiPerAnno();

    @Query("SELECT p FROM Partita p WHERE p.torneo.id = :idTorneo")
    List<Partita> findCalendarioByTorneoId(@Param("idTorneo") Long id);
}
