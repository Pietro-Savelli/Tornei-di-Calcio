# Changelog modifiche — Tornei di Calcio

Documento di tracciamento delle modifiche apportate al progetto.
Per ogni voce: **file**, **cosa è cambiato**, **motivo tecnico**.

> Nota: il **Punto 4 (upload immagini)** è stato escluso su richiesta e **non** è stato implementato.
> Questo changelog copre il **Punto 2** (code review / sicurezza / N+1), il **Punto 3** (paginazione)
> e, in coda, una sintesi del **Punto 1** (Home React + build) per completezza.

---

## PUNTO 2 — Code review, sicurezza e N+1

Filosofia seguita: **solo modifiche strettamente necessarie** (bug critici, falle di
sicurezza, criteri di valutazione). Nessuna riscrittura stilistica.

### 2.1 — Bug di routing: profilo giocatore irraggiungibile (404)
- **File:** `controller/GiocatoreController.java`
- **Cambiato:** `@RequestMapping("/")` → `@RequestMapping("/giocatori")`
- **Motivo:** il controller era mappato sulla radice con `@GetMapping("/{id}")`, quindi
  intercettava `/{id}` (es. `/5`) ma **non** `/giocatori/{id}`. I link "Vedi profilo"
  (`@{/giocatori/{id}}`) generati da `giocatori/list.html` e `formSquadre.html` finivano in
  **404**. Inoltre una rotta `/{id}` a livello di root è pericolosamente ampia. Con la nuova
  mappatura i link funzionano e la superficie di routing è circoscritta.

### 2.2 — Bug: getter/setter errati su Arbitro
- **File:** `model/Arbitro.java`
- **Cambiato:** `getCongome()`/`setCongome()` → `getCognome()`/`setCognome()`
- **Motivo:** il template `partite/show.html` accede a `partita.arbitro.cognome`, che richiede
  un getter `getCognome()`. Con il nome errato, appena un arbitro fosse stato valorizzato, la
  pagina partita avrebbe lanciato un errore di property non risolvibile. Bug latente corretto.

### 2.3 — Bug: equals() con `==` su Long
- **File:** `model/Giocatore.java`
- **Cambiato:** `return id == giocatore.id;` → `return Objects.equals(id, giocatore.id);`
- **Motivo:** `id` è di tipo `Long` (oggetto): `==` confronta i **riferimenti**, non i valori,
  e fallisce fuori dalla cache di Integer/Long. Le altre entità (`Torneo`, `Squadra`, `Partita`)
  erano già state corrette in questo modo; `Giocatore` era rimasto indietro. Correzione di
  coerenza/correttezza (l'uso in `Set`/collezioni JPA dipende da equals/hashCode corretti).

### 2.4 — Sicurezza: NPE nel controllo di proprietà dei commenti
- **File:** `service/CommentoService.java`
- **Cambiato:** `commento.getUtente().equals(utenteLoggato)` →
  `utenteLoggato.equals(commento.getUtente())`
- **Motivo:** il requisito "**USER modifica solo i propri commenti**" è implementato da
  `canUserModify()`. Se `commento.getUtente()` fosse `null`, la vecchia forma lanciava NPE.
  Invertendo l'`equals` (chiamato sull'oggetto non nullo `utenteLoggato`) il controllo resta
  corretto e diventa null-safe, senza indebolire la regola di autorizzazione.

### 2.5 — Robustezza avvio: fetchTest su DB vuoto
- **File:** `test/fetchTest.java`
- **Cambiato:** sostituito `torneoRepository.findAll().iterator().next()` con una guardia
  `if (!iterator.hasNext()) { ...salta il test...; return; }`
- **Motivo:** `iterator().next()` lanciava `NoSuchElementException` se non c'erano tornei,
  facendo **fallire l'avvio** dell'intera applicazione (il test è un `CommandLineRunner` che
  parte ad ogni boot). Con la guardia, in assenza di dati il benchmark viene semplicemente saltato.

### 2.6 — Validazione dati: @NotNull su Partita + @Valid nei controller
- **File:** `model/Partita.java`, `controller/AdminController.java`
- **Cambiato:**
  - `Partita`: aggiunti `@NotNull` su `torneo`, `squadraCasa`, `squadraOspite`
    (import `jakarta.validation.constraints.NotNull`).
  - `AdminController.createPartita()` e `editPartita()`: aggiunti `@Valid` + `BindingResult` +
    `Model`; in caso di errori si ri-renderizza il form (helper `popolaFormPartita()` che
    ripopola le tendine tornei/squadre).
