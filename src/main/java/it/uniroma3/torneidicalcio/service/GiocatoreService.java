package it.uniroma3.torneidicalcio.service;

import it.uniroma3.torneidicalcio.model.Giocatore;
import it.uniroma3.torneidicalcio.repository.GiocatoreRepository;
import org.springframework.stereotype.Service;

@Service
public class GiocatoreService {


    private final GiocatoreRepository giocatoreRepository;

    public GiocatoreService(GiocatoreRepository giocatoreRepository) {
        this.giocatoreRepository = giocatoreRepository;
    }

    public Giocatore findById(Long id) {
        return giocatoreRepository.findById(id).orElse(null);// ritorno del torneo se esiste se no null
    }
}