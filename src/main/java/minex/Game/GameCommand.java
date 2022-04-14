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
                        if(Game.getGame(id) != null) {
                            player.sendMessage("There is no game with this id!");
                        } else {
                            player.sendMessage("Creating a new game with the id " + id);
                            Game game = new Game(id);
                        }
                    }
                } else if(args[0].equalsIgnoreCase("list")) {
                    for(Game game : Game.games) {
                        player.sendMessage(game.getId());
                    }
                } else if(args[0].equalsIgnoreCase("tp")) {

                }
            }
        }
        return false;
    }

}
