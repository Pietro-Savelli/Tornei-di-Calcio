import React from 'react';
import { matchUrl } from '../utils/helpers';

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
  );
}

export default UpcomingMatch;
