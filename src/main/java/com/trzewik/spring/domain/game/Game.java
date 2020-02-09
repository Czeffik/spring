package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.common.Deck;

import java.util.List;
import java.util.Set;

public interface Game {
    String getId();

    void addPlayer(String playerId) throws GameException;

    Game startGame() throws GameException;

    Status getStatus();

    List<Result> getResults() throws GameException;

    Game auction(String playerId, Move move) throws GameException;

    String getCurrentPlayerId();

    String getCroupierId();

    Set<GamePlayer> getPlayers();

    Deck getDeck();

    GamePlayer getCurrentPlayer();

    GamePlayer getCroupier();

    enum Move {
        HIT, STAND
    }

    enum Status {
        NOT_STARTED, STARTED, ENDED
    }
}
