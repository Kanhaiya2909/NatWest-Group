package natwest.com;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Map;
import java.util.stream.*;


//https://spring.io/guides/gs/rest-service/

@RestController // Annotated with @Controller and @ResponseBody, where @Controller is a specialization of @Component
public class GameController {

    private static List<Game> games = new ArrayList<>();

    /**
     * Uses stream, more expressive and terse than the old school for loop
     * More functional, stronger affinity with functions, encourage less mutability, more scalable
     * Worse performance than looping through an array though
     * @param uuid Unique Identifier of the game being searched for
     * @return The first found game if it exists, else null
     */
    private Game getGame(UUID uuid) {
        List<Game> gameList = games.stream()
                .filter(game -> game.getUUID().equals(uuid))
                .collect(Collectors.toList());
        return gameList.size() == 1 ? gameList.get(0) : null;
    }


    @GetMapping("/games/{uuid}") // Return game with given uuid
    public String getGameResult(@PathVariable("uuid") UUID uuid) {
        Game game = getGame(uuid);
        if (game != null) {
            return ResponseUtil.gameFound(game);
        } else {
            return ResponseUtil.gameNotFound();
        }
    }

    @PostMapping("/games") // Create new game, return uuid
    public String createGame(@RequestBody Map<String, String> body) {
        Game newGame = new Game();
        UUID uuid = newGame.getUUID();
        String playerName = body.get("name");

        if (playerName != null && !playerName.isEmpty()) {
            newGame.addPlayer(playerName);
            games.add(newGame);
            return ResponseUtil.gameCreated(uuid);
        }
        return ResponseUtil.missingName();
    }

    @PostMapping("/games/{uuid}/join") //Add player to game
    public String addPlayer(@PathVariable("uuid") UUID uuid, @RequestBody Map<String, String> body) {
        Game game = getGame(uuid);
        String playerName = body.get("name");

        if (playerName == null)
            return ResponseUtil.missingName();
        if (game == null)
            return ResponseUtil.gameNotFound();
        if (game.playerIsInGame(playerName))
            return ResponseUtil.playerAlreadyInGame();
        if (game.gameIsFull())
            return ResponseUtil.gameIsFull();

        game.addPlayer(playerName);

        return ResponseUtil.gameJoined(uuid, game);
    }

    @PostMapping("/games/{uuid}/move") //Add move to game
    public String addPlayerMove(@PathVariable("uuid") UUID uuid, @RequestBody Map<String, String> body) {
        Game game = getGame(uuid);
        if (game == null)
            return ResponseUtil.gameNotFound();

        String playerName = body.get("name");
        if (playerName == null)
            return ResponseUtil.missingName();
        if (!game.playerIsInGame(playerName) && game.gameIsFull())
            return ResponseUtil.gameIsFull();

        String playerMove = body.get("move");
        if (!Move.validMove(playerMove))
            return ResponseUtil.notValidMove(playerMove);
        if (game.playerIsInGame(playerName) && game.getPlayer(playerName).playerHasMadeMove())
            return ResponseUtil.moveAlreadyMade(playerName);

        StringBuilder response = new StringBuilder();

        if (game.gameHasRoom() && !game.playerIsInGame(playerName)) {
            game.addPlayer(playerName);
            response.append(ResponseUtil.gameJoined(uuid, game));
        }

        game.addPlayerMove(playerName, playerMove);
        game.setResultOfGameIfPossible();
        response.append(ResponseUtil.moveAdded(game.getPlayer(playerName).getPlayerMove().toString()));
        return response.toString();
    }
}
