package it.uniroma3.torneidicalcio.dto;

/**
 * DTO leggero per la Home React: evita di serializzare l'entità JPA Squadra
 * direttamente in JSON.
 */
public record SquadraHomeDto(
        Long id,
        String nome,
        String citta,
        Integer annoFondazione
) {}
