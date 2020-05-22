package com.trzewik.spring.infrastructure.db.game;

import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.infrastructure.db.player.PlayerEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "games")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class GameEntity implements Serializable {
    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Type(type = "jsonb")
    @Column(name = "deck", columnDefinition = "json")
    private DeckDto deck;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Game.Status status;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private PlayerEntity croupier;

    @MapKeyJoinColumn(name = "player_id")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "game", cascade = CascadeType.ALL)
    private Map<PlayerEntity, PlayerInGameEntity> players;

    public GameEntity(Game game) {
        this.id = game.getId();
        this.deck = DeckDto.from(game.getDeck());
        this.status = game.getStatus();
        this.croupier = new PlayerEntity(game.getCroupier());
        this.players = game.getPlayers().entrySet().stream()
            .collect(Collectors.toMap(
                e -> new PlayerEntity(e.getKey()),
                e -> new PlayerInGameEntity(id, e.getKey().getId(), e.getValue())
            ));
    }

    public Game toGame() {
        return new Game(
            this.id,
            this.deck.toDeck(),
            this.players.entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey().toPlayer(),
                e -> e.getValue().toPlayerInGame()
            )),
            this.croupier.toPlayer(),
            this.status
        );
    }
}
