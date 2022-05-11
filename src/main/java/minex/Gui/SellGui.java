package minex.Gui;

import minex.Main;
import minex.Managers.PlayerManager;
import minex.Player.mPlayer;
import minex.Utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

public class SellGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/Sell.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    File file2 = new File(main.getDataFolder().getAbsolutePath() + "/shops.yml");
    YamlConfiguration shopConfig = YamlConfiguration.loadConfiguration(file2);


    public void makeGui(Player p, String type) {
        Gui.NoobPage g  = gui.create(Utils.color(config.getString("title").replace("{type}", WordUtils.capitalizeFully(type))), config.getInt("size"));

        g.onClose(e -> {
            Player player = (Player) e.getPlayer();
            mPlayer mp = PlayerManager.getmPlayer(player.getUniqueId());
            if(g.getContents() == null) return;
            for(ItemStack stack : g.getContents()) {
                if(stack == null || stack.getType().equals(Material.AIR)) return;
                if(shopConfig.isSet(type + "." + stack.getType().name().toUpperCase())) {
                    int sell = shopConfig.getInt(type + "." + stack.getType().name().toUpperCase());
                    mp.setBalance(mp.getBalance() + sell);
                } else {
                    player.getInventory().addItem(stack);
                }
            }
        });

        gui.show(p, 0);
    }

}
