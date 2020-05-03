package com.trzewik.spring.infrastructure.db.game;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class PlayerInGameId implements Serializable {
    @Column(name = "game_id")
    private String gameId;

    @Column(name = "player_id")
    private String playerId;
}
