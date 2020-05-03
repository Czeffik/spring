package com.trzewik.spring.infrastructure.db.player;

import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerRepository;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
class PlayerRepoImpl implements PlayerRepository {
    private final PlayerJpaRepository jpaRepository;

    @Override
    public void save(Player player) {
        jpaRepository.save(new PlayerEntity(PlayerDto.from(player)));
    }

    @Override
    public Optional<Player> findById(String id) {
        Optional<PlayerEntity> optional = jpaRepository.findById(id);
        return optional.map(PlayerDto::from).map(PlayerDto::to);
    }

    @Override
    public List<Player> findAllById(Collection<String> ids) {
        return jpaRepository.findAllById(ids).stream()
            .map(PlayerDto::from)
            .map(PlayerDto::to)
            .collect(Collectors.toList());
    }
}
