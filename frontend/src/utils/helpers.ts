import { PartitaHomeDto } from '../types';

export const matchUrl = (m: PartitaHomeDto): string => `/tornei/${m.torneoId}/calendario/partita/${m.id}`;
export const initials = (name: string): string => (name || '?').trim().charAt(0).toUpperCase();
