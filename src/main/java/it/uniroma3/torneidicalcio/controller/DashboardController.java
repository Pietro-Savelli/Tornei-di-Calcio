package it.uniroma3.torneidicalcio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller per servire la Dashboard React embedded.
 * Spring Boot servirà i file statici da src/main/resources/static/dashboard/.
 * Il mapping /dashboard/** forwarda qualsiasi sottopercorso a index.html,
 * permettendo a React Router (se usato in futuro) di gestire le route client-side.
 */
@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "forward:/dashboard/index.html";
    }

    @GetMapping("/dashboard/{path:[^\\.]*}")
    public String dashboardRoutes() {
        return "forward:/dashboard/index.html";
    }
}
