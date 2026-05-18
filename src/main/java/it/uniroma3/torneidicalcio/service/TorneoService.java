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

    public @Nullable Object findCalendarioByTorneoId(Long id) {
        return torneoRepository.findCalendarioByTorneoId(id);
    }

    @Transactional(readOnly = true) // Ottimizzazione richiesta (punto 7)
    public List<RigaClassificaDto> calcolaClassifica(Long torneoId) {

        //torneo con tutte le sue partite
        Torneo torneo = torneoRepository.findTorneoWithPartiteEFeat(torneoId); //.orElseThrow(() -> new IllegalArgumentException("Torneo non trovato con ID: " + torneoId));

        // mappa che associa ogni squadra alla riga in classifica
        Map<Long, RigaClassificaDto> classificaMap = new HashMap<>();

        for (Squadra squadra : torneo.getSquadre()) { //inizializzazione
            classificaMap.put(squadra.getId(), new RigaClassificaDto(squadra.getId(), squadra.getNome()));
        }

        for (Partita partita : torneo.getPartite()) {
            if (partita.getStato() == Stato.FINISHED) {
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