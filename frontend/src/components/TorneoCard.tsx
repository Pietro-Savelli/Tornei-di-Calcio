import { useAuth } from '../context/AuthContext';
import type { PartitaHomeDto, TorneoHomeDto } from '../types';

interface TorneoCardProps {
    t: TorneoHomeDto;
    onTogglePreferito: (id: number) => void;
}

function UpcomingMatch({ m }: { m: PartitaHomeDto }) {
    return (
        <a className="match-row upcoming" href={`/tornei/${m.torneoId}/calendario/partita/${m.id}`}>
            <span className="match-team home" title={m.squadraCasa}>{m.squadraCasa}</span>
            <span className="match-vs">
        <span className="vs">VS</span>
        <span className="match-date">{m.dataOra || 'da definire'}</span>
      </span>
            <span className="match-team away" title={m.squadraOspite}>{m.squadraOspite}</span>
        </a>
    );
}

export default function TorneoCard({ t, onTogglePreferito }: TorneoCardProps) {
    const { isAuthenticated } = useAuth();

    return (
        <article className="torneo-card">
            <div className="torneo-card-head">
                <div className="torneo-card-meta">
                    <span className="torneo-badge">{t.anno}</span>
                    <span className="torneo-meta">{t.numeroSquadre} squadre</span>
                    {isAuthenticated && (
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

            <a className="torneo-card-title" href={`/tornei/${t.id}`}>
                {t.nome}
            </a>
            <p className="torneo-card-desc">{t.descrizione}</p>

            <div className="torneo-card-cols">
                <div className="match-col">
                    <h4 className="match-col-title">Prossime partite</h4>
                    <div className="match-list">
                        {t.prossimePartite.length > 0
                            ? t.prossimePartite.map((m) => <UpcomingMatch key={m.id} m={m} />)
                            : <p className="match-empty">Nessuna partita in programma.</p>}
                    </div>
                </div>
            </div>

            <a className="torneo-card-foot" href={`/tornei/${t.id}`}>
                Apri torneo · classifica e calendario →
            </a>
        </article>
    );
}