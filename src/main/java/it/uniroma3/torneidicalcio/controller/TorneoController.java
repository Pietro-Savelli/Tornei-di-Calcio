package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.model.*;
import it.uniroma3.torneidicalcio.service.CredentialsService;
import it.uniroma3.torneidicalcio.service.PartitaService;
import it.uniroma3.torneidicalcio.service.TorneoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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

    public TorneoController(TorneoService torneoService, PartitaService partitaService){
        this.torneoService = torneoService;
        this.partitaService = partitaService;
    }

    @GetMapping("/")
    public String list(Model model){
        model.addAttribute("tornei", this.torneoService.findAllOrdinatiPerAnno());
        return "tornei/list";
    }

    @GetMapping("/{id}")
    public String showTorneo(@PathVariable("id") Long id, @RequestParam(name = "partitaPage", defaultValue = "0") int partitaPage, Model model){
        model.addAttribute("torneo", this.torneoService.findById(id));
        Optional<Partita> prossima = this.partitaService.findProssimaPartita(id);
        prossima.ifPresent(p -> model.addAttribute("prossimaPartita", p));
        model.addAttribute("classifica", this.torneoService.calcolaClassifica(id));

        //10 partite per pagina, con Precedente/Successivo
        Page<Partita> paginaPartite = this.partitaService.getPaginaPartite(id, Math.max(partitaPage, 0), 10);
        model.addAttribute("paginaPartite", paginaPartite);
        model.addAttribute("partitaCorrente",
                paginaPartite.hasContent() ? paginaPartite.getContent().get(0) : null);

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

        return "partite/show";
    }

}
