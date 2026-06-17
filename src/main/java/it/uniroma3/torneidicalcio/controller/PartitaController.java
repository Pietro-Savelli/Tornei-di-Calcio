package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.model.Commento;
import it.uniroma3.torneidicalcio.model.Credentials;
import it.uniroma3.torneidicalcio.model.Partita;
import it.uniroma3.torneidicalcio.model.Utente;
import it.uniroma3.torneidicalcio.service.CommentoService;
import it.uniroma3.torneidicalcio.service.CredentialsService;
import it.uniroma3.torneidicalcio.service.PartitaService;
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

}
