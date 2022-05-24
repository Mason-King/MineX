package minex.Gui;

import minex.Main;
import minex.Utils.Utils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class TraderGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/TraderGui.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    int task = config.getInt("tasks");
    int sell = config.getInt("sell");

    public void makeGui(Player p, String type) {
        System.out.println(gui);
        Gui.NoobPage g  = gui.create(Utils.color(config.getString("title").replace("{type}", WordUtils.capitalizeFully(type))), config.getStringList("format").size() * 9).c().s();
        Utils.makeFormat("TraderGui.yml", g, "items");
        System.out.println(g);

        g.onClick(e -> {
            System.out.println("here?");
           int slot = e.getSlot();
            System.out.println(slot);
           if(slot == task) {
               System.out.println("here?");
                new TaskGui().makeGui(p, type);
           } else if(slot == sell) {
               System.out.println("no");
               p.closeInventory();
               new SellGui().makeGui(p, type);
           } else {
               e.setCancelled(true);
               return;
           }
        });

        gui.show(p, 0);
    }

}
