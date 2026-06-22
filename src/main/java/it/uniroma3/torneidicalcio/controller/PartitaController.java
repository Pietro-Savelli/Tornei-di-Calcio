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

    public PartitaController(PartitaService partitaService, CommentoService commentoService, CredentialsService credentialsService) {
        this.partitaService = partitaService;
        this.commentoService = commentoService;
        this.credentialsService = credentialsService;

    }

    @GetMapping("/{id}")
    public String list(@PathVariable("id") Long id, Model model){
        Partita partita = this.partitaService.findById(id);
        if (partita == null) return "redirect:/tornei/";

        model.addAttribute("partita", partita);
        model.addAttribute("commento", new Commento());

        return "partite/show";
    }

    @PostMapping("/{id}/commenti")
    public String addCommento(@PathVariable("id") Long id, @RequestParam("testo") String testo) {
        Partita partita = this.partitaService.findById(id);

        Utente utente = credentialsService.getUtenteCorrente();

        if (partita != null && utente != null) {
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

        Utente utenteLoggato = credentialsService.getUtenteCorrente();

        if (commento != null && utenteLoggato != null) {
            if (this.commentoService.canUserModify(commento, utenteLoggato)) {
                commento.setTesto(testo);
                this.commentoService.save(commento);
            }
        }
        return "redirect:/partite/" + id;
    }
}
