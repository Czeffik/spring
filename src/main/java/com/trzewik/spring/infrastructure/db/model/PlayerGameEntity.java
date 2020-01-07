package com.trzewik.spring.infrastructure.db.model;

import com.trzewik.spring.infrastructure.db.dto.CardDto;
import com.trzewik.spring.infrastructure.db.dto.PlayerGameDto;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "player_game")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@Getter
@Setter
@EqualsAndHashCode
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
    @Column(name = "move")
    private String move;

    @NonNull
    @Type(type = "jsonb")
    @Column(name = "hand")
    private Set<CardDto> hand;

    public PlayerGameEntity(PlayerGameDto dto) {
        this.playerGameId = PlayerGameId.from(dto.getGameId(), dto.getPlayer().getId());
        this.move = dto.getMove();
        this.hand = dto.getHand();
    }

    public PlayerGameDto getPlayerGame() {
        return PlayerGameDto.from(this);
    }
}
