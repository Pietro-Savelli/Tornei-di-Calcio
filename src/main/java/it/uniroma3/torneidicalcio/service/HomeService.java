package it.uniroma3.torneidicalcio.service;

import it.uniroma3.torneidicalcio.dto.HomeDto;
import it.uniroma3.torneidicalcio.dto.PartitaHomeDto;
import it.uniroma3.torneidicalcio.dto.SquadraHomeDto;
import it.uniroma3.torneidicalcio.dto.TorneoHomeDto;
import it.uniroma3.torneidicalcio.model.*;
import it.uniroma3.torneidicalcio.repository.TorneoRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class HomeService {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final int N_PARTITE = 3; // 3 recenti + 3 prossime per ogni torneo

    private final TorneoService torneoService;
    private final PartitaService partitaService;
    private final SquadraService squadraService;
    private final PreferitoService preferitoService;
    private final CredentialsService credentialsService;
    private final TorneoRepository torneoRepository;

    public HomeService(TorneoService torneoService,
                       PartitaService partitaService,
                       SquadraService squadraService,
                       PreferitoService preferitoService,
                       CredentialsService credentialsService, TorneoRepository torneoRepository) {
        this.torneoService = torneoService;
        this.partitaService = partitaService;
        this.squadraService = squadraService;
        this.preferitoService = preferitoService;
        this.credentialsService = credentialsService;
        this.torneoRepository = torneoRepository;
    }

    // da elimidare i  temi per test
    @Transactional(readOnly = true)
    public HomeDto getHomeData() {
        //Utente utente = getUtenteLoggato();
        Utente utente = credentialsService.getUtenteCorrente();

        Set<Long> preferiti;
        if (utente != null) {
            preferiti = preferitoService.getTorneoIdsPreferiti(utente);
        } else {
            preferiti = new HashSet<>();
        }

        long t0 = System.currentTimeMillis();
        List<Torneo> entitaTornei = torneoService.findAllOrdinatiPerAnno();
        System.out.println("findAllOrdinatiPerAnno: " + (System.currentTimeMillis() - t0) + "ms");

        List<TorneoHomeDto> tornei = new ArrayList<>();

        long t3 = System.currentTimeMillis();
        for (Torneo t : entitaTornei) {
            TorneoHomeDto dto = toTorneoHomeDto(t, preferiti);
            tornei.add(dto);
        }
        System.out.println("loop tornei: " + (System.currentTimeMillis() - t3) + "ms");


        tornei.sort((t1, t2) -> Boolean.compare(t2.preferito(), t1.preferito()));

        long t2 = System.currentTimeMillis();
        List<Squadra> entitaSquadre = squadraService.findAll();
        System.out.println("fidAll: " + (System.currentTimeMillis() - t2) + "ms");

        List<SquadraHomeDto> squadre = new ArrayList<>();

        for (Squadra s : entitaSquadre) {
            SquadraHomeDto dto = toSquadraHomeDto(s);
            squadre.add(dto);
        }

        //long n = torneoService.conta();

        return new HomeDto(tornei, squadre);
    }

    private TorneoHomeDto toTorneoHomeDto(Torneo t, Set<Long> preferiti) {
        List<PartitaHomeDto> prossime = partitaService.getProssimePartite(t.getId(), N_PARTITE)
                .stream().map(this::toPartitaHomeDto).toList();

        boolean isPreferito = preferiti.contains(t.getId());

//        return new TorneoHomeDto(
//                t.getId(), t.getNome(), t.getAnno(), t.getDescrizione(),
//                torneoRepository.countSquadreByTorneoId(t.getId()), isPreferito, prossime);
        return new TorneoHomeDto(
                t.getId(), t.getNome(), t.getAnno(), t.getDescrizione(),
                t.getSquadre().size(), isPreferito, prossime);
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
        return new SquadraHomeDto(s.getId(), s.getNome(), s.getCitta(), s.getAnnoFondazione(), s.getStemmaUrl());
    }

    private Utente getUtenteLoggato() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        String username = authentication.getName();
        Credentials credentials = credentialsService.getCredentials(username);
        return (credentials != null) ? credentials.getUtente() : null;
    }
}
