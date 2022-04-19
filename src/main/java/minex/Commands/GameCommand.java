package minex.Commands;

import minex.Game.Game;
import minex.Game.GameManager;
import minex.Gui.MapSelector;
import minex.Utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;
        if(args.length == 0) {
            player.sendMessage(Utils.color("&c&lMineX &7| Game help usage"));
        } else {
            if(args[0].equalsIgnoreCase("create")) {
                if(args.length < 2) {
                    player.sendMessage(Utils.color("&c&lMineX &7| Invalid command usage try: /game create <id>"));
                } else {
                    String id = args[1];
                    if(GameManager.exists(id)) {
                        player.sendMessage(Utils.color("&c&lMineX &7| A game with this id already exists!"));
                    } else {
                        Game game = new Game(id);
                        player.sendMessage(Utils.color("&c&lMineX &7| You have created a game with the id " + id));
                        player.sendMessage(Utils.color("&c&lMineX &7| Use /game tp to teleport to the game!"));
                        player.sendMessage(Utils.color("&c&lMineX &7| Use /game lobby tp to teleport to the game lobby!"));
                    }
                }
            } else if(args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {
                if(args.length < 2) {
                    player.sendMessage(Utils.color("&c&lMineX &7| Invalid command usage try: /game teleport <id>"));
                } else {
                    String id = args[1];
                    Game game = GameManager.getGame(id);
                    if(game == null) {
                        player.sendMessage(Utils.color("&c&lMineX &7| A game with this id does not exist!"));
                    } else {
                        new MapSelector().makeGui(player);
                    }
                }
            } else if(args[0].equalsIgnoreCase("addspawn")) {
                if(args.length < 2) {
                    player.sendMessage(Utils.color("&c&lMineX &7| Invalid command usage try: /game addspawn <id>"));
                } else {
                    String id = args[1];
                    Game game = GameManager.getGame(id);
                    if(game == null) {
                        player.sendMessage(Utils.color("&c&lMineX &7| A game with this id does not exist!"));
                    } else {
                        game.addSpawn(Utils.toString(player.getLocation()));
                        player.sendMessage(Utils.color("&c&lMineX &7| Added a new spawn at your current location!"));
                    }
                }
            }
        }
        return false;
    }
}
