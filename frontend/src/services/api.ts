import axios from 'axios';

// In produzione React e Spring sono lo stesso dominio (Spring serve la build
// React come risorse statiche): baseURL vuota = richieste relative, vanno
// automaticamente all'host corrente, niente da configurare.
// In dev locale (Vite su :5173, Spring su :8080) serve invece l'origin
// esplicito, preso da una variabile d'ambiente Vite.
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '',
  withCredentials: true, // indispensabile: fa viaggiare il cookie JSESSIONID
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },
});

export default api;