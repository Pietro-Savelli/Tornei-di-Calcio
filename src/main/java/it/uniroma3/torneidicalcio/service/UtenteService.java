package it.uniroma3.torneidicalcio.service;

import it.uniroma3.torneidicalcio.model.Utente;
import it.uniroma3.torneidicalcio.repository.PartitaRepository;
import it.uniroma3.torneidicalcio.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    public Optional<Utente> findByUsername(String username) {
        return utenteRepository.findByUsername(username);
    }
}