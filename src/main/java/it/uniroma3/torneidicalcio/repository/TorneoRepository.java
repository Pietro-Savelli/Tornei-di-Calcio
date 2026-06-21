package it.uniroma3.torneidicalcio.repository;

import it.uniroma3.torneidicalcio.model.Partita;
import it.uniroma3.torneidicalcio.model.Torneo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TorneoRepository extends CrudRepository<Torneo, Long> {

    @Query("SELECT DISTINCT t FROM Torneo t LEFT JOIN FETCH t.squadre ORDER BY t.anno desc")
    List<Torneo> findAllOrdinatiPerAnno();

    @Query("SELECT p FROM Partita p WHERE p.torneo.id =  :idTorneo AND p.eliminata = false")
    List<Partita> findCalendarioByTorneoId(@Param("idTorneo") Long id);

    @Query("SELECT p FROM Partita p WHERE p.torneo.id = :id AND p.eliminata = false")
    List<Partita> findCalendarioAttivoByTorneoId(Long id);

    // Query 1: carica il torneo con le partite e le squadre coinvolte (Casa/Trasferta).
    //  NON c'è prodotto cartesiano in questa query
    @Query("SELECT DISTINCT t FROM Torneo t " +
            "LEFT JOIN FETCH t.partite p " +
            "LEFT JOIN FETCH p.squadraCasa " +
            "LEFT JOIN FETCH p.squadraOspite " +
            "WHERE t.id = :torneoId")
    Torneo findTorneoWithPartite(@Param("torneoId") Long torneoId);

    // Query 2: carica lo stesso torneo con le squadre iscritte (solo bag t.squadre).
    // Eseguita subito dopo la prima: Hibernate ritrova il Torneo in L1 cache
    // e popola la collezione squadre senza duplicare righe.
    @Query("SELECT DISTINCT t FROM Torneo t " +
            "LEFT JOIN FETCH t.squadre " +
            "WHERE t.id = :torneoId")
    Torneo findTorneoWithSquadre(@Param("torneoId") Long torneoId);


    boolean existsByNomeAndAnno(String nome, Integer anno);

    Torneo findByNomeAndAnno(String nome, Integer anno);

    @Query("SELECT COUNT(s) FROM Torneo t JOIN t.squadre s WHERE t.id = :torneoId")
    int countSquadreByTorneoId(@Param("torneoId") Long torneoId);
}
