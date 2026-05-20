-- Inserimento di 5 tornei di esempio
INSERT INTO torneo (id, anno, descrizione, nome) VALUES (nextval('torneo_seq'), 2024, 'Campionato italiano di massima serie', 'Serie A');
INSERT INTO torneo (id, anno, descrizione, nome) VALUES (nextval('torneo_seq'), 2024, 'La competizione europea più prestigiosa', 'Champions League');
INSERT INTO torneo (id, anno, descrizione, nome) VALUES (nextval('torneo_seq'), 2023, 'Torneo amatoriale estivo di Roma', 'Torneo di Ferragosto');
INSERT INTO torneo (id, anno, descrizione, nome) VALUES (nextval('torneo_seq'), 2024, 'Coppa nazionale italiana', 'Coppa Italia');
INSERT INTO torneo (id, anno, descrizione, nome) VALUES (nextval('torneo_seq'), 2026, 'Coppa del mondo per nazionali', 'Mondiali');

-- ==========================================
-- 1. CREAZIONE DELLE SQUADRE
-- ==========================================
INSERT INTO squadra (id, anno_fondazione, citta, nome) VALUES (nextval('squadra_seq'), 1900, 'Roma', 'Lazio');
INSERT INTO squadra (id, anno_fondazione, citta, nome) VALUES (nextval('squadra_seq'), 1927, 'Roma', 'Roma');
INSERT INTO squadra (id, anno_fondazione, citta, nome) VALUES (nextval('squadra_seq'), 1899, 'Milano', 'Milan');
INSERT INTO squadra (id, anno_fondazione, citta, nome) VALUES (nextval('squadra_seq'), 1908, 'Milano', 'Inter');
INSERT INTO squadra (id, anno_fondazione, citta, nome) VALUES (nextval('squadra_seq'), 1897, 'Torino', 'Juventus');
INSERT INTO squadra (id, anno_fondazione, citta, nome) VALUES (nextval('squadra_seq'), 1926, 'Napoli', 'Napoli');

-- ==========================================
-- 2. COLLEGAMENTO SQUADRE - TORNEI (Tabella di mezzo)
-- Usiamo le subquery per trovare gli ID corretti senza saperli a priori
-- ==========================================

-- Lazio partecipa a Serie A e Coppa Italia
INSERT INTO torneo_squadre (tornei_id, squadre_id)
VALUES ((SELECT id FROM torneo WHERE nome = 'Serie A'), (SELECT id FROM squadra WHERE nome = 'Lazio'));
INSERT INTO torneo_squadre (tornei_id, squadre_id)
VALUES ((SELECT id FROM torneo WHERE nome = 'Coppa Italia'), (SELECT id FROM squadra WHERE nome = 'Lazio'));

-- Roma partecipa a Serie A e Coppa Italia
INSERT INTO torneo_squadre (tornei_id, squadre_id)
VALUES ((SELECT id FROM torneo WHERE nome = 'Serie A'), (SELECT id FROM squadra WHERE nome = 'Roma'));
INSERT INTO torneo_squadre (tornei_id, squadre_id)
VALUES ((SELECT id FROM torneo WHERE nome = 'Coppa Italia'), (SELECT id FROM squadra WHERE nome = 'Roma'));

-- Milan partecipa a Serie A e Champions League
INSERT INTO torneo_squadre (tornei_id, squadre_id)
VALUES ((SELECT id FROM torneo WHERE nome = 'Serie A'), (SELECT id FROM squadra WHERE nome = 'Milan'));
INSERT INTO torneo_squadre (tornei_id, squadre_id)
VALUES ((SELECT id FROM torneo WHERE nome = 'Champions League'), (SELECT id FROM squadra WHERE nome = 'Milan'));

-- Inter partecipa a Serie A e Champions League
INSERT INTO torneo_squadre (tornei_id, squadre_id)
VALUES ((SELECT id FROM torneo WHERE nome = 'Serie A'), (SELECT id FROM squadra WHERE nome = 'Inter'));
INSERT INTO torneo_squadre (tornei_id, squadre_id)
VALUES ((SELECT id FROM torneo WHERE nome = 'Champions League'), (SELECT id FROM squadra WHERE nome = 'Inter'));

-- Juventus partecipa a Serie A e Coppa Italia
INSERT INTO torneo_squadre (tornei_id, squadre_id)
VALUES ((SELECT id FROM torneo WHERE nome = 'Serie A'), (SELECT id FROM squadra WHERE nome = 'Juventus'));
INSERT INTO torneo_squadre (tornei_id, squadre_id)
VALUES ((SELECT id FROM torneo WHERE nome = 'Coppa Italia'), (SELECT id FROM squadra WHERE nome = 'Juventus'));

-- Napoli partecipa a Serie A e Champions League
INSERT INTO torneo_squadre (tornei_id, squadre_id)
VALUES ((SELECT id FROM torneo WHERE nome = 'Serie A'), (SELECT id FROM squadra WHERE nome = 'Napoli'));
INSERT INTO torneo_squadre (tornei_id, squadre_id)
VALUES ((SELECT id FROM torneo WHERE nome = 'Champions League'), (SELECT id FROM squadra WHERE nome = 'Napoli'));

-- ==========================================
-- 3. CREAZIONE DELLE PARTITE
-- ==========================================

-- ------------------------------------------
-- Torneo 1: SERIE A (5 partite)
-- ------------------------------------------
INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'Stadio Olimpico, Roma', 3, 2, '2026-05-15 20:45:00', 'FINISHED', NULL,
        (SELECT id FROM torneo WHERE nome = 'Serie A'),
        (SELECT id FROM squadra WHERE nome = 'Lazio'),
        (SELECT id FROM squadra WHERE nome = 'Roma'));

INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'San Siro, Milano', 1, 1, '2026-05-16 18:00:00', 'FINISHED', NULL,
        (SELECT id FROM torneo WHERE nome = 'Serie A'),
        (SELECT id FROM squadra WHERE nome = 'Milan'),
        (SELECT id FROM squadra WHERE nome = 'Inter'));

INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'Allianz Stadium, Torino', 2, 0, '2026-05-16 20:45:00', 'FINISHED', NULL,
        (SELECT id FROM torneo WHERE nome = 'Serie A'),
        (SELECT id FROM squadra WHERE nome = 'Juventus'),
        (SELECT id FROM squadra WHERE nome = 'Napoli'));

INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'Stadio Olimpico, Roma', 0, 1, '2026-05-20 20:45:00', 'FINISHED', NULL,
        (SELECT id FROM torneo WHERE nome = 'Serie A'),
        (SELECT id FROM squadra WHERE nome = 'Roma'),
        (SELECT id FROM squadra WHERE nome = 'Milan'));

INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'San Siro, Milano', 2, 2, '2026-05-21 15:00:00', 'FINISHED', NULL,
        (SELECT id FROM torneo WHERE nome = 'Serie A'),
        (SELECT id FROM squadra WHERE nome = 'Inter'),
        (SELECT id FROM squadra WHERE nome = 'Lazio'));

-- ------------------------------------------
-- Torneo 2: CHAMPIONS LEAGUE (3 partite)
-- ------------------------------------------
INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'San Siro, Milano', 2, 1, '2026-05-25 21:00:00', 'FINISHED', NULL,
        (SELECT id FROM torneo WHERE nome = 'Champions League'),
        (SELECT id FROM squadra WHERE nome = 'Milan'),
        (SELECT id FROM squadra WHERE nome = 'Napoli'));

INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'Stadio Diego Armando Maradona, Napoli', 1, 1, '2026-05-26 21:00:00', 'FINISHED', NULL,
        (SELECT id FROM torneo WHERE nome = 'Champions League'),
        (SELECT id FROM squadra WHERE nome = 'Napoli'),
        (SELECT id FROM squadra WHERE nome = 'Inter'));

INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'San Siro, Milano', 0, 0, '2026-05-30 21:00:00', 'FINISHED', NULL,
        (SELECT id FROM torneo WHERE nome = 'Champions League'),
        (SELECT id FROM squadra WHERE nome = 'Inter'),
        (SELECT id FROM squadra WHERE nome = 'Milan'));

-- ------------------------------------------
-- Torneo 3: COPPA ITALIA (2 partite)
-- ------------------------------------------
INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'Stadio Olimpico, Roma', 3, 0, '2026-06-02 21:00:00', 'FINISHED', NULL,
        (SELECT id FROM torneo WHERE nome = 'Coppa Italia'),
        (SELECT id FROM squadra WHERE nome = 'Lazio'),
        (SELECT id FROM squadra WHERE nome = 'Juventus'));

INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'Allianz Stadium, Torino', 1, 2, '2026-06-05 21:00:00', 'FINISHED', NULL,
        (SELECT id FROM torneo WHERE nome = 'Coppa Italia'),
        (SELECT id FROM squadra WHERE nome = 'Juventus'),
        (SELECT id FROM squadra WHERE nome = 'Roma'));

-- ==========================================
-- 4. CREAZIONE DEI GIOCATORI
-- ==========================================

-- ------------------------------------------
-- Squadra: LAZIO
-- ------------------------------------------
INSERT INTO giocatore (id, nome, cognome, data_di_nascita, ruolo, altezza, squadra_id)
VALUES (nextval('giocatore_seq'), 'Ivan', 'Provedel', '1994-03-17', 'PORTIERE', 192, (SELECT id FROM squadra WHERE nome = 'Lazio'));

INSERT INTO giocatore (id, nome, cognome, data_di_nascita, ruolo, altezza, squadra_id)
VALUES (nextval('giocatore_seq'), 'Luis', 'Alberto', '1992-09-28', 'CENTROCAMPISTA', 182, (SELECT id FROM squadra WHERE nome = 'Lazio'));

INSERT INTO giocatore (id, nome, cognome, data_di_nascita, ruolo, altezza, squadra_id)
VALUES (nextval('giocatore_seq'), 'Ciro', 'Immobile', '1990-02-20', 'ATTACCANTE', 185, (SELECT id FROM squadra WHERE nome = 'Lazio'));

-- ------------------------------------------
-- Squadra: ROMA
-- ------------------------------------------
INSERT INTO giocatore (id, nome, cognome, data_di_nascita, ruolo, altezza, squadra_id)
VALUES (nextval('giocatore_seq'), 'Mile', 'Svilar', '1999-08-27', 'PORTIERE', 189, (SELECT id FROM squadra WHERE nome = 'Roma'));

INSERT INTO giocatore (id, nome, cognome, data_di_nascita, ruolo, altezza, squadra_id)
VALUES (nextval('giocatore_seq'), 'Lorenzo', 'Pellegrini', '1996-06-19', 'CENTROCAMPISTA', 186, (SELECT id FROM squadra WHERE nome = 'Roma'));

INSERT INTO giocatore (id, nome, cognome, data_di_nascita, ruolo, altezza, squadra_id)
VALUES (nextval('giocatore_seq'), 'Paulo', 'Dybala', '1993-11-15', 'ATTACCANTE', 177, (SELECT id FROM squadra WHERE nome = 'Roma'));

-- ------------------------------------------
-- Squadra: MILAN
-- ------------------------------------------
INSERT INTO giocatore (id, nome, cognome, data_di_nascita, ruolo, altezza, squadra_id)
VALUES (nextval('giocatore_seq'), 'Mike', 'Maignan', '1995-07-03', 'PORTIERE', 191, (SELECT id FROM squadra WHERE nome = 'Milan'));

INSERT INTO giocatore (id, nome, cognome, data_di_nascita, ruolo, altezza, squadra_id)
VALUES (nextval('giocatore_seq'), 'Theo', 'Hernandez', '1997-10-06', 'DIFENSORE', 184, (SELECT id FROM squadra WHERE nome = 'Milan'));

INSERT INTO giocatore (id, nome, cognome, data_di_nascita, ruolo, altezza, squadra_id)
VALUES (nextval('giocatore_seq'), 'Rafael', 'Leao', '1999-06-10', 'ATTACCANTE', 188, (SELECT id FROM squadra WHERE nome = 'Milan'));

-- ------------------------------------------
-- Squadra: INTER
-- ------------------------------------------
INSERT INTO giocatore (id, nome, cognome, data_di_nascita, ruolo, altezza, squadra_id)
VALUES (nextval('giocatore_seq'), 'Yann', 'Sommer', '1988-12-17', 'PORTIERE', 183, (SELECT id FROM squadra WHERE nome = 'Inter'));

INSERT INTO giocatore (id, nome, cognome, data_di_nascita, ruolo, altezza, squadra_id)
VALUES (nextval('giocatore_seq'), 'Nicolò', 'Barella', '1997-02-07', 'CENTROCAMPISTA', 172, (SELECT id FROM squadra WHERE nome = 'Inter'));

INSERT INTO giocatore (id, nome, cognome, data_di_nascita, ruolo, altezza, squadra_id)
VALUES (nextval('giocatore_seq'), 'Lautaro', 'Martinez', '1997-08-22', 'ATTACCANTE', 174, (SELECT id FROM squadra WHERE nome = 'Inter'));

-- ------------------------------------------
-- Squadra: JUVENTUS
-- ------------------------------------------
INSERT INTO giocatore (id, nome, cognome, data_di_nascita, ruolo, altezza, squadra_id)
VALUES (nextval('giocatore_seq'), 'Wojciech', 'Szczesny', '1990-04-18', 'PORTIERE', 195, (SELECT id FROM squadra WHERE nome = 'Juventus'));

INSERT INTO giocatore (id, nome, cognome, data_di_nascita, ruolo, altezza, squadra_id)
VALUES (nextval('giocatore_seq'), 'Adrien', 'Rabiot', '1995-04-03', 'CENTROCAMPISTA', 188, (SELECT id FROM squadra WHERE nome = 'Juventus'));

INSERT INTO giocatore (id, nome, cognome, data_di_nascita, ruolo, altezza, squadra_id)
VALUES (nextval('giocatore_seq'), 'Dusan', 'Vlahovic', '2000-01-28', 'ATTACCANTE', 190, (SELECT id FROM squadra WHERE nome = 'Juventus'));

-- ------------------------------------------
-- Squadra: NAPOLI
-- ------------------------------------------
INSERT INTO giocatore (id, nome, cognome, data_di_nascita, ruolo, altezza, squadra_id)
VALUES (nextval('giocatore_seq'), 'Alex', 'Meret', '1997-03-22', 'PORTIERE', 190, (SELECT id FROM squadra WHERE nome = 'Napoli'));

INSERT INTO giocatore (id, nome, cognome, data_di_nascita, ruolo, altezza, squadra_id)
VALUES (nextval('giocatore_seq'), 'Stanislav', 'Lobotka', '1994-11-25', 'CENTROCAMPISTA', 170, (SELECT id FROM squadra WHERE nome = 'Napoli'));

INSERT INTO giocatore (id, nome, cognome, data_di_nascita, ruolo, altezza, squadra_id)
VALUES (nextval('giocatore_seq'), 'Victor', 'Osimhen', '1998-12-29', 'ATTACCANTE', 186, (SELECT id FROM squadra WHERE nome = 'Napoli'));