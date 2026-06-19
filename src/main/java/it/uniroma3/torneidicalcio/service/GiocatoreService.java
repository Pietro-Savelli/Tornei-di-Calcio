package it.uniroma3.torneidicalcio.service;

import it.uniroma3.torneidicalcio.model.Giocatore;
import it.uniroma3.torneidicalcio.model.Squadra;
import it.uniroma3.torneidicalcio.repository.GiocatoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GiocatoreService {


    private final GiocatoreRepository giocatoreRepository;

    public GiocatoreService(GiocatoreRepository giocatoreRepository) {
        this.giocatoreRepository = giocatoreRepository;
    }

    public Giocatore findById(Long id) {
        return giocatoreRepository.findById(id).orElse(null);// ritorno del torneo se esiste se no null
    }

    //ADMIN
    @Transactional
    public Giocatore save(Giocatore giocatore) {
        return giocatoreRepository.save(giocatore);
    }

    @Transactional
    public void deleteById(Long id) {
        giocatoreRepository.deleteById(id);
    }

    @Transactional
    public Giocatore update(Long id, Giocatore giocatoreAggiornato) {
        Giocatore giocatore = findById(id);
        if (giocatore != null) {
            giocatore.setNome(giocatoreAggiornato.getNome());
            giocatore.setCognome(giocatoreAggiornato.getCognome());
            giocatore.setDataDiNascita(giocatoreAggiornato.getDataDiNascita());
            giocatore.setRuolo(giocatoreAggiornato.getRuolo());
            giocatore.setAltezza(giocatoreAggiornato.getAltezza());
            giocatore.setSquadra(giocatoreAggiornato.getSquadra());
            return giocatoreRepository.save(giocatore);
        }
        return null;
    }

    @Transactional
    public void assegnaSquadra(Long giocatoreId, Squadra squadra) {
        Giocatore giocatore = findById(giocatoreId);
        if (giocatore != null) {
            giocatore.setSquadra(squadra);
            giocatoreRepository.save(giocatore);
        }
    }
}