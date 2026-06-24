import { initials } from '../utils/helpers';
import type { SquadraHomeDto } from '../types';

interface TeamCarouselProps {
    squadre: SquadraHomeDto[];
}

export default function TeamCarousel({ squadre }: TeamCarouselProps) {
    if (!squadre.length) return null;

    return (
        <section className="section" id="squadre">
            <div className="container">
                <h2 className="section-title">Le squadre</h2>
                <p className="section-sub">Scorri e seleziona una squadra per vedere rosa, partite e tornei.</p>

                <div className="team-carousel">
                    {squadre.map((s) => (
                        <a
                            className="team-chip"
                            key={s.id}
                            href={`/squadre/${s.id}`}
                        >
                            {/* Controllo se esiste lo stemmaUrl */}
                            {s.stemmaUrl ? (
                                <img
                                    src={s.stemmaUrl}
                                    alt={`Stemma ${s.nome}`}
                                    style={{
                                        width: '70px',
                                        height: '70px',
                                        objectFit: 'contain',
                                        marginBottom: '15px'
                                    }}
                                />
                            ) : (
                                <span className="team-logo">{initials(s.nome)}</span>
                            )}

                            <span className="team-chip-name">{s.nome}</span>
                        </a>
                    ))}
                </div>
            </div>
        </section>
    );
}