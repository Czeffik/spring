package com.trzewik.spring.infrastructure.db.player;

import com.trzewik.spring.infrastructure.db.game.PlayerInGameEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "players")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class PlayerEntity implements Serializable {
    @Id
    @NonNull
    @Column(name = "id")
    private String id;

    @NonNull
    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "player", cascade = CascadeType.ALL)
    private Set<PlayerInGameEntity> games;

    public PlayerEntity(PlayerDto player) {
        this.id = player.getId();
        this.name = player.getName();
    }
}
