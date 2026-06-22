package it.uniroma3.torneidicalcio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static it.uniroma3.torneidicalcio.model.Credentials.ADMIN_ROLE;

/**
 * Espone l'utente autenticato della sessione Spring Security corrente a React.
 * Non c'è nessun JWT qui: il browser manda già il cookie JSESSIONID ad ogni
 * richiesta (perché React e Spring sono sullo stesso dominio in produzione,
 * o grazie a withCredentials/CORS in dev), quindi questo endpoint si limita
 * a leggere chi è loggato lato server e a restituirlo come JSON.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(401).build();
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(ADMIN_ROLE::equals);

        Map<String, Object> body = new HashMap<>();
        body.put("username", authentication.getName());
        body.put("isAdmin", isAdmin);

        return ResponseEntity.ok(body);
    }
}