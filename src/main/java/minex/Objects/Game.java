    package minex.Objects;

    import minex.Enums.Message;
    import minex.Fastboard.FastBoard;
    import minex.Main;
    import minex.Managers.GameManager;
    import minex.Managers.PlayerManager;
    import minex.Utils.Utils;
    import net.minecraft.server.v1_8_R3.NBTTagCompound;
    import org.bukkit.Bukkit;
    import org.bukkit.Location;
    import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
    import org.bukkit.entity.Player;
    import org.bukkit.inventory.ItemStack;
    import org.bukkit.inventory.meta.ItemMeta;
    import org.bukkit.scheduler.BukkitRunnable;
    import org.bukkit.scheduler.BukkitTask;

    import java.util.*;
    import java.util.concurrent.TimeUnit;

    public class Game {

        private String id;
        private int maxPlayers = 25;
        private List<UUID> players = new ArrayList<>();
        private List<Team> allTeams = new ArrayList<>();
        private  Map<UUID, Team> playerTeams = new HashMap<>();
        private List<LootChest> chests = new ArrayList<>();
        private List<MobSpawn> spawns = new ArrayList<>();
        private int currPlayers;
        private Arena arena;
        private int lobbyCountdown = 30;
        private int gameTimer = 1800;
        private boolean inGame;
        private Lobby lobby;
        private transient BukkitTask scheduler;
        private transient BukkitTask timer;

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
            this.playerTeams.remove(u);
            currPlayers = currPlayers - 1;
            if(currPlayers <= 1) {
                this.reset();
            }
            // TODO - edit spawn here
            Bukkit.getPlayer(u).teleport(new Location(Bukkit.getWorld("world"), 0, 73, 0));
            mPlayer mp = PlayerManager.getmPlayer(u);
            mp.setCurrGame(null);
            mp.setTeam(null);
        }

        public void joinGame(UUID u) {
            List<String> scoreboard = Main.getInstance().getConfig().getStringList("gameScoreboard");
            String partyFormat = Main.getInstance().getConfig().getString("partyFormat");
            Player player = Bukkit.getPlayer(u);
            mPlayer mp = PlayerManager.getmPlayer(u);
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
            currPlayers++;
            if(currPlayers == 1) lobbyCountdown(this);

            FastBoard board = new FastBoard(player);

            mp.setBoard(board);


                Party p = mp.getParty();

            List<String> lines = new ArrayList<>();
            for(int i = 0; i < scoreboard.size(); i++) {
                if(mp.getCurrGame() == null) continue;
                Game game = mp.getCurrGame();
                List<String> locs = game.getArena().getExtractions();
                Location closest = null;
                for(String s : locs) {
                    if(closest == null) {
                        closest = Utils.fromString(s);
                    } else if(Utils.fromString(s).distanceSquared(Bukkit.getPlayer(u).getLocation()) < closest.distanceSquared(Bukkit.getPlayer(u).getLocation())) {
                        closest = Utils.fromString(s);
                    }
                }

                String exName = "null";
                for(Map.Entry e : game.getArena().getExtractionNames().entrySet()) {
                    if(((String) e.getValue()).equals(Utils.toString(closest))) {
                        exName = (String) e.getKey();
                    }
                }

                String string = Utils.color(scoreboard.get(i)
                        .replace("{remaining}", "" + lobbyCountdown)
                        .replace("{nearest_extraction}", exName));

                if(string.contains("{party}")) {
//                                if(p == null) {
//                                    System.out.println("should not add " + string);
//                                    board.removeLine(i);
//                                    continue;
//                                } else {
//
//                                }
                    for(int x = 0; x < 4; x++) {
                        lines.add(("test line " + x));
                        continue;
                    }
                    continue;
                }

                lines.add(string);
            }
            board.updateLines(lines);
        }


        public void joinGame(Party p) {
            Team t = getRandomEmptyTeam();
            if(currPlayers == 0) {
                lobbyCountdown(this);
            }
            for(UUID u : p.getMembers()) {
                Player player = Bukkit.getPlayer(u);
                mPlayer mp = PlayerManager.getmPlayer(u);
                player.teleport(lobby.getSpawn());
                addPlayer(u);
                mp.setCurrGame(this);
                if(t == null) {
                    this.lobbyCountdown = 5;
                } else {
                    t.addMember(u);
                    mp.setTeam(t);
                }
                currPlayers++;
            }
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
            team.setSpawn(spawn);
            getArena().addClaimed(spawn);
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

        public void addExtraction(String name, Location loc) {
            this.arena.addExtractionPoint(name, loc);
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

        public List<LootChest> getChests() {
            return chests;
        }

        public void setChests(List<LootChest> chests) {
            this.chests = chests;
        }

        public void addChest(LootChest c) {
            chests.add(c);
            GameManager.save(this);

        }

        public void removeChest(LootChest c) {
            chests.remove(c);
        }

        public List<MobSpawn> getSpawns() {
            return spawns;
        }

        public void setSpawns(List<MobSpawn> spawns) {
            this.spawns = spawns;
        }

        public void addMobSpawn(MobSpawn s, int min, int max) {
            s.setMaxMobs(max);
            s.setMinMobs(min);
            this.spawns.add(s);
            GameManager.save(this);
        }

        public void gameTimer(Game game) {
            gameTimer = 1800;
            timer = new BukkitRunnable() {
                @Override
                public void run() {
                    long minute = TimeUnit.SECONDS.toMinutes(gameTimer) - (TimeUnit.SECONDS.toHours(gameTimer) * 60);
                    long second = TimeUnit.SECONDS.toSeconds(gameTimer) - (TimeUnit.SECONDS.toMinutes(gameTimer) * 60);

                    if(gameTimer == 0) {
                        game.broadcast(Message.GAME_ENDING.getMessage());
                        reset();
                        this.cancel();
                    } else if(gameTimer <= 600) {
                        //10 minutes or less
                        if(gameTimer == 60) {
                            //a minute left
                            game.broadcast(Message.TIME_REMAINING.getMessage().replace("{minutes}", minute + "").replace("{seconds}", second + ""));
                        }
                        if(gameTimer == 300) {
                            //a minute left
                            game.broadcast(Message.TIME_REMAINING.getMessage().replace("{minutes}", minute + "").replace("{seconds}", second + ""));
                        }
                        if(gameTimer == 600) {
                            game.broadcast(Message.TIME_REMAINING.getMessage().replace("{minutes}", minute + "").replace("{seconds}", second + ""));
                        }
                        if(gameTimer < 60) {
                            if(gameTimer % 10 == 0) {
                                game.broadcast(Message.TIME_REMAINING.getMessage().replace("{minutes}", minute + "").replace("{seconds}", second + ""));
                            }
                        }
                        if(gameTimer <= 10) {
                            game.broadcast(Message.TIME_REMAINING.getMessage().replace("{minutes}", minute + "").replace("{seconds}", second + ""));
                        }
                    } else if(gameTimer % 600 == 0) {
                        //every 10 mins
                        game.broadcast(Message.TIME_REMAINING.getMessage().replace("{minutes}", minute + "").replace("{seconds}", second + ""));
                    }
                    gameTimer--;
                }
            }.runTaskTimer(Main.getInstance(), 0, 20);
        }

        public void lobbyCountdown(Game game) {
            List<String> scoreboard = Main.getInstance().getConfig().getStringList("gameScoreboard");
            String partyFormat = Main.getInstance().getConfig().getString("partyFormat");
            scheduler = new BukkitRunnable() {
                @Override
                public void run() {
                    long minute = TimeUnit.SECONDS.toMinutes(lobbyCountdown) - (TimeUnit.SECONDS.toHours(lobbyCountdown) * 60);
                    long second = TimeUnit.SECONDS.toSeconds(lobbyCountdown) - (TimeUnit.SECONDS.toMinutes(lobbyCountdown) * 60);
                    for(UUID u : players) {
                        mPlayer mp = PlayerManager.getmPlayer(u);
                        Party p = mp.getParty();

                        FastBoard board = mp.getBoard();

                        List<String> lines = new ArrayList<>();
                        for(int i = 0; i < scoreboard.size(); i++) {
                            if(mp.getCurrGame() == null) continue;
                            Game game = mp.getCurrGame();
                            List<String> locs = game.getArena().getExtractions();
                            Location closest = null;
                            for(String s : locs) {
                                if(closest == null) {
                                    closest = Utils.fromString(s);
                                } else if(Utils.fromString(s).distanceSquared(Bukkit.getPlayer(u).getLocation()) < closest.distanceSquared(Bukkit.getPlayer(u).getLocation())) {
                                    closest = Utils.fromString(s);
                                }
                            }

                            String exName = "null";
                            for(Map.Entry e : game.getArena().getExtractionNames().entrySet()) {
                                if(((String) e.getValue()).equals(Utils.toString(closest))) {
                                    exName = (String) e.getKey();
                                }
                            }

                            String string = Utils.color(scoreboard.get(i)
                                    .replace("{remaining}", "" + lobbyCountdown)
                                    .replace("{nearest_extraction}", exName));

                            if(string.contains("{party}")) {
//                                if(p == null) {
//                                    System.out.println("should not add " + string);
//                                    board.removeLine(i);
//                                    continue;
//                                } else {
//
//                                }
                                for(int x = 0; x < 4; x++) {
                                    lines.add(("test line " + x));
                                    continue;
                                }
                                continue;
                            }

                            lines.add(string);
                        }
                        board.updateLines(lines);
                    }
                    if(lobbyCountdown == 0) {
                        gameTimer(game);
                        this.cancel();
                        broadcast("&c&lMineX &7| Starting the game!");
                        //need a method to do this randomly if they havent choosen
                        for(UUID u : players) {
                            Player pl = Bukkit.getPlayer(u);
                            mPlayer mp = PlayerManager.getmPlayer(u);
                            if(mp.getTeam().getSpawn() == null) {
                                for(Map.Entry e : game.getArena().getSpawnNames().entrySet()) {
                                    if(!game.getArena().isClaimed((String) e.getKey())) {
                                        game.setTeamSpawn(mp.getTeam(), (String) e.getKey());
                                    }
                                }
                            }

                            if (mp.getTeam().getSpawn() == null) {
                                pl.teleport(game.getArena().getSpawn(1));
                            } else {
                                pl.teleport(getArena().getSpawn(mp.getTeam().getSpawn()));
                            }


                            for(ItemStack remove : mp.getFullStash()) {
                                net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(remove);
                                NBTTagCompound tag = (stack.hasTag()) ? stack.getTag() : new NBTTagCompound();
                                tag.remove("active");
                                stack.setTag(tag);
                                remove = CraftItemStack.asBukkitCopy(stack);

                                ItemMeta im = remove.getItemMeta();
                                im.setLore(null);
                                remove.setItemMeta(im);

                                pl.getInventory().addItem(remove);
                                mp.removeItem(remove);
                            }
                        }

                        for(LootChest chest : game.getChests()) {
                            chest.fill();
                        }

                        for(MobSpawn spawn : game.getSpawns()) {
                            spawn.spawn();
                        }

                        game.broadcast(Message.GAME_STARTING.getMessage());

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
                                mPlayer mp = (PlayerManager.getmPlayer(u) == null) ? new mPlayer(u) : PlayerManager.getmPlayer(u);
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

        public void reset() {
            for(UUID u : players) {
                leaveGame(u);
            }
            players = new ArrayList<>();
            if(allTeams != null) {
                for(Team t : allTeams) {
                    t.setMembers(new ArrayList<>());
                }
            }
            playerTeams = new HashMap<>();
            currPlayers = 0;
            getArena().reset();
            this.lobbyCountdown = 30;
            this.inGame = false;


            if(timer != null) {
                timer.cancel();
            }
            if(scheduler != null) {
                scheduler.cancel();
            }

            GameManager.save(this);
        }

    }
