package it.uniroma3.torneidicalcio.service;

import it.uniroma3.torneidicalcio.exception.SquadraDuplicataException;
import it.uniroma3.torneidicalcio.model.Giocatore;
import it.uniroma3.torneidicalcio.model.Squadra;
import it.uniroma3.torneidicalcio.model.Stato;
import it.uniroma3.torneidicalcio.model.Torneo;
import it.uniroma3.torneidicalcio.repository.PartitaRepository;
import it.uniroma3.torneidicalcio.repository.SquadraRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SquadraService {
    SquadraRepository squadraRepository;
    PartitaRepository partitaRepository;

    public SquadraService(SquadraRepository squadraRepository, PartitaRepository partitaRepository) {
        this.squadraRepository = squadraRepository;
        this.partitaRepository = partitaRepository;
    }


    public List<Squadra> fidAll() {
        return squadraRepository.findAllAttive();
    }

    public Squadra findById(Long id) {
        return squadraRepository.findById(id).orElse(null);// ritorno del torneo se esiste se no null
    }

    //ADMIN
    @Transactional
    public Squadra save(Squadra squadra) {
        return squadraRepository.save(squadra);
    }

    /**
     * Validazione semantica di business: prima di salvare verifica che non esista
     * già una squadra (non eliminata) con lo stesso nome. Se il controllo fallisce,
     * lancia un'eccezione di dominio che verrà gestita nel Controller.
     */
    @Transactional
    public Squadra iscriviSquadra(Squadra squadra) {
        if (squadraRepository.existsByNomeAndEliminataFalse(squadra.getNome())) {
            throw new SquadraDuplicataException(squadra.getNome());
        }
        return squadraRepository.save(squadra);
    }

    @Transactional
    public void deleteById(Long id) {
        Squadra squadra = findById(id);
        if (squadra == null) return;

        for (Giocatore g : squadra.getGiocatori()) {
            g.setSquadra(null);
        }

        squadra.getPartiteGiocateInCasa().removeIf(p -> {
            if (p.getStato() != Stato.FINISHED) {
                partitaRepository.delete(p);
                return true;
            }
            return false;
        });
        squadra.getPartiteGiocateInTrasferta().removeIf(p -> {
            if (p.getStato() != Stato.FINISHED) {
                partitaRepository.delete(p);
                return true;
            }
            return false;
        });

        squadra.setEliminata(true);
        squadraRepository.save(squadra);
    }

    @Transactional
    public Squadra update(Long id, Squadra squadraAggiornata) {
        Squadra squadra = findById(id);
        if (squadra != null) {
            squadra.setNome(squadraAggiornata.getNome());
            squadra.setCitta(squadraAggiornata.getCitta());
            squadra.setAnnoFondazione(squadraAggiornata.getAnnoFondazione());
            return squadraRepository.save(squadra);
        }
        return null;
    }
}