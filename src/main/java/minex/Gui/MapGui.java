package minex.Gui;

import minex.Game.Game;
import minex.Main;
import minex.Managers.GameManager;
import minex.Party.Party;
import minex.Player.mPlayer;
import minex.Utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class MapGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/MapSelector.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);


    public void makeGui(Player p, Game game) {
        Gui.NoobPage g  = gui.create(Utils.color(config.getString("title")), config.getStringList("format").size() * 9).c().s();
        Utils.makeFormat("MapSelector.yml", g, "items");
        Utils.makeSpawnFormat(game,"MapSelector.yml", g, "items");
        gui.show(p, 0);
    }

}
