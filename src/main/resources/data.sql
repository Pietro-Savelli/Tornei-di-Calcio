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