package it.uniroma3.torneidicalcio.service;

import it.uniroma3.torneidicalcio.model.Preferito;
import it.uniroma3.torneidicalcio.model.Torneo;
import it.uniroma3.torneidicalcio.model.Utente;
import it.uniroma3.torneidicalcio.repository.PreferitoRepository;
import it.uniroma3.torneidicalcio.repository.TorneoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PreferitoService {

    private final PreferitoRepository preferitoRepository;
    private final TorneoRepository torneoRepository;
    public PreferitoService(PreferitoRepository preferitoRepository, TorneoRepository torneoRepository) {
        this.preferitoRepository = preferitoRepository;
        this.torneoRepository = torneoRepository;
    }

    @Transactional(readOnly = true)
    public Set<Long> getTorneoIdsPreferiti(Utente utente) {
        Set<Long> torneoIds = new HashSet<>();
        List<Preferito> preferiti = preferitoRepository.findByUtente(utente);
        for (Preferito p : preferiti) {
            torneoIds.add(p.getTorneo().getId());
        }
        return torneoIds;
    }

    @Transactional
    public void aggiungi(Utente utente, Long torneoId) {
        if (preferitoRepository.existsByUtenteAndTorneoId(utente, torneoId)) {
            return;
        }
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new RuntimeException("Torneo non trovato"));
        Preferito p = new Preferito(utente, torneo);
        preferitoRepository.save(p);
    }

    @Transactional
    public void rimuovi(Utente utente, Long torneoId) {
        preferitoRepository.deleteByUtenteAndTorneoId(utente, torneoId);
    }

    @Transactional(readOnly = true)
    public boolean isPreferito(Utente utente, Long torneoId) {
        return preferitoRepository.existsByUtenteAndTorneoId(utente, torneoId);
    }
}