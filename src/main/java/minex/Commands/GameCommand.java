package minex.Commands;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import minex.Objects.Arena;
import minex.Objects.Lobby;
import minex.Events.RegionListener;
import minex.Objects.Game;
import minex.Gui.FarmGui;
import minex.Gui.GameSelectorGui;
import minex.Gui.MapGui;
import minex.Gui.StashGui;
import minex.Main;
import minex.Managers.GameManager;
import minex.Managers.PlayerManager;
import minex.Enums.Message;
import minex.Objects.MobSpawn;
import minex.Objects.mPlayer;
import minex.Utils.Utils;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GameCommand implements CommandExecutor {

    Main main = Main.getInstance();

    public GameCommand() {
        main.getCommand("game").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))  {
            sender.sendMessage(Message.CONSOLE.getMessage());
        } else {
            Player player = (Player) sender;
            if(args.length == 0) {
                for(String s : Utils.color(main.getConfig().getStringList("helpCommand"))) {
                    player.sendMessage(s);
                }
            } else {
                if(args[0].equalsIgnoreCase("create")) {
                    if(!player.hasPermission("minex.create")) {
                        player.sendMessage(Message.NO_PERMISSION.getMessage());
                        return false;
                    }
                    if(args.length < 2) {
                        player.sendMessage(Message.GAME_CREATE_USAGE.getMessage());
                    } else {
                        String id = args[1];
                        if(GameManager.getGame(id) != null) {
                            player.sendMessage(Message.GAME_EXISTS.getMessage());
                        } else {
                            player.sendMessage(Message.GAME_CREATING.getMessage().replace("{id}", id));
                            Game game = GameManager.createGame(id);
                        }
                    }
                } else if(args[0].equalsIgnoreCase("list")) {
                    if(!player.hasPermission("minex.list")) {
                        player.sendMessage(Message.NO_PERMISSION.getMessage());
                        return false;
                    }
                    for (Game game : GameManager.games) {
                        player.sendMessage(game.getId());
                    }
                } else if(args[0].equalsIgnoreCase("addmobspawn")) {
                    if(!player.hasPermission("minex.mobspawn.add")) {
                        player.sendMessage(Message.NO_PERMISSION.getMessage());
                        return false;
                    }
                    if(args.length < 5) {
                        player.sendMessage(Message.ADD_MOB_SPAWN_USAGE.getMessage());
                        return false;
                    }
                    Game game = GameManager.getGame(args[1]);
                    if(game == null) {
                        player.sendMessage(Message.INVALID_GAME.getMessage());
                        return false;
                    }
                    String loc = Utils.toString(new Location(player.getWorld(), Math.round(player.getLocation().getBlockX() + 0.5), player.getLocation().getBlockY(), Math.round(player.getLocation().getBlockZ() + 0.5)));
                    EntityType type = EntityType.fromName(args[2]);
                    if(type == null) {
                        player.sendMessage(Message.INVALID_MOB_TYPE.getMessage());
                        return false;
                    }
                    MobSpawn ms = new MobSpawn(loc, game.getId());
                    ms.addEntity(args[2]);
                    int min = Integer.parseInt(args[3]);
                    int max = Integer.parseInt(args[4]);
                    game.addMobSpawn(ms, min, max);
                    player.sendMessage(Message.MOBSPAWN_ADDED.getMessage());

                } else if(args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {
                    if(!player.hasPermission("minex.tp")) {
                        player.sendMessage(Message.NO_PERMISSION.getMessage());
                        return false;
                    }
                    if (args.length < 2) {
                        player.sendMessage(Message.GAME_TELEPORT_USAGE.getMessage());
                    } else {
                        String id = args[1];
                        Game game = GameManager.getGame(id);
                        int index = (args.length >= 3) ? Integer.valueOf(args[2]) : 0;
                        if (game == null) {
                            player.sendMessage(Message.INVALID_GAME.getMessage());
                        } else {
                            Arena arena = game.getArena();
                            if (index > arena.getSpawns().size()) {
                                player.sendMessage(Message.INVALID_SPAWN.getMessage());
                            } else {
                                if(arena.getSpawn(index) == null) {
                                    player.sendMessage(Message.INVALID_SPAWN.getMessage());
                                    return false;
                                }
                                player.teleport(arena.getSpawn(index));
                            }
                        }
                    }
                } else if(args[0].equalsIgnoreCase("region")) {
                    if(!player.hasPermission("minex.region")) {
                        player.sendMessage(Message.NO_PERMISSION.getMessage());
                        return false;
                    }
                    if(args.length < 2) {
                        player.sendMessage(Utils.color("&c&lMineX &7| Region hepl"));
                        return false;
                    }
                    if(args[1].equalsIgnoreCase("give")) {
                        org.bukkit.inventory.ItemStack axe = new org.bukkit.inventory.ItemStack(Material.GOLD_AXE);
                        ItemMeta im = axe.getItemMeta();
                        im.setDisplayName(Utils.color("&c&lRegion Selection"));
                        List<String> lore = new ArrayList<>();
                        lore.add(Utils.color("&7Create a new game region"));
                        lore.add(Utils.color(""));
                        lore.add(Utils.color("&7Right-click to set pos1"));
                        lore.add(Utils.color("&7Left-click to set pos2"));

                        axe.setItemMeta(im);

                        ItemStack stack = CraftItemStack.asNMSCopy(axe);
                        NBTTagCompound tag = (stack.hasTag() ? stack.getTag() : new NBTTagCompound());
                        tag.setBoolean("region", true);
                        axe = CraftItemStack.asBukkitCopy(stack);

                        player.getInventory().addItem(axe);
                        player.sendMessage(Message.REGION_WAND.getMessage());
                    } else if(args[1].equalsIgnoreCase("create")) {
                        if(args.length < 4) {
                            player.sendMessage(Message.REGION_CREATE_USAGE.getMessage());
                            return false;
                        }
                        WorldGuardPlugin wgp = main.getWorldGuard();
                        RegionContainer regionContainer = wgp.getRegionContainer();
                        RegionManager rm = regionContainer.get(player.getWorld());
                        Location pos1 = RegionListener.pos1.get(player.getUniqueId());
                        Location pos2 = RegionListener.pos2.get(player.getUniqueId());

                        ProtectedCuboidRegion region = new ProtectedCuboidRegion(args[3], new BlockVector(pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ()), new BlockVector(pos2.getBlockX(), pos2.getBlockY(), pos2.getBlockZ()));
                        rm.addRegion(region);
                        player.sendMessage(Message.REGION_CREATED.getMessage().replace("{id}", args[3]));
                    }
                } else if(args[0].equalsIgnoreCase("leave")) {
                    mPlayer mp = PlayerManager.getmPlayer(player.getUniqueId());
                    Game game = mp.getCurrGame();
                    if (game == null) {
                        player.sendMessage(Message.NO_GAME.getMessage());
                    } else {
                        game.leaveGame(player.getUniqueId());
                        player.sendMessage(Message.LEFT_GAME.getMessage());
                    }
                } else if(args[0].equalsIgnoreCase("stash")) {
                    mPlayer mp = PlayerManager.getmPlayer(player.getUniqueId());
                    if(mp.getCurrGame() != null) return false;
                    Game game = mp.getCurrGame();
                    new StashGui().makeGui(player);
                } else if(args[0].equalsIgnoreCase("lootchest")) {
                    if(!player.hasPermission("minex.lootchest")) {
                        player.sendMessage(Message.NO_PERMISSION.getMessage());
                        return false;
                    }
                    if(args.length < 2) {
                        player.sendMessage(Utils.color("&c&lMineX &7| Lootchest help!"));
                    } else {
                        if(args[1].equalsIgnoreCase("give")) {
                            if (args.length < 3) {
                                player.sendMessage(Message.LOOTCHEST_GIVE_USAGE.getMessage());
                            } else {
                                org.bukkit.inventory.ItemStack uncommon = Utils.getItem(new org.bukkit.inventory.ItemStack(Material.CHEST), "&a&lUncommon Chest", "&7Place this down to place", "&7A uncommon loot chest", "", "&7&o(( Shift-Right click to edit ))");
                                org.bukkit.inventory.ItemStack common = Utils.getItem(new org.bukkit.inventory.ItemStack(Material.CHEST), "&a&lCommon Chest", "&7Place this down to place", "&7A common loot chest", "", "&7&o(( Shift-Right click to edit ))");
                                org.bukkit.inventory.ItemStack rare = Utils.getItem(new org.bukkit.inventory.ItemStack(Material.CHEST), "&a&lRare Chest", "&7Place this down to place", "&7A rare loot chest", "", "&7&o(( Shift-Right click to edit ))");

                                String type = args[2];
                                if (type.equalsIgnoreCase("uncommon")) {
                                    ItemStack nbtStack = CraftItemStack.asNMSCopy(uncommon);
                                    NBTTagCompound tag = (nbtStack.hasTag() ? nbtStack.getTag() : new NBTTagCompound());
                                    tag.setString("type", "uncommon");
                                    uncommon = CraftItemStack.asBukkitCopy(nbtStack);

                                    player.getInventory().addItem(uncommon);
                                    player.sendMessage(Message.LOOTCHEST_GIVE.getMessage().replace("{type}", "uncommon"));
                                } else if (type.equalsIgnoreCase("common")) {
                                    ItemStack nbtStack = CraftItemStack.asNMSCopy(      common);
                                    NBTTagCompound tag = (nbtStack.hasTag() ? nbtStack.getTag() : new NBTTagCompound());
                                    tag.setString("type", "common");
                                    common = CraftItemStack.asBukkitCopy(nbtStack);

                                    player.getInventory().addItem(common);
                                    player.sendMessage(Message.LOOTCHEST_GIVE.getMessage().replace("{type}", "common"));
                                } else if (type.equalsIgnoreCase("rare")) {
                                    ItemStack nbtStack = CraftItemStack.asNMSCopy(rare);
                                    NBTTagCompound tag = (nbtStack.hasTag() ? nbtStack.getTag() : new NBTTagCompound());
                                    tag.setString("type", "rare");
                                    rare = CraftItemStack.asBukkitCopy(nbtStack);

                                    player.getInventory().addItem(rare);
                                    player.sendMessage(Message.LOOTCHEST_GIVE.getMessage().replace("{type}", "rare"));
                                } else {
                                    player.sendMessage(Message.INVALID_TYPE.getMessage());
                                }
                            }
                        }
                    }
                } else if(args[0].equalsIgnoreCase("addSpawn")) {
                    if(!player.hasPermission("minex.spawn.add")) {
                        player.sendMessage(Message.NO_PERMISSION.getMessage());
                        return false;
                    }
                    if (args.length < 3 || args.length > 3) {
                        player.sendMessage(Message.ADD_SPAWN_USAGE.getMessage());
                    } else {
                        String id = args[1];
                        String name = args[2];
                        Game game = GameManager.getGame(id);
                        if (game == null) {
                            player.sendMessage(Message.NO_GAME.getMessage());
                        } else {
                            if (game.getArena().exists(name)) {
                                player.sendMessage(Message.SPAWN_EXISTS.getMessage());
                            } else {
                                if(!player.getWorld().getName().equals(args[1] + "Game")) return false;
                                Location loc = new Location(player.getWorld(), Math.round(player.getLocation().getBlockX() + 0.5), player.getLocation().getBlockY(), Math.round(player.getLocation().getBlockZ() + 0.5));
                                game.addSpawn(loc, name);
                                player.sendMessage(Message.ADDED_SPAWN.getMessage().replace("{name}", name));
                            }
                        }
                    }
                } else if(args[0].equalsIgnoreCase("addextraction")) {
                    if(!player.hasPermission("minex.extraction.add")) {
                        player.sendMessage(Message.NO_PERMISSION.getMessage());
                        return false;
                    }
                    if(args.length < 3) {
                        player.sendMessage(Message.ADD_EXTRACTION_USAGE.getMessage());
                    } else {
                        if(GameManager.getGame(args[1]) == null) {
                            player.sendMessage(Message.NO_GAME.getMessage());
                        } else {
                            Game game = GameManager.getGame(args[1]);
                            if(!player.getWorld().getName().equals(args[1] + "Game")) return false;
                            if(game.getArena().getExtractions().contains(Utils.toString(player.getLocation()))) {
                                player.sendMessage(Message.EXTRACTION_EXISTS.getMessage());
                                return false;
                            }
                            if(game.getArena().getExtractionNames().containsKey(args[2])) {
                                player.sendMessage(Message.EXTRACTION_EXISTS.getMessage());
                                return false;
                            }
                            Location loc = new Location(player.getWorld(), Math.round(player.getLocation().getBlockX() + 0.5), player.getLocation().getBlockY(), Math.round(player.getLocation().getBlockZ() + 0.5));
                            game.addExtraction(args[2], loc);
                            player.sendMessage(Message.NEW_EXTRACTION.getMessage());

                        }
                    }
                } else if(args[0].equalsIgnoreCase("spawns")) {
                    if(!player.hasPermission("minex.spawns")) {
                        player.sendMessage(Message.NO_PERMISSION.getMessage());
                        return false;
                    }
                    if (args.length < 2) {
                        mPlayer mp = PlayerManager.getmPlayer(player.getUniqueId());
                        if(mp.getCurrGame() == null) {
                            player.sendMessage(Message.IN_GAME.getMessage());
                        } else {
                            new MapGui().makeGui(player, mp.getCurrGame());
                        }
                    } else {
                        String id = args[1];
                        Game game = GameManager.getGame(id);
                        if (game == null) {
                            player.sendMessage(Message.NO_GAME.getMessage());
                        } else {
                            List<String> spawns = game.getArena().getSpawns();
                            for (int i = 0; i < spawns.size(); i++) {
                                Location loc = Utils.fromString(spawns.get(i));
                                player.sendMessage(i + ": " + "x: " + loc.getBlockX() + " y:" + loc.getBlockY() + " z:" + loc.getBlockZ());
                            }
                        }
                    }
                } else if(args[0].equalsIgnoreCase("join")) {
                    new GameSelectorGui().makeGui(player);
                } else if(args[0].equalsIgnoreCase("farm")) {
                    new FarmGui().makeGui(player);
                } else if(args[0].equalsIgnoreCase("lobby")) {
                    if(!player.hasPermission("minex.lobby")) {
                        player.sendMessage(Message.NO_PERMISSION.getMessage());
                        return false;
                    }
                    if(args.length < 2) {
                        player.sendMessage("game lobby help command");
                    } else {
                        if(args[1].equalsIgnoreCase("setspawn")) {
                            if(args.length < 3) {
                                player.sendMessage(Message.GAME_LOBBY_SETSPAWN_USAGE.getMessage());
                            } else {
                                String id = args[2];
                                Game game = GameManager.getGame(id);
                                if(!player.getWorld().getName().equals(id + "Lobby")) return false;
                                if(game == null) {
                                    player.sendMessage(Message.INVALID_GAME.getMessage());
                                } else {
                                    game.setLobbySpawn(player.getLocation());
                                    player.sendMessage(Message.GAME_LOBBY_SPAWNSET.getMessage());
                                }
                            }
                        } else if(args[1].equalsIgnoreCase("tp") || args[1].equalsIgnoreCase("teleport")) {
                            if(args.length < 3) {
                                player.sendMessage(Message.GAME_LOBBY_TP_USAGE.getMessage());
                            } else {
                                String id = args[2];
                                Game game = GameManager.getGame(id);
                                if(game == null) {
                                    player.sendMessage(Message.NO_GAME.getMessage());
                                } else {
                                    Lobby lobby = game.getLobby();
                                    player.teleport(lobby.getSpawn());
                                }
                            }
                        }
                    }
                } else {
                    for(String s : Utils.color(main.getConfig().getStringList("helpCommand"))) {
                        player.sendMessage(s);
                    }
                }
            }
        }
        return false;
    }

}
