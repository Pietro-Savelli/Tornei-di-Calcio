import { Link } from 'react-router-dom';

export default function Hero() {
  return (
    <header className="home-hero">
      <div className="container">
        <p className="home-hero-kicker">SISTEMA INFORMATIVO · CALCIO AMATORIALE</p>
        <h1 className="home-hero-title">Vivi ogni <span className="brand-accent">torneo</span>.</h1>
        <p className="home-hero-sub">
          Risultati, calendario delle prossime sfide e squadre.
        </p>
        <div className="home-hero-cta">
          <Link className="btn btn-accent btn-lg" to="/#tornei">Esplora i tornei</Link>
          <Link className="btn btn-ghost btn-lg" to="/squadre/">Vedi le squadre</Link>
        </div>
      </div>
    </header>
  );
}
