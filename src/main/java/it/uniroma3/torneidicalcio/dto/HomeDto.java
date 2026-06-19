package it.uniroma3.torneidicalcio.dto;

import java.util.List;

/**
 * Payload completo consumato dalla Home React via GET /api/home.
 */
public record HomeDto(
        List<TorneoHomeDto> tornei,
        List<SquadraHomeDto> squadre
) {}
