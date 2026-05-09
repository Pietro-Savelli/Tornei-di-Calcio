package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.service.SquadraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/squadre")
class SquadraController {

    private final SquadraService squadraService;

    public SquadraController(SquadraService squadraService){
        this.squadraService = squadraService;
    }

    @GetMapping("/")
    public  String list(Model model){
        model.addAttribute("squadre", this.squadraService.fidAll());
        return "squadre/list";
    }

    @GetMapping("/{id}")
    public String list(@PathVariable("id") Long id, Model model){
        model.addAttribute("squadra", this.squadraService.findById(id));
        return "squadre/show";
    }
}
