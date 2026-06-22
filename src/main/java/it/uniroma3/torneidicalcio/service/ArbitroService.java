package it.uniroma3.torneidicalcio.service;

import it.uniroma3.torneidicalcio.model.Arbitro;
import it.uniroma3.torneidicalcio.repository.ArbitroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArbitroService {

    private ArbitroRepository arbitroRepository;

    public ArbitroService(ArbitroRepository arbitroRepository) {
        this.arbitroRepository = arbitroRepository;
    }

    public List<Arbitro> findAll() {
        return arbitroRepository.findAll();
    }
}