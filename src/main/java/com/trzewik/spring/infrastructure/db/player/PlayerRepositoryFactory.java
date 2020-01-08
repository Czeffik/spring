package com.trzewik.spring.infrastructure.db.player;

import com.trzewik.spring.domain.player.PlayerRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerRepositoryFactory {
    public static PlayerRepository create(PlayerJpaRepository jpaRepository) {
        return new PlayerRepoImpl(jpaRepository);
    }
}
