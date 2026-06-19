import React, { useEffect, useState } from "react";
import axios from "axios";
import MatchTicker from "./MatchTicker";
import HeroMatch from "./HeroMatch";
import GalleryCards from "./GalleryCards";

// Il frontend React viene servito dallo stesso server Spring Boot (embedded),
// quindi le chiamate API usano URL relativi. CORS non è necessario.
axios.defaults.headers.common["Content-Type"] = "application/json";

const Dashboard = ({ torneoId = 1 }) => {
  const [partite, setPartite] = useState([]);
  const [squadre, setSquadre] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const [resPartite, resSquadre] = await Promise.all([
          axios.get(`/api/tornei/${torneoId}/partite`),
          axios.get(`/api/tornei/${torneoId}/squadre`),
        ]);
        setPartite(resPartite.data || []);
        setSquadre(resSquadre.data || []);
      } catch (err) {
        console.error(err);
        setError("Impossibile caricare i dati del torneo. Verifica che il backend sia attivo.");
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [torneoId]);

  if (loading) {
    return (
      <div className="min-h-screen bg-zinc-950 flex items-center justify-center text-white">
        <div className="flex flex-col items-center gap-4">
          <div className="animate-spin rounded-full h-12 w-12 border-t-4 border-red-600" />
          <p className="text-zinc-400 text-sm uppercase tracking-widest">Caricamento dashboard...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-zinc-950 flex items-center justify-center text-white px-6">
        <div className="text-center max-w-md">
          <p className="text-red-500 text-lg font-bold mb-2">⚠️ Errore di connessione</p>
          <p className="text-zinc-400 text-sm">{error}</p>
        </div>
      </div>
    );
  }

  const featuredMatch = partite.find((p) => p.stato === "IN_CORSO") || partite[0] || null;

  return (
    <div className="min-h-screen bg-zinc-950 text-white font-sans">
      <MatchTicker partite={partite} />
      <HeroMatch featuredMatch={featuredMatch} />
      <GalleryCards squadre={squadre} />
    </div>
  );
};

export default Dashboard;
