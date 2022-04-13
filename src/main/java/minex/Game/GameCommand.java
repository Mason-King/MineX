package minex.Game;

import minex.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameCommand implements CommandExecutor {

    Main main = Main.getInstance();

    public GameCommand() {
        main.getCommand("game").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command");
        } else {
            Player player = (Player) sender;
            if(args.length == 0) {
                player.sendMessage("Game help command!");
            } else {
                if(args[0].equalsIgnoreCase("create")) {
                    if(args.length < 2) {
                        player.sendMessage("Invalid game create usage");
                    } else {
                        String id = args[1];
                        for(Game game : Game.allGames) {
                            if(game.getId() == id) {
                                player.sendMessage("A game already exists");
                                return false;
                            }
                        }
                        Game game = new Game(id);
                    }
                } else if(args[0].equalsIgnoreCase("list")) {
                    for(Game game : Game.allGames) {
                        player.sendMessage(game.getId());
                    }
                } else if(args[0].equalsIgnoreCase("tp")) {
                    if(args.length < 2) {
                        player.sendMessage("Invalid tp command");
                    } else {
                        String id = args[1];
                        Game game = Game.getGame(id);
                        if(game == null) {
                            player.sendMessage("There is no game with this id!");
                        } else {
                            game.teleport(player);
                            player.sendMessage("You have been teleported to the game arena!");
                        }
                    }
                }
            }
        }
        return false;
    }
}
