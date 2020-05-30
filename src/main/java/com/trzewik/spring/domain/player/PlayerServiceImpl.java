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
    public Player getCroupier(@NonNull final GetCroupierCommand getCroupierCommand) {
        log.info("Received get croupier command: [{}]", getCroupierCommand);
        final Player croupier = Player.createCroupier();
        try {
            return get(new GetPlayerCommand(croupier.getId()));
        } catch (PlayerRepository.PlayerNotFoundException ex) {
            log.error("Croupier not found in repository.");
            playerRepo.save(croupier);
            return croupier;
        }
    }

    @Override
    public Player get(@NonNull final GetPlayerCommand getPlayerCommand)
        throws PlayerRepository.PlayerNotFoundException {
        log.info("Received get player command: [{}]", getPlayerCommand);
        return playerRepo.getById(getPlayerCommand.getPlayerId());
    }

    @Override
    public List<Player> get(@NonNull final GetPlayersCommand getPlayersCommand) {
        log.info("Received get players command: [{}]", getPlayersCommand);
        return playerRepo.findAllById(getPlayersCommand.getPlayerIds());
    }
}
