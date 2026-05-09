package it.uniroma3.torneidicalcio.service;

import it.uniroma3.torneidicalcio.model.Squadra;
import it.uniroma3.torneidicalcio.model.Torneo;
import it.uniroma3.torneidicalcio.repository.SquadraRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SquadraService {
    SquadraRepository squadraRepository;

    public SquadraService(SquadraRepository squadraRepository){
        this.squadraRepository = squadraRepository;
    }


    public List<Squadra> fidAll() {
        return squadraRepository.findAll();
    }

    public Squadra findById(Long id) {
        return squadraRepository.findById(id).orElse(null);// ritorno del torneo se esiste se no null
    }
}