package it.uniroma3.torneidicalcio.service;

import it.uniroma3.torneidicalcio.exception.PartitaDuplicataException;
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

    // Ritorna una pagine contenente size partite
    @Transactional(readOnly = true)
    public Page<Partita> getPaginaPartite(Long torneoId, int page, int size) {
        return partitaRepository.findPaginaPartiteByTorneoId(torneoId, PageRequest.of(page, size));
    }

    // Le 3 partite più recenti FINISHED
    @Transactional(readOnly = true)
    public List<Partita> getUltimePartiteGiocate(Long torneoId, int quante) {
        return partitaRepository.findUltimePartiteGiocate(torneoId, Stato.FINISHED, PageRequest.of(0, quante));
    }

    // Le prossime 3 partite in programma
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
        if (partitaRepository.existsByTorneoDataOraSquadre(
                partita.getTorneo().getId(),
                partita.getDataOra(),
                partita.getSquadraCasa().getId(),
                partita.getSquadraOspite().getId())) {
            throw new PartitaDuplicataException(
                    partita.getSquadraCasa().getNome(),
                    partita.getSquadraOspite().getNome(),
                    partita.getDataOra().toString());
        }
        return partitaRepository.save(partita);
    }

    @Transactional
    public void deleteById(Long id) {
        Partita partita = findById(id);
        if (partita == null) return;
        partitaRepository.deleteById(id);
    }

    @Transactional
    public Partita update(Long id, Partita partitaAggiornata) {
        Partita partita = findById(id);
        if (partita == null) return null;

        if (partitaRepository.existsByTorneoDataOraSquadreEscludendoId(
                partitaAggiornata.getTorneo().getId(),
                partitaAggiornata.getDataOra(),
                partitaAggiornata.getSquadraCasa().getId(),
                partitaAggiornata.getSquadraOspite().getId(),
                id)) {
            throw new PartitaDuplicataException(
                    partitaAggiornata.getSquadraCasa().getNome(),
                    partitaAggiornata.getSquadraOspite().getNome(),
                    partitaAggiornata.getDataOra().toString());
        }

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

    public void deleteSoft(Partita partita) {
        partita.setEliminata(true);
    }

}