import React from 'react';
import FinishedMatch from './FinishedMatch';
import UpcomingMatch from './UpcomingMatch';
import { matchUrl } from '../utils/helpers';

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
  );
}

export default TorneoCard;
