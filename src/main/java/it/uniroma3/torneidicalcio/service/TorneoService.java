package it.uniroma3.torneidicalcio.service;

import it.uniroma3.torneidicalcio.model.Torneo;
import it.uniroma3.torneidicalcio.repository.TorneoRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TorneoService {
    private final TorneoRepository torneoRepository;

    public TorneoService(TorneoRepository torneoRepository){
        this.torneoRepository = torneoRepository;
    }

    public List<Torneo> findAllOrdinatiPerAnno(){
        return torneoRepository.findAllOrdinatiPerAnno();  //return (List<Torneo>) torneoRepository.findAll(); se non sovrascrivo il metodo
    }

    public Torneo findById(Long id) {
        return torneoRepository.findById(id).orElse(null);// ritorno del torneo se esiste se no null
    }
}