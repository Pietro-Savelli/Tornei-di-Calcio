# Documentazione Frontend — Tornei di Calcio

> Questo documento descrive lo scopo di ogni file/cartella del frontend React dopo il refactoring.

---

## 📁 Struttura ad alto livello

```
frontend/
├── .env.example              # Template variabili d'ambiente
├── index.html                # Entry point del dev server (Vite)
├── vite.config.js            # Configurazione build Vite
├── tailwind.config.js        # Configurazione Tailwind CSS
├── postcss.config.js         # Configurazione PostCSS
├── package.json              # Dipendenze e script npm
└── src/
    ├── main.jsx              # Entry point unico (HomePage)
    ├── index.css             # Stili globali
    ├── pages/                # Schermate principali (views)
    │   └── HomePage.jsx
    ├── components/           # Componenti UI riutilizzabili
    │   ├── Navbar.jsx
    │   ├── Hero.jsx
    │   ├── TorneoCard.jsx
    │   ├── FinishedMatch.tsx
    │   ├── UpcomingMatch.jsx
    │   └── TeamCarousel.jsx
    ├── services/             # Chiamate API centralizzate
    │   └── homeService.js
    ├── config/               # Configurazione globale
    │   ├── axiosConfig.js
    │   └── appConfig.js
    └── utils/                # Helper/utility
        └── helpers.js
```

---

## 📄 File di configurazione (root)

### `vite.config.js`
Configurazione del bundler **Vite**. Punti chiave:
- `base: '/app/'` → tutti gli asset sono serviti da Spring Boot sotto `/app/`.
- `build.outDir` → il bundle finisce in `../src/main/resources/static/app/` (cartella Spring Boot).
- `input: 'src/main.jsx'` → unico entry point: la **HomePage**.
- `output.entryFileNames: 'home.js'` → nome fisso del bundle JS, così Thymeleaf lo referenzia in modo stabile.

### `index.html`
HTML usato dal **dev server** di Vite (`npm run dev`). Punta a `src/main.jsx`, quindi in locale vedi la **HomePage**.
In produzione Spring Boot serve la sua `index.html` Thymeleaf, che include `home.js`.

### `tailwind.config.js` / `postcss.config.js`
Configurazione di **Tailwind CSS** per utility-first styling. La build di Vite li processa attraverso PostCSS.

### `.env.example`
Template per le variabili d'ambiente. Vite espone al client solo le variabili che iniziano con `VITE_`.
```
VITE_API_BASE_URL=          # lascia vuoto per path relativi
# VITE_API_BASE_URL=http://localhost:8080  # esempio per backend separato
```

---

## 📄 Entry point (src/)

### `main.jsx`
Entry point unico. Monta `<HomePage />` nel `div#root`.
Questo è il file che Vite compila in `home.js`.

### `index.css`
Foglio di stile globale importato dall'entry point. Contiene le regole CSS di base (Tailwind + eventuali custom).

---

## 📄 Pagine (src/pages/)

### `HomePage.jsx`
Landing page pubblica del sito. Si occupa di:
- Chiamare `GET /api/home` tramite `homeService.js`.
- Mostrare `Navbar`, `Hero`, l'elenco dei tornei (`TorneoCard`), e il carosello squadre (`TeamCarousel`).
- Gestire stati di loading ed errore.

---

## 📄 Componenti (src/components/)

### `Navbar.jsx`
Barra di navigazione in cima alla Home. Legge lo stato di autenticazione da `window.__APP__` (iniettato da Thymeleaf) e mostra:
- Link a Tornei, Squadre, Partite, Classifiche.
- Se loggato: username, pulsante Admin (se admin), form di logout con CSRF.
- Se non loggato: Login, Registrati.

### `Hero.jsx`
Sezione hero della Home: titolo, sottotitolo, e CTA che portano alla sezione tornei o alle squadre. Nessuna logica, solo presentazione.

### `TorneoCard.jsx`
Card che rappresenta un singolo torneo. Mostra:
- Badge anno, numero squadre, nome e descrizione.
- Colonna "Ultime partite" (usa `FinishedMatch`).
- Colonna "Prossime partite" (usa `UpcomingMatch`).
- Link in fondo alla pagina del torneo.

### `FinishedMatch.tsx`
Riga riassuntiva di una partita già terminata. Mostra squadra casa, risultato (`goalsHome : goalsAway`), squadra ospite. Linkabile alla pagina della partita.

### `UpcomingMatch.jsx`
Riga di una partita futura. Mostra squadra casa, "VS" + data, squadra ospite. Linkabile alla pagina della partita.

### `TeamCarousel.jsx`
Carosello orizzontale di "chip" squadra nella Home. Ogni chip ha:
- Iniziali del nome (cerchio colorato).
- Nome e città della squadra.
- Link alla pagina della squadra (`/squadre/{id}`).

---

## 📄 Servizi API (src/services/)

### `homeService.js`
Servizio per l'endpoint globale della Home.
```js
getHome() → GET /api/home
```

> **Vantaggio**: se il backend cambia URL, modifichi solo qui (e in `axiosConfig.js`), non in ogni componente.

---

## 📄 Configurazione (src/config/)

### `axiosConfig.js`
Istanza centralizzata di **Axios**:
- `baseURL` = `import.meta.env.VITE_API_BASE_URL` (o stringa vuota per path relativi).
- Header default `Content-Type: application/json` e `Accept: application/json`.
- Tutti i servizi importano questa istanza invece di `axios` direttamente.

### `appConfig.js`
Oggetto di configurazione letto da `window.__APP__` (iniettato da Thymeleaf nel backend Java).
Contiene:
- `authenticated` (boolean)
- `isAdmin` (boolean)
- `username` (string)
- `csrf` (param name + token)

> Usato da `Navbar.jsx` per decidere cosa mostrare senza fare ulteriori chiamate API.

---

## 📄 Utility (src/utils/)

### `helpers.js`
Funzioni di supporto pure, senza dipendenze da React:
- `matchUrl(m)` → costruisce il percorso `/tornei/{torneoId}/calendario/partita/{id}`.
- `initials(name)` → ritorna la prima lettera maiuscola di una stringa (es. "Roma" → "R").

> Centralizzare queste funzioni evita duplicazioni tra componenti.

---

## 🔧 Flusso di una chiamata API (dopo il refactoring)

```
Componente (es. HomePage.jsx)
  → importa da services/homeService.js
    → importa da config/axiosConfig.js
      → usa axios con baseURL pre-configurata
        → chiama il backend (relativo o assoluto, a seconda di .env)
```

---

## ⚠️ Note operative

1. **URL relativi**: `VITE_API_BASE_URL` vuota significa che le API usano path relativi (`/api/...`). Va bene quando il frontend è servito dallo stesso dominio del backend Spring Boot.
2. **Auth**: la navbar non fa chiamate API per sapere se sei loggato; legge `window.__APP__` che Spring/Thymeleaf scrive nel HTML server-side.
3. **Entry point unico**: il progetto ha una sola pagina React (HomePage). Se in futuro serviranno più pagine, occorrerà configurare Vite per multi-page build o aggiungere React Router.
