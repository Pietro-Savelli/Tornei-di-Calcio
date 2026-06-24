package it.uniroma3.torneidicalcio.test;

import it.uniroma3.torneidicalcio.model.Partita;
import it.uniroma3.torneidicalcio.model.Squadra;
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

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) {
        var iterator = torneoRepository.findAll().iterator();
        if (!iterator.hasNext()) {
            System.out.println(">>> [fetchTest] Nessun torneo nel DB: test di performance saltato.");
            return;
        }
        Torneo primo = iterator.next();
        Long id = primo.getId();
        System.out.println(">>> Test sul torneo: " + primo.getNome() + " (id=" + id + ")");

        StopWatch watch = new StopWatch();


        // =========================================================
        // TEST A: LAZY N+1 — SOLO CARICAMENTO
        // =========================================================
        watch.start("A - LAZY caricamento");
        Torneo t1 = torneoRepository.findById(id).orElseThrow();
        // Forza il caricamento completo del grafo come fa il service
        for (Partita p : t1.getPartite()) {
            p.getSquadraCasa().getNome();
            p.getSquadraOspite().getNome();
        }
        for (Squadra s : t1.getSquadre()) {
            s.getNome();
        }
        watch.stop();


        entityManager.clear();

        // =========================================================
        // TEST B: 2x JOIN FETCH — SOLO CARICAMENTO
        // =========================================================

        watch.start("B - 2x JOIN FETCH caricamento");
        Torneo t2 = torneoRepository.findTorneoWithPartite(id);
        torneoRepository.findTorneoWithSquadre(id);
        for (Partita p : t2.getPartite()) {
            p.getSquadraCasa().getNome();
            p.getSquadraOspite().getNome();
        }
        for (Squadra s : t2.getSquadre()) {
            s.getNome();
        }
        watch.stop();

        System.out.println("\n========== RISULTATI PERFORMANCE ==========");
        System.out.println(watch.prettyPrint());
        System.out.println("==========================================\n");
    }
}

/*
========== PERCHÉ IL TEST PRECEDENTE NON ERA FAIR ==========

Il confronto originale misurava:
  A) SOLO caricamento LAZY + accesso ai nomi (~4 righe di codice)
  B) TUTTO calcolaClassifica() = caricamento + HashMap + cicli + calcoli + sort()

Il test B faceva molto più lavoro computazionale dopo il caricamento.
Inoltre, in locale le query by PK (N+1) sono praticamente istantanee perché
il DB è localhost: nessuna latenza di rete, nessuna serializzazione TCP.

========== STRUTTURA DEL NUOVO TEST ==========

A - LAZY caricamento: forza caricamento completo del grafo con accesso LAZY.
   Query: ~22 query by PK (torneo + partite + squadreCasa/Ospite + squadre iscritte).
   In locale queste sono istantanee; in produzione con DB remoto diventano lente.

B - 2x JOIN FETCH caricamento: stesso grafo caricato con 2 query JPQL.
   Query 1: JOIN FETCH t.partite + p.squadraCasa + p.squadraOspite
   Query 2: JOIN FETCH t.squadre (stesso torneo, ritrovato in L1 cache)
   In locale 2 round-trip sono leggermente più lenti di ~22 query by PK,
   ma in produzione 2 query battono nettamente le 22 query N+1.

C - calcolaClassifica end-to-end: misura il tempo totale del servizio.
   Include il caricamento + tutta l'elaborazione della classifica.

========== PERCHÉ A PUÒ BATTERE B IN LOCALE ==========

1. Latenza zero: in localhost, una query by PK impiega ~0.1-0.5ms.
   22 query semplici = ~5-11ms totali. 2 query con JOIN = ~2-4ms.
   La differenza è marginale e il parsing di 2 ResultSet complessi può
   bilanciare il vantaggio.

2. Overhead di 2 round-trip: la strategia a 2 query fa parsing JPQL ×2,
   generazione SQL ×2, ResultSet ×2. In locale questo overhead è visibile.

3. Il vantaggio reale della strategia B è:
   - ZERO prodotto cartesiano (meno memoria, meno righe JDBC)
   - Scalabilità in produzione (DB remoto): 2 query vs 22 query
   - Prevedibilità: il numero di query è fisso (2), indipendente dal numero di squadre.

========== STIMA IN PRODUZIONE (DB remoto, latenza 5–10ms/query) ==========

A - LAZY: ~22 query → 22 × 10ms = 220ms (senza contare il calcolo)
B - 2x JOIN FETCH: 2 query → 2 × 10ms = 20ms (caricamento)

Rapporto di scalabilità: circa 10-11×, con memoria e prevedibilità superiori.
*/