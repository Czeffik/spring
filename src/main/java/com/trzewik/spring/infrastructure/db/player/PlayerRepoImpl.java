package com.trzewik.spring.infrastructure.db.player;

import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerRepository;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
class PlayerRepoImpl implements PlayerRepository {
    private PlayerJpaRepository jpaRepository;

    @Override
    public void save(Player player) {
        jpaRepository.save(new PlayerEntity(PlayerDto.from(player)));
    }

    @Override
    public Optional<Player> findById(String id) {
        Optional<PlayerEntity> optional = jpaRepository.findById(id);
        return optional.map(PlayerEntity::getPlayer).map(PlayerDto::to);
    }
}
