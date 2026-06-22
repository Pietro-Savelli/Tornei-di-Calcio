package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.model.Credentials;
import it.uniroma3.torneidicalcio.model.Torneo;
import it.uniroma3.torneidicalcio.model.Utente;
import it.uniroma3.torneidicalcio.service.CredentialsService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {

    private final CredentialsService credentialsService;

    public AuthenticationController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @GetMapping(value = "/register")
    public String showRegisterForm (Model model) {
        model.addAttribute("user", new Utente());
        model.addAttribute("credentials", new Credentials());
        return "authentication/registerUtente";
    }

    @GetMapping(value = "/login")
    public String showLoginForm (Model model) {
        return "authentication/login";
    }

    @GetMapping(value = "/admin/index")
    public String index() {
        return "admin/index";
    }

    @PostMapping(value = { "/register" })
    public String registerUtente(@Valid @ModelAttribute("user") Utente utente, BindingResult userBindingResult, @Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult) {
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            credentials.setUtente(utente);
            credentialsService.saveCredentials(credentials);
            return "redirect:/login";
        }
        return "authentication/registerUtente";
    }
}