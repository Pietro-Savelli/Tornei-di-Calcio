package it.uniroma3.torneidicalcio.dto;

/**
 * DTO leggero per una partita mostrata nella Home React.
 * - per le partite GIOCATE: goalsHome/goalsAway valorizzati.
 * - per le partite IN PROGRAMMA: data/ora valorizzata, gol null. (commentate per ora)
 */
public record PartitaHomeDto(
        Long id,
        Long torneoId,
        Long squadraCasaId,
        Long squadraOspiteId,
        String squadraCasa,
        String squadraOspite,
        Integer goalsHome,
        Integer goalsAway,
        String dataOra,
        String stato
) {}
