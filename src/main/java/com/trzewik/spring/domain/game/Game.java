package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.common.Deck;
import com.trzewik.spring.domain.player.Player;

import java.util.List;

public interface Game {
    String getId();

    void addPlayer(Player player) throws GameException;

    Game startGame() throws GameException;

    Status getStatus();

    List<Result> getResults() throws GameException;

    Game auction(String playerId, Move move) throws GameException;

    Player getCurrentPlayer();

    String getCurrentPlayerId();

    Player getCroupier();

    String getCroupierId();

    List<Player> getPlayers();

    Deck getDeck();

    enum Move {
        HIT, STAND
    }

    enum Status {
        NOT_STARTED, STARTED, ENDED
    }
}
