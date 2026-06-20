package it.uniroma3.torneidicalcio.service;

import it.uniroma3.torneidicalcio.exception.PartitaNotFoundException;
import it.uniroma3.torneidicalcio.model.Partita;
import it.uniroma3.torneidicalcio.model.Stato;
import it.uniroma3.torneidicalcio.repository.PartitaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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

    // Ritorna UNA pagina di partite attive del torneo (size=1 → una partita alla volta).
    @Transactional(readOnly = true)
    public Page<Partita> getPaginaPartite(Long torneoId, int page, int size) {
        return partitaRepository.findPaginaPartiteByTorneoId(torneoId, PageRequest.of(page, size));
    }

    // ===== DATI HOME (Requisito 1) =====
    // Le 3 partite più recenti già giocate (FINISHED) del torneo.
    @Transactional(readOnly = true)
    public List<Partita> getUltimePartiteGiocate(Long torneoId, int quante) {
        return partitaRepository.findUltimePartiteGiocate(torneoId, Stato.FINISHED, PageRequest.of(0, quante));
    }

    // Le prossime 3 partite in programma (data futura) del torneo.
    @Transactional(readOnly = true)
    public List<Partita> getProssimePartite(Long torneoId, int quante) {
        return partitaRepository.findProssimePartite(torneoId, LocalDateTime.now(), Stato.FINISHED, PageRequest.of(0, quante));
    }

    public Partita findById(Long id) {
        return partitaRepository.findById(id)
                .orElseThrow(() -> new PartitaNotFoundException(id));
    }

    //ADMIN
    @Transactional
    public Partita save(Partita partita) {
        return partitaRepository.save(partita);
    }

    @Transactional
    public void deleteById(Long id) {
        Partita partita = findById(id);
        if (partita == null) return;

        if (partita.getStato() == Stato.FINISHED) {
            // Partita giocata: soft delete per non sballare la classifica
            partita.setEliminata(true);
            partitaRepository.save(partita);
        } else {
            // Partita non giocata: hard delete, nessun danno storico
            partitaRepository.deleteById(id);
        }
    }

    @Transactional
    public Partita update(Long id, Partita partitaAggiornata) {
        Partita partita = findById(id);
        if (partita == null) return null;

        partita.setLuogo(partitaAggiornata.getLuogo());
        partita.setDataOra(partitaAggiornata.getDataOra());
        partita.setSquadraCasa(partitaAggiornata.getSquadraCasa());
        partita.setSquadraOspite(partitaAggiornata.getSquadraOspite());
        partita.setTorneo(partitaAggiornata.getTorneo());

        Stato nuovoStato = partitaAggiornata.getStato();
        if (nuovoStato != null && nuovoStato != partita.getStato()) {
            partita.setStato(nuovoStato);

            if (nuovoStato == Stato.SCHEDULED || nuovoStato == Stato.POSTPONED) {
                partita.setGoalsHome(null);
                partita.setGoalsAway(null);
            }
        }

        return partitaRepository.save(partita);
    }

    @Transactional
    public void inserisciRisultato(Long id, Integer goalsHome, Integer goalsAway) {
        Partita partita = findById(id);
        if (partita != null) {
            partita.setGoalsHome(goalsHome);
            partita.setGoalsAway(goalsAway);
            partita.setStato(Stato.FINISHED);
            partitaRepository.save(partita);
        }
    }

    @Transactional
    public void modificaRisultato(Long id, Integer goalsHome, Integer goalsAway) {
        Partita partita = findById(id);
        if (partita != null && partita.getStato() == Stato.FINISHED) {
            partita.setGoalsHome(goalsHome);
            partita.setGoalsAway(goalsAway);
            partitaRepository.save(partita);
        }
    }
}