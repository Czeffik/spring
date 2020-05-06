CREATE TABLE games
(
    id                VARCHAR(36) PRIMARY KEY,
    deck              json,
    status            TEXT,
    current_player_id VARCHAR(36),
    croupier_id       VARCHAR(36)
);

CREATE TABLE players
(
    id   VARCHAR(36) PRIMARY KEY,
    name TEXT
);

CREATE TABLE games_players
(
    game_id   VARCHAR(36) REFERENCES games (id) ON UPDATE CASCADE ON DELETE CASCADE,
    player_id VARCHAR(36) REFERENCES players (id) ON UPDATE CASCADE,
    hand json,
    move TEXT,
    CONSTRAINT game_player_pkey PRIMARY KEY (game_id, player_id)
);
INSERT INTO players (id, name) VALUES ('CROUPIER-ID', 'CROUPIER');
