package com.trzewik.spring.domain.game;

public interface PlayerServiceClient {
    PlayerInGame getCroupier();

    PlayerInGame getPlayer(String playerId) throws PlayerNotFoundException;

    class PlayerNotFoundException extends Exception {
        public PlayerNotFoundException(Exception exception) {
            super(exception);
        }
    }
}
