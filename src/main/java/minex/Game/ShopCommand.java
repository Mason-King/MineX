package minex.Game;

import minex.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand implements CommandExecutor {

    Main main = Main.getInstance();

    public ShopCommand() {
        Main.getInstance().getCommand("shop").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player) commandSender;
        new ShopGui().makeGui(p, args[0]);
        return false;
    }
}
