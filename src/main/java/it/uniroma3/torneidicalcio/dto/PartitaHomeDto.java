package it.uniroma3.torneidicalcio.dto;


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
