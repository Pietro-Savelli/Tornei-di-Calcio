import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Hero from '../components/Hero';
import TorneoCard from '../components/TorneoCard';
import TeamCarousel from '../components/TeamCarousel';
import { getHome } from '../services/homeService';
import { aggiungiPreferito, rimuoviPreferito } from '../services/torneoService';
import { useAuth } from '../context/AuthContext';
import { HomeDto, TorneoHomeDto } from '../types';

export default function HomePage() {
  const [data, setData] = useState<HomeDto | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    getHome()
      .then((res) => setData(res.data))
      .catch((e) => setError(e.message));
  }, []);

  const handleTogglePreferito = async (torneoId: number) => {
    if (!isAuthenticated) {
      window.location.href = '/login';
      return;
    }
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
    <>
      <Navbar />
      <Hero />

      {error && (
        <div className="container home-state error">
          Errore nel caricamento dei dati: {error}
        </div>
      )}
      {!data && !error && <div className="container home-state">Caricamento in corso…</div>}
      {data && (
        <>
          <section className="section" id="tornei">
            <div className="container">
              <h2 className="section-title">Tornei {data.n}</h2>
              <p className="section-sub">
                Le ultime partite giocate e le prossime sfide, torneo per torneo.
              </p>

              <div style={{ marginBottom: '2rem', maxWidth: '400px' }}>
                <input
                  type="text"
                  placeholder="Cerca un torneo..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  style={{
                    width: '100%',
                    padding: '0.75rem 1rem',
                    borderRadius: '6px',
                    border: '1px solid #27272a',
                    backgroundColor: '#18181b',
                    color: '#fff',
                    fontSize: '1rem',
                    outline: 'none',
                  }}
                />
              </div>

              {torneiFiltrati.length > 0 ? (
                <div className="torneo-cards">
                  {torneiFiltrati.map((t) => (
                    <TorneoCard key={t.id} t={t} onTogglePreferito={handleTogglePreferito} />
                  ))}
                </div>
              ) : (
                <p className="match-empty">Nessun torneo corrisponde alla ricerca.</p>
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
