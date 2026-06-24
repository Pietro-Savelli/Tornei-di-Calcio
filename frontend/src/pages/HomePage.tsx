import { useState, useEffect } from 'react';
import Hero from '../components/Hero';
import TorneoCard from '../components/TorneoCard';
import TeamCarousel from '../components/TeamCarousel';
import FadeIn from '../components/FadeIn';
import { getHome } from '../services/homeService';
import { aggiungiPreferito, rimuoviPreferito } from '../services/torneoService';
import type { HomeDto } from '../types';

function SkeletonCard() {
  return (
      <div className="torneo-card skeleton-card" aria-hidden="true">
        <div className="skeleton-line" style={{ width: '40%', height: '20px', marginBottom: '12px' }} />
        <div className="skeleton-line" style={{ width: '70%', height: '28px', marginBottom: '8px' }} />
        <div className="skeleton-line" style={{ width: '100%', height: '16px', marginBottom: '16px' }} />
        <div className="skeleton-line" style={{ width: '100%', height: '60px' }} />
      </div>
  );
}

export default function HomePage() {
  const [data, setData] = useState<HomeDto | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    getHome()
        .then((res) => setData(res.data))
        .catch((e) => setError(e.message));
  }, []);

  useEffect(() => {
    if (window.location.hash === '#tornei') {
      const el = document.getElementById('tornei');
      if (el) el.scrollIntoView({ behavior: 'smooth' });
    }
  }, [data]);

  const handleTogglePreferito = async (torneoId: number) => {
    const torneo = data?.tornei.find((t) => t.id === torneoId);
    if (!torneo) return;
    const isPreferito = torneo.preferito;
    try {
      if (isPreferito) {
        await rimuoviPreferito(torneoId);
      } else {
        await aggiungiPreferito(torneoId);
      }
      setData((prev) => {
        if (!prev) return prev;
        return {
          ...prev,
          tornei: prev.tornei
              .map((t) => (t.id === torneoId ? { ...t, preferito: !isPreferito } : t))
              .sort((a, b) => (b.preferito === a.preferito ? 0 : b.preferito ? 1 : -1)),
        };
      });
    } catch (err) {
      console.error('Errore preferito:', err);
      alert("Errore durante l'aggiornamento dei preferiti.");
    }
  };

  const torneiFiltrati =
      data?.tornei?.filter((t) => {
        const matchNome = t.nome.toLowerCase().includes(searchTerm.toLowerCase());
        const matchAnno = String(t.anno).toLowerCase().includes(searchTerm.toLowerCase());
        return matchNome || matchAnno;
      }) || [];

  return (
      <div className="home-wrapper">
        <Hero />

        {error && (
            <div className="container">
              <div className="home-state error">Errore nel caricamento dei dati: {error}</div>
            </div>
        )}

        {!data && !error && (
            <section className="section" id="tornei">
              <div className="container">
                <div className="skeleton-header" />
                <div className="torneo-cards">
                  {Array.from({ length: 4 }).map((_, i) => (
                      <SkeletonCard key={i} />
                  ))}
                </div>
              </div>
            </section>
        )}

        {data && (
            <>
              <section className="section" id="tornei">
                <div className="container">
                  <FadeIn delay={0}>
                    <div className="section-header">
                      <h2 className="section-title">Tornei </h2>
                      <p className="section-sub">
                        Le prossime sfide, torneo per torneo.
                      </p>
                    </div>
                  </FadeIn>

                  <FadeIn delay={100}>
                    <div className="search-wrapper">
                      <div className="search-icon" aria-hidden="true">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                          <circle cx="11" cy="11" r="8" />
                          <line x1="21" y1="21" x2="16.65" y2="16.65" />
                        </svg>
                      </div>
                      <input
                          type="text"
                          className="home-search"
                          placeholder="Cerca un torneo..."
                          value={searchTerm}
                          onChange={(e) => setSearchTerm(e.target.value)}
                          aria-label="Cerca un torneo"
                      />
                      {searchTerm && (
                          <button
                              className="search-clear"
                              onClick={() => setSearchTerm('')}
                              aria-label="Cancella ricerca"
                          >
                            ×
                          </button>
                      )}
                    </div>
                  </FadeIn>

                  {torneiFiltrati.length > 0 ? (
                      <div className="torneo-cards">
                        {torneiFiltrati.map((t, index) => (
                            <FadeIn key={t.id} delay={index * 80} direction="up">
                              <TorneoCard t={t} onTogglePreferito={handleTogglePreferito} />
                            </FadeIn>
                        ))}
                      </div>
                  ) : (
                      <FadeIn>
                        <div className="match-empty" style={{ textAlign: 'center', padding: '3rem 0' }}>
                          Nessun torneo corrisponde alla ricerca.
                        </div>
                      </FadeIn>
                  )}
                </div>
              </section>

              <FadeIn>
                <TeamCarousel squadre={data.squadre} />
              </FadeIn>
            </>
        )}

        <footer className="home-footer">
          <div className="container">
            <span>SIW Tornei di Calcio</span>
            <span className="muted">Progetto Sistemi Informativi su Web · Università Roma Tre</span>
          </div>
        </footer>
      </div>
  );
}