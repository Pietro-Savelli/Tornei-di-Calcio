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
VALUES (nextval('partita_seq'), 'Stadio Olimpico, Roma', 3, 2, '2026-05-15 20:45:00', 0, NULL,
        (SELECT id FROM torneo WHERE nome = 'Serie A'),
        (SELECT id FROM squadra WHERE nome = 'Lazio'),
        (SELECT id FROM squadra WHERE nome = 'Roma'));

INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'San Siro, Milano', 1, 1, '2026-05-16 18:00:00', 0, NULL,
        (SELECT id FROM torneo WHERE nome = 'Serie A'),
        (SELECT id FROM squadra WHERE nome = 'Milan'),
        (SELECT id FROM squadra WHERE nome = 'Inter'));

INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'Allianz Stadium, Torino', 2, 0, '2026-05-16 20:45:00', 0, NULL,
        (SELECT id FROM torneo WHERE nome = 'Serie A'),
        (SELECT id FROM squadra WHERE nome = 'Juventus'),
        (SELECT id FROM squadra WHERE nome = 'Napoli'));

INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'Stadio Olimpico, Roma', 0, 1, '2026-05-20 20:45:00', 0, NULL,
        (SELECT id FROM torneo WHERE nome = 'Serie A'),
        (SELECT id FROM squadra WHERE nome = 'Roma'),
        (SELECT id FROM squadra WHERE nome = 'Milan'));

INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'San Siro, Milano', 2, 2, '2026-05-21 15:00:00', 0, NULL,
        (SELECT id FROM torneo WHERE nome = 'Serie A'),
        (SELECT id FROM squadra WHERE nome = 'Inter'),
        (SELECT id FROM squadra WHERE nome = 'Lazio'));

-- ------------------------------------------
-- Torneo 2: CHAMPIONS LEAGUE (3 partite)
-- ------------------------------------------
INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'San Siro, Milano', 2, 1, '2026-05-25 21:00:00', 0, NULL,
        (SELECT id FROM torneo WHERE nome = 'Champions League'),
        (SELECT id FROM squadra WHERE nome = 'Milan'),
        (SELECT id FROM squadra WHERE nome = 'Napoli'));

INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'Stadio Diego Armando Maradona, Napoli', 1, 1, '2026-05-26 21:00:00', 0, NULL,
        (SELECT id FROM torneo WHERE nome = 'Champions League'),
        (SELECT id FROM squadra WHERE nome = 'Napoli'),
        (SELECT id FROM squadra WHERE nome = 'Inter'));

INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'San Siro, Milano', 0, 0, '2026-05-30 21:00:00', 0, NULL,
        (SELECT id FROM torneo WHERE nome = 'Champions League'),
        (SELECT id FROM squadra WHERE nome = 'Inter'),
        (SELECT id FROM squadra WHERE nome = 'Milan'));

-- ------------------------------------------
-- Torneo 3: COPPA ITALIA (2 partite)
-- ------------------------------------------
INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'Stadio Olimpico, Roma', 3, 0, '2026-06-02 21:00:00', 0, NULL,
        (SELECT id FROM torneo WHERE nome = 'Coppa Italia'),
        (SELECT id FROM squadra WHERE nome = 'Lazio'),
        (SELECT id FROM squadra WHERE nome = 'Juventus'));

INSERT INTO partita (id, luogo, goals_home, goals_away, data_ora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id)
VALUES (nextval('partita_seq'), 'Allianz Stadium, Torino', 1, 2, '2026-06-05 21:00:00', 0, NULL,
        (SELECT id FROM torneo WHERE nome = 'Coppa Italia'),
        (SELECT id FROM squadra WHERE nome = 'Juventus'),
        (SELECT id FROM squadra WHERE nome = 'Roma'));