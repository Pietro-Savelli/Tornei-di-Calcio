import React from 'react';
import FinishedMatch from './FinishedMatch';
import UpcomingMatch from './UpcomingMatch';
import { matchUrl } from '../utils/helpers';
import { APP } from '../config/appConfig';

function TorneoCard({ t, onTogglePreferito }) {
  return (
    <article className="torneo-card">
      <div className="torneo-card-head">
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', width: '100%' }}>
          <span className="torneo-badge">{t.anno}</span>
          <span className="torneo-meta">{t.numeroSquadre} squadre</span>
            {APP.authenticated && (
                <button
                    className={`preferito-btn ${t.preferito ? 'active' : ''}`}
                    onClick={(e) => {
                        e.preventDefault();
                        e.stopPropagation();
                        onTogglePreferito(t.id);
                    }}
                    title={t.preferito ? 'Rimuovi dai preferiti' : 'Aggiungi ai preferiti'}
                    aria-label={t.preferito ? 'Rimuovi dai preferiti' : 'Aggiungi ai preferiti'}
                >
                    <svg viewBox="0 0 24 24" className="preferito-star" aria-hidden="true">
                        <path d="M12 2.5l2.92 6.34 6.98.62-5.27 4.66 1.6 6.88L12 17.6l-6.23 3.4 1.6-6.88L2.1 9.46l6.98-.62L12 2.5z" />
                    </svg>
                </button>
            )}
        </div>
      </div>
      <a className="torneo-card-title" href={`/tornei/${t.id}`}>{t.nome}</a>
      <p className="torneo-card-desc">{t.descrizione}</p>

      <div className="torneo-card-cols">
          {/*<div className="match-col">
          <h4 className="match-col-title">Ultime partite</h4>
          {t.partiteRecenti.length > 0
            ? t.partiteRecenti.map((m) => <FinishedMatch key={m.id} m={m} />)
            : <p className="match-empty">Nessuna partita giocata.</p>}
        </div>*/}
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
  );
}

export default TorneoCard;
