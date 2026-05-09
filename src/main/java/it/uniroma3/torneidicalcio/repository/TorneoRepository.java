package it.uniroma3.torneidicalcio.repository;

import it.uniroma3.torneidicalcio.model.Torneo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TorneoRepository extends CrudRepository<Torneo, Long> {

    @Query("SELECT t FROM Torneo t ORDER BY t.anno desc")
    List<Torneo> findAllOrdinatiPerAnno();
}
