package it.uniroma3.torneidicalcio.service;

import it.uniroma3.torneidicalcio.dto.HomeDto;
import it.uniroma3.torneidicalcio.dto.PartitaHomeDto;
import it.uniroma3.torneidicalcio.dto.SquadraHomeDto;
import it.uniroma3.torneidicalcio.dto.TorneoHomeDto;
import it.uniroma3.torneidicalcio.model.Partita;
import it.uniroma3.torneidicalcio.model.Squadra;
import it.uniroma3.torneidicalcio.model.Torneo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Assembla il payload della Home (Requisito 1) consumato dalla SPA React via /api/home.
 * Mappa le entità JPA in DTO record, così Jackson non serializza proxy/collezioni LAZY.
 */
@Service
public class HomeService {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final int N_PARTITE = 3; // 3 recenti + 3 prossime per ogni torneo

    private final TorneoService torneoService;
    private final PartitaService partitaService;
    private final SquadraService squadraService;

    public HomeService(TorneoService torneoService, PartitaService partitaService, SquadraService squadraService) {
        this.torneoService = torneoService;
        this.partitaService = partitaService;
        this.squadraService = squadraService;
    }

    @Transactional(readOnly = true)
    public HomeDto getHomeData() {
        List<TorneoHomeDto> tornei = torneoService.findAllOrdinatiPerAnno().stream()
                .map(this::toTorneoHomeDto)
                .toList();

        List<SquadraHomeDto> squadre = squadraService.fidAll().stream()
                .map(this::toSquadraHomeDto)
                .toList();

        return new HomeDto(tornei, squadre);
    }

    private TorneoHomeDto toTorneoHomeDto(Torneo t) {
        List<PartitaHomeDto> recenti = partitaService.getUltimePartiteGiocate(t.getId(), N_PARTITE)
                .stream().map(this::toPartitaHomeDto).toList();
        List<PartitaHomeDto> prossime = partitaService.getProssimePartite(t.getId(), N_PARTITE)
                .stream().map(this::toPartitaHomeDto).toList();

        return new TorneoHomeDto(
                t.getId(), t.getNome(), t.getAnno(), t.getDescrizione(),
                t.getSquadre().size(), recenti, prossime);
    }

    private PartitaHomeDto toPartitaHomeDto(Partita p) {
        return new PartitaHomeDto(
                p.getId(),
                p.getTorneo() != null ? p.getTorneo().getId() : null,
                p.getSquadraCasa() != null ? p.getSquadraCasa().getId() : null,
                p.getSquadraOspite() != null ? p.getSquadraOspite().getId() : null,
                p.getSquadraCasa() != null ? p.getSquadraCasa().getNome() : "?",
                p.getSquadraOspite() != null ? p.getSquadraOspite().getNome() : "?",
                p.getGoalsHome(),
                p.getGoalsAway(),
                p.getDataOra() != null ? p.getDataOra().format(FMT) : null,
                p.getStato() != null ? p.getStato().name() : null);
    }

    private SquadraHomeDto toSquadraHomeDto(Squadra s) {
        return new SquadraHomeDto(s.getId(), s.getNome(), s.getCitta(), s.getAnnoFondazione());
    }
}
