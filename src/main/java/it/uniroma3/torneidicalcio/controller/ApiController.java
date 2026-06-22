package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.model.*;
import it.uniroma3.torneidicalcio.service.CredentialsService;
import it.uniroma3.torneidicalcio.service.PreferitoService;
import it.uniroma3.torneidicalcio.service.TorneoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final TorneoService torneoService;
    private final PreferitoService preferitoService;
    private final CredentialsService credentialsService;

    public ApiController(TorneoService torneoService, PreferitoService preferitoService, CredentialsService credentialsService) {
        this.torneoService = torneoService;
        this.preferitoService = preferitoService;
        this.credentialsService = credentialsService;
    }

    @GetMapping("/tornei/{id}/partite")
    public ResponseEntity<List<Map<String, Object>>> getPartite(@PathVariable Long id) {
        Torneo torneo = torneoService.findByIdWithDetails(id);
        if (torneo == null) {
            return ResponseEntity.notFound().build();
        }
        List<Map<String, Object>> partite = new ArrayList<>();
        for (Partita p : torneo.getPartite()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("dataOra", p.getDataOra());
            map.put("luogo", p.getLuogo());
            map.put("stato", p.getStato() != null ? p.getStato().name() : null);
            map.put("goalsHome", p.getGoalsHome());
            map.put("goalsAway", p.getGoalsAway());
            map.put("eliminata", p.isEliminata());

            Map<String, Object> casa = new HashMap<>();
            if (p.getSquadraCasa() != null) {
                casa.put("nome", p.getSquadraCasa().getNome());
                casa.put("sigla", p.getSquadraCasa().getNome().substring(0, Math.min(2, p.getSquadraCasa().getNome().length())));
            }
            map.put("squadraCasa", casa);

            Map<String, Object> ospite = new HashMap<>();
            if (p.getSquadraOspite() != null) {
                ospite.put("nome", p.getSquadraOspite().getNome());
                ospite.put("sigla", p.getSquadraOspite().getNome().substring(0, Math.min(2, p.getSquadraOspite().getNome().length())));
            }
            map.put("squadraOspite", ospite);

            partite.add(map);
        }
        return ResponseEntity.ok(partite);
    }

    @GetMapping("/tornei/{id}/squadre")
    public ResponseEntity<List<Map<String, Object>>> getSquadre(@PathVariable Long id) {
        Torneo torneo = torneoService.findByIdWithDetails(id);
        if (torneo == null) {
            return ResponseEntity.notFound().build();
        }
        List<Map<String, Object>> squadre = new ArrayList<>();
        for (Squadra s : torneo.getSquadre()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", s.getId());
            map.put("nome", s.getNome());
            map.put("citta", s.getCitta());
            map.put("annoFondazione", s.getAnnoFondazione());
            map.put("imgUrl", null);
            squadre.add(map);
        }
        return ResponseEntity.ok(squadre);
    }

    @PostMapping("/tornei/{id}/preferito")
    public ResponseEntity<Void> aggiungi(@PathVariable Long id) {
        Utente utente = credentialsService.getUtenteCorrente(); // Richiama il service
        if (utente == null) return ResponseEntity.status(401).build();
        preferitoService.aggiungi(utente, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tornei/{id}/preferito")
    public ResponseEntity<Void> rimuovi(@PathVariable Long id) {
        Utente utente = credentialsService.getUtenteCorrente(); // Richiama il service
        if (utente == null) return ResponseEntity.status(401).build();
        preferitoService.rimuovi(utente, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/utente/preferiti")
    public ResponseEntity<Set<Long>> getPreferiti() {
        Utente utente = credentialsService.getUtenteCorrente(); // Richiama il service
        if (utente == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(preferitoService.getTorneoIdsPreferiti(utente));
    }

}
