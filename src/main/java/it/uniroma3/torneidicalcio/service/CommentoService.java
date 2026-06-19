package it.uniroma3.torneidicalcio.service;

import it.uniroma3.torneidicalcio.model.Commento;
import it.uniroma3.torneidicalcio.model.Partita;
import it.uniroma3.torneidicalcio.model.Utente;
import it.uniroma3.torneidicalcio.repository.CommentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentoService {

    private final CommentoRepository commentoRepository;

    public CommentoService(CommentoRepository commentoRepository) {
        this.commentoRepository = commentoRepository;
    }

    @Transactional
    public Commento save(Commento commento) {
        if (commento.getDataOra() == null) {
            commento.setDataOra(LocalDateTime.now());
        }
        return commentoRepository.save(commento);
    }

    public List<Commento> findByPartita(Partita partita) {
        return commentoRepository.findByPartita(partita);
    }

    public Commento findById(Long id) {
        return commentoRepository.findById(id).orElse(null);
    }

    public boolean canUserModify(Commento commento, Utente utenteLoggato) {
        // FIX: invertito l'equals per evitare NPE se commento.getUtente() è null;
        // un utente può modificare SOLO i commenti di cui è autore (requisito di sicurezza USER).
        return commento != null && utenteLoggato != null && utenteLoggato.equals(commento.getUtente());
    }
}
