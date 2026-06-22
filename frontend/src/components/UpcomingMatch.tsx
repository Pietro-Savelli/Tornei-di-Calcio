import type { PartitaHomeDto } from '../types';

interface UpcomingMatchProps {
    m: PartitaHomeDto;
}

export default function UpcomingMatch({ m }: UpcomingMatchProps) {
    const urlPartita = `http://localhost:8080/tornei/${m.torneoId}/calendario/partita/${m.id}`;

    return (
        <a className="match-row upcoming" href={urlPartita}>
            <span className="match-team home" title={m.squadraCasa}>{m.squadraCasa}</span>
            <span className="match-vs">
        <span className="vs">VS</span>
        <span className="match-date">{m.dataOra || 'da definire'}</span>
      </span>
            <span className="match-team away" title={m.squadraOspite}>{m.squadraOspite}</span>
        </a>
    );
}