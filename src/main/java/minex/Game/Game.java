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

    import java.util.*;
    import java.util.concurrent.TimeUnit;

    public class Game {

        private String id;
        private int maxPlayers = 25;
        private int teamSize;
        private List<UUID> players = new ArrayList<>();
        private List<Team> teams = new ArrayList<>();
        private Map<UUID, Team> playerTeams = new HashMap<>();
        private int currPlayers;
        private Arena arena;
        private int lobbyCountdown = 30;
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
            mPlayer mp = mPlayer.uuidPlayers.get(u);
            mp.setCurrGame(this);
        }

        public void addParty(Party p) {
            for(UUID id : p.getMembers()) {
                addPlayer(id);
                Bukkit.getPlayer(id).teleport(lobby.getSpawn());
                mPlayer mp = mPlayer.uuidPlayers.get(id);
                mp.setCurrGame(this);
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

        public void setTeam(UUID u, Team t) {
            playerTeams.put(u, t);
        }

        public void removeTeam(UUID u) {
            playerTeams.remove(u);
        }

        public Team getTeam(UUID u) {
            return playerTeams.get(u);
        }

        public int getCurrPlayers() {
            return currPlayers;
        }

        public void setCurrPlayers(int currPlayers) {
            this.currPlayers = currPlayers;
        }

        public void addSpawn(Location loc, String name) {
            this.arena.addSpawn(loc, name);
            GameManager.save(this);
        }

        public Team getTeam(String s) {
            for(Team team : teams) {
                if(team.getName().equalsIgnoreCase(s)) {
                    return team;
                }
            }
            return null;
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

        public boolean isInGame() {
            return inGame;
        }

        public void setInGame(boolean inGame) {
            this.inGame = inGame;
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
                    System.out.println("ran");
                    long minute = TimeUnit.SECONDS.toMinutes(lobbyCountdown) - (TimeUnit.SECONDS.toHours(lobbyCountdown) * 60);
                    long second = TimeUnit.SECONDS.toSeconds(lobbyCountdown) - (TimeUnit.SECONDS.toMinutes(lobbyCountdown) * 60);
                    if(lobbyCountdown <= 5) {
                        broadcast("&c&lMineX &7| Game starting in " + second + "s");
                        lobbyCountdown = lobbyCountdown - 1;
                        return;
                    } else if(lobbyCountdown % 10 == 0) {
                        broadcast("&c&lMineX &7| Game starting in " + (minute == 0 ? "" : minute + "m " )  + second + "s");
                        lobbyCountdown = lobbyCountdown - 1;
                        return;
                    } else if(lobbyCountdown == 30) {
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
                        lobbyCountdown = lobbyCountdown - 1;
                        return;
                    } else if(lobbyCountdown == 0) {
                        cancel();
                        broadcast("&c&lMineX &7| Starting the game!");
                        for(UUID u : players) {
                            Player pl = Bukkit.getPlayer(u);
                            pl.teleport(Utils.fromString(getTeam(u).getSpawn()));
                        }
                        setInGame(true);
                    }
                }
            }.runTaskTimer(Main.getInstance(), 0, 20);
        }

    }
