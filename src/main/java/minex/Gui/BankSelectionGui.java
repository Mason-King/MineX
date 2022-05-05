package minex.Gui;

import minex.Main;
import minex.Utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class BankSelectionGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/BankSelection.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);


    public void makeGui(Player p, String type) {
        Gui.NoobPage g  = gui.create(Utils.color(config.getString("title")), config.getStringList("format").size() * 9).c().s();
        Utils.makeBankFormat("BankSelection.yml", g, "items", p);
        gui.show(p, 0);

        g.onClick(e -> {
            int slot = e.getSlot();

            if(type.equalsIgnoreCase("withdraw")) {
                config.getConfigurationSection("items").getKeys(false).forEach(key -> {
                    int amount = 0;
                    if(config.getInt("items." + key + ".addAmount") > 0) {
                        amount =+ config.getInt("items." + key + ".addAmount");
                    }
                });

            } else if(type.equalsIgnoreCase("deposit")) {
                int amount = 0;
            }
        });
    }

}
