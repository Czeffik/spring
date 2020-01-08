CREATE TABLE game
(
    id                VARCHAR(36) PRIMARY KEY,
    deck              json,
    status            TEXT,
    current_player_id VARCHAR(36),
    croupier_id       VARCHAR(36)
);

CREATE TABLE player
(
    id   VARCHAR(36) PRIMARY KEY,
    name TEXT
);

CREATE TABLE player_game
(
    game_id   VARCHAR(36) REFERENCES game (id) ON UPDATE CASCADE ON DELETE CASCADE,
    player_id VARCHAR(36) REFERENCES player (id) ON UPDATE CASCADE,
    hand json,
    move TEXT,
    CONSTRAINT game_player_pkey PRIMARY KEY (game_id, player_id)
);
