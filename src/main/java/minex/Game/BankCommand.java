package minex.Game;

import minex.Gui.BankGui;
import minex.Main;
import minex.Utils.Utils;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BankCommand implements CommandExecutor {

    Main main = Main.getInstance();

    public BankCommand() {
        Main.getInstance().getCommand("bank").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player) commandSender;
        if(args.length == 0) {
            new BankGui().makeGui(p);
        } else if(args[0].equalsIgnoreCase("give")) {
            if(p.hasPermission("economy.give")) {
                if(args.length < 2) {
                    p.sendMessage(Utils.color("&c&lMineX &7| Invalid usage : /bank give <amount>"));
                    return false;
                }
                ItemStack ingot = new ItemStack(Material.matchMaterial(main.getConfig().getString("economy.item.material")));
                ItemMeta im = ingot.getItemMeta();
                im.setDisplayName(Utils.color(main.getConfig().getString("economy.item.name")));
                im.setLore(Utils.color(main.getConfig().getStringList("economy.item.lore")));
                ingot.setItemMeta(im);

                net.minecraft.server.v1_8_R3.ItemStack nbtStack = CraftItemStack.asNMSCopy(ingot);
                NBTTagCompound nbt = (nbtStack.hasTag() ? nbtStack.getTag() : new NBTTagCompound());
                nbt.setBoolean("eco", true);
                ingot = CraftItemStack.asBukkitCopy(nbtStack);

                for(int i = 0; i < Integer.parseInt(args[1]); i++) {
                    p.getInventory().addItem(ingot);
                }
            }
        }
        return false;
    }
}