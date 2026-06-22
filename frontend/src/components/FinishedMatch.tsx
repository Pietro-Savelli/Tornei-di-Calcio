import { Link } from 'react-router-dom';
import { matchUrl } from '../utils/helpers';
import { PartitaHomeDto } from '../types';

interface FinishedMatchProps {
  m: PartitaHomeDto;
}

export default function FinishedMatch({ m }: FinishedMatchProps) {
  return (
    <Link className="match-row" to={matchUrl(m)}>
      <span className="match-team home" title={m.squadraCasa}>{m.squadraCasa}</span>
      <span className="match-score">
        {m.goalsHome ?? '-'}<span className="dash">:</span>{m.goalsAway ?? '-'}
      </span>
      <span className="match-team away" title={m.squadraOspite}>{m.squadraOspite}</span>
    </Link>
  );
}
