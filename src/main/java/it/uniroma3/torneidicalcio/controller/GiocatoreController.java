package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.model.Giocatore;
import it.uniroma3.torneidicalcio.service.GiocatoreService;
import it.uniroma3.torneidicalcio.service.SquadraService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/giocatori") // FIX: era "/" → il link "/giocatori/{id}" non aveva handler (404) e "/{id}" a root era una rotta troppo ampia
class GiocatoreController {
    private final GiocatoreService giocatoreService;
    private final SquadraService squadraService;

    public GiocatoreController(GiocatoreService giocatoreService, SquadraService squadraService) {
        this.giocatoreService = giocatoreService;
        this.squadraService = squadraService;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("giocatore", this.giocatoreService.findById(id));
        return "giocatori/show";
    }
}