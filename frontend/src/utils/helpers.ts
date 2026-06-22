import type { PartitaHomeDto } from '../types';

    export const APP: any = (typeof window !== 'undefined' && (window as any).__APP__);
    export const matchUrl = (m: PartitaHomeDto): string => `/tornei/${m.torneoId}/calendario/partita/${m.id}`;
    export const initials = (name: string): string => (name || '?').trim().charAt(0).toUpperCase();
