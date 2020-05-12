package com.trzewik.spring.domain.game;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.trzewik.spring.domain.player.Player;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Getter
@EqualsAndHashCode
public class Game {
    private final @NonNull String id;
    private final @NonNull Deck deck;
    private final @NonNull Map<Player, PlayerInGame> players;
    private final @NonNull Player croupier;
    private final @NonNull Status status;

    public Game(@NonNull String id,
                @NonNull Deck deck,
                @NonNull Map<Player, PlayerInGame> players,
                @NonNull Player croupier,
                @NonNull Status status
    ) {
        this.id = id;
        this.deck = deck;
        this.players = ImmutableMap.copyOf(players);
        this.croupier = croupier;
        this.status = status;
    }

    Game(@NonNull Player croupier) {
        this(
            UUID.randomUUID().toString(),
            new Deck(),
            ImmutableMap.<Player, PlayerInGame>builder()
                .put(croupier, new PlayerInGame())
                .build(),
            croupier,
            Status.NOT_STARTED
        );
    }

    private Game(@NonNull Game game) {
        this(game, game.getStatus());
    }

    private Game(@NonNull Game game, @NonNull Status status) {
        this(
            game.getId(),
            game.getDeck(),
            game.getPlayers(),
            game.getCroupier(),
            status
        );
    }

    private Game(@NonNull Game game, @NonNull Map<Player, PlayerInGame> players, @NonNull Status status) {
        this(
            game.getId(),
            game.getDeck(),
            players,
            game.getCroupier(),
            status
        );
    }

    public Player getCurrentPlayer() {
        return players.keySet().stream()
            .filter(p -> !p.equals(croupier))
            .filter(p -> !getPlayerInGame(p).isLooser())
            .filter(p -> Move.isNotStand(getPlayerInGame(p).getMove()))
            .findFirst()
            .orElse(null);
    }

    Game addPlayer(@NonNull Player player) throws Exception {
        validatePlayerAddition(player);

        return new Game(this, createPlayersWith(player), this.getStatus());
    }

    private Map<Player, PlayerInGame> createPlayersWith(Player player) {
        return ImmutableMap.<Player, PlayerInGame>builder()
            .putAll(this.players)
            .put(player, new PlayerInGame())
            .build();
    }

    Game start() throws Exception {
        validateStart();

        return new Game(this, dealCards(), Status.STARTED);
    }

    private Map<Player, PlayerInGame> dealCards() {
        return players.entrySet().stream()
            .map(this::addCard)
            .map(this::addCard)
            .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<Player, PlayerInGame> addCard(Map.Entry<Player, PlayerInGame> entry) {
        return Maps.immutableEntry(entry.getKey(), entry.getValue().addCard(takeCard()));
    }

    Game auction(@NonNull String playerId, @NonNull Move move) throws Exception {
        validateAuction(playerId);

        return makeMove(move);
    }

    private Game makeMove(Move move) {
        Player currentPlayer = getCurrentPlayer();
        PlayerInGame playerAfterMove = makeMove(getPlayerInGame(currentPlayer), move);
        return new Game(this, updatePlayerInGame(currentPlayer, playerAfterMove), this.getStatus());
    }

    private PlayerInGame makeMove(PlayerInGame currentPlayer, Move move) {
        PlayerInGame playerWithChangedMove = currentPlayer.changeMove(move);
        if (Move.isHit(move)) {
            return playerWithChangedMove.addCard(takeCard());
        }
        return playerWithChangedMove;
    }

    private Map<Player, PlayerInGame> updatePlayerInGame(Player player, PlayerInGame playerInGame) {
        return ImmutableMap.<Player, PlayerInGame>builder()
            .putAll(putPlayer(player, playerInGame))
            .build();
    }

    private Map<Player, PlayerInGame> putPlayer(Player player, PlayerInGame playerInGame) {
        Map<Player, PlayerInGame> copy = new LinkedHashMap<>(this.players);
        copy.put(player, playerInGame);
        return copy;
    }

    Game end() {
        if (getCurrentPlayer() == null && status.started()) {
            PlayerInGame croupierAfterDrawing = croupierDrawCards(getCroupierInGame()).changeMove(Move.STAND);

            return new Game(this, updatePlayerInGame(croupier, croupierAfterDrawing), Status.ENDED);
        }
        return new Game(this);
    }

    private PlayerInGame croupierDrawCards(PlayerInGame croupier) {
        if (croupier.handValue() < 17) {
            return croupierDrawCards(croupier.addCard(takeCard()));
        }
        return croupier;
    }

    private Card takeCard() {
        try {
            return deck.take();
        } catch (Deck.Exception ex) {
            throw new NoCardsException(ex);
        }
    }

    private void validatePlayerAddition(Player player) throws Exception {
        if (status.isStarted()) {
            throw new Exception("Game started, can not add new player");
        }
        if (players.containsKey(player)) {
            throw new Exception(String.format("Player: [%s] already added to game!", player));
        }
    }

    private void validateStart() throws Exception {
        if (status.isStarted()) {
            throw new Exception("Game started, can not start again");
        }
        if (players.size() <= 1) {
            throw new Exception("Please add at least one player before start.");
        }
    }

    private void validateAuction(String playerId) throws Exception {
        if (!status.isStarted()) {
            throw new Exception("Game NOT started, please start game before auction");
        }
        if (status.isEnded()) {
            throw new Exception("Game finished. Now you can check results!");
        }
        if (isNotPlayerTurn(playerId)) {
            throw new Exception(String.format("Waiting for move from player: [%s] instead of: [%s]",
                getCurrentPlayer().getId(), playerId));
        }
    }

    private PlayerInGame getCroupierInGame() {
        return getPlayerInGame(croupier);
    }

    private PlayerInGame getPlayerInGame(Player player) {
        return players.get(player);
    }

    private boolean isNotPlayerTurn(String playerId) {
        return !playerId.equals(getCurrentPlayer().getId());
    }

    public enum Move {
        HIT, STAND;

        static boolean isNotStand(Move move) {
            return !STAND.equals(move);
        }

        static boolean isHit(Move move) {
            return HIT.equals(move);
        }
    }

    public enum Status {
        NOT_STARTED, STARTED, ENDED;

        boolean isEnded() {
            return this.equals(ENDED);
        }

        boolean isStarted() {
            return !this.equals(NOT_STARTED);
        }

        boolean started() {
            return this.equals(STARTED);
        }
    }

    public static class Exception extends java.lang.Exception {
        public Exception(String msg) {
            super(msg);
        }
    }

    public static class NoCardsException extends RuntimeException {
        public NoCardsException(Deck.Exception exception) {
            super(exception);
        }
    }
}
