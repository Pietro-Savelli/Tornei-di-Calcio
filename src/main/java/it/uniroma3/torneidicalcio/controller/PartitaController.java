package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.service.PartitaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/partite")
class PartitaController {

    private final PartitaService partitaService;
    public PartitaController(PartitaService partitaService){
        this.partitaService = partitaService;
    }

//    @GetMapping("/") da finire
//    public String list(Model model){
//        model.addAttribute("tornei", this.partitaService);
//        return "tornei/list";
//    }

}
