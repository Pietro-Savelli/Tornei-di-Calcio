package it.uniroma3.torneidicalcio.repository;

import it.uniroma3.torneidicalcio.model.Partita;
import it.uniroma3.torneidicalcio.model.Torneo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TorneoRepository extends CrudRepository<Torneo, Long> {

    @Query("SELECT t FROM Torneo t ORDER BY t.anno desc")
    List<Torneo> findAllOrdinatiPerAnno();

    @Query("SELECT p FROM Partita p WHERE p.torneo.id =  :idTorneo AND p.eliminata = false")
    List<Partita> findCalendarioByTorneoId(@Param("idTorneo") Long id);

    @Query("SELECT p FROM Partita p WHERE p.torneo.id = :id AND p.eliminata = false")
    List<Partita> findCalendarioAttivoByTorneoId(Long id);
    // Questa query carica il torneo, le sue partite e le squadre associate in un colpo solo
    @Query("SELECT DISTINCT t FROM Torneo t " +
            "LEFT JOIN FETCH t.partite p " +
            "LEFT JOIN FETCH t.squadre " +
            "LEFT JOIN FETCH p.squadraCasa " +
            "LEFT JOIN FETCH p.squadraOspite " +
            "WHERE t.id = :torneoId")
    Torneo findTorneoWithPartiteEFeat(@Param("torneoId") Long torneoId);
}
