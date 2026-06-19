package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.dto.HomeDto;
import it.uniroma3.torneidicalcio.service.HomeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API JSON consumata dalla Home React (Requisito 1).
 * Espone i tornei con le ultime/prossime partite e l'elenco squadre.
 */
@RestController
@RequestMapping("/api")
public class HomeApiController {

    private final HomeService homeService;

    public HomeApiController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/home")
    public HomeDto home() {
        return homeService.getHomeData();
    }
}
