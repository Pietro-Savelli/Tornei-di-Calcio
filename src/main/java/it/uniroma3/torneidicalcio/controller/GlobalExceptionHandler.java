package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.exception.PartitaNotFoundException;
import it.uniroma3.torneidicalcio.exception.SquadraDuplicataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
 * Handler globale per eccezioni non gestite direttamente nei Controller.
 * Separa la logica di gestione degli errori (cross-cutting concern) dai Controller,
 * mantenendo il codice di business pulito.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;


    @ExceptionHandler(PartitaNotFoundException.class)
    public String handlePartitaNotFound(PartitaNotFoundException ex, Model model) {
        String msg = messageSource.getMessage("error.partita.notfound", null, ex.getMessage(), LocaleContextHolder.getLocale());
        model.addAttribute("errorMessage", msg);
        return "error/404";
    }

    @ExceptionHandler(SquadraDuplicataException.class)
    public String handleSquadraDuplicata(SquadraDuplicataException ex, Model model) {
        String msg = messageSource.getMessage("error.squadra.duplicata", new Object[]{ex.getNomeSquadra()}, ex.getMessage(), LocaleContextHolder.getLocale());
        model.addAttribute("errorMessage", msg);
        return "error/400";
    }


    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        String msg = messageSource.getMessage("error.generic", null, ex.getMessage(), LocaleContextHolder.getLocale());
        model.addAttribute("errorMessage", msg);
        return "error/500";
    }
}
