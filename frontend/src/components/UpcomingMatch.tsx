import { Link } from 'react-router-dom';
import { matchUrl } from '../utils/helpers';
import type { PartitaHomeDto } from '../types';

interface UpcomingMatchProps {
  m: PartitaHomeDto;
}

export default function UpcomingMatch({ m }: UpcomingMatchProps) {
  return (
    <Link className="match-row upcoming" to={matchUrl(m)}>
      <span className="match-team home" title={m.squadraCasa}>{m.squadraCasa}</span>
      <span className="match-vs">
        <span className="vs">VS</span>
        <span className="match-date">{m.dataOra || 'da definire'}</span>
      </span>
      <span className="match-team away" title={m.squadraOspite}>{m.squadraOspite}</span>
    </Link>
  );
}
