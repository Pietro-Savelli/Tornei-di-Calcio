package it.uniroma3.torneidicalcio.service;

import it.uniroma3.torneidicalcio.model.Partita;
import it.uniroma3.torneidicalcio.repository.PartitaRepository;
import it.uniroma3.torneidicalcio.repository.TorneoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PartitaService {
    private final PartitaRepository partitaRepository;

    public PartitaService(PartitaRepository partitaRepository){
        this.partitaRepository = partitaRepository;
    }

    public Optional<Partita> findProssimaPartita(Long id) {
        return partitaRepository.findFirstByTorneoIdAndDataOraAfterOrderByDataOraAsc(id, LocalDateTime.now());
    }
}