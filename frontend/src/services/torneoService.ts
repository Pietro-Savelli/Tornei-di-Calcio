import api from './api';

export const aggiungiPreferito = (torneoId: number) => api.post(`/api/tornei/${torneoId}/preferito`);
export const rimuoviPreferito = (torneoId: number) => api.delete(`/api/tornei/${torneoId}/preferito`);
export const getPreferiti = () => api.get<number[]>('/api/utente/preferiti');
