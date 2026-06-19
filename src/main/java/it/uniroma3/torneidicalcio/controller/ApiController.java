package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.model.Partita;
import it.uniroma3.torneidicalcio.model.Squadra;
import it.uniroma3.torneidicalcio.model.Torneo;
import it.uniroma3.torneidicalcio.service.TorneoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final TorneoService torneoService;

    public ApiController(TorneoService torneoService) {
        this.torneoService = torneoService;
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
}
