    package minex.Game;

    import minex.Arena.Arena;
    import minex.Arena.Lobby;
    import minex.Main;
    import minex.Managers.GameManager;
    import minex.Party.Party;
    import minex.Utils.Utils;
    import org.bukkit.Bukkit;
    import org.bukkit.Location;
    import org.bukkit.entity.Player;
    import org.bukkit.scheduler.BukkitRunnable;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.UUID;
    import java.util.concurrent.TimeUnit;

    public class Game {

        private String id;
        private int maxPlayers = 25;
        private int teamSize;
        private List<UUID> players = new ArrayList<>();
        private int currPlayers;
        private Arena arena;
        private int lobbyCountdown = 120;
        private boolean inGame;
        private Lobby lobby;


        public Game(String id, int maxPlayers, int currPlayers) {
            this.id = id;
            this.maxPlayers = maxPlayers;
            this.currPlayers = currPlayers;
        }

        public Game(String id) {
           this.id = id;
           this.arena = new Arena(id);
           this.lobby = new Lobby(id);
           this.currPlayers = 0;
        }

        public void addPlayer(UUID u) {
            this.players.add(u);
//            if(currPlayers == 0) {
//                countdown = new Countdown(this.id, 120);
//                CountdownManager.addCountdown(id, countdown);
//            }
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

        public void lobbyCountdown() {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(lobbyCountdown == 30) {
                        lobbyCountdown--;
                        broadcast("&c&lMineX &7| Merging games!");

                    }
                    if(lobbyCountdown > 0) {
                        lobbyCountdown--;
                        long minute = TimeUnit.SECONDS.toMinutes(lobbyCountdown) - (TimeUnit.SECONDS.toHours(lobbyCountdown) * 60);
                        long second = TimeUnit.SECONDS.toSeconds(lobbyCountdown) - (TimeUnit.SECONDS.toMinutes(lobbyCountdown) * 60);
                        broadcast("&c&lMineX &7| Game starting in " + minute + "m " + second + "s");
                    }
                }
            }.runTaskTimer(Main.getInstance(), 0, 20);
        }

    }
