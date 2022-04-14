package minex.Game;

import minex.Arena.Arena;

import java.util.ArrayList;
import java.util.List;

public class Game {

    public static List<Game> games = new ArrayList<>();

    private String id;
    private int maxPlayers;
    private int currPlayers;

    public Game(String id, int maxPlayers, int currPlayers) {
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.currPlayers = currPlayers;

        games.add(this);
    }

    public Game(String id) {
        this.id = id;

        games.add(this);
        new Arena(id);
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

    public int getCurrPlayers() {
        return currPlayers;
    }

    public void setCurrPlayers(int currPlayers) {
        this.currPlayers = currPlayers;
    }

    public static Game getGame(String s) {
        for(Game g :  games) {
            if(g.getId() == s) {
                return g;
            }
        }
        return null;
    }

}
