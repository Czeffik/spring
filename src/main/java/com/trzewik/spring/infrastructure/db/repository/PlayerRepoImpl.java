package com.trzewik.spring.infrastructure.db.repository;

import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerRepository;
import com.trzewik.spring.infrastructure.db.dto.PlayerDto;
import com.trzewik.spring.infrastructure.db.model.PlayerEntity;
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
