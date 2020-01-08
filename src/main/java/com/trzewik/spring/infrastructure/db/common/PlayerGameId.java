package com.trzewik.spring.infrastructure.db.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerGameId implements Serializable {
    @Column(name = "game_id")
    private String gameId;

    @Column(name = "player_id")
    private String playerId;

    public static PlayerGameId from(String gameId, String playerId) {
        return new PlayerGameId(gameId, playerId);
    }
}
