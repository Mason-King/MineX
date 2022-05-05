package minex.Gui;

import minex.Game.Game;
import minex.Main;
import minex.Managers.PlayerManager;
import minex.Player.mPlayer;
import minex.Utils.Utils;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class BankGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/Bank.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);


    public void makeGui(Player p) {
        Gui.NoobPage g  = gui.create(Utils.color(config.getString("title")), config.getStringList("format").size() * 9).c().s();
        Utils.makeBankFormat("Bank.yml", g, "items", p);
        gui.show(p, 0);

        g.onClick(e -> {
            int slot = e.getSlot();
            int deposit = config.getInt("deposit");
            int withdraw = config.getInt("withdraw");

            if(slot == deposit) {
                p.closeInventory();
                new BankSelectionGui().makeGui(p, "deposit");
            } else if(slot == withdraw) {
                p.closeInventory();
                new BankSelectionGui().makeGui(p, "withdraw");
            }
        });
    }

}
