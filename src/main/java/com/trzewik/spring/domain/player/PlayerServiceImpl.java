package com.trzewik.spring.domain.player;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
class PlayerServiceImpl implements PlayerService {
    private final @NonNull PlayerRepository playerRepo;

    @Override
    public Player create(@NonNull final PlayerService.CreatePlayerCommand createPlayerCommand) {
        log.info("Received create player command: [{}]", createPlayerCommand);
        final Player player = new Player(createPlayerCommand);

        log.info("Created player: [{}]", player);
        playerRepo.save(player);

        return player;
    }

    @Override
    public Player getCroupier() {
        try {
            return get(Player.CROUPIER_ID);
        } catch (PlayerRepository.PlayerNotFoundException ex) {
            log.error("Croupier not found in repository.");

            final Player croupier = Player.createCroupier();
            playerRepo.save(croupier);
            return croupier;
        }
    }

    @Override
    public Player get(@NonNull final String id) throws PlayerRepository.PlayerNotFoundException {
        log.info("Get player with id: [{}]", id);
        return playerRepo.getById(id);
    }

    @Override
    public List<Player> get(@NonNull final List<String> playerIds) {
        log.info("Get players with ids: [{}]", playerIds);
        return playerRepo.findAllById(playerIds);
    }
}
