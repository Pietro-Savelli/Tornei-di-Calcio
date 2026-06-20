export const matchUrl = (m) => `/tornei/${m.torneoId}/calendario/partita/${m.id}`;
export const initials = (name) => (name || '?').trim().charAt(0).toUpperCase();
