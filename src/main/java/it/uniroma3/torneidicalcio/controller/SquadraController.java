package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.service.GiocatoreService;
import it.uniroma3.torneidicalcio.service.SquadraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/squadre")
class SquadraController {

    private final SquadraService squadraService;
    private final GiocatoreService giocatoreService;

    public SquadraController(SquadraService squadraService, GiocatoreService giocatoreService){
        this.squadraService = squadraService;
        this.giocatoreService = giocatoreService;
    }

    @GetMapping("/")
    public  String list(Model model){
        model.addAttribute("squadre", this.squadraService.findAll());
        return "squadre/list";
    }

    @GetMapping("/{id}")
    public String list(@PathVariable("id") Long id, Model model){
        model.addAttribute("squadra", this.squadraService.findById(id));
        return "squadre/show";
    }

    @GetMapping("/{idSquadra}/giocatori/{idGiocatore}")
    public String mostraGiocatore (@PathVariable("idSquadra") Long idSquadra, @PathVariable("idGiocatore") Long idGiocatore, Model model){
        model.addAttribute("giocatore", this.giocatoreService.findById(idGiocatore));
        return "giocatori/show";
    }

    @GetMapping("/{idSquadra}/giocatori")
    public String mostraRosa (@PathVariable("idSquadra") Long idSquadra, Model model){
        model.addAttribute("squadra", this.squadraService.findById(idSquadra));
        return "giocatori/list";
    }

}
