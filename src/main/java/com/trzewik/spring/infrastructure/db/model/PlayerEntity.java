package com.trzewik.spring.infrastructure.db.model;

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
    private List<PlayerGameEntity> games;

    public static PlayerEntity from(Player player) {
        return new PlayerEntity(
            player.getId(),
            player.getName()
        );
    }

    public Player getPlayer() {
        return PlayerFactory.createPlayer(id, name);
    }
}
