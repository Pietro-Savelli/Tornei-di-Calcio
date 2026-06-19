import React from "react";

const HeroMatch = ({ featuredMatch }) => {
  if (!featuredMatch) {
    return (
      <div className="bg-zinc-950 text-zinc-500 p-8 text-center">
        Nessuna partita di cartello disponibile.
      </div>
    );
  }

  const isLive = featuredMatch.stato === "IN_CORSO";

  return (
    <section className="w-full">
      <div className="flex flex-col lg:flex-row min-h-[420px]">
        {/* BLOCCO SINISTRA — Focus Partita */}
        <div className="lg:w-3/5 w-full bg-gradient-to-br from-red-900 via-red-950 to-black flex flex-col justify-center items-center px-8 py-10 relative overflow-hidden">
          {/* Pattern decorativo di sfondo */}
          <div className="absolute inset-0 opacity-10 bg-[radial-gradient(circle_at_center,_white_1px,_transparent_1px)] bg-[length:20px_20px]" />

          {/* Badge Diretta */}
          {isLive && (
            <div className="absolute top-6 left-6 bg-white text-red-900 font-black text-xs uppercase tracking-widest px-3 py-1 rounded-full animate-pulse">
              🔴 Diretta
            </div>
          )}

          {/* Data e luogo */}
          <p className="text-red-200/80 text-sm uppercase tracking-widest mb-2 relative z-10">
            {featuredMatch.dataOra
              ? new Date(featuredMatch.dataOra).toLocaleString("it-IT", {
                  weekday: "long",
                  day: "numeric",
                  month: "long",
                  hour: "2-digit",
                  minute: "2-digit",
                })
              : "Data da definire"}
          </p>
          <p className="text-red-300/60 text-xs uppercase tracking-wider mb-10 relative z-10">
            {featuredMatch.luogo || "Stadio Centrale"}
          </p>

          {/* Squadre e Punteggio */}
          <div className="flex items-center justify-center gap-6 relative z-10 w-full max-w-2xl">
            {/* Squadra Casa */}
            <div className="flex flex-col items-center flex-1">
              <div className="w-20 h-20 rounded-full bg-red-800 border-4 border-red-700 flex items-center justify-center text-2xl font-black shadow-lg">
                {featuredMatch.squadraCasa?.sigla?.substring(0, 2) || "??"}
              </div>
              <h3 className="mt-4 text-lg font-bold text-white text-center leading-tight">
                {featuredMatch.squadraCasa?.nome || "Squadra Casa"}
              </h3>
            </div>

            {/* Risultato centrale */}
            <div className="flex flex-col items-center px-4">
              <div className="text-6xl md:text-7xl font-black text-white tracking-tighter drop-shadow-lg">
                {isLive || featuredMatch.stato === "FINISHED" ? (
                  <>
                    {featuredMatch.goalsHome ?? 0}
                    <span className="text-red-400 mx-2">-</span>
                    {featuredMatch.goalsAway ?? 0}
                  </>
                ) : (
                  <span className="text-4xl text-red-300">VS</span>
                )}
              </div>
              <p className="mt-2 text-xs text-red-200/70 uppercase tracking-widest">
                {isLive ? "In corso" : featuredMatch.stato === "FINISHED" ? "Terminata" : "Programmata"}
              </p>
            </div>

            {/* Squadra Trasferta */}
            <div className="flex flex-col items-center flex-1">
              <div className="w-20 h-20 rounded-full bg-zinc-700 border-4 border-zinc-600 flex items-center justify-center text-2xl font-black shadow-lg">
                {featuredMatch.squadraOspite?.sigla?.substring(0, 2) || "??"}
              </div>
              <h3 className="mt-4 text-lg font-bold text-white text-center leading-tight">
                {featuredMatch.squadraOspite?.nome || "Squadra Ospite"}
              </h3>
            </div>
          </div>
        </div>

        {/* BLOCCO DESTRA — Immagine Torneo + Classifica */}
        <div className="lg:w-2/5 w-full bg-zinc-900 flex flex-col">
          {/* Immagine di copertina */}
          <div className="relative h-64 lg:h-72 overflow-hidden">
            <img
              src="https://images.unsplash.com/photo-1517466787929-bc90951d0974?w=800&q=80"
              alt="Copertina Torneo"
              className="w-full h-full object-cover"
            />
            <div className="absolute inset-0 bg-gradient-to-t from-zinc-900 via-transparent to-transparent" />
            <div className="absolute bottom-4 left-4">
              <h2 className="text-2xl font-black text-white drop-shadow-lg">Torneo Amatori 2025</h2>
              <p className="text-sm text-zinc-300">La competizione più attesa dell'anno</p>
            </div>
          </div>

          {/* Mini-card Classifica e Calendario */}
          <div className="flex-1 p-5 flex flex-col justify-center">
            <h4 className="text-zinc-400 text-xs uppercase tracking-widest mb-3">🏆 Classifica</h4>
            <div className="grid grid-cols-2 gap-3">
              <a href="/classifica" className="bg-zinc-800 hover:bg-zinc-700 rounded-lg p-3 transition flex items-center gap-3">
                <div className="w-8 h-8 rounded-full bg-red-600 flex items-center justify-center text-sm font-bold">1</div>
                <div>
                  <p className="text-sm font-bold text-white">Vedi Classifica</p>
                  <p className="text-xs text-zinc-500">Generale</p>
                </div>
              </a>
              <a href="/calendario" className="bg-zinc-800 hover:bg-zinc-700 rounded-lg p-3 transition flex items-center gap-3">
                <div className="w-8 h-8 rounded-full bg-zinc-600 flex items-center justify-center text-sm font-bold">📅</div>
                <div>
                  <p className="text-sm font-bold text-white">Calendario</p>
                  <p className="text-xs text-zinc-500">Prossime partite</p>
                </div>
              </a>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default HeroMatch;
