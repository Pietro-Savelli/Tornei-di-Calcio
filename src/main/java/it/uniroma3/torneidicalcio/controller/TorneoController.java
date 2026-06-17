package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.model.Commento;
import it.uniroma3.torneidicalcio.model.Credentials;
import it.uniroma3.torneidicalcio.model.Partita;
import it.uniroma3.torneidicalcio.service.CredentialsService;
import it.uniroma3.torneidicalcio.service.PartitaService;
import it.uniroma3.torneidicalcio.service.TorneoService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/tornei")
public class TorneoController {
    private final TorneoService torneoService;
    private final PartitaService partitaService;
    private final CredentialsService credentialsService;

    public TorneoController(TorneoService torneoService, PartitaService partitaService, CredentialsService credentialsService){
        this.torneoService = torneoService;
        this.partitaService = partitaService;
        this.credentialsService = credentialsService;
    }

    @GetMapping("/")
    public String list(Model model){
        model.addAttribute("tornei", this.torneoService.findAllOrdinatiPerAnno());
        return "tornei/list";
    }

    @GetMapping("/{id}")
    public String showTorneo(@PathVariable("id") Long id, Model model){
        model.addAttribute("torneo", this.torneoService.findById(id));
        Optional<Partita> prossima = this.partitaService.findProssimaPartita(id);
        prossima.ifPresent(p -> model.addAttribute("prossimaPartita", p));
        model.addAttribute("classifica", this.torneoService.calcolaClassifica(id));
        return "tornei/show";
    }

    @GetMapping("/{id}/calendario")
    public String showCalendario(@PathVariable("id") Long id, Model model){
        model.addAttribute("calendario", this.torneoService.findCalendarioByTorneoId(id));
        return "partite/list";
    }

    @GetMapping("/{idTorneo}/calendario/partita/{idPartita}")
    public String showPartita(@PathVariable("idTorneo") Long idTorneo, @PathVariable("idPartita") Long idPartita, Model model) {
        Partita partita = this.partitaService.findById(idPartita);
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


    //@GetMapping("/{id}/squadre") // potrei fare una nuova richeista ma cosa ne guadagno?
}
