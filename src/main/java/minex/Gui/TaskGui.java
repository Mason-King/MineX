package minex.Gui;

import minex.Main;
import minex.Managers.PlayerManager;
import minex.Player.mPlayer;
import minex.Quests.Quest;
import minex.Utils.Utils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;

public class TaskGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/Task.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);


    public void makeGui(Player p, String type) {
        Gui.NoobPage g  = gui.create(Utils.color(config.getString("title").replace("{type}", WordUtils.capitalizeFully(type))), config.getStringList("format").size() * 9).c().s();
        Utils.makeFormat("Task.yml", g, "items");

        mPlayer mp = PlayerManager.getmPlayer(p.getUniqueId());

        for(Quest q : mp.getQuests()) {
            if(q.getTraderType().equalsIgnoreCase(type)) {
                if(q.isCompleted()) {
                    ItemStack stack = new ItemStack(Material.BOOK);
                    ItemMeta im = stack.getItemMeta();
                    im.setDisplayName(Utils.color(q.getName()));
                    im.setLore(q.getLore());
                    stack.setItemMeta(im);
                    g.addItem(stack);
                } else {
                    ItemStack stack = new ItemStack(Material.PAPER);
                    ItemMeta im = stack.getItemMeta();
                    im.setDisplayName(Utils.color(q.getName()));
                    im.setLore(q.getLore());
                    stack.setItemMeta(im);
                    g.addItem(stack);
                }
            }
        }

        gui.show(p, 0);
    }

}
