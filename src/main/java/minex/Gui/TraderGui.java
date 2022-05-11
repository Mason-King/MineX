package minex.Gui;

import com.mysql.jdbc.StringUtils;
import minex.Main;
import minex.Managers.PlayerManager;
import minex.Player.mPlayer;
import minex.Utils.Utils;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class    TraderGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/Trader.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);


    public void makeGui(Player p, String type) {
        Gui.NoobPage g  = gui.create(Utils.color(config.getString("title").replace("{type}", WordUtils.capitalizeFully(type))), config.getStringList("format").size() * 9).c().s();
        Utils.makeFormat("Trader.yml", g, "items");

        g.onClick(e -> {
           int slot = e.getSlot();
           int task = config.getInt("task");
           int sell = config.getInt("sell");
           if(slot == task) {
                new TaskGui().makeGui(p, type);
           } else if(slot == sell) {
                new SellGui().makeGui(p, type);
           } else {
               e.setCancelled(true);
               return;
           }
        });

        gui.show(p, 0);
    }

}
