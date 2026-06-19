import React from "react";

const MatchTicker = ({ partite }) => {
  if (!partite || partite.length === 0) {
    return (
      <div className="bg-zinc-900 border-b border-zinc-800 py-3 px-4 text-zinc-500 text-sm">
        Nessuna partita disponibile per il ticker.
      </div>
    );
  }

  return (
    <div className="bg-zinc-900 border-b border-zinc-800">
      <div className="flex overflow-x-auto gap-3 px-4 py-3 scrollbar-hide">
        {partite.map((p) => (
          <div
            key={p.id}
            className="flex-shrink-0 bg-zinc-800/80 rounded-xl px-4 py-3 flex items-center gap-3 min-w-[220px] hover:bg-zinc-800 transition cursor-pointer"
          >
            {/* Cerchi sovrapposti per simulare i loghi */}
            <div className="relative flex items-center">
              <div className="w-10 h-10 rounded-full bg-red-800 border-2 border-zinc-900 flex items-center justify-center text-xs font-bold text-white z-10">
                {p.squadraCasa?.sigla?.charAt(0) || "?"}
              </div>
              <div className="w-10 h-10 rounded-full bg-zinc-700 border-2 border-zinc-900 flex items-center justify-center text-xs font-bold text-white -ml-4 z-0">
                {p.squadraOspite?.sigla?.charAt(0) || "?"}
              </div>
            </div>

            {/* Sigle e risultato / data */}
            <div className="flex flex-col items-center">
              <div className="flex items-center gap-2 text-sm font-bold tracking-wide">
                <span className="text-white">{p.squadraCasa?.sigla || "---"}</span>
                <span className="text-zinc-500">-</span>
                <span className="text-white">{p.squadraOspite?.sigla || "---"}</span>
              </div>
              <div className="text-lg font-black text-white mt-0.5">
                {p.stato === "SCHEDULED" ? (
                  <span className="text-xs text-zinc-400 font-normal">
                    {p.dataOra
                      ? new Date(p.dataOra).toLocaleDateString("it-IT", { day: "2-digit", month: "2-digit" })
                      : "--/--"}
                  </span>
                ) : (
                  <>
                    {p.goalsHome ?? 0} <span className="text-zinc-500 mx-1">-</span> {p.goalsAway ?? 0}
                  </>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default MatchTicker;
