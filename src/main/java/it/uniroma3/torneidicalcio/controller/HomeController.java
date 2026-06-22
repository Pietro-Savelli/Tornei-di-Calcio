package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.model.Utente;
import it.uniroma3.torneidicalcio.service.CredentialsService;
import it.uniroma3.torneidicalcio.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private CredentialsService credentialsService;

    @GetMapping("/")
    public String getHome(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean authenticated = auth != null && auth.isAuthenticated() && !(auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken);

        boolean admin = authenticated && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));

        model.addAttribute("isAuth", authenticated);
        model.addAttribute("isAdmin", admin);
        model.addAttribute("username", authenticated ? auth.getName() : null);

        return "index";
    }
    @GetMapping("/admin")
    public String getAdminHome() {
        return "admin/index";
    }
}