package minex.Game;

import minex.Arena.Lobby;
import minex.Main;
import minex.Managers.GameManager;
import minex.Party.Party;
import minex.Player.mPlayer;
import minex.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GameRecode {

    private String id;
    private int maxPlayers;
    private int minPlayers;
    private int currPlayers;
    private int lobbyCountdown = 30;
    private boolean inGame;
    private BukkitTask countdown;

    //lobby and arena here!
    private Lobby lobby;

    //Player Managment here
    //List of all players in the game
    private List<UUID> players;
    //List of all teams
    private List<Team> teams;

    //Constructors

    //Probably wont be used because object is loaded from mongo
    public GameRecode(String id, int maxPlayers, int minPlayers, int currPlayers, boolean inGame, List<UUID> players, List<Team> teams, Lobby lobby) {
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.currPlayers = currPlayers;
        this.inGame = inGame;
        this.players = players;
        this.teams = teams;
        this.lobby = lobby;
    }

    //creating a new game
    public GameRecode(String id) {
        this.id = id;
        this.maxPlayers = 25;
        this.minPlayers = 10;
        this.currPlayers = 0;
        this.inGame = false;
        this.players = new ArrayList<>();
        this.teams = new ArrayList<>();

        this.lobby = new Lobby(id);

        initTeams();
    }

    //misc functions
    public void joinGame(UUID u) {
        Player player = Bukkit.getPlayer(u);
        mPlayer mp = mPlayer.uuidPlayers.get(u);
        player.teleport(lobby.getSpawn());
        addPlayer(u);
        //mp.setCurrGame(this);

        Team t = getRandomEmptyTeam();
        if(t == null) {
            this.lobbyCountdown = 5;
        } else {
            t.addMember(u);
        }
    }

    public void joinGame(Party p) {
        Team t = getRandomEmptyTeam();
        for(UUID u : p.getMembers()) {
            Player player = Bukkit.getPlayer(u);
            mPlayer mp = mPlayer.uuidPlayers.get(u);
            player.teleport(lobby.getSpawn());
            addPlayer(u);
            //mp.setCurrGame(this);
            if(t == null) {
                this.lobbyCountdown = 5;
            } else {
                t.addMember(u);
            }
        }
    }

    public Team getRandomEmptyTeam() {
        for(Team t : teams) {
            if(!t.isHasMembers()) {
                return t;
            }
        }
        return null;
    }

    public void leaveGame() {

    }

    //send msg to everyone in gamer
    public void broadcast(String s) {
        for(UUID u : players) {
            Player p = Bukkit.getPlayer(u);
            p.sendMessage(Utils.color(s));
        }
    }

    public void initTeams() {
        String[] teamNames = {"Red", "Orange", "Yellow", "Green", "Lime", "Blue", "Cyan", "Light Blue", "Purple", "Magenta", "Pink", "White", "Light Grey", "Grey", "Black"};
        for(String s : teamNames) {
            Team t = new Team(s, getId());
            addTeam(t);
        }
    }

    public void addTeam(Team t) {
        getTeams().add(t);
    }

    public void addPlayer(UUID u) {
        getPlayers().add(u);
    }

    public void removePlayer(UUID u) {
        if(getPlayers().contains(u)) getPlayers().remove(u);
    }

    //Getters and setters
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

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getCurrPlayers() {
        return currPlayers;
    }

    public void setCurrPlayers(int currPlayers) {
        this.currPlayers = currPlayers;
    }

    public int getLobbyCountdown() {
        return lobbyCountdown;
    }

    public void setLobbyCountdown(int lobbyCountdown) {
        this.lobbyCountdown = lobbyCountdown;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public BukkitTask getCountdown() {
        return countdown;
    }

    public void setCountdown(BukkitTask countdown) {
        this.countdown = countdown;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public void setPlayers(List<UUID> players) {
        this.players = players;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    //countdown

    public void lobbyCountdown(Game game) {
        countdown = new BukkitRunnable() {
            @Override
            public void run() {
//                long minute = TimeUnit.SECONDS.toMinutes(lobbyCountdown) - (TimeUnit.SECONDS.toHours(lobbyCountdown) * 60);
//                long second = TimeUnit.SECONDS.toSeconds(lobbyCountdown) - (TimeUnit.SECONDS.toMinutes(lobbyCountdown) * 60);
//                if(lobbyCountdown == 0) {
//                    this.cancel();
//                    broadcast("&c&lMineX &7| Starting the game!");
//                    //need a method to do this randomly if they havent choosen
//                    for(UUID u : players) {
//                        Player pl = Bukkit.getPlayer(u);
//                        mPlayer mp = mPlayer.uuidPlayers.get(u);
//                        if(game.getArena().getTeamSpawn(mp.getTeamName()) == null) {
//                            for(String s : game.getArena().getSpawns()) {
//                                if(!game.getArena().claied(s)) {
//                                    game.setTeamSpawn(mp.getTeamName(), s);
//                                }
//                            }
//                        }
//                        pl.teleport(Utils.fromString(game.getTeamSpawn(mp.getTeamName())));
//                    }
//                    setInGame(true);
//                    return;
//                }
//                if(lobbyCountdown <= 5) {
//                    broadcast("&c&lMineX &7| Game starting in " + second + "s");
//                    lobbyCountdown = lobbyCountdown - 1;
//                    return;
//                } else if(lobbyCountdown % 10 == 0) {
//                    broadcast("&c&lMineX &7| Game starting in " + (minute == 0 ? "" : minute + "m ") + second + "s");
//                    lobbyCountdown = lobbyCountdown - 1;
//                    return;
//                } else if(lobbyCountdown % 20 == 0){
//                    broadcast("&c&lMineX &7| Dont forget to select your spawn with /game spawns");
//                    lobbyCountdown = lobbyCountdown - 1;
//                } else if(lobbyCountdown == 30) {
//                    broadcast("&c&lMineX &7| Searching for games to merge!!");
//                    Game current = game;
//                    Game closest = null;
//                    int check = 25;
//                    for(Game g : GameManager.games) {
//                        //Make sure the games arnt the same!
//                        if(g.getId().equals(current.getId())) continue;
//                        //Check if they addup to 25, very unlikely to be perfect
//                        if(current.getCurrPlayers() + g.getCurrPlayers() == check) {
//                            closest = g;
//                        } else if((current.getCurrPlayers() + g.getCurrPlayers()) - check > (game.getCurrPlayers() + ((closest == null) ? 0 : closest.getCurrPlayers())) - check) {
//                            //basically looks if the current game and the game in the loop remainder is less then the current game and the current closes.
//                            closest = g;
//                        }
//                    }
//                    if(closest != null) {
//                        //The closest game isnt null, so we move all players from one to the other.
//                        for(UUID u : closest.players) {
//                            Player p = Bukkit.getPlayer(u);
//                            mPlayer mp = (mPlayer.uuidPlayers.get(u) == null) ? new mPlayer(u) : mPlayer.uuidPlayers.get(u);
//                            mp.setCurrGame(current);
//                            p.teleport(current.getLobby().getSpawn());
//                            current.setCurrPlayers(current.getCurrPlayers() - 1);
//                        }
//                    }
//                    lobbyCountdown = lobbyCountdown - 1;
//                    return;
//                }
                lobbyCountdown = lobbyCountdown - 1;
            }
        }.runTaskTimer(Main.getInstance(), 0, 20);
    }

}
