package it.uniroma3.torneidicalcio.dto;

import java.util.List;

/**
 * DTO di un torneo per la Home React: include le 3 partite più recenti già giocate
 * e le 3 prossime partite in programma (Requisito 1).
 */
public record TorneoHomeDto(
        Long id,
        String nome,
        Integer anno,
        String descrizione,
        int numeroSquadre,
        List<PartitaHomeDto> partiteRecenti,
        List<PartitaHomeDto> prossimePartite
) {}
