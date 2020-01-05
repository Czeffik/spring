package com.trzewik.spring.infrastructure.db.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PlayerGameId implements Serializable {
    @Column(name = "game_id")
    private String gameId;

    @Column(name = "player_id")
    private String playerId;

    public static PlayerGameId from(String gameId, String playerId) {
        return new PlayerGameId(gameId, playerId);
    }
}
