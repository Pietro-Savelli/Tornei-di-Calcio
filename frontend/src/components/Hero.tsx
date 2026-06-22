import FadeIn from './FadeIn';

export default function Hero() {
  return (
      <header className="home-hero">
        <div className="home-hero-bg" aria-hidden="true" />
        <div className="container" style={{ position: 'relative', zIndex: 2 }}>
          <FadeIn delay={0} duration={900}>
            <p className="home-hero-kicker">SISTEMA INFORMATIVO · CALCIO AMATORIALE</p>
          </FadeIn>
          <FadeIn delay={150} duration={900}>
            <h1 className="home-hero-title">
              Vivi ogni <span className="brand-accent">torneo</span>.
            </h1>
          </FadeIn>
          <FadeIn delay={300} duration={900}>
            <p className="home-hero-sub">
              Risultati, calendario delle prossime sfide e squadre.
            </p>
          </FadeIn>
          <FadeIn delay={450} duration={900}>
            <div className="home-hero-cta">
              <a className="btn btn-accent btn-lg" href="/tornei/">
                Esplora i tornei
              </a>

              <a className="btn btn-ghost btn-lg" href="/squadre/">
                Vedi le squadre
              </a>
            </div>
          </FadeIn>
        </div>
      </header>
  );
}