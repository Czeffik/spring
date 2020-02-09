package com.trzewik.spring.infrastructure.db.game;

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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Set;
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
    private String status;

    @Column(name = "current_player_id", length = 36)
    private String currentPlayerId;

    @Column(name = "croupier_id", length = 36)
    private String croupierId;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "game", cascade = CascadeType.ALL)
    private Set<GamePlayerEntity> players;

    public GameEntity(GameDto dto) {
        this.id = dto.getId();
        this.deck = dto.getDeck();
        this.status = dto.getStatus();
        this.currentPlayerId = dto.getCurrentPlayerId();
        this.croupierId = dto.getCroupierId();
        this.players = dto.getPlayers().stream().map(p -> new GamePlayerEntity(id, p)).collect(Collectors.toSet());
    }
}
