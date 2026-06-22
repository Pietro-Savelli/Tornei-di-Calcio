package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.exception.PartitaDuplicataException;
import it.uniroma3.torneidicalcio.exception.PartitaNotFoundException;
import it.uniroma3.torneidicalcio.exception.SquadraDuplicataException;
import it.uniroma3.torneidicalcio.exception.TorneoDuplicataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;


    @ExceptionHandler(PartitaNotFoundException.class)
    public String handlePartitaNotFound(PartitaNotFoundException ex, Model model) {
        String msg = messageSource.getMessage("partita.notfound", null, ex.getMessage(), LocaleContextHolder.getLocale());
        model.addAttribute("errorMessage", msg);
        return "error/404";
    }

    @ExceptionHandler(SquadraDuplicataException.class)
    public String handleSquadraDuplicata(SquadraDuplicataException ex, Model model) {
        String msg = messageSource.getMessage("squadra.duplicata", new Object[]{ex.getNomeSquadra()}, ex.getMessage(), LocaleContextHolder.getLocale());
        model.addAttribute("errorMessage", msg);
        return "error/400";
    }

    @ExceptionHandler(TorneoDuplicataException.class)
    public String handleTorneoDuplicataException(TorneoDuplicataException ex, Model model) {
        String msg = messageSource.getMessage("torneo.duplicato", null, ex.getMessage(), LocaleContextHolder.getLocale());
        model.addAttribute("errorMessage", msg);
        return "error/400";
    }

    @ExceptionHandler(PartitaDuplicataException.class)
    public String handlePartitaDuplicataException(PartitaDuplicataException ex, Model model) {
        String msg = messageSource.getMessage("partita.duplicata", null, ex.getMessage(), LocaleContextHolder.getLocale());
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
