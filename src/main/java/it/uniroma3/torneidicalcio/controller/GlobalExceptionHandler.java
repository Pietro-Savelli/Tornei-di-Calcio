package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.exception.PartitaNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handler globale per eccezioni non gestite direttamente nei Controller.
 * Separa la logica di gestione degli errori (cross-cutting concern) dai Controller,
 * mantenendo il codice di business pulito.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Cattura eccezioni di risorsa non trovata (es. partita inesistente)
     * e mostra la pagina 404 personalizzata passando il messaggio al Model.
     */
    @ExceptionHandler(PartitaNotFoundException.class)
    public String handlePartitaNotFound(PartitaNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/404";
    }

    /**
     * Cattura qualsiasi altra eccezione imprevista (errori 500).
     * Ritorna la vista generica di errore del server.
     */
    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/500";
    }
}
