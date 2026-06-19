import React from "react";

const GalleryCards = ({ squadre }) => {
  if (!squadre || squadre.length === 0) {
    return (
      <div className="bg-zinc-950 px-6 py-12 text-zinc-500 text-center">
        Nessuna squadra iscritta al torneo.
      </div>
    );
  }

  return (
    <section className="bg-zinc-950 px-4 sm:px-6 lg:px-8 py-10">
      <h2 className="text-xl font-black text-white uppercase tracking-widest mb-6 border-l-4 border-red-600 pl-4">
        Le Squadre del Torneo
      </h2>

      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
        {squadre.map((s) => (
          <div
            key={s.id}
            className="group relative h-64 rounded-xl overflow-hidden cursor-pointer hover:scale-[1.02] transition-transform duration-300"
          >
            {/* Immagine di background */}
            <img
              src={s.imgUrl || `https://source.unsplash.com/featured/?football,team/${s.id}`}
              alt={s.nome}
              className="w-full h-full object-cover"
              onError={(e) => {
                e.target.src = "https://images.unsplash.com/photo-1574629810360-7efbbe195018?w=600&q=80";
              }}
            />

            {/* Overlay gradiente scuro verso il basso */}
            <div className="absolute inset-0 bg-gradient-to-t from-black/90 via-black/30 to-transparent" />

            {/* Testo in basso */}
            <div className="absolute bottom-0 left-0 right-0 p-4">
              <h3 className="text-lg font-bold text-white leading-tight group-hover:text-red-400 transition">
                {s.nome}
              </h3>
              <p className="text-xs text-zinc-400 mt-1">{s.citta || "Città"}</p>
            </div>

            {/* Badge anno fondazione */}
            {s.annoFondazione && (
              <div className="absolute top-3 right-3 bg-black/60 backdrop-blur-sm rounded-full px-2 py-1 text-xs font-bold text-white">
                {s.annoFondazione}
              </div>
            )}
          </div>
        ))}
      </div>
    </section>
  );
};

export default GalleryCards;
