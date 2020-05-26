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
    public Player create(@NonNull PlayerService.CreatePlayerCommand command) {
        log.info("Create player from form: [{}]", command);
        Player player = new Player(command);

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

            Player croupier = Player.createCroupier();
            playerRepo.save(croupier);
            return croupier;
        }
    }

    @Override
    public Player get(@NonNull String id) throws PlayerRepository.PlayerNotFoundException {
        log.info("Get player with id: [{}]", id);
        return playerRepo.getById(id);
    }

    @Override
    public List<Player> get(@NonNull List<String> playerIds) {
        log.info("Get players with ids: [{}]", playerIds);
        return playerRepo.findAllById(playerIds);
    }
}
