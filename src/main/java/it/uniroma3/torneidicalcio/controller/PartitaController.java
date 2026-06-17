package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.model.*;
import it.uniroma3.torneidicalcio.service.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/partite")
class PartitaController {

    private final PartitaService partitaService;
    private final CommentoService commentoService;
    private final CredentialsService credentialsService;
    private final TorneoService torneoService;
    private final SquadraService squadraService;

    public PartitaController(PartitaService partitaService, CommentoService commentoService,
                             CredentialsService credentialsService, TorneoService torneoService,
                             SquadraService squadraService) {
        this.partitaService = partitaService;
        this.commentoService = commentoService;
        this.credentialsService = credentialsService;
        this.torneoService = torneoService;
        this.squadraService = squadraService;
    }
    @GetMapping("/{id}")
    public String list(@PathVariable("id") Long id, Model model){
        Partita partita = this.partitaService.findById(id);
        if (partita == null) return "redirect:/tornei/";
        model.addAttribute("partita", partita);
        model.addAttribute("commento", new Commento());

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Credentials credentials = credentialsService.getCredentials(username);
            model.addAttribute("utenteLoggato", credentials.getUtente());
        }

        return "partite/show";
    }

    @PostMapping("/{id}/commenti")
    public String addCommento(@PathVariable("id") Long id, @RequestParam("testo") String testo) {
        Partita partita = this.partitaService.findById(id);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Credentials credentials = credentialsService.getCredentials(username);
            Utente utente = credentials.getUtente();

            Commento commento = new Commento();
            commento.setTesto(testo);
            commento.setPartita(partita);
            commento.setUtente(utente);
            this.commentoService.save(commento);
        }
        return "redirect:/partite/" + id;
    }

    @PostMapping("/{id}/commenti/{commentoId}/edit")
    public String editCommento(@PathVariable("id") Long id, @PathVariable("commentoId") Long commentoId, @RequestParam("testo") String testo) {
        Commento commento = this.commentoService.findById(commentoId);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Credentials credentials = credentialsService.getCredentials(username);
            Utente utenteLoggato = credentials.getUtente();

            if (this.commentoService.canUserModify(commento, utenteLoggato)) {
                commento.setTesto(testo);
                this.commentoService.save(commento);
            }
        }
        return "redirect:/partite/" + id;
    }
//
//    // ADMIN->CREAZIONE PARTITA
//    @GetMapping("/new")
//    public String createPartitaForm(Model model) {
//        model.addAttribute("partita", new Partita());
//        model.addAttribute("tornei", this.torneoService.findAllOrdinatiPerAnno());
//        model.addAttribute("squadre", this.squadraService.fidAll());
//        return "formPartite";
//    }
//
//    @PostMapping("/new")
//    public String createPartita(@ModelAttribute("partita") Partita partita) {
//        partita.setStato(Stato.SCHEDULED);
//        this.partitaService.save(partita);
//        return "redirect:/tornei/" + partita.getTorneo().getId() + "/calendario";
//    }
//
//    // ============ ADMIN: MODIFICA PARTITA ============
//    @GetMapping("/{id}/edit")
//    public String editPartitaForm(@PathVariable("id") Long id, Model model) {
//        Partita partita = this.partitaService.findById(id);
//        if (partita == null) return "redirect:/tornei/";
//        model.addAttribute("partita", partita);
//        model.addAttribute("tornei", this.torneoService.findAllOrdinatiPerAnno());
//        model.addAttribute("squadre", this.squadraService.fidAll());
//        return "formPartite";
//    }
//
//    @PostMapping("/{id}/edit")
//    public String editPartita(@PathVariable("id") Long id, @ModelAttribute("partita") Partita partita) {
//        this.partitaService.update(id, partita);
//        return "redirect:/partite/" + id;
//    }
//
//    // ADMIN->ELIMINAZIONE PARTITA
//    @PostMapping("/{id}/delete")
//    public String deletePartita(@PathVariable("id") Long id) {
//        Partita partita = this.partitaService.findById(id);
//        Long torneoId = (partita != null && partita.getTorneo() != null) ? partita.getTorneo().getId() : null;
//        this.partitaService.deleteById(id);
//        return (torneoId != null) ? "redirect:/tornei/" + torneoId + "/calendario" : "redirect:/tornei/";
//    }
//
//    // ADMIN->INSERIMENTO/MODIFICA RISULTATO
//    @GetMapping("/{id}/risultato")
//    public String risultatoForm(@PathVariable("id") Long id, Model model) {
//        Partita partita = this.partitaService.findById(id);
//        if (partita == null) return "redirect:/tornei/";
//        model.addAttribute("partita", partita);
//        return "partite/risultato";
//    }
//
//    @PostMapping("/{id}/risultato")
//    public String inserisciRisultato(@PathVariable("id") Long id,
//                                     @RequestParam("goalsHome") Integer goalsHome,
//                                     @RequestParam("goalsAway") Integer goalsAway) {
//        this.partitaService.inserisciRisultato(id, goalsHome, goalsAway);
//        return "redirect:/partite/" + id;
//    }
//
//    @PostMapping("/{id}/risultato/edit")
//    public String modificaRisultato(@PathVariable("id") Long id,
//                                    @RequestParam("goalsHome") Integer goalsHome,
//                                    @RequestParam("goalsAway") Integer goalsAway) {
//        this.partitaService.modificaRisultato(id, goalsHome, goalsAway);
//        return "redirect:/partite/" + id;
//    }

}
