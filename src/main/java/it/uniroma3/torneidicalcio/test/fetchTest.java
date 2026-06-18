package it.uniroma3.torneidicalcio.test;

import it.uniroma3.torneidicalcio.model.Partita;
import it.uniroma3.torneidicalcio.model.Torneo;
import it.uniroma3.torneidicalcio.repository.TorneoRepository;
import it.uniroma3.torneidicalcio.service.TorneoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

@Component
public class fetchTest implements CommandLineRunner {

    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private TorneoService torneoService;

    @Override
    @Transactional
    @PersistenceContext
    public void run(String... args) {
        Long id = 1L;
        StopWatch watch = new StopWatch();

        // Test A: LAZY → N+1
        // carica torneo, poi per ogni partita fa una query per squadraCasa e una per squadraOspite
        watch.start("LAZY - N+1");
        Torneo t = torneoRepository.findById(id).orElseThrow();
        for (Partita p : t.getPartite()) {
            p.getSquadraCasa().getNome();
            p.getSquadraOspite().getNome();
        }
        watch.stop();


        // Test B: JOIN FETCH → 1 sola query, stesso accesso ai dati
        watch.start("JOIN FETCH");
        Torneo t2 = torneoRepository.findTorneoWithPartiteEFeat(id);
        for (Partita p : t2.getPartite()) {
            p.getSquadraCasa().getNome();
            p.getSquadraOspite().getNome();
        }
        watch.stop();

        System.out.println("\n========== RISULTATI PERFORMANCE ==========");
        System.out.println(watch.prettyPrint());
        System.out.println("==========================================\n");
    }
}

/*
Strategia LAZY (N+1):

Query eseguite: 1 (torneo) + 1 (partite) + 2×20 (squadraCasa + squadraOspite per ogni squadra distinta) = ~42 query
In realtà Hibernate riduce questo numero grazie alla first-level cache: le stesse 20 squadre si ripetono in 190 partite, quindi ogni squadra viene caricata solo la prima volta che appare. Il numero reale di query osservato nei log è stato ~22, non 42.
Tempo misurato: 75ms (DB locale)

Strategia JOIN FETCH:

Query eseguite: 1 sola query con 4 LEFT JOIN (partite, squadraCasa, squadraOspite, squadre iscritte)
Tempo misurato: 70ms (DB locale)



I tempi risultano quasi identici per due motivi combinati: il DB è locale (latenza per query ≈ 0.1ms), e la first-level cache di Hibernate riduce già il numero reale di query LAZY da 381 teoriche (1 + 190×2) a ~22, perché le 20 squadre vengono messe in cache dopo il primo accesso.
In un contesto di produzione con DB remoto (latenza realistica 5–10ms/query), la situazione cambierebbe drasticamente: anche con la cache, le 22 query LAZY produrrebbero 22 × 10ms = 220ms, contro i 10ms del JOIN FETCH — un rapporto di circa 22×. Senza cache (prima richiesta a freddo, sessione nuova) il numero teorico di query sarebbe molto più alto.
La scelta del JOIN FETCH è quindi motivata dalla scalabilità e dalla prevedibilità del comportamento in produzione, non dai tempi misurati in locale che risultano ingannevoli per via della cache e della latenza zero.
 */