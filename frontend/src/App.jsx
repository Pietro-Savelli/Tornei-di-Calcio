import React, { useEffect, useState } from 'react'

// Stato di autenticazione + csrf iniettati da Thymeleaf nella index.html
const APP = (typeof window !== 'undefined' && window.__APP__) || {
  authenticated: false, isAdmin: false, username: null, csrf: { param: '_csrf', token: '' },
}

// Route pubblica del dettaglio partita (sotto /tornei/** → permitAll)
const matchUrl = (m) => `/tornei/${m.torneoId}/calendario/partita/${m.id}`
const initials = (name) => (name || '?').trim().charAt(0).toUpperCase()

function Navbar() {
  return (
    <nav className="navbar">
      <div className="navbar-inner container">
        <a className="navbar-brand" href="/">
          <span className="brand-ball"></span> SIW <span className="brand-accent">Tornei</span>
        </a>
        <div className="navbar-links">
          <a className="nav-link" href="/tornei/">Tornei</a>
          <a className="nav-link" href="/squadre/">Squadre</a>
          <a className="nav-link" href="#tornei">Partite</a>
          <a className="nav-link" href="#tornei">Classifiche</a>
        </div>
        <div className="navbar-auth">
          {APP.authenticated ? (
            <>
              <span className="nav-user"> {APP.username}</span>
              {APP.isAdmin && <a className="btn btn-ghost" href="/admin">Area Admin</a>}
              <form action="/logout" method="post" className="logout-form">
                <input type="hidden" name={APP.csrf.param} value={APP.csrf.token} />
                <button type="submit" className="btn btn-accent">Logout</button>
              </form>
            </>
          ) : (
            <>
              <a className="btn btn-ghost" href="/login">Login</a>
              <a className="btn btn-accent" href="/register">Registrati</a>
            </>
          )}
        </div>
      </div>
    </nav>
  )
}

function Hero() {
  return (
    <header className="home-hero">
      <div className="container">
        <p className="home-hero-kicker">SISTEMA INFORMATIVO · CALCIO AMATORIALE</p>
        <h1 className="home-hero-title">Vivi ogni <span className="brand-accent">torneo</span>.</h1>
        <p className="home-hero-sub">
          Risultati, calendario delle prossime sfide e squadre.
        </p>
        <div className="home-hero-cta">
          <a className="btn btn-accent btn-lg" href="#tornei">Esplora i tornei</a>
          <a className="btn btn-ghost btn-lg" href="/squadre/">Vedi le squadre</a>
        </div>
      </div>
    </header>
  )
}

function FinishedMatch({ m }) {
  return (
    <a className="match-row" href={matchUrl(m)}>
      <span className="match-team home" title={m.squadraCasa}>{m.squadraCasa}</span>
      <span className="match-score">
        {m.goalsHome ?? '-'}<span className="dash">:</span>{m.goalsAway ?? '-'}
      </span>
      <span className="match-team away" title={m.squadraOspite}>{m.squadraOspite}</span>
    </a>
  )
}

function UpcomingMatch({ m }) {
  return (
    <a className="match-row upcoming" href={matchUrl(m)}>
      <span className="match-team home" title={m.squadraCasa}>{m.squadraCasa}</span>
      <span className="match-vs">
        <span className="vs">VS</span>
        <span className="match-date">{m.dataOra || 'da definire'}</span>
      </span>
      <span className="match-team away" title={m.squadraOspite}>{m.squadraOspite}</span>
    </a>
  )
}

function TorneoCard({ t }) {
  return (
    <article className="torneo-card">
      <div className="torneo-card-head">
        <span className="torneo-badge">{t.anno}</span>
        <span className="torneo-meta">{t.numeroSquadre} squadre</span>
      </div>
      <a className="torneo-card-title" href={`/tornei/${t.id}`}>{t.nome}</a>
      <p className="torneo-card-desc">{t.descrizione}</p>

      <div className="torneo-card-cols">
        <div className="match-col">
          <h4 className="match-col-title">Ultime partite</h4>
          {t.partiteRecenti.length > 0
            ? t.partiteRecenti.map((m) => <FinishedMatch key={m.id} m={m} />)
            : <p className="match-empty">Nessuna partita giocata.</p>}
        </div>
        <div className="match-col">
          <h4 className="match-col-title">Prossime partite</h4>
          {t.prossimePartite.length > 0
            ? t.prossimePartite.map((m) => <UpcomingMatch key={m.id} m={m} />)
            : <p className="match-empty">Nessuna partita in programma.</p>}
        </div>
      </div>

      <a className="torneo-card-foot" href={`/tornei/${t.id}`}>
        Apri torneo · classifica e calendario →
      </a>
    </article>
  )
}

function TeamCarousel({ squadre }) {
  if (!squadre.length) return null
  return (
    <section className="section" id="squadre">
      <div className="container">
        <h2 className="section-title">Le squadre</h2>
        <p className="section-sub">Scorri e seleziona una squadra per vedere rosa, partite e tornei.</p>
        <div className="team-carousel">
          {squadre.map((s) => (
            <a className="team-chip" key={s.id} href={`/squadre/${s.id}`}>
              <span className="team-logo">{initials(s.nome)}</span>
              <span className="team-chip-name">{s.nome}</span>
              <span className="team-chip-city">{s.citta}</span>
            </a>
          ))}
        </div>
      </div>
    </section>
  )
}

export default function App() {
  const [data, setData] = useState(null)
  const [error, setError] = useState(null)

  useEffect(() => {
    fetch('/api/home', { headers: { Accept: 'application/json' } })
      .then((r) => { if (!r.ok) throw new Error('HTTP ' + r.status); return r.json() })
      .then(setData)
      .catch((e) => setError(e.message))
  }, [])

  return (
    <>
      <Navbar />
      <Hero />

      {error && (
        <div className="container home-state error">Errore nel caricamento dei dati: {error}</div>
      )}
      {!data && !error && (
        <div className="container home-state">Caricamento in corso…</div>
      )}

      {data && (
        <>
          <section className="section" id="tornei">
            <div className="container">
              <h2 className="section-title">Tornei attivi</h2>
              <p className="section-sub">Le ultime partite giocate e le prossime sfide, torneo per torneo.</p>
              {data.tornei.length > 0 ? (
                <div className="torneo-cards">
                  {data.tornei.map((t) => <TorneoCard key={t.id} t={t} />)}
                </div>
              ) : (
                <p className="match-empty">Nessun torneo disponibile.</p>
              )}
            </div>
          </section>

          <TeamCarousel squadre={data.squadre} />
        </>
      )}

      <footer className="home-footer">
        <div className="container">
          <span>SIW Tornei di Calcio</span>
          <span className="muted">Progetto Sistemi Informativi su Web · Università Roma Tre</span>
        </div>
      </footer>
    </>
  )
}
