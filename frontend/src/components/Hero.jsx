import React from 'react';

function Hero() {
  return (
    <header className="home-hero">
      <div className="container">
        <p className="home-hero-kicker">SISTEMA INFORMATIVO · CALCIO AMATORIALE</p>
        <h1 className="home-hero-title">Vivi ogni <span className="brand-accent">torneo</span>.</h1>
        <p className="home-hero-sub">
          Risultati, calendario delle prossime sfide e squadre.
        </p>
        <div className="home-hero-cta">
          <a className="btn btn-accent btn-lg" href="#tornei">Esplora i tornei</a>
          <a className="btn btn-ghost btn-lg" href="/squadre/">Vedi le squadre</a>
        </div>
      </div>
    </header>
  );
}

export default Hero;
