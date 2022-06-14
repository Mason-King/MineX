package minex.Gui;

import minex.Main;
import minex.Utils.Utils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class TraderGui {

    Main main = Main.getInstance();

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/TraderGui.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    int task = config.getInt("tasks");
    int sell = config.getInt("sell");

    public Gui makeGui(Player p, String type) {
        Gui gui = new Gui(main);
        Gui.NoobPage g  = gui.create(Utils.color(config.getString("title").replace("{type}", WordUtils.capitalizeFully(type))), config.getStringList("format").size() * 9).c().s();
        Utils.makeFormat("TraderGui.yml", g, "items");

        g.onClick(e -> {
            int slot = e.getSlot();
            if(slot == task) {
                new TaskGui().makeGui(p, type).show(p,0);
            } else if(slot == sell) {
                new SellGui().makeGui(p, type).show(p,0);
            } else {
                e.setCancelled(true);
                return;
            }
            System.out.println(6);
        });

        return gui;
    }

}
