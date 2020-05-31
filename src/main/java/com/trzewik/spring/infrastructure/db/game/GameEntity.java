package com.trzewik.spring.infrastructure.db.game;

import com.trzewik.spring.domain.game.Game;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "games")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class GameEntity implements Serializable {
    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Type(type = "jsonb")
    @Column(name = "deck", columnDefinition = "jsonb")
    private DeckDto deck;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Game.Status status;

    @Column(name = "croupier_id")
    private String croupierId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumns({
        @JoinColumn(name = "id", insertable = false, updatable = false),
        @JoinColumn(name = "croupier_id", insertable = false, updatable = false)
    })
    private PlayerInGameEntity croupier;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "game", cascade = CascadeType.ALL)
    private List<PlayerInGameEntity> players;

    public GameEntity(final Game game) {
        this.id = game.getId();
        this.deck = DeckDto.from(game.getDeck());
        this.status = game.getStatus();
        this.croupierId = game.getCroupierId();
        this.players = game.getPlayers().stream()
            .map(p -> new PlayerInGameEntity(game.getId(), p))
            .collect(Collectors.toList());
    }

    public Game toGame() {
        return new Game(
            this.id,
            this.deck.toDeck(),
            this.players.stream()
                .map(PlayerInGameEntity::toPlayerInGame)
                .collect(Collectors.toList()),
            this.croupierId,
            this.status
        );
    }
}
