package it.uniroma3.torneidicalcio.dto;

public record SquadraHomeDto(
        Long id,
        String nome,
        String citta,
        Integer annoFondazione,
        String stemmaUrl
) {}
