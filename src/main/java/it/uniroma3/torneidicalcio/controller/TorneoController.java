package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.service.TorneoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tornei")
public class TorneoController {
    private final TorneoService torneoService;

    public TorneoController(TorneoService torneoService){
        this.torneoService = torneoService;
    }

    @GetMapping("/")
    public String list(Model model){
        model.addAttribute("tornei", this.torneoService.findAllOrdinatiPerAnno());
        return "tornei/list";
    }

    @GetMapping("/{id}")
    public String list(@PathVariable("id") Long id, Model model){
        model.addAttribute("torneo", this.torneoService.findById(id));
        return "tornei/show";
    }

    //@GetMapping("/{id}/squadre") // potrei fare una nuova richeista ma cosa ne guadagno?
}
