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
