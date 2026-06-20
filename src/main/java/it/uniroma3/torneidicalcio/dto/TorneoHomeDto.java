package it.uniroma3.torneidicalcio.dto;

import java.util.List;

//DTO di un torneo per la Home React

public record TorneoHomeDto(
        Long id,
        String nome,
        Integer anno,
        String descrizione,
        int numeroSquadre,
        boolean preferito,
        List<PartitaHomeDto> partiteRecenti,
        List<PartitaHomeDto> prossimePartite
) {}
