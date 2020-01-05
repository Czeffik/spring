package com.trzewik.spring.infrastructure.db.model;

import com.trzewik.spring.domain.deck.Deck;
import com.trzewik.spring.domain.deck.DeckFactory;
import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.GameFactory;
import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerFactory;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "game")
@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class GameEntity implements Serializable {
    @Id
    @Column(name = "id", length = 36)
    private String id;

    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "game"
    )
    private List<PlayerGameEntity> players = new ArrayList<>();

    @Column(name = "deck")
    private String deck;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Game.Status status;

    @Column(name = "current_player_id", length = 36)
    private String currentPlayerId;

    @Column(name = "croupier_id", length = 36)
    private String croupierId;

    public static GameEntity from(Game game) {
        return new GameEntity(
            game.getId(),
            PlayerGameEntity.from(game),
            "{}",   //game.getDeck().toString(), todo
            game.getStatus(),
            game.getCurrentPlayer() == null ? null : game.getCurrentPlayer().getId(),
            game.getCroupier().getId()
        );
    }

    public Game getGame() {
        return GameFactory.createGame(
            id,
            getPlayers(),
            getCroupier(),
            getDeck(),
            getStatus(),
            getCurrentPlayer()
        );
    }

    private Deck getDeck() {
        //todo make it return real deck from db
        return DeckFactory.createDeck();
    }

    private List<Player> getPlayers() {
        return players.stream()
            .map(player -> getPlayer(player.getPlayerId()))
            .filter(player -> !player.getId().equals(croupierId))
            .collect(Collectors.toList());
    }

    private Player getCroupier() {
        return getPlayer(croupierId);
    }

    private Player getCurrentPlayer() {
        return currentPlayerId == null ? null : getPlayer(currentPlayerId);
    }

    private Player getPlayer(String playerId) {
        return PlayerFactory.createPlayer(
            playerId,
            getPlayerName(playerId),
            getPlayerHand(playerId),
            getPlayerMove(playerId)
        );
    }

    private String getPlayerName(String playerId) {
        return getGamePlayer(playerId).getName();
    }

    private Game.Move getPlayerMove(String playerId) {
        return getGamePlayer(playerId).getMove();
    }

    private Set<Deck.Card> getPlayerHand(String playerId) {
        return getGamePlayer(playerId).getHand();
    }

    private PlayerGameEntity getGamePlayer(String playerId) {
        return players.stream()
            .filter(player -> player.getPlayerId().equals(playerId))
            .findFirst()
            .orElseThrow(() -> new GameEntityException(playerId, id));
    }

    static class GameEntityException extends RuntimeException {
        GameEntityException(String playerId, String gameId) {
            super(String.format("Player with id: [%s] not found in game with id: [%s]", playerId, gameId));
        }
    }
}
