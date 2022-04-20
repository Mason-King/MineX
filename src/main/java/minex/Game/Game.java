    package minex.Game;

    import minex.Arena.Arena;
    import minex.Arena.Lobby;
    import minex.Managers.CountdownManager;
    import minex.Managers.GameManager;
    import minex.Party.Party;
    import org.bukkit.Bukkit;
    import org.bukkit.Location;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.UUID;

    public class Game {

        private String id;
        private int maxPlayers = 25;
        private int teamSize;
        private List<UUID> players = new ArrayList<>();
        private int currPlayers;
        private Arena arena;
        private Lobby lobby;
        private Countdown countdown;

        public Game(String id, int maxPlayers, int currPlayers) {
            this.id = id;
            this.maxPlayers = maxPlayers;
            this.currPlayers = currPlayers;
        }

        public Game(String id) {
           this.id = id;
           this.arena = new Arena(id);
           this.lobby = new Lobby(id);
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

        public void addPlayer(UUID u) {
            this.players.add(u);
            if(currPlayers == 0) {
                countdown = new Countdown(this.id, 120);
                CountdownManager.addCountdown(id, countdown);
            }
            currPlayers++;
            Bukkit.getPlayer(u).teleport(lobby.getSpawn());
        }

        public void addParty(Party p) {
            for(UUID id : p.getMembers()) {
                addPlayer(id);
                Bukkit.getPlayer(id).teleport(lobby.getSpawn());
            }
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
    }
