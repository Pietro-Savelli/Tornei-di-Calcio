package it.uniroma3.torneidicalcio.dto;

import java.util.List;

//n
public record HomeDto(
        List<TorneoHomeDto> tornei,
        List<SquadraHomeDto> squadre
) {}
