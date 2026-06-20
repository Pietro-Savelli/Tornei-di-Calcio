package it.uniroma3.torneidicalcio.dto;

import java.util.List;

public record HomeDto(
        List<TorneoHomeDto> tornei,
        List<SquadraHomeDto> squadre,
        long n
) {}
