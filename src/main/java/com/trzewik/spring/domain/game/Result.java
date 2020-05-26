package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.Player;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.Set;

@Getter
@EqualsAndHashCode
public class Result {
    private final int place;
    private final @NonNull String name;
    private final @NonNull Set<Card> hand;
    private final int handValue;

    Result(final int place, @NonNull final Player player, @NonNull final PlayerInGame playerInGame) {
        this.place = place;
        this.name = player.getName();
        this.hand = playerInGame.getHand();
        this.handValue = playerInGame.handValue();
    }

    public static class Exception extends java.lang.Exception {
        public Exception(String msg) {
            super(msg);
        }
    }
}
