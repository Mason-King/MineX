package minex.Game;

import minex.Gui.BankGui;
import minex.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankCommand implements CommandExecutor {

    public BankCommand() {
        Main.getInstance().getCommand("bank").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player p = (Player) commandSender;
        new BankGui().makeGui(p);
        return false;
    }
}