- **Motivo:** criterio di valutazione "validazione dei dati (`@Valid`, `@NotNull`)". La `Partita`
  era l'unica entità priva di vincoli e i suoi controller non validavano l'input. Ora la
  partita non può essere salvata senza torneo/squadre (integrità referenziale lato applicazione),
  in linea con le altre entità (`Torneo`, `Squadra`, `Giocatore`) che già usavano `@Valid`.

### 2.7 — Sicurezza: whitelist API/asset pubblici
- **File:** `authentication/SecurityConfiguration.java`
- **Cambiato:** aggiunta regola `permitAll()` in GET per `"/app/**", "/assets/**", "/js/**", "/api/home"`.
- **Motivo:** la Home React (statica, build Vite) e la sua API JSON devono essere accessibili
  senza autenticazione. Il resto della catena di sicurezza è invariato: `/admin/**` resta
  `hasAuthority('ADMIN')`, i commenti restano `authenticated()`, le viste pubbliche restano permesse.

### 2.8 — N+1 / JOIN FETCH: verifica (nessuna modifica necessaria)
- **File:** `repository/TorneoRepository.java`, `service/TorneoService.java`, `test/fetchTest.java`
- **Esito della review:** le query `findTorneoWithPartite` e `findTorneoWithSquadre` sono
  **corrette** e supportano correttamente il test:
  - ciascuna query fa `JOIN FETCH` su **una sola** collezione (`t.partite` con i suoi `@ManyToOne`
    `squadraCasa`/`squadraOspite`, oppure `t.squadre`): **nessun prodotto cartesiano** tra due bag.
  - vengono eseguite in **due query separate** nello stesso contesto transazionale
    (`@Transactional(readOnly = true)` su `findByIdWithDetails`/`calcolaClassifica`,
    `@Transactional` sul `fetchTest`): la seconda ritrova il `Torneo` in **L1 cache** e popola
    solo la collezione mancante.
  - il confronto LAZY (Test A) vs JOIN FETCH (Test B) + `calcolaClassifica` (Test C) con
    `entityManager.clear()` tra A e B è coerente con la consegna (confronto strategie + misura
    tempi + discussione in `relazione-fetch-test.md`).
  - Conclusione: **nessuna correzione di codice** richiesta su questo punto. È stata solo
    aggiunta la guardia su DB vuoto (vedi 2.5).

### 2.9 — @Transactional: verifica (nessuna modifica necessaria)
- Le operazioni di scrittura nei service (`save`/`update`/`delete`/`inserisciRisultato`/…)
  sono già annotate `@Transactional`; le letture pesanti multi-query
  (`findByIdWithDetails`, `calcolaClassifica`) sono `@Transactional(readOnly = true)`.
  I nuovi metodi di `PartitaService` (paginazione/home, vedi sotto) sono stati annotati
  `@Transactional(readOnly = true)`. Configurazione giudicata corretta.

---

## PUNTO 3 — Paginazione delle partite del torneo (una alla volta)

Obiettivo: nella pagina di dettaglio del singolo torneo, mostrare **una sola partita alla volta**
con pulsanti **Precedente** / **Successiva**.

### 3.1 — Query paginata sul repository
- **File:** `repository/PartitaRepository.java`
- **Cambiato:** aggiunto metodo
  `Page<Partita> findPaginaPartiteByTorneoId(Long torneoId, Pageable pageable)` con `@Query`
  (`value` con `LEFT JOIN FETCH` su `squadraCasa`/`squadraOspite` + `countQuery` separata).
  Aggiunti import `Page`, `Pageable`, `Stato`.
- **Motivo:** serve una pagina di partite ordinate per data, con le squadre già caricate per
  evitare lazy-loading nel template. Il `JOIN FETCH` è solo su relazioni **to-one**, quindi
  Hibernate applica la paginazione in SQL (`LIMIT/OFFSET`) senza il warning di "paginazione in
  memoria" tipico del fetch di collezioni. La `countQuery` separata calcola il totale pagine.

### 3.2 — Metodo di servizio
- **File:** `service/PartitaService.java`
- **Cambiato:** aggiunto `getPaginaPartite(Long torneoId, int page, int size)`
  (`@Transactional(readOnly = true)`, usa `PageRequest.of(page, size)`). Aggiunti import
  `Page`, `PageRequest`, `List`.
- **Motivo:** incapsula la creazione del `Pageable` e tiene il controller sottile.

