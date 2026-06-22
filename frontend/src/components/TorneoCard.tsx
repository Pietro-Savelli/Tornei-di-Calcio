import UpcomingMatch from './UpcomingMatch';
import { useAuth } from '../context/AuthContext';
import type { TorneoHomeDto } from '../types';

interface TorneoCardProps {
    readonly t: TorneoHomeDto;
    readonly onTogglePreferito: (id: number) => void;
}

export default function TorneoCard({ t, onTogglePreferito }: TorneoCardProps) {
    const { isAuthenticated } = useAuth();

    const handleToggle = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        e.stopPropagation();
        onTogglePreferito(t.id);
    };

    return (
        <article className="torneo-card">
            <header className="torneo-card-head">
                <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', width: '100%' }}>
                    <span className="torneo-badge">{t.anno}</span>
                    <span className="torneo-meta">{t.numeroSquadre} squadre</span>
                    {isAuthenticated && (
                        <button
                            type="button"
                            className={`preferito-btn ${t.preferito ? 'active' : ''}`}
                            onClick={handleToggle}
                            title={t.preferito ? 'Rimuovi dai preferiti' : 'Aggiungi ai preferiti'}
                            aria-label={t.preferito ? 'Rimuovi dai preferiti' : 'Aggiungi ai preferiti'}
                        >
                            <svg viewBox="0 0 24 24" className="preferito-star" aria-hidden="true">
                                <path d="M12 2.5l2.92 6.34 6.98.62-5.27 4.66 1.6 6.88L12 17.6l-6.23 3.4 1.6-6.88L2.1 9.46l6.98-.62L12 2.5z" />
                            </svg>
                        </button>
                    )}
                </div>
            </header>

            <a className="torneo-card-foot" href={`http://localhost:8080/tornei/${t.id}`}>
                {t.nome}
            </a>

            <p className="torneo-card-desc">{t.descrizione}</p>

            <section className="torneo-card-cols">
                <div className="match-col">
                    <h4 className="match-col-title">Prossime partite</h4>
                    {t.prossimePartite.length > 0 ? (
                        t.prossimePartite.map((m) => <UpcomingMatch key={m.id} m={m} />)
                    ) : (
                        <p className="match-empty">Nessuna partita in programma.</p>
                    )}
                </div>
            </section>

            <a className="torneo-card-foot" href={`http://localhost:8080/tornei/${t.id}`}>
                Apri torneo - classifica e calendario →
            </a>
        </article>
    );
}