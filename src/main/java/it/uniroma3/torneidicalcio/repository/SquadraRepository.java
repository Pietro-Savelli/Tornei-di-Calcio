package it.uniroma3.torneidicalcio.repository;

import it.uniroma3.torneidicalcio.model.Squadra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SquadraRepository extends CrudRepository<Squadra, Long> {

    @Override
    public List<Squadra> findAll(); //implemta dei filtri nella scgermata (ad esmpio ornia per a-z o per anno)
    @Query("SELECT s FROM Squadra s WHERE s.eliminata = false")
    List<Squadra> findAllAttive();
}
