import React, { useEffect, useState } from 'react';
import Navbar from '../components/Navbar';
import Hero from '../components/Hero';
import TorneoCard from '../components/TorneoCard';
import TeamCarousel from '../components/TeamCarousel';
import { getHome } from '../services/homeService';

export default function HomePage() {
  const [data, setData] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    getHome()
      .then((res) => setData(res.data))
      .catch((e) => setError(e.message));
  }, []);

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
              <h2 className="section-title">Tornei {data.n} </h2>
              <p className="section-sub">Le ultime partite giocate e le prossime sfide, torneo per torneo.</p>
              {data.n > 0 ? (
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
  );
}
