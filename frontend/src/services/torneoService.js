import api from '../config/axiosConfig';

export const aggiungiPreferito = (torneoId) => api.post(`/api/tornei/${torneoId}/preferito`);
export const rimuoviPreferito = (torneoId) => api.delete(`/api/tornei/${torneoId}/preferito`);
export const getPreferiti = () => api.get('/api/utente/preferiti');
