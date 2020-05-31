package com.trzewik.spring.infrastructure.db.game;

import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.PlayerInGame;
import com.trzewik.spring.infrastructure.db.player.PlayerEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "games_players")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AssociationOverrides({
    @AssociationOverride(
        name = "game",
        joinColumns = @JoinColumn(name = "game_id", insertable = false, updatable = false)),
    @AssociationOverride(
        name = "player",
        joinColumns = @JoinColumn(name = "player_id", insertable = false, updatable = false))})
public class PlayerInGameEntity implements Serializable {

    @EmbeddedId
    private PlayerInGameId id;

    @ManyToOne(cascade = CascadeType.ALL)
    private PlayerEntity player;

    @ManyToOne(cascade = CascadeType.ALL)
    private GameEntity game;

    @NonNull
    @Type(type = "jsonb")
    @Column(name = "hand")
    private Set<CardDto> hand;

    @Column(name = "move")
    @Enumerated(EnumType.STRING)
    private Game.Move move;

    public PlayerInGameEntity(final String gameId, final PlayerInGame playerInGame) {
        this.id = new PlayerInGameId(gameId, playerInGame.getPlayerId());
        this.hand = playerInGame.getHand().stream().map(CardDto::from).collect(Collectors.toSet());
        this.move = playerInGame.getMove();
    }

    public PlayerInGame toPlayerInGame() {
        return new PlayerInGame(
            this.player.getId(),
            this.player.getName(),
            this.hand.stream().map(CardDto::toCard).collect(Collectors.toSet()),
            this.move
        );
    }
}
