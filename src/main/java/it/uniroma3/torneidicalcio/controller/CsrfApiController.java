package it.uniroma3.torneidicalcio.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Espone il token CSRF corrente a React. Spring Security lo calcola e lo mette
 * in request.getAttribute(CsrfToken.class.getName()) ogni volta che il filtro
 * CSRF gira sulla richiesta
 */
@RestController
@RequestMapping("/api")
public class CsrfApiController {

    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken token) {
        Map<String, String> body = new HashMap<>();
        body.put("headerName", token.getHeaderName());
        body.put("token", token.getToken());
        return body;
    }
}