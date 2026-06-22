export enum Stato {
  SCHEDULED = 'SCHEDULED',
  FINISHED = 'FINISHED',
  CANCELLED = 'CANCELLED',
}

export enum Ruolo {
  PORTIERE = 'PORTIERE',
  DIFENSORE = 'DIFENSORE',
  CENTROCAMPISTA = 'CENTROCAMPISTA',
  ATTACCANTE = 'ATTACCANTE',
}

export interface Arbitro {
  id: number;
  nome: string;
  cognome: string;
  codiceArbitrale: string;
  partite?: Partita[];
}

export interface Giocatore {
  id: number;
  nome: string;
  cognome: string;
  dataDiNascita: string;
  ruolo: Ruolo;
  altezza: number;
  squadra?: Squadra;
}

export interface Commento {
  id: number;
  testo: string;
  dataOra: string;
  utente: Utente;
  partita: Partita;
}

export interface Utente {
  id: number;
  username: string;
  password: string;
  ruolo: string;
  name: string;
}

export interface Credentials {
  id: number;
  username: string;
  password: string;
  role: string;
  utente?: Utente;
}

export interface Preferito {
  id: number;
  utente: Utente;
  torneo: Torneo;
}

export interface Squadra {
  id: number;
  nome: string;
  annoFondazione: number;
  citta: string;
  tornei?: Torneo[];
  giocatori?: Giocatore[];
  partiteGiocateInCasa?: Partita[];
  partiteGiocateInTrasferta?: Partita[];
  eliminata: boolean;
}

export interface Partita {
  id: number;
  luogo: string;
  goalsAway: number | null;
  goalsHome: number | null;
  dataOra: string;
  stato: Stato;
  torneo: Torneo;
  arbitro?: Arbitro;
  squadraCasa: Squadra;
  squadraOspite: Squadra;
  commenti?: Commento[];
  eliminata: boolean;
}

export interface Torneo {
  id: number;
  nome: string;
  anno: number;
  descrizione: string;
  squadre?: Squadra[];
  partite?: Partita[];
}

export interface RigaClassificaDto {
  squadraId: number;
  nomeSquadra: string;
  punti: number;
  partiteGiocate: number;
  vittorie: number;
  pareggi: number;
  sconfitte: number;
  golFatti: number;
  golSubiti: number;
  ritirata: boolean;
}

export interface PartitaHomeDto {
  id: number;
  torneoId: number;
  squadraCasaId: number;
  squadraOspiteId: number;
  squadraCasa: string;
  squadraOspite: string;
  goalsHome: number | null;
  goalsAway: number | null;
  dataOra: string;
  stato: string;
}

export interface SquadraHomeDto {
  id: number;
  nome: string;
  citta: string;
  annoFondazione: number;
}

export interface TorneoHomeDto {
  id: number;
  nome: string;
  anno: number;
  descrizione: string;
  numeroSquadre: number;
  preferito: boolean;
  prossimePartite: PartitaHomeDto[];
}

export interface HomeDto {
  tornei: TorneoHomeDto[];
  squadre: SquadraHomeDto[];
  n: number;
}
