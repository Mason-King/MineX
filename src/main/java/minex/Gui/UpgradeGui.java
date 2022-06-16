package minex.Gui;

import minex.Enums.Message;
import minex.Main;
import minex.Managers.PlayerManager;
import minex.Objects.mPlayer;
import minex.Utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class UpgradeGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/UpgradeGui.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);


    public void makeGui(Player p) {
        Gui.NoobPage g  = gui.create(Utils.color(config.getString("title")), config.getStringList("format").size() * 9).c().s();
        Utils.makeUpgradeFormat("UpgradeGui.yml", g, "items", p);
        gui.show(p, 0);

        g.onClick(e -> {
            int slot = e.getSlot();
            int farm = config.getInt("farm");
            int stash = config.getInt("stash");
            mPlayer mp = PlayerManager.getmPlayer(e.getWhoClicked().getUniqueId());

            if(slot == farm) {
                int nextLevel = mp.getFarmLevel() + 1;
                int cost = Main.getInstance().getConfig().getInt("upgrades.farm." + nextLevel + ".cost");
                if(cost == 0) return;
                if(mp.getBalance() >= cost) {
                    //enough
                } else {
                    p.sendMessage(Message.NO_MONEY.getMessage());
                    return;
                }
                int up = Main.getInstance().getConfig().getInt("upgrades.farm." + nextLevel + ".amount");
                mp.setFarmLevel(nextLevel);
                mp.setFarmLimit(up);
            } else if(slot == stash) {

            }
        });
    }

}
