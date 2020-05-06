package com.trzewik.spring.infrastructure.db.player;

import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
class PlayerRepoImpl implements PlayerRepository {
    private final PlayerJpaRepository jpaRepository;

    @Override
    public void save(Player player) {
        log.info("Saving player: [{}] in repository", player);
        jpaRepository.save(new PlayerEntity(PlayerDto.from(player)));
    }

    @Override
    public Optional<Player> findById(String id) {
        log.info("Looking for player with id: [{}] in repository", id);
        Optional<PlayerEntity> optional = jpaRepository.findById(id);
        return optional.map(PlayerDto::from).map(PlayerDto::to);
    }

    @Override
    public List<Player> findAllById(Collection<String> ids) {
        log.info("Looking for players with ids: [{}] in repository", ids);
        return jpaRepository.findAllById(ids).stream()
            .map(PlayerDto::from)
            .map(PlayerDto::to)
            .collect(Collectors.toList());
    }
}
