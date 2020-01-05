package com.trzewik.spring.infrastructure.db.model;

import com.trzewik.spring.domain.deck.Deck;
import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerFactory;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "player")
@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class PlayerEntity implements Serializable {
    @Id
    @NonNull
    @Column(name = "id")
    private String id;

    @NonNull
    @Column(name = "name")
    private String name;

    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "player"
    )
    private List<PlayerGameEntity> games = new ArrayList<>();

    public static PlayerEntity from(Player player) {
        return new PlayerEntity(
            player.getId(),
            player.getName()
        );
    }

    public Player getPlayer() {
        return PlayerFactory.createPlayer(id, name);
    }

    public Player getPlayer(String gameId) {
        return PlayerFactory.createPlayer(id, name, getHand(gameId), getMove(gameId));
    }

    private Game.Move getMove(String gameId) {
        return getGame(gameId).getMove();
    }

    private Set<Deck.Card> getHand(String gameId) {
        return getGame(gameId).getHand();
    }

    private PlayerGameEntity getGame(String gameId) {
        return games.stream()
            .filter(game -> game.getGameId().equals(gameId))
            .findFirst()
            .orElseThrow(() -> new PlayerEntityException(gameId, id));
    }

    static class PlayerEntityException extends RuntimeException {
        PlayerEntityException(String gameId, String playerId) {
            super(String.format("Not found game with id: [%s] for player with id: [%s]", gameId, playerId));
        }
    }
}
