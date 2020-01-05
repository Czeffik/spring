package com.trzewik.spring.infrastructure.db.model;

import com.trzewik.spring.domain.deck.Deck;
import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerFactory;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "player_game")
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
@NoArgsConstructor
public class PlayerGameEntity {

    @NonNull
    @EmbeddedId
    private PlayerGameId playerGameId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id", nullable = false, insertable = false, updatable = false)
    private PlayerEntity player;

    @Getter
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", nullable = false, insertable = false, updatable = false)
    private GameEntity game;

    @Getter
    @NonNull
    @Column(name = "move")
    @Enumerated(EnumType.STRING)
    private Game.Move move;

    @NonNull
    @Column(name = "hand")
    private String hand;

    static List<PlayerGameEntity> from(Game game) {
        return game.getPlayers().stream()
            .map(player -> PlayerGameEntity.from(game.getId(), player))
            .collect(Collectors.toList());
    }

    public static PlayerGameEntity from(String gameId, Player player) {
        return new PlayerGameEntity(
            PlayerGameId.from(gameId, player.getId()),
            player.getMove(),
            "{}"    // player.getHand().toString()   todo
        );
    }

    public Player getPlayer() {
        return PlayerFactory.createPlayer(
            getPlayerId(),
            getName(),
            getHand(),
            getMove()
        );
    }

    String getPlayerId() {
        return player.getId();
    }

    String getGameId() {
        return game.getId();
    }

    String getName() {
        return player.getName();
    }

    Set<Deck.Card> getHand() {
        //todo replace with mapping to hand from db
        return new HashSet<>();
    }
}
