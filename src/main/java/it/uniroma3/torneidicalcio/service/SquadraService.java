package it.uniroma3.torneidicalcio.service;

import it.uniroma3.torneidicalcio.exception.SquadraDuplicataException;
import it.uniroma3.torneidicalcio.model.Giocatore;
import it.uniroma3.torneidicalcio.model.Partita;
import it.uniroma3.torneidicalcio.model.Squadra;
import it.uniroma3.torneidicalcio.model.Stato;
import it.uniroma3.torneidicalcio.repository.PartitaRepository;
import it.uniroma3.torneidicalcio.repository.SquadraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Service
public class SquadraService {
    SquadraRepository squadraRepository;
    PartitaRepository partitaRepository;

    public SquadraService(SquadraRepository squadraRepository, PartitaRepository partitaRepository) {
        this.squadraRepository = squadraRepository;
        this.partitaRepository = partitaRepository;
    }

    @Transactional(readOnly = true)
    public List<Squadra> findAll() {
        return squadraRepository.findAllAttive();
    }

    @Transactional(readOnly = true)
    public Squadra findById(Long id) {
        return squadraRepository.findById(id).orElse(null);// ritorno del torneo se esiste se no null
    }

    //ADMIN
    @Transactional
    public Squadra save(Squadra squadra) {
        return squadraRepository.save(squadra);
    }


    @Transactional
    public Squadra iscriviSquadra(Squadra squadra) {
        if (squadraRepository.existsByNomeAndAnnoFondazioneAndEliminataFalse(squadra.getNome(), squadra.getAnnoFondazione())) {
            throw new SquadraDuplicataException(squadra.getNome());
        }
        if(squadraRepository.existsByNomeAndAnnoFondazioneAndEliminataTrue(squadra.getNome(), squadra.getAnnoFondazione())) {
            Squadra esistente = squadraRepository.findByNomeAndAnnoFondazione(squadra.getNome(), squadra.getAnnoFondazione());
            esistente.setEliminata(false);
            if (squadra.getStemmaUrl() != null) {
                esistente.setStemmaUrl(squadra.getStemmaUrl());
            }
            return squadraRepository.save(esistente);
        }
        else
            return squadraRepository.save(squadra);
    }

    @Transactional
    public void deleteById(Long id) {
        Squadra squadra = findById(id);
        if (squadra == null) {
            return;
        }

        for (Giocatore g : squadra.getGiocatori()) {
            g.setSquadra(null);
        }

        Iterator<Partita> iterCasa = squadra.getPartiteGiocateInCasa().iterator();
        while (iterCasa.hasNext()) {
            Partita p = iterCasa.next();
            if (p.getStato() != Stato.FINISHED) {
                partitaRepository.delete(p);
                iterCasa.remove();
            }
        }

        Iterator<Partita> iterTrasferta = squadra.getPartiteGiocateInTrasferta().iterator();
        while (iterTrasferta.hasNext()) {
            Partita p = iterTrasferta.next();
            if (p.getStato() != Stato.FINISHED) {
                partitaRepository.delete(p);
                iterTrasferta.remove();
            }
        }

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
            if (squadraAggiornata.getStemmaUrl() != null) {
                squadra.setStemmaUrl(squadraAggiornata.getStemmaUrl());
            }
            return squadraRepository.save(squadra);
        }
        return null;
    }
}