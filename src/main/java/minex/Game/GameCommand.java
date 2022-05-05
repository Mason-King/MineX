package minex.Game;

import minex.Arena.Arena;
import minex.Arena.Lobby;
import minex.Gui.GameSelectorGui;
import minex.Gui.MapGui;
import minex.Gui.StashGui;
import minex.Main;
import minex.Managers.GameManager;
import minex.Managers.PlayerManager;
import minex.Messages.Message;
import minex.MobSpawns.MobSpawn;
import minex.Player.mPlayer;
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

import java.util.List;

public class GameCommand implements CommandExecutor {

    Main main = Main.getInstance();

    public GameCommand() {
        main.getCommand("game").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))  {
            sender.sendMessage(Message.CONSOLE_ERROR.getMessage());
        } else {
            Player player = (Player) sender;
            if(args.length == 0) {
                player.sendMessage("Game command help!");
            } else {
                if(args[0].equalsIgnoreCase("create")) {
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
                    for (Game game : GameManager.games) {
                        player.sendMessage(game.getId());
                    }
                } else if(args[0].equalsIgnoreCase("addmobspawn")) {
                    if(args.length < 3) {
                        player.sendMessage(Utils.color("&c&lMineX &7| Incorrect mobspawn usage try: /game addmobspawn <id> <type>"));
                        return false;
                    }
                    Game game = GameManager.getGame(args[1]);
                    if(game == null) {
                        player.sendMessage(Utils.color("&c&lMineX &7| There is no valid game"));
                        return false;
                    }
                    String loc = Utils.toString(player.getLocation());
                    EntityType type = EntityType.fromName(args[2]);
                    if(type == null) {
                        player.sendMessage(Utils.color("&c&lMineX &7| Invalid mob type!"));
                        return false;
                    }
                    MobSpawn ms = new MobSpawn(loc, game.getId());
                    ms.addEntity(args[2]);
                    game.addMobSpawn(ms);

                } else if(args[0].equalsIgnoreCase("tp")) {
                    if (args.length < 2) {
                        player.sendMessage(Message.GAME_TP_USAGE.getMessage());
                    } else {
                        String id = args[1];
                        Game game = GameManager.getGame(id);
                        int index = (args.length >= 3) ? Integer.valueOf(args[2]) : 0;
                        if (game == null) {
                            player.sendMessage(Message.NO_GAME.getMessage());
                        } else {
                            Arena arena = game.getArena();
                            if (index > arena.getSpawns().size()) {
                                player.sendMessage(Message.NO_SPAWN.getMessage());
                            } else {
                                player.teleport(arena.getSpawn(index));
                            }
                        }
                    }
                } else if(args[0].equalsIgnoreCase("leave")) {
                    mPlayer mp = PlayerManager.getmPlayer(player.getUniqueId());
                    Game game = mp.getCurrGame();
                    if (game == null) {
                        player.sendMessage(Utils.color("&c&lMineX &7| You are not in a game!"));
                    } else {
                        game.leaveGame(player.getUniqueId());
                        player.sendMessage(Utils.color("&c&lMineX &7| You have left a game!"));
                    }
                } else if(args[0].equalsIgnoreCase("stash")) {
                    mPlayer mp = PlayerManager.getmPlayer(player.getUniqueId());
                    Game game = mp.getCurrGame();
                    new StashGui().makeGui(player);
                } else if(args[0].equalsIgnoreCase("lootchest")) {
                    if(args.length < 2) {
                        player.sendMessage(Utils.color("&c&lMineX &7| Lootchest help!"));
                    } else {
                        if(args[1].equalsIgnoreCase("give")) {
                            if (args.length < 3) {
                                player.sendMessage(Utils.color("&c&lMineX &7| Lootchest give usage"));
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
                                    player.sendMessage(Utils.color("&c&lMineX &7| You have given yourself a uncommon lootchest"));
                                } else if (type.equalsIgnoreCase("common")) {
                                    ItemStack nbtStack = CraftItemStack.asNMSCopy(      common);
                                    NBTTagCompound tag = (nbtStack.hasTag() ? nbtStack.getTag() : new NBTTagCompound());
                                    tag.setString("type", "common");
                                    common = CraftItemStack.asBukkitCopy(nbtStack);

                                    player.getInventory().addItem(common);
                                    player.sendMessage(Utils.color("&c&lMineX &7| You have given yourself a common lootchest"));
                                } else if (type.equalsIgnoreCase("rare")) {
                                    ItemStack nbtStack = CraftItemStack.asNMSCopy(rare);
                                    NBTTagCompound tag = (nbtStack.hasTag() ? nbtStack.getTag() : new NBTTagCompound());
                                    tag.setString("type", "rare");
                                    rare = CraftItemStack.asBukkitCopy(nbtStack);

                                    player.getInventory().addItem(rare);
                                    player.sendMessage(Utils.color("&c&lMineX &7| You have given yourself a common lootchest"));
                                } else {
                                    player.sendMessage(Utils.color("&c&lMineX &7| Invalid type!"));
                                }
                            }
                        }
                    }
                } else if(args[0].equalsIgnoreCase("addSpawn")) {
                    if (args.length < 3 || args.length > 3) {
                        player.sendMessage(Message.GAME_ADDSPAWN_USAGE.getMessage());
                    } else {
                        String id = args[1];
                        String name = args[2];
                        Game game = GameManager.getGame(id);
                        if (game == null) {
                            player.sendMessage(Message.NO_GAME.getMessage());
                        } else {
                            if(game.getArena().exists(name)) {
                                player.sendMessage(Utils.color("&c&lMineX &7| A game already exists with this name"));
                            } else {
                                game.addSpawn(player.getLocation(), name);
                                player.sendMessage(Message.GAME_SPAWN_SET.getMessage());
                            }
                        }
                    }
                } else if(args[0].equalsIgnoreCase("spawns")) {
                    if (args.length < 2) {
                        mPlayer mp = PlayerManager.getmPlayer(player.getUniqueId());
                        if(mp.getCurrGame() == null) {
                            player.sendMessage(Utils.color("&c&lMineX &7| You must be in a game to do this!"));
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
                } else if(args[0].equalsIgnoreCase("lobby")) {
                    if(args.length < 2) {
                        player.sendMessage("game lobby help command");
                    } else {
                        if(args[1].equalsIgnoreCase("setspawn")) {
                            if(args.length < 3) {
                                player.sendMessage(Message.GAME_LOBBY_SPAWN_USAGE.getMessage());
                            } else {
                                String id = args[2];
                                Game game = GameManager.getGame(id);
                                if(game == null) {
                                    player.sendMessage(Message.NO_GAME.getMessage());
                                } else {
                                    game.setLobbySpawn(player.getLocation());
                                    player.sendMessage(Message.GAME_SPAWN_SET.getMessage());
                                }
                            }
                        } else if(args[1].equalsIgnoreCase("tp")) {
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
                }
            }
        }
        return false;
    }

}
