    package minex.Game;

    import minex.Arena.Arena;
    import minex.Arena.Lobby;
    import minex.Main;
    import minex.Managers.GameManager;
    import minex.Party.Party;
    import minex.Player.mPlayer;
    import minex.Utils.Utils;
    import org.bukkit.Bukkit;
    import org.bukkit.Location;
    import org.bukkit.entity.Player;
    import org.bukkit.scheduler.BukkitRunnable;
    import scala.collection.mutable.HashMap;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.UUID;
    import java.util.concurrent.TimeUnit;

    public class Game {

        private String id;
        private int maxPlayers = 25;
        private int teamSize;
        private List<UUID> players = new ArrayList<>();
        private List<Team> teams = new ArrayList<>();
        private int currPlayers;
        private Arena arena;
        private int lobbyCountdown = 120;
        private boolean inGame;
        private Lobby lobby;


        public Game(String id, int maxPlayers, int currPlayers) {
            this.id = id;
            this.maxPlayers = maxPlayers;
            this.currPlayers = currPlayers;

            loadTeams();
        }

        public Game(String id) {
           this.id = id;
           this.arena = new Arena(id);
           this.lobby = new Lobby(id);
           this.currPlayers = 0;

           loadTeams();
        }

        public void addPlayer(UUID u) {
            this.players.add(u);
            if(currPlayers == 0) {
                lobbyCountdown(this);
            }
            currPlayers = currPlayers + 1;
            Bukkit.getPlayer(u).teleport(lobby.getSpawn());
        }

        public void addParty(Party p) {
            for(UUID id : p.getMembers()) {
                addPlayer(id);
                Bukkit.getPlayer(id).teleport(lobby.getSpawn());
            }
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

        public void loadTeams() {
            String[] teamNames = {"Red", "Orange", "Yellow", "Green", "Lime", "Blue", "Cyan", "Light Blue", "Purple", "Magenta", "Pink", "White", "Light Grey", "Grey", "Black"};
            for(String s : teamNames) {
                Team team = new Team(s);
                teams.add(team);
            }
        }

        public int getCurrPlayers() {
            return currPlayers;
        }

        public void setCurrPlayers(int currPlayers) {
            this.currPlayers = currPlayers;
        }

        public void addSpawn(Location loc) {
            this.arena.addSpawn(loc);
            GameManager.save(this);
        }

        public void setLobbySpawn(Location loc) {
            this.lobby.setSpawn(loc);
            GameManager.save(this);
        }

        public Arena getArena() {
            return arena;
        }

        public Lobby getLobby() {
            return lobby;
        }

        public int getTeamSize() {
            return teamSize;
        }

        public void setTeamSize(int teamSize) {
            this.teamSize = teamSize;
        }

        public void broadcast(String s) {
            for(UUID u : players) {
                Player p = Bukkit.getPlayer(u);
                p.sendMessage(Utils.color(s));
            }
        }

        public void lobbyCountdown(Game game) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(lobbyCountdown == 30) {
                        lobbyCountdown--;
                        broadcast("&c&lMineX &7| Searching for games to merge!!");
                        Game current = game;
                        Game closest = null;
                        int check = 25;
                        for(Game g : GameManager.games) {
                            //Make sure the games arnt the same!
                            if(g.getId().equals(current.getId())) continue;
                            //Check if they addup to 25, very unlikely to be perfect
                            if(current.getCurrPlayers() + g.getCurrPlayers() == check) {
                                closest = g;
                            } else if((current.getCurrPlayers() + g.getCurrPlayers()) - check > (game.getCurrPlayers() + ((closest == null) ? 0 : closest.getCurrPlayers())) - check) {
                                //basically looks if the current game and the game in the loop remainder is less then the current game and the current closes.
                                closest = g;
                            }
                        }
                        if(closest != null) {
                            //The closest game isnt null, so we move all players from one to the other.
                            for(UUID u : closest.players) {
                                Player p = Bukkit.getPlayer(u);
                                mPlayer mp = (mPlayer.uuidPlayers.get(u) == null) ? new mPlayer(u) : mPlayer.uuidPlayers.get(u);
                                mp.setCurrGame(current);
                                p.teleport(current.getLobby().getSpawn());
                                current.setCurrPlayers(current.getCurrPlayers() - 1);
                            }
                        }

                    }
                    //every 10 seconds
                    if(lobbyCountdown % 10 == 0) {
                        lobbyCountdown--;
                        long minute = TimeUnit.SECONDS.toMinutes(lobbyCountdown) - (TimeUnit.SECONDS.toHours(lobbyCountdown) * 60);
                        long second = TimeUnit.SECONDS.toSeconds(lobbyCountdown) - (TimeUnit.SECONDS.toMinutes(lobbyCountdown) * 60);
                        broadcast("&c&lMineX &7| Game starting in " + (minute == 0 ? "" : minute + "m" )  + second + "s");
                    }
                }
            }.runTaskTimer(Main.getInstance(), 0, 20);
        }

    }
