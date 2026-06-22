import { Link } from 'react-router-dom';
import { initials } from '../utils/helpers';
import { SquadraHomeDto } from '../types';

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
            <Link className="team-chip" key={s.id} to={`/squadre/${s.id}`}>
              <span className="team-logo">{initials(s.nome)}</span>
              <span className="team-chip-name">{s.nome}</span>
              <span className="team-chip-city">{s.citta}</span>
            </Link>
          ))}
        </div>
      </div>
    </section>
  );
}
