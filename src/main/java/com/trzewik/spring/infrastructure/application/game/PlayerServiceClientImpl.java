package com.trzewik.spring.infrastructure.application.game;

import com.trzewik.spring.domain.game.PlayerInGame;
import com.trzewik.spring.domain.game.PlayerServiceClient;
import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerRepository;
import com.trzewik.spring.domain.player.PlayerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerServiceClientImpl implements PlayerServiceClient {
    private final @NonNull PlayerService playerService;

    @Override
    public PlayerInGame getCroupier() {
        final PlayerService.GetCroupierCommand command = new PlayerService.GetCroupierCommand();
        final Player player = playerService.getCroupier(command);
        return new PlayerInGame(player.getId(), player.getName());
    }

    @Override
    public PlayerInGame getPlayer(final @NonNull String playerId) throws PlayerNotFoundException {
        try {
            final PlayerService.GetPlayerCommand command = new PlayerService.GetPlayerCommand(playerId);
            final Player player = playerService.get(command);
            return new PlayerInGame(player.getId(), player.getName());
        } catch (PlayerRepository.PlayerNotFoundException exception) {
            throw new PlayerNotFoundException(exception);
        }
    }
}
