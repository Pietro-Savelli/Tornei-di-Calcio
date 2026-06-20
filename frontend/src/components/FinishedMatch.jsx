import React from 'react';
import { matchUrl } from '../utils/helpers';

function FinishedMatch({ m }) {
  return (
    <a className="match-row" href={matchUrl(m)}>
      <span className="match-team home" title={m.squadraCasa}>{m.squadraCasa}</span>
      <span className="match-score">
        {m.goalsHome ?? '-'}<span className="dash">:</span>{m.goalsAway ?? '-'}
      </span>
      <span className="match-team away" title={m.squadraOspite}>{m.squadraOspite}</span>
    </a>
  );
}

export default FinishedMatch;
