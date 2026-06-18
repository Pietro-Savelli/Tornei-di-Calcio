package it.uniroma3.torneidicalcio.service;

import it.uniroma3.torneidicalcio.dto.RigaClassificaDto;
import it.uniroma3.torneidicalcio.model.Partita;
import it.uniroma3.torneidicalcio.model.Squadra;
import it.uniroma3.torneidicalcio.model.Stato;
import it.uniroma3.torneidicalcio.model.Torneo;
import it.uniroma3.torneidicalcio.repository.TorneoRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TorneoService {
    private final TorneoRepository torneoRepository;

    public TorneoService(TorneoRepository torneoRepository){
        this.torneoRepository = torneoRepository;
    }

    public List<Torneo> findAllOrdinatiPerAnno(){
        return torneoRepository.findAllOrdinatiPerAnno();  //return (List<Torneo>) torneoRepository.findAll(); se non sovrascrivo il metodo
    }

    public Torneo findById(Long id) {
        return torneoRepository.findById(id).orElse(null);// ritorno del torneo se esiste se no null
    }

    //ADMIN
    @Transactional
    public Torneo save(Torneo torneo) {
        return torneoRepository.save(torneo);
    }

    @Transactional
    public void deleteById(Long id) {
        Torneo torneo = findById(id);
        if (torneo != null) {
            torneoRepository.delete(torneo);
        }
    }
    @Transactional
    public Torneo update(Long id, Torneo torneoAggiornato) {
        Torneo torneo = findById(id);
        if (torneo != null) {
            torneo.setNome(torneoAggiornato.getNome());
            torneo.setAnno(torneoAggiornato.getAnno());
            torneo.setDescrizione(torneoAggiornato.getDescrizione());
            return torneoRepository.save(torneo);
        }
        return null;
    }

    @Transactional
    public void aggiungiSquadra(Long torneoId, Squadra squadra) {
        Torneo torneo = findById(torneoId);
        if (torneo != null) {
            torneo.getSquadre().add(squadra);
            torneoRepository.save(torneo);
        }
    }

    @Transactional
    public void rimuoviSquadra(Long torneoId, Squadra squadra) {
        Torneo torneo = findById(torneoId);
        if (torneo != null) {
            torneo.getSquadre().remove(squadra);
            torneoRepository.save(torneo);
        }
    }

    @Transactional(readOnly = true)
    public Torneo findByIdWithDetails(Long id) {
        // Carica partite e squadre in due query separate per evitare il prodotto cartesiano
        // tra due collezioni @OneToMany/@ManyToMany dello stesso padre.
        // Hibernate ritrova la stessa istanza di Torneo in L1 cache e popola entrambe le collezioni.
        Torneo torneo = torneoRepository.findTorneoWithPartite(id);
        torneoRepository.findTorneoWithSquadre(id);
        return torneo;
    }

    public @Nullable Object findCalendarioByTorneoId(Long id) {
        return torneoRepository.findCalendarioByTorneoId(id);
    }

    @Transactional(readOnly = true)
    public List<RigaClassificaDto> calcolaClassifica(Long torneoId) {
        // Stesso pattern: una query per il grafo partite, una per le squadre iscritte.
        // Nessun prodotto cartesiano; il DB trasferisce solo le righe necessarie.
        Torneo torneo = torneoRepository.findTorneoWithPartite(torneoId);
        torneoRepository.findTorneoWithSquadre(torneoId);
        Map<Long, RigaClassificaDto> classificaMap = new HashMap<>();

        for (Squadra squadra : torneo.getSquadre()) {
            RigaClassificaDto riga = new RigaClassificaDto(squadra.getId(), squadra.getNome());
            riga.setRitirata(squadra.isEliminata());
            classificaMap.put(squadra.getId(), riga);
        }

        for (Partita partita : torneo.getPartite()) {
            if (partita.getStato() == Stato.FINISHED && !partita.isEliminata()) {
                RigaClassificaDto rigaHome = classificaMap.get(partita.getSquadraCasa().getId());
                RigaClassificaDto rigaAway = classificaMap.get(partita.getSquadraOspite().getId());

                if (rigaHome != null && rigaAway != null) {
                    rigaHome.aggiungiPartita(partita.getGoalsHome(), partita.getGoalsAway());
                    rigaAway.aggiungiPartita(partita.getGoalsAway(), partita.getGoalsHome());
                }
            }
        }

        List<RigaClassificaDto> classificaOrdinata = new ArrayList<>(classificaMap.values());

        classificaOrdinata.sort((a, b) -> {
            if (a.isRitirata() && !b.isRitirata()) return 1;
            if (!a.isRitirata() && b.isRitirata()) return -1;
            if (a.isRitirata() && b.isRitirata()) return 0; // mantieni ordine originale tra ritirate

            if (b.getPunti() != a.getPunti()) {
                return Integer.compare(b.getPunti(), a.getPunti());
            }
            int diffRetiB = b.getGolFatti() - b.getGolSubiti();
            int diffRetiA = a.getGolFatti() - a.getGolSubiti();
            return Integer.compare(diffRetiB, diffRetiA);
        });

        return classificaOrdinata;
    }
}