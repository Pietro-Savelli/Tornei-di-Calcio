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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    // TORNEI
    @GetMapping("/tornei/new")
    public String createTorneoForm(Model model) {
        model.addAttribute("torneo", new Torneo());
        return "admin/formTornei";
    }

    @PostMapping("/tornei/new")
    public String createTorneo(@Valid @ModelAttribute("torneo") Torneo torneo,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/formTornei";
        }
        this.torneoService.save(torneo);
        return "redirect:/tornei/";
    }

    @GetMapping("/tornei/{id}/edit")
    public String editTorneoForm(@PathVariable("id") Long id, Model model) {
        Torneo torneo = this.torneoService.findById(id);
        if (torneo == null) return "redirect:/tornei/";
        model.addAttribute("torneo", torneo);
        return "admin/formTornei";
    }

    @PostMapping("/tornei/{id}/edit")
    public String editTorneo(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("torneo") Torneo torneo,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/formTornei";
        }
        this.torneoService.update(id, torneo);
        return "redirect:/tornei/" + id;
    }

    @PostMapping("/tornei/{id}/delete")
    public String deleteTorneo(@PathVariable("id") Long id) {
        this.torneoService.deleteById(id);
        return "redirect:/tornei/";
    }


    // SQUADRE
    @GetMapping("/squadre/new")
    public String createSquadraForm(Model model) {
        model.addAttribute("squadra", new Squadra());
        return "admin/formSquadre";
    }

    @PostMapping("/squadre/new")
    public String createSquadra(@Valid @ModelAttribute("squadra") Squadra squadra,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/formSquadre";
        }
        Squadra salvata = this.squadraService.save(squadra);
        return "redirect:/squadre/" + salvata.getId();
    }

    @GetMapping("/squadre/{id}/edit")
    public String editSquadraForm(@PathVariable("id") Long id, Model model) {
        Squadra squadra = this.squadraService.findById(id);
        if (squadra == null) return "redirect:/squadre/";
        model.addAttribute("squadra", squadra);
        return "admin/formSquadre";
    }

    @PostMapping("/squadre/{id}/edit")
    public String editSquadra(@PathVariable("id") Long id,
                              @Valid @ModelAttribute("squadra") Squadra squadra,
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/formSquadre";
        }
        this.squadraService.update(id, squadra);
        return "redirect:/squadre/" + id;
    }

    @PostMapping("/squadre/{id}/delete")
    public String deleteSquadra(@PathVariable("id") Long id) {
        this.squadraService.deleteById(id);
        return "redirect:/squadre/";
    }


    // GIOCATORI
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
        Giocatore salvato = this.giocatoreService.save(giocatore);
        return "redirect:/squadre/";
    }

    @GetMapping("/giocatori/{id}/edit")
    public String editGiocatoreForm(@PathVariable("id") Long id, Model model) {
        Giocatore giocatore = this.giocatoreService.findById(id);
        if (giocatore == null) return "redirect:/squadre/";
        model.addAttribute("giocatore", giocatore);
        model.addAttribute("squadre", this.squadraService.fidAll());
        return "admin/formGiocatore";
    }

    @PostMapping("/giocatori/{id}/edit")
    public String editGiocatore(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("giocatore") Giocatore giocatore,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("squadre", this.squadraService.fidAll());
            return "admin/formGiocatore";
        }
        this.giocatoreService.update(id, giocatore);
        return "redirect:/giocatori/" + id;
    }


    // PARTITE

    @GetMapping("/partite/new")
    public String createPartitaForm(Model model) {
        model.addAttribute("partita", new Partita());
        model.addAttribute("tornei", this.torneoService.findAllOrdinatiPerAnno());
        model.addAttribute("squadre", this.squadraService.fidAll());
        return "admin/formPartite";
    }

    @PostMapping("/partite/new")
    public String createPartita(@ModelAttribute("partita") Partita partita) {
        partita.setStato(Stato.SCHEDULED);
        Partita salvata = this.partitaService.save(partita);
        return "redirect:/tornei/" + salvata.getTorneo().getId() + "/calendario";
    }

    @GetMapping("/partite/{id}/edit")
    public String editPartitaForm(@PathVariable("id") Long id, Model model) {
        Partita partita = this.partitaService.findById(id);
        if (partita == null) return "redirect:/tornei/";
        model.addAttribute("partita", partita);
        model.addAttribute("tornei", this.torneoService.findAllOrdinatiPerAnno());
        model.addAttribute("squadre", this.squadraService.fidAll());
        return "admin/formPartite";
    }

    @PostMapping("/partite/{id}/edit")
    public String editPartita(@PathVariable("id") Long id, @ModelAttribute("partita") Partita partita) {
        this.partitaService.update(id, partita);
        return "redirect:/partite/" + id;
    }

    @PostMapping("/partite/{id}/delete")
    public String deletePartita(@PathVariable("id") Long id) {
        Partita partita = this.partitaService.findById(id);
        Long torneoId = (partita != null && partita.getTorneo() != null) ? partita.getTorneo().getId() : null;
        this.partitaService.deleteById(id);
        return (torneoId != null) ? "redirect:/tornei/" + torneoId + "/calendario" : "redirect:/tornei/";
    }

    // RISULTATI
    @GetMapping("/partite/{id}/risultato")
    public String risultatoForm(@PathVariable("id") Long id, Model model) {
        Partita partita = this.partitaService.findById(id);
        if (partita == null) return "redirect:/tornei/";
        model.addAttribute("partita", partita);
        return "admin/risultato";
    }

    @PostMapping("/partite/{id}/risultato")
    public String inserisciRisultato(@PathVariable("id") Long id,
                                     @RequestParam("goalsHome") Integer goalsHome,
                                     @RequestParam("goalsAway") Integer goalsAway) {
        this.partitaService.inserisciRisultato(id, goalsHome, goalsAway);
        return "redirect:/partite/" + id;
    }

    @PostMapping("/partite/{id}/risultato/edit")
    public String modificaRisultato(@PathVariable("id") Long id,
                                    @RequestParam("goalsHome") Integer goalsHome,
                                    @RequestParam("goalsAway") Integer goalsAway) {
        this.partitaService.modificaRisultato(id, goalsHome, goalsAway);
        return "redirect:/partite/" + id;
    }
}