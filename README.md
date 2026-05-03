# Sistema Informativo per Tornei di Calcio Amatoriale

Progetto sviluppato per il corso di Sistemi Informativi su Web (SIW).

## Obiettivo

Realizzare un sistema informativo web per la gestione di tornei di calcio amatoriale utilizzando:

- Spring Boot (backend)
- JPA / Hibernate (persistenza)
- PostgreSQL (database)
- Thymeleaf (frontend server-side)
- React (parte frontend client-side)

---

## Architettura

L'applicazione segue una **architettura a livelli**:

- **Controller Layer**
  - Gestione richieste HTTP
  - Mapping delle rotte
- **Service Layer**
  - Logica di business
  - Gestione transazioni (`@Transactional`)
- **Repository Layer**
  - Accesso ai dati tramite JPA

---

##  Modello Dati

### Entità principali:

- **Torneo**
- **Squadra**
- **Giocatore**
- **Partita**
- **Arbitro**
- **Utente**
- **Commento** (estensione per utenti registrati)

### Relazioni chiave:

- Squadra ↔ Torneo (ManyToMany)
- Squadra → Giocatore (OneToMany)
- Partita → Squadre (ManyToOne x2)
- Partita → Arbitro (ManyToOne)
- Partita → Torneo (ManyToOne)
- Utente → Commento (OneToMany)

---

## Sicurezza

Implementata con Spring Security.

### Ruoli:

- `USER`
  - Visualizzazione contenuti
  - Gestione commenti
- `ADMIN`
  - Gestione completa sistema

### Funzionalità:

- Login con username/password
- Protezione rotte amministrative

---

##  Funzionalità

### Pubbliche

- Visualizzazione tornei
- Dettaglio torneo
- Squadre partecipanti
- Calendario partite
- Dettaglio squadra
- Classifica

###  Utenti Registrati

- Visualizzazione commenti
- Inserimento/modifica commenti

###  Admin

- CRUD Tornei
- CRUD Squadre
- CRUD Giocatori
- Gestione Partite
- Inserimento risultati
- Eliminazione dati

---

## Gestione Transazioni

Nel service layer:

- `@Transactional(readOnly = true)` → operazioni di lettura
- `@Transactional` → operazioni di modifica

---

##  Ottimizzazione Accesso ai Dati

È stata effettuata un’analisi su:

- Fetch LAZY vs EAGER
- Uso di `JOIN FETCH`
- `EntityGraph`

### Caso di studio:

Visualizzazione torneo con squadre e partite.

### Risultati:

- LAZY → problema N+1 query
- JOIN FETCH → riduzione query
- EntityGraph → soluzione più flessibile

---

##  Frontend

- **Thymeleaf** → rendering server-side
- **React** → componenti dinamici (es. commenti o classifica live)

---

## Tecnologie

- Java 17+
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Thymeleaf
- React
- Maven / Gradle
