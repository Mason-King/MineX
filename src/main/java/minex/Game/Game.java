package minex.Game;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {

    public static List<Game> allGames = new ArrayList<>();

    private int maxGames = 10;
    private String id;
    private int maxPlayers;
    private int curPlayers;
    private Arena arena;
    private GameState state;
    private GameType type;
    private Arena lobby;

    public Game(String id, int maxPlayers, int currPlayers, Arena arena, GameState state, GameType type) {
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.curPlayers = currPlayers;
        this.arena = arena;
        this.state = state;
        this.type = type;
    }

    public Game(String id) {
        this.id = id;
        this.state = GameState.IN_LOBBY;

        this.arena = new Arena(id, ArenaType.LOBBY);
        arena.generate();

        this.lobby = new Arena(id, ArenaType.GAME);
        lobby.generate();

        allGames.add(this);
    }

    public void teleport(Player p) {
        p.teleport(arena.getSpawn());
    }


    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public static List<Game> getAllGames() {
        return allGames;
    }

    public static void setAllGames(List<Game> allGames) {
        Game.allGames = allGames;
    }

    public int getCurPlayers() {
        return curPlayers;
    }

    public void setCurPlayers(int curPlayers) {
        this.curPlayers = curPlayers;
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public GameType getType() {
        return type;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public static Game getGame(String id) {
        for(Game game: allGames) {
            if(game.getId() == id) {
                return game;
            }
        }
        return null;
    }

}