### 3.3 — Controller
- **File:** `controller/TorneoController.java`
- **Cambiato:** `showTorneo()` ora accetta `@RequestParam(name="partitaPage", defaultValue="0") int partitaPage`
  e aggiunge al model `paginaPartite` (size **1** = una partita per pagina) e `partitaCorrente`.
  Aggiunto import `org.springframework.data.domain.Page`. `Math.max(partitaPage, 0)` evita indici negativi.
- **Motivo:** size = 1 realizza la "paginazione semplice, una partita alla volta". Il numero di
  pagina arriva via query string così i link Precedente/Successiva sono semplici GET navigabili.

### 3.4 — Vista
- **File:** `templates/tornei/show.html` (ricostruita)
- **Cambiato:** aggiunta una card "Partite del torneo" che mostra la singola `partitaCorrente`
  (punteggio se `FINISHED`, altrimenti data/ora) e i controlli `← Precedente` / `Successiva →`
  con `th:href="@{/tornei/{id}(id=..., partitaPage=...)}"`, indicatore "Partita X di N", e
  disabilitazione dei bottoni su `paginaPartite.first` / `paginaPartite.last`.
- **Motivo:** rende la paginazione richiesta, integrata nella pagina di dettaglio del torneo.

---

## PUNTO 1 — Home React (stile FIFA) + build + uniformità grafica (sintesi)

Non richiesto nel changelog ma incluso per completezza del tracciamento.

### Backend a supporto della Home
- **Nuovi:** `controller/HomeApiController.java` (`@RestController`, `GET /api/home`),
  `service/HomeService.java`, DTO `dto/HomeDto`, `dto/TorneoHomeDto`, `dto/PartitaHomeDto`,
  `dto/SquadraHomeDto` (record Java, per non serializzare proxy/collezioni LAZY in JSON).
- **`repository/PartitaRepository.java`:** aggiunte `findUltimePartiteGiocate(...)` (ultime 3
  partite `FINISHED`) e `findProssimePartite(...)` (prossime 3 in programma), usate con
  `PageRequest.of(0, 3)`.
- **`service/PartitaService.java`:** aggiunti `getUltimePartiteGiocate` / `getProssimePartite`.
- **`controller/HomeController.java`:** aggiunge al model `isAuth` / `isAdmin` / `username`
  per iniettare lo stato di autenticazione nella SPA (`window.__APP__`).

### Frontend React + build Maven
- **Nuovo modulo `frontend/`** (Vite + React 18): `package.json`, `vite.config.js`,
  `src/main.jsx`, `src/App.jsx` (navbar, hero, card torneo con **3 ultime + 3 prossime partite**,
  carosello squadre). Build con nomi fissi → `src/main/resources/static/app/home.js`.
- **`pom.xml`:** aggiunto `com.github.eirslett:frontend-maven-plugin` (install Node + `npm install`
  + `npm run build` in fase `generate-resources`); proprietà `node.version` e `skipFrontend`
  (`mvn ... -DskipFrontend=true` per saltare il frontend).
- **`templates/index.html`:** ora è l'host della SPA (monta `#root`, inietta `window.__APP__`
  con auth + CSRF via Thymeleaf, carica `/app/home.js`).

### Uniformità grafica (tema unico "FIFA dark")
- **`static/css/stile.css`:** riscritto come **unica fonte di stile** (variabili, navbar, bottoni,
  card, tabelle, hero/score partita, commenti, paginazione, carosello, form auth, modale).
- **`static/css/admin-form.css`:** nuovo file (dark) referenziato dai form admin; **rimosso**
  il vecchio `adminForm.css` (il nome non corrispondeva ai riferimenti `/css/admin-form.css`
  → i form admin erano di fatto **senza CSS**: bug grafico corretto).
- **`templates/fragments/navbar.html`:** navbar Thymeleaf condivisa, identica a quella React.
- **Tutti i template** (`tornei/*`, `squadre/*`, `giocatori/*`, `partite/*`, `admin/*`,
  `authentication/*`, `error/*`) allineati al tema scuro, con navbar e correzione dei
  riferimenti CSS rotti (`schede.css`, `style.css`, `admin-form.css` non risolti).

---

## Come si esegue

```bash
# Build completo (compila Java + builda la Home React via Vite)
./mvnw -DskipTests package

# Avvio (richiede PostgreSQL su localhost:5432, db tornei_di_calcio, user/pass postgres/postgres)
./mvnw spring-boot:run

# Per saltare la build del frontend (es. sviluppo backend):
./mvnw -DskipFrontend=true spring-boot:run
```
