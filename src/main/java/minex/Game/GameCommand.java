package minex.Game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import minex.Arena.Arena;
import minex.Arena.Lobby;
import minex.Gui.GameSelectorGui;
import minex.Main;
import minex.Managers.GameManager;
import minex.Messages.Message;
import minex.Party.Party;
import minex.Player.mPlayer;
import minex.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import redis.clients.jedis.Builder;
import redis.clients.jedis.Pipeline;

import java.util.List;
import java.util.UUID;

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
                    for(Game game : GameManager.games) {
                        player.sendMessage(game.getId());
                    }
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
                            if(index > arena.getSpawns().size()) {
                                player.sendMessage(Message.NO_SPAWN.getMessage());
                            } else {
                                player.teleport(arena.getSpawn(index));
                            }
                        }
                    }
                } else if(args[0].equalsIgnoreCase("addSpawn")) {
                    if (args.length < 2) {
                        player.sendMessage(Message.GAME_ADDSPAWN_USAGE.getMessage());
                    } else {
                        String id = args[1];
                        Game game = GameManager.getGame(id);
                        if (game == null) {
                            player.sendMessage(Message.NO_GAME.getMessage());
                        } else {
                            game.addSpawn(player.getLocation());
                            player.sendMessage(Message.GAME_SPAWN_SET.getMessage());
                        }
                    }
                } else if(args[0].equalsIgnoreCase("spawns")) {
                    if (args.length < 2) {
                        player.sendMessage("game spawn usage");
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
