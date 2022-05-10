package minex.Gui;

import minex.Main;
import minex.Utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class ShopGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/Shop.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);


    public void makeGui(Player p) {
        gui.create(Utils.makeTemplate("Shop.yml", gui.createTemplate(Utils.color(config.getString("title")), config.getInt("size")), "items"), );
    }

}
