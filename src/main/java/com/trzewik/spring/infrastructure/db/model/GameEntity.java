package com.trzewik.spring.infrastructure.db.model;

import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.infrastructure.db.dto.DeckDto;
import com.trzewik.spring.infrastructure.db.dto.GameDto;
import com.trzewik.spring.infrastructure.db.dto.PlayerGameDto;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "game")
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class GameEntity implements Serializable {
    @Id
    @Column(name = "id", length = 36)
    private String id;

    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "game"
    )
    private List<PlayerGameEntity> players;

    @Type(type = "jsonb")
    @Column(name = "deck", columnDefinition = "json")
    private DeckDto deck;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Game.Status status;

    @Column(name = "current_player_id", length = 36)
    private String currentPlayerId;

    @Column(name = "croupier_id", length = 36)
    private String croupierId;

    public GameEntity(GameDto dto) {
        this.id = dto.getId();
        this.players = createPlayerGames(dto.getPlayers());
        this.deck = dto.getDeck();
        this.status = dto.getStatus();
        this.currentPlayerId = dto.getCurrentPlayerId();
        this.croupierId = dto.getCroupierId();
    }

    public GameDto getGame() {
        return GameDto.from(this);
    }

    private List<PlayerGameEntity> createPlayerGames(List<PlayerGameDto> dtos) {
        return dtos.stream()
            .map(PlayerGameEntity::new)
            .collect(Collectors.toList());
    }
}
