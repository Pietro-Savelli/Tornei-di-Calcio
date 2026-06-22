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
@RequestMapping("/giocatori")
class GiocatoreController {
    private final GiocatoreService giocatoreService;

    public GiocatoreController(GiocatoreService giocatoreService) {
        this.giocatoreService = giocatoreService;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("giocatore", this.giocatoreService.findById(id));
        return "giocatori/show";
    }
}