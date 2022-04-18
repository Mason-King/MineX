package minex.Game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import minex.Arena.Arena;
import minex.Arena.Lobby;
import minex.Main;
import minex.Managers.GameManager;
import minex.Utils.Utils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import redis.clients.jedis.Pipeline;

import java.util.List;

public class GameCommand implements CommandExecutor {

    Main main = Main.getInstance();

    public GameCommand() {
        main.getCommand("game").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))  {
            sender.sendMessage("You must be a player!");
        } else {
            Player player = (Player) sender;
            if(args.length == 0) {
                player.sendMessage("Game command help!");
            } else {
                if(args[0].equalsIgnoreCase("create")) {
                    if(args.length < 2) {
                        player.sendMessage("Invalid usage!");
                    } else {
                        String id = args[1];
                        if(GameManager.getGame(id) != null) {
                            player.sendMessage("There is no game with this id!");
                        } else {
                            player.sendMessage("Creating a new game with the id " + id);
                            Game game = GameManager.createGame(id);
                        }
                    }
                } else if(args[0].equalsIgnoreCase("list")) {
                    for(Game game : GameManager.games) {
                        player.sendMessage(game.getId());
                    }
                } else if(args[0].equalsIgnoreCase("tp")) {
                    if (args.length < 2) {
                        player.sendMessage("Game tp command usage");
                    } else {
                        String id = args[1];
                        Game game = GameManager.getGame(id);
                        int index = (args.length >= 3) ? Integer.valueOf(args[2]) : 0;
                        if (game == null) {
                            player.sendMessage("There is no game with this id!");
                        } else {
                            Arena arena = game.getArena();
                            if(index > arena.getSpawns().size()) {
                                player.sendMessage("There is no spawn with this index!");
                            } else {
                                player.teleport(arena.getSpawn(index));
                            }
                        }
                    }
                } else if(args[0].equalsIgnoreCase("addSpawn")) {
                    if (args.length < 2) {
                        player.sendMessage("game addspawn usage");
                    } else {
                        String id = args[1];
                        Game game = GameManager.getGame(id);
                        if (game == null) {
                            player.sendMessage("There is no game with this id!");
                        } else {
                            game.addSpawn(player.getLocation());
                            player.sendMessage("You have added a spawn to your location!");
                        }
                    }
                } else if(args[0].equalsIgnoreCase("spawns")) {
                    if (args.length < 2) {
                        player.sendMessage("game addspawn usage");
                    } else {
                        String id = args[1];
                        Game game = GameManager.getGame(id);
                        if (game == null) {
                            player.sendMessage("There is no game with this id!");
                        } else {
                            List<String> spawns = game.getArena().getSpawns();
                            for(int i = 0; i < spawns.size(); i++) {
                                Location loc = Utils.fromString(spawns.get(i));
                                player.sendMessage(i + ": " + "x: " + loc.getBlockX() + " y:" + loc.getBlockY() + " z:" + loc.getBlockZ());
                            }
                        }
                    }
                } else if(args[0].equalsIgnoreCase("lobby")) {
                    if(args.length < 2) {
                        player.sendMessage("game lobby help command");
                    } else {
                        if(args[1].equalsIgnoreCase("setspawn")) {
                            if(args.length < 3) {
                                player.sendMessage("game lobby setspawn usage");
                            } else {
                                String id = args[2];
                                Game game = GameManager.getGame(id);
                                if(game == null) {
                                    player.sendMessage("There is no game with this id!");
                                } else {
                                    game.setLobbySpawn(player.getLocation());
                                    player.sendMessage("You have set this game's lobby spawn to your location!");
                                }
                            }
                        } else if(args[1].equalsIgnoreCase("tp")) {
                            if(args.length < 3) {
                                player.sendMessage("Game lobby tp command usage");
                            } else {
                                String id = args[2];
                                Game game = GameManager.getGame(id);
                                if(game == null) {
                                    player.sendMessage("There is no game with this id!");
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
