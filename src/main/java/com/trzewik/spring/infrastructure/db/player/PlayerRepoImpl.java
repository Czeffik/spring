package com.trzewik.spring.infrastructure.db.player;

import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
class PlayerRepoImpl implements PlayerRepository {
    private final PlayerJpaRepository jpaRepository;

    @Override
    public void save(Player player) {
        log.info("Saving player: [{}] in repository", player);
        jpaRepository.save(new PlayerEntity(player));
    }

    @Override
    public Optional<Player> findById(String id) {
        log.info("Looking for player with id: [{}] in repository", id);
        Optional<PlayerEntity> optional = jpaRepository.findById(id);
        return optional.map(PlayerEntity::toPlayer);
    }

    @Override
    public List<Player> findAllById(Collection<String> ids) {
        log.info("Looking for players with ids: [{}] in repository", ids);
        return jpaRepository.findAllById(ids).stream()
            .map(PlayerEntity::toPlayer)
            .collect(Collectors.toList());
    }
}
