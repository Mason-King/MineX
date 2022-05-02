    package minex.Game;

    import minex.Arena.Arena;
    import minex.Arena.Lobby;
    import minex.LootChest.LootChest;
    import minex.Main;
    import minex.Managers.GameManager;
    import minex.Party.Party;
    import minex.Player.mPlayer;
    import minex.Utils.Utils;
    import net.minecraft.server.v1_8_R3.NBTTagCompound;
    import org.bukkit.Bukkit;
    import org.bukkit.Location;
    import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
    import org.bukkit.entity.Player;
    import org.bukkit.inventory.ItemStack;
    import org.bukkit.inventory.meta.ItemMeta;
    import org.bukkit.scheduler.BukkitRunnable;
    import org.bukkit.scheduler.BukkitScheduler;
    import org.bukkit.scheduler.BukkitTask;

    import java.util.*;
    import java.util.concurrent.ThreadLocalRandom;
    import java.util.concurrent.TimeUnit;

    public class Game {

        private String id;
        private int maxPlayers = 25;
        private int teamSize;
        private List<UUID> players = new ArrayList<>();
        private List<Team> allTeams = new ArrayList<>();
        private Map<UUID, Team> playerTeams = new HashMap<>();
        //private List<LootChest> chests = new ArrayList<>();
        private int currPlayers;
        private Arena arena;
        private int lobbyCountdown = 30;
        private boolean inGame;
        private Lobby lobby;
        private BukkitTask scheduler;


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

        public void leaveGame(UUID u) {
            this.players.remove(u);
            currPlayers = currPlayers - 1;
            if(currPlayers == 0) {
                scheduler.cancel();
            }
            Bukkit.getPlayer(u).teleport(new Location(Bukkit.getWorld("world"), 0, 73, 0));
            mPlayer mp = mPlayer.uuidPlayers.get(u);
            mp.setCurrGame(null);
        }

        public void joinGame(UUID u) {
            Player player = Bukkit.getPlayer(u);
            mPlayer mp = mPlayer.uuidPlayers.get(u);
            player.teleport(lobby.getSpawn());
            addPlayer(u);
            mp.setCurrGame(this);

            Team t = getRandomEmptyTeam();
            if(t == null) {
                this.lobbyCountdown = 5;
            } else {
                t.addMember(u);
                mp.setTeam(t);
            }
            if(currPlayers == 0) lobbyCountdown(this);
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
                    mp.setTeam(t);
                }
            }
            if(currPlayers == 0) lobbyCountdown(this);
        }

        public Team getRandomEmptyTeam() {
            for(Team t : allTeams) {
                if(!t.isHasMembers()) {
                    return t;
                }
            }
            return null;
        }

        public void loadTeams() {
            String[] teamNames = {"Red", "Orange", "Yellow", "Green", "Lime", "Blue", "Cyan", "Light Blue", "Purple", "Magenta", "Pink", "White", "Light Grey", "Grey", "Black"};
            for(String s : teamNames) {
                Team t = new Team(s, this.id);
                allTeams.add(t);
            }
        }

        public Team getTeam(UUID u) {
            return playerTeams.get(u);
        }

        public void addPlayer(UUID u) {
            getPlayers().add(u);
        }

        public void setTeamSpawn(Team team, String spawn) {
            getArena().setTeamSpawn(team, spawn);
        }

        public Location getTeamSpawn(Team t) {
            return getArena().getTeamSpawn(t);
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

        public void addSpawn(Location loc, String name) {
            this.arena.addSpawn(name, loc);
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

        public List<UUID> getPlayers() {
            return players;
        }

        public void setPlayers(List<UUID> players) {
            this.players = players;
        }

        public List<Team> getAllTeams() {
            return allTeams;
        }

        public void setAllTeams(List<Team> allTeams) {
            this.allTeams = allTeams;
        }

        public Map<UUID, Team> getPlayerTeams() {
            return playerTeams;
        }

        public void setPlayerTeams(Map<UUID, Team> playerTeams) {
            this.playerTeams = playerTeams;
        }

        public void setArena(Arena arena) {
            this.arena = arena;
        }

        public int getLobbyCountdown() {
            return lobbyCountdown;
        }

        public void setLobbyCountdown(int lobbyCountdown) {
            this.lobbyCountdown = lobbyCountdown;
        }

        public void setLobby(Lobby lobby) {
            this.lobby = lobby;
        }

        public BukkitTask getScheduler() {
            return scheduler;
        }

        public void setScheduler(BukkitTask scheduler) {
            this.scheduler = scheduler;
        }

//        public List<LootChest> getChests() {
//            return chests;
//        }
//
//        public void setChests(List<LootChest> chests) {
//            this.chests = chests;
//        }
//
//        public void addChest(LootChest c) {
//            chests.add(c);
//        }
//
//        public void removeChest(LootChest c) {
//            chests.remove(c);
//        }

        public void lobbyCountdown(Game game) {
            scheduler = new BukkitRunnable() {
                @Override
                public void run() {
                    long minute = TimeUnit.SECONDS.toMinutes(lobbyCountdown) - (TimeUnit.SECONDS.toHours(lobbyCountdown) * 60);
                    long second = TimeUnit.SECONDS.toSeconds(lobbyCountdown) - (TimeUnit.SECONDS.toMinutes(lobbyCountdown) * 60);
                    if(lobbyCountdown == 0) {
                        this.cancel();
                        broadcast("&c&lMineX &7| Starting the game!");
                        //need a method to do this randomly if they havent choosen
                        for(UUID u : players) {
                            Player pl = Bukkit.getPlayer(u);
                            mPlayer mp = mPlayer.uuidPlayers.get(u);
                            if(!game.getArena().getTeamSpawns().containsKey(mp.getTeam()) || game.getArena().getTeamSpawn(mp.getTeam()) == null) {
                                for(String s : game.getArena().getSpawns()) {
                                    //loops all spawns
                                    if(!game.getArena().isClaimed(s)) {
                                        game.getArena().setTeamSpawn(mp.getTeam(), s);
                                    }
                                }
                            }
                            pl.teleport(game.getArena().getTeamSpawn(mp.getTeam()));
                            for(ItemStack remove : mp.getSelectedStash()) {
                                net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(remove);
                                NBTTagCompound tag = (stack.hasTag()) ? stack.getTag() : new NBTTagCompound();
                                tag.remove("active");
                                stack.setTag(tag);
                                remove = CraftItemStack.asBukkitCopy(stack);

                                ItemMeta im = remove.getItemMeta();
                                im.setLore(null);
                                remove.setItemMeta(im);

                                pl.getInventory().addItem(remove);
                            }
                        }
                        setInGame(true);
                        return;
                    }
                    if(lobbyCountdown <= 5) {
                        broadcast("&c&lMineX &7| Game starting in " + second + "s");
                        lobbyCountdown = lobbyCountdown - 1;
                        return;
                    } else if(lobbyCountdown % 10 == 0) {
                        broadcast("&c&lMineX &7| Game starting in " + (minute == 0 ? "" : minute + "m ") + second + "s");
                        lobbyCountdown = lobbyCountdown - 1;
                        return;
                    } else if(lobbyCountdown % 20 == 0){
                        broadcast("&c&lMineX &7| Dont forget to select your spawn with /game spawns");
                        lobbyCountdown = lobbyCountdown - 1;
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
                    }
                    lobbyCountdown = lobbyCountdown - 1;
                }
            }.runTaskTimer(Main.getInstance(), 0, 20);
        }

    }
