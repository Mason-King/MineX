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
        System.out.println(1);
        Gui gui = new Gui(main);
        System.out.println(2);
        Gui.NoobPage g  = gui.create(Utils.color(config.getString("title").replace("{type}", WordUtils.capitalizeFully(type))), config.getStringList("format").size() * 9).c().s();
        System.out.println(3);
        Utils.makeFormat("TraderGui.yml", g, "items");

        System.out.println(4);
        g.onClick(e -> {
            System.out.println(5);
            int slot = e.getSlot();
            System.out.println(6);
            if(slot == task) {
                System.out.println(7);
                new TaskGui().makeGui(p, type).show(p,0);
                System.out.println(8);
            } else if(slot == sell) {
                System.out.println(9);
                new SellGui().makeGui(p, type).show(p,0);
                System.out.println(10);
            } else {
                System.out.println(11);
                e.setCancelled(true);
                return;
            }
            System.out.println(12);
        });

        System.out.println(13);

        return gui;
    }

}
