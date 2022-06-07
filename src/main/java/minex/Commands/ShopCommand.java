package minex.Commands;

import minex.Gui.Gui;
import minex.Gui.TraderGui;
import minex.Main;
import minex.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand implements CommandExecutor {

    Main main = Main.getInstance();

    public ShopCommand() {
        Main.getInstance().getCommand("trader").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player) commandSender;
        if(args.length < 1) {
            for(String sm : Utils.color(main.getConfig().getStringList("helpCommand"))) {
                p.sendMessage(sm);
            }
            return false;
        }
        Gui gui = new TraderGui().makeGui(p, args[0]);
        gui.show(p, 0);
        return false;
    }
}
