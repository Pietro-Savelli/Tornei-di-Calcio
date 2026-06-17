package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.model.Commento;
import it.uniroma3.torneidicalcio.model.Credentials;
import it.uniroma3.torneidicalcio.model.Partita;
import it.uniroma3.torneidicalcio.model.Torneo;
import it.uniroma3.torneidicalcio.service.CredentialsService;
import it.uniroma3.torneidicalcio.service.PartitaService;
import it.uniroma3.torneidicalcio.service.TorneoService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

//    // ADMIN->CREAZIONE TORNEO
//    @GetMapping("/new")
//    public String createTorneoForm(Model model) {
//        model.addAttribute("torneo", new Torneo());
//        return "formSquadre";
//    }
//
//    @PostMapping("/new")
//    public String createTorneo(@Valid @ModelAttribute("torneo") Torneo torneo,
//                               BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            return "formSquadre";
//        }
//        this.torneoService.save(torneo);
//        return "redirect:/tornei/";
//    }
//
//    // ADMIN->MODIFICA TORNEO
//    @GetMapping("/{id}/edit")
//    public String editTorneoForm(@PathVariable("id") Long id, Model model) {
//        Torneo torneo = this.torneoService.findById(id);
//        if (torneo == null) return "redirect:/tornei/";
//        model.addAttribute("torneo", torneo);
//        return "formSquadre";
//    }
//
//    @PostMapping("/{id}/edit")
//    public String editTorneo(@PathVariable("id") Long id,
//                             @Valid @ModelAttribute("torneo") Torneo torneo,
//                             BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            return "formSquadre";
//        }
//        this.torneoService.update(id, torneo);
//        return "redirect:/tornei/" + id;
//    }
//
//    // ADMIN->ELIMINAZIONE TORNEO
//    @PostMapping("/{id}/delete")
//    public String deleteTorneo(@PathVariable("id") Long id) {
//        this.torneoService.deleteById(id);
//        return "redirect:/tornei/";
//    }
}
