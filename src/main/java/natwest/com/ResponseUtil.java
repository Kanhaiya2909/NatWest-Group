package natwest.com;

import java.util.List;

import java.util.UUID;

class ResponseUtil {


    static String gameFound(Game game) {
        StringBuilder response = new StringBuilder("Game found"); //Async, i.e. not thread-safe, but fast and efficient

        response.append("\nPlayers: ");
        List<Player> players = game.getPlayers();
        for (Player player : players) {
            response.append(player.getPlayerName()).append(" ");
        }

        game.setResultOfGameIfPossible();

        if (game.getResultOfGame() != null) {
            response.append("\n").append(game.getResultOfGame());
        } else {
            for (Player player : players) {
                response.append(hasMadeMove(player));
            }
        }
        return response.toString();
    }

    static String gameNotFound() {
        return "Game not found";
    }

    static String gameCreated(UUID uuid) {
        return (String.format("Game created with uuid: %s\nShare the UUID with your opponent", uuid.toString()));
    }

    static String gameIsFull() {
        return "Game is full.";
    }

    static String gameJoined(UUID uuid, Game game) {
        StringBuilder response = new StringBuilder();
        response.append(String.format("Joined Game with uuid: %s", uuid));
        response.append("\nCurrent players: ");
        for (String name : game.getPlayerNames()) {
            response.append(name).append(" ");
        }
        response.append("\n");
        return response.toString();
    }

    static String missingName() {
        return "You need to supply your name";
    }

    static String playerAlreadyInGame() {
        return "Player is already in game.";
    }

    private static String hasMadeMove(Player player) {
        if (player.getPlayerMove() != null) {
            return String.format("\n%s has made their move", player.getPlayerName());
        }
        return String.format("\n%s has not made their move", player.getPlayerName());
    }

    static String notValidMove(String move) {
        if (move == null)
            return "You must supply your move";
        else
            return "Your move must be either ROCK, PAPER or SCISSOR";
    }

    static String moveAlreadyMade(String name) {
        return String.format("Player %s has already made a move", name);
    }

    static String moveAdded(String move) {
        return String.format("Move %s added", move);
    }

    static String winsOver(Player playerA, Player playerB) {
        return String.format("%s wins, with %s over %s's %s!",
                playerA.getPlayerName(),
                playerA.getPlayerMove(),
                playerB.getPlayerName(),
                playerB.getPlayerMove());
    }

    static String draw() {
        return "Draw!";
    }
}
