package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.model.Utente;
import it.uniroma3.torneidicalcio.service.CredentialsService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

// Questo metodo viene eseguito in automatico da Spring prima di caricare qualsiasi pagina
// Inserisce l'utente loggato nel Model, rendendo la variabile "utenteLoggato" sempre disponibile su tutti i template HTML
@ControllerAdvice
public class GlobalController {

    private final CredentialsService credentialsService;

    public GlobalController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @ModelAttribute("utenteLoggato")
    public Utente getUtente() {
        return credentialsService.getUtenteCorrente();
    }
}

