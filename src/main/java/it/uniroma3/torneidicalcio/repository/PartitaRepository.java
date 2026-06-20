package it.uniroma3.torneidicalcio.repository;

import it.uniroma3.torneidicalcio.model.Partita;
import it.uniroma3.torneidicalcio.model.Stato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    //pagina lunga n
    @Query(value = "SELECT p FROM Partita p " +
            "LEFT JOIN FETCH p.squadraCasa " +
            "LEFT JOIN FETCH p.squadraOspite " +
            "WHERE p.torneo.id = :torneoId AND p.eliminata = false " +
            "ORDER BY p.dataOra ASC",
            countQuery = "SELECT count(p) FROM Partita p " +
                    "WHERE p.torneo.id = :torneoId AND p.eliminata = false")
    Page<Partita> findPaginaPartiteByTorneoId(@Param("torneoId") Long torneoId, Pageable pageable);

   //partite finite
    @Query("SELECT p FROM Partita p " +
            "LEFT JOIN FETCH p.squadraCasa " +
            "LEFT JOIN FETCH p.squadraOspite " +
            "LEFT JOIN FETCH p.torneo " +
            "WHERE p.torneo.id = :torneoId AND p.eliminata = false AND p.stato = :stato " +
            "ORDER BY p.dataOra DESC")
    List<Partita> findUltimePartiteGiocate(@Param("torneoId") Long torneoId,
                                           @Param("stato") Stato stato,
                                           Pageable pageable);

    //partite programmate
    @Query("SELECT p FROM Partita p " +
            "LEFT JOIN FETCH p.squadraCasa " +
            "LEFT JOIN FETCH p.squadraOspite " +
            "LEFT JOIN FETCH p.torneo " +
            "WHERE p.torneo.id = :torneoId AND p.eliminata = false " +
            "AND p.dataOra > :adesso AND p.stato <> :statoFinita " +
            "ORDER BY p.dataOra ASC")
    List<Partita> findProssimePartite(@Param("torneoId") Long torneoId,
                                      @Param("adesso") LocalDateTime adesso,
                                      @Param("statoFinita") Stato statoFinita,
                                      Pageable pageable);


    //verifica per eccezioni
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Partita p " +
            "WHERE p.torneo.id = :torneoId AND p.dataOra = :dataOra " +
            "AND p.squadraCasa.id = :squadraCasaId AND p.squadraOspite.id = :squadraOspiteId " +
            "AND p.eliminata = false")
    boolean existsByTorneoDataOraSquadre(@Param("torneoId") Long torneoId,
                                         @Param("dataOra") LocalDateTime dataOra,
                                         @Param("squadraCasaId") Long squadraCasaId,
                                         @Param("squadraOspiteId") Long squadraOspiteId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Partita p " +
            "WHERE p.torneo.id = :torneoId AND p.dataOra = :dataOra " +
            "AND p.squadraCasa.id = :squadraCasaId AND p.squadraOspite.id = :squadraOspiteId " +
            "AND p.id != :id AND p.eliminata = false")
    boolean existsByTorneoDataOraSquadreEscludendoId(@Param("torneoId") Long torneoId,
                                                      @Param("dataOra") LocalDateTime dataOra,
                                                      @Param("squadraCasaId") Long squadraCasaId,
                                                      @Param("squadraOspiteId") Long squadraOspiteId,
                                                      @Param("id") Long id);
}
