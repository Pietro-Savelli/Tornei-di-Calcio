package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.model.Giocatore;
import it.uniroma3.torneidicalcio.model.Partita;
import it.uniroma3.torneidicalcio.model.Squadra;
import it.uniroma3.torneidicalcio.model.Stato;
import it.uniroma3.torneidicalcio.model.Torneo;
import it.uniroma3.torneidicalcio.service.GiocatoreService;
import it.uniroma3.torneidicalcio.service.PartitaService;
import it.uniroma3.torneidicalcio.service.SquadraService;
import it.uniroma3.torneidicalcio.service.TorneoService;
import jakarta.validation.Valid;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final TorneoService torneoService;
    private final SquadraService squadraService;
    private final GiocatoreService giocatoreService;
    private final PartitaService partitaService;

    public AdminController(TorneoService torneoService, SquadraService squadraService,
                           GiocatoreService giocatoreService, PartitaService partitaService) {
        this.torneoService = torneoService;
        this.squadraService = squadraService;
        this.giocatoreService = giocatoreService;
        this.partitaService = partitaService;
    }

    // =========================================================
    // TORNEI
    // =========================================================

    @GetMapping("/tornei/new")
    public String createTorneoForm(Model model) {
        model.addAttribute("torneo", new Torneo());
        return "admin/formTornei";
    }

    @PostMapping("/tornei/new")
    public String createTorneo(@Valid @ModelAttribute("torneo") Torneo torneo,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/formTornei";
        }
        this.torneoService.save(torneo);
        return "redirect:/tornei/";
    }

    @GetMapping("/tornei/{id}/edit")
    public String editTorneoForm(@PathVariable Long id, Model model) {
        Torneo torneo = this.torneoService.findById(id);
        if (torneo == null) return "redirect:/tornei/";

        Hibernate.initialize(torneo.getPartite());
        Hibernate.initialize(torneo.getSquadre());

        Set<Partita> partite = torneo.getPartite();
        Set<Squadra> squadre = torneo.getSquadre();

        List<Squadra> squadreAttive     = squadre.stream().filter(s -> !s.isEliminata()).toList();
        List<Squadra> squadreEliminate  = squadre.stream().filter(Squadra::isEliminata).toList();
        List<Partita> partiteAttive     = partite.stream().filter(p -> !p.isEliminata()).toList();
        List<Partita> partiteEliminate  = partite.stream().filter(Partita::isEliminata).toList();

        // Squadre disponibili = tutte quelle non eliminate e non già nel torneo
        List<Squadra> squadreDisponibili = this.squadraService.fidAll().stream()
                .filter(s -> !s.isEliminata() && !squadre.contains(s))
                .toList();

        model.addAttribute("torneo", torneo);
        model.addAttribute("partiteAttive", partiteAttive);
        model.addAttribute("partiteEliminate", partiteEliminate);
        model.addAttribute("squadreAttive", squadreAttive);
        model.addAttribute("squadreEliminate", squadreEliminate);
        model.addAttribute("squadreDisponibili", squadreDisponibili);
        return "admin/formTornei";
    }

    @PostMapping("/tornei/{id}/edit")
    public String editTorneo(@PathVariable Long id,
                             @Valid @ModelAttribute("torneo") Torneo torneo,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/formTornei";
        }
        this.torneoService.update(id, torneo);
        return "redirect:/tornei/" + id;
    }

    @PostMapping("/tornei/{id}/squadre/add")
    public String addSquadraToTorneo(@PathVariable Long id,
                                     @RequestParam Long squadraId) {
        Torneo torneo = this.torneoService.findById(id);
        Squadra squadra = this.squadraService.findById(squadraId);
        if (torneo != null && squadra != null) {
            torneo.getSquadre().add(squadra);
            this.torneoService.save(torneo);
        }
        return "redirect:/admin/tornei/" + id + "/edit";
    }

    // FIX: questo metodo era chiamato dal bottone "Rimuovi" in formTornei ma non esisteva
    @PostMapping("/tornei/{id}/squadre/{squadraId}/remove")
    public String removeSquadraFromTorneo(@PathVariable Long id,
                                          @PathVariable Long squadraId) {
        Torneo torneo = this.torneoService.findById(id);
        Squadra squadra = this.squadraService.findById(squadraId);
        if (torneo != null && squadra != null) {
            torneo.getSquadre().remove(squadra);
            this.torneoService.save(torneo);
        }
        return "redirect:/admin/tornei/" + id + "/edit";
    }

    @PostMapping("/tornei/{id}/delete")
    public String deleteTorneo(@PathVariable Long id) {
        this.torneoService.deleteById(id);
        return "redirect:/tornei/";
    }


    // =========================================================
    // SQUADRE
    // =========================================================

    @GetMapping("/squadre/new")
    public String createSquadraForm(Model model) {
        model.addAttribute("squadra", new Squadra());
        return "admin/formSquadre";
    }

    @PostMapping("/squadre/new")
    public String createSquadra(@Valid @ModelAttribute("squadra") Squadra squadra,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/formSquadre";
        }
        Squadra salvata = this.squadraService.save(squadra);
        return "redirect:/squadre/" + salvata.getId();
    }

    @GetMapping("/squadre/{id}/edit")
    public String editSquadraForm(@PathVariable Long id, Model model) {
        Squadra squadra = this.squadraService.findById(id);
        if (squadra == null) return "redirect:/squadre/";
        model.addAttribute("squadra", squadra);
        return "admin/formSquadre";
    }

    @PostMapping("/squadre/{id}/edit")
    public String editSquadra(@PathVariable Long id,
                              @Valid @ModelAttribute("squadra") Squadra squadra,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/formSquadre";
        }
        this.squadraService.update(id, squadra);
        return "redirect:/squadre/" + id;
    }

    // Soft delete: la squadra viene marcata come eliminata, non cancellata dal DB
    @PostMapping("/squadre/{id}/delete")
    public String deleteSquadra(@PathVariable Long id) {
        Squadra squadra = this.squadraService.findById(id);
        if (squadra != null) {
            squadra.setEliminata(true);
            this.squadraService.save(squadra);
        }
        return "redirect:/squadre/";
    }


    // =========================================================
    // GIOCATORI
    // =========================================================

    @GetMapping("/giocatori/new")
    public String createGiocatoreForm(Model model) {
        model.addAttribute("giocatore", new Giocatore());
        model.addAttribute("squadre", this.squadraService.fidAll());
        return "admin/formGiocatore";
    }

    @PostMapping("/giocatori/new")
    public String createGiocatore(@Valid @ModelAttribute("giocatore") Giocatore giocatore,
                                  BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("squadre", this.squadraService.fidAll());
            return "admin/formGiocatore";
        }
        this.giocatoreService.save(giocatore);
        return "redirect:/squadre/";
    }

    @GetMapping("/giocatori/{id}/edit")
    public String editGiocatoreForm(@PathVariable Long id, Model model) {
        Giocatore giocatore = this.giocatoreService.findById(id);
        if (giocatore == null) return "redirect:/squadre/";
        model.addAttribute("giocatore", giocatore);
        model.addAttribute("squadre", this.squadraService.fidAll());
        return "admin/formGiocatore";
    }

    @PostMapping("/giocatori/{id}/edit")
    public String editGiocatore(@PathVariable Long id,
                                @Valid @ModelAttribute("giocatore") Giocatore giocatore,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("squadre", this.squadraService.fidAll());
            return "admin/formGiocatore";
        }
        this.giocatoreService.update(id, giocatore);
        return "redirect:/giocatori/" + id;
    }

    // FIX: metodo delete giocatore mancava del tutto
    @PostMapping("/giocatori/{id}/delete")
    public String deleteGiocatore(@PathVariable Long id) {
        Giocatore giocatore = this.giocatoreService.findById(id);
        if (giocatore != null) {
            Long squadraId = giocatore.getSquadra() != null ? giocatore.getSquadra().getId() : null;
            this.giocatoreService.deleteById(id);
            if (squadraId != null) return "redirect:/squadre/" + squadraId;
        }
        return "redirect:/squadre/";
    }


    // =========================================================
    // PARTITE
    // =========================================================

    @GetMapping("/partite/new")
    public String createPartitaForm(@RequestParam(required = false) Long torneoId, Model model) {
        Partita partita = new Partita();
        // FIX: se si arriva da un link con ?torneoId=X, pre-seleziona il torneo nel form
        if (torneoId != null) {
            Torneo torneo = this.torneoService.findById(torneoId);
            if (torneo != null) partita.setTorneo(torneo);
        }
        model.addAttribute("partita", partita);
        model.addAttribute("tornei", this.torneoService.findAllOrdinatiPerAnno());
        model.addAttribute("squadre", this.squadraService.fidAll().stream()
                .filter(s -> !s.isEliminata()).toList());
        return "admin/formPartite";
    }

    @PostMapping("/partite/new")
    public String createPartita(@ModelAttribute("partita") Partita partita) {
        partita.setStato(Stato.SCHEDULED);
        Partita salvata = this.partitaService.save(partita);
        return "redirect:/tornei/" + salvata.getTorneo().getId() + "/calendario";
    }

    @GetMapping("/partite/{id}/edit")
    public String editPartitaForm(@PathVariable Long id, Model model) {
        Partita partita = this.partitaService.findById(id);
        if (partita == null) return "redirect:/tornei/";
        model.addAttribute("partita", partita);
        model.addAttribute("tornei", this.torneoService.findAllOrdinatiPerAnno());
        model.addAttribute("squadre", this.squadraService.fidAll().stream()
                .filter(s -> !s.isEliminata()).toList());
        return "admin/formPartite";
    }

    @PostMapping("/partite/{id}/edit")
    public String editPartita(@PathVariable Long id,
                              @ModelAttribute("partita") Partita partita) {
        // FIX: update non sovrascrive lo stato della partita (gestito nel service)
        this.partitaService.update(id, partita);
        return "redirect:/partite/" + id;
    }

    // FIX: rimossa la doppia chiamata findById ridondante
    @PostMapping("/partite/{id}/delete")
    public String deletePartita(@PathVariable Long id) {
        Partita partita = this.partitaService.findById(id);
        if (partita == null) return "redirect:/tornei/";
        Long torneoId = partita.getTorneo() != null ? partita.getTorneo().getId() : null;
        partita.setEliminata(true);
        this.partitaService.save(partita);
        return torneoId != null
                ? "redirect:/tornei/" + torneoId + "/calendario"
                : "redirect:/tornei/";
    }


    // =========================================================
    // RISULTATI
    // =========================================================

    @GetMapping("/partite/{id}/risultato")
    public String risultatoForm(@PathVariable Long id, Model model) {
        Partita partita = this.partitaService.findById(id);
        if (partita == null) return "redirect:/tornei/";
        model.addAttribute("partita", partita);
        return "admin/risultato";
    }

    @PostMapping("/partite/{id}/risultato")
    public String inserisciRisultato(@PathVariable Long id,
                                     @RequestParam Integer goalsHome,
                                     @RequestParam Integer goalsAway) {
        this.partitaService.inserisciRisultato(id, goalsHome, goalsAway);
        return "redirect:/partite/" + id;
    }

    @PostMapping("/partite/{id}/risultato/edit")
    public String modificaRisultato(@PathVariable Long id,
                                    @RequestParam Integer goalsHome,
                                    @RequestParam Integer goalsAway) {
        this.partitaService.modificaRisultato(id, goalsHome, goalsAway);
        return "redirect:/partite/" + id;
    }
}