## Relazione: Ottimizzazione N+1 vs JOIN FETCH — Cosa dire all'esame

### 1. Il problema N+1
Con il caricamento **LAZY** di Hibernate, quando accedi a una collezione (`torneo.getPartite()`) o a una relazione `@ManyToOne` (es. `partita.getSquadraCasa()`), Hibernate esegue una query SQL aggiuntiva. Se ci sono 190 partite, ognuna con 2 squadre, il numero teorico di query diventa esplosivo: `1 + 190 × 2 = 381`.
In pratica, la **first-level cache** di Hibernate riduce questo numero a ~22, perché le 20 squadre distinte vengono caricate una sola volta.

### 2. Perché il JOIN FETCH (a 2 query) è la soluzione corretta
Hibernate permette **massimo una collezione multi-valore** (`@OneToMany` o `@ManyToMany`) per query con `JOIN FETCH`. Se proviamo a fare `JOIN FETCH` su **due** collezioni dello stesso padre (`t.partite` e `t.squadre`), il database genera un **prodotto cartesiano**: `20 partite × 16 squadre = 320` righe per ricostruire un grafo di 36 entità. `DISTINCT` deduplica in memoria, ma il danno di rete e memoria è già fatto.

**La soluzione corretta**: usare **due query separate**:
- Query 1: `JOIN FETCH t.partite` + `p.squadraCasa` + `p.squadraOspite` (un solo bag: partite)
- Query 2: `JOIN FETCH t.squadre` (un solo bag: squadre)

Hibernate, nella seconda query, ritrova il `Torneo` in **L1 cache** (stesso `id`, stessa sessione) e popola solo la collezione mancante. Nessun prodotto cartesiano, nessun overhead di memoria.

### 3. Perché il test in locale mostra JOIN più lento
Il nostro benchmark misura 3 fasi:

| Test | Cosa misura | Tempo reale |
|---|---|---|
| **A** | Caricamento LAZY completo: ~22 query by PK (torneo, partite, squadreCasa, squadraOspite, squadre iscritte) | ~31ms |
| **B** | Caricamento con 2 query JPQL (JOIN FETCH partite + JOIN FETCH squadre) | ~39ms |
| **C** | Chiamata a `calcolaClassifica()` (stesse 2 query + elaborazione Java) | ~7ms ← **FALSO** |

**C è falso**: manca `entityManager.clear()` prima di C. Hibernate trova il `Torneo` già in cache (caricato da B) e non esegue NESSUNA query SQL. I 7ms sono solo calcolo Java puro (`HashMap` + cicli + `sort()`).

**Perché B è più lento di A in locale?**
- In localhost, una query by PK impiega ~0.1-0.5ms. ~22 query semplici = ~5-11ms totali. Sono praticamente gratis.
- La strategia B fa **parsing JPQL ×2**, **generazione SQL ×2**, **ResultSet ×2** con JOIN più complessi. In locale, il doppio round-trip e il parsing del ResultSet più grande superano il vantaggio teorico.
- Il vero vantaggio di B emerge con **DB remoto**: 2 query × 10ms = 20ms vs ~22 query × 10ms = 220ms. Rapporto ~10-11×.

### 4. Perché comunque scegliamo JOIN FETCH a 2 query
Non scegliamo la strategia per il tempo in locale, ma per **prevedibilità e scalabilità**:
- **Numero di query fisso**: 2, indipendente dal numero di squadre e partite.
- **Zero prodotto cartesiano**: memoria stabile, nessun overhead JDBC inutile.
- **DB remoto**: la latenza di rete rende ogni query costosa. 2 query sono scalabili, ~22 query no.

### 5. Alternativa: `@Fetch(FetchMode.SUBSELECT)`
Annotando le collezioni con `@Fetch(FetchMode.SUBSELECT)`, Hibernate carica ogni bag con una sotto-query:
1. `SELECT * FROM Torneo WHERE id = ?`
2. `SELECT * FROM Partita WHERE torneo_id IN (...)`
3. `SELECT * FROM Squadra JOIN ... WHERE torneo_id IN (...)`

Vantaggi: zero prodotto cartesiano, dichiarativo, nessuna query custom da scrivere.  
Svantaggi: 3 query invece di 2, meno controllo sul grafo caricato.

### 6. Alternativa "nucleare": DTO/Projection
Per operazioni **read-only** come la classifica, la massima performance si ottiene con una query nativa o JPQL che proietta direttamente in un DTO, evitando completamente il caricamento del grafo di entità. Questo è il top delle performance, ma a scapito della manutenibilità.

---

**Conclusione per l'esame**: 
> "Il pattern N+1 si risolve con JOIN FETCH, ma con il vincolo di una sola collezione per query. Per evitare il prodotto cartesiano tra due bag collections, ho usato due query separate sfruttando la L1 cache di Hibernate. In locale il benchmark non è significativo a causa della latenza zero; la scelta è motivata dalla scalabilità in produzione."
