package com.trzewik.spring.infrastructure.db.model;

import com.trzewik.spring.infrastructure.db.dto.PlayerDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "player")
@Setter
@Getter
@EqualsAndHashCode
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

    public PlayerEntity(PlayerDto player) {
        this.id = player.getId();
        this.name = player.getName();
    }

    public PlayerDto getPlayer() {
        return PlayerDto.from(this);
    }
}
