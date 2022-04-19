package minex.Game;

import it.unimi.dsi.fastutil.Hash;

import java.util.HashMap;
import java.util.Map;

public class GameManager {

    public static Map<String, Game> games = new HashMap<>();

    public void addGame(String id, Game game) {
        games.put(id, game);
    }

    public static Game getGame(String id) {
        return games.get(id);
    }

    public static boolean exists(String id) {
        if(games.containsKey(id)) return true;
        return false;
    }

}
