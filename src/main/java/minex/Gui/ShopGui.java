package minex.Gui;

import minex.Main;
import minex.Utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.List;

public class ShopGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/Shop.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    YamlConfiguration shopConfig = YamlConfiguration.loadConfiguration(new File(main.getDataFolder().getAbsoluteFile() + "/shops.yml"));


    public void makeGui(Player p, String type) {
        Gui.NoobPage g  = gui.create(Utils.color(config.getString("title")), config.getStringList("format").size() * 9).c().s();
        Utils.makeBankFormat("Shop.yml", g, "items", p);

        String name = config.getString("shopItem.name");
        List<String> lore = config.getStringList("shopItem.lore");

        shopConfig.getConfigurationSection(type).getKeys(false).forEach(key -> {
            System.out.println(key);
            System.out.println(shopConfig.getString(type + "." +  key + ".id"));
            System.out.println(Material.matchMaterial(shopConfig.getString(type + "." +  key + ".id")));
            ItemStack stack = new ItemStack(Material.matchMaterial(shopConfig.getString(type + "." +  key + ".id")), (shopConfig.getInt(type + "." + key + ".amount") == 0 ? 1 : shopConfig.getInt(type + "." + key + ".amount")), (short) shopConfig.getInt(type + "." + key + ".damage"));
            ItemMeta im = stack.getItemMeta();
            im.setDisplayName(Utils.color(name).replace("{material}", stack.getType().name()));
            for(String s : lore) {
               if(s.contains("{buy}")) {
                   if(shopConfig.isSet(type + "." + key + ".buy-price")) {
                       s.replace("{buy}", "" + shopConfig.getInt(type + "." + key + ".buy-price"));
                   } else {
                       lore.remove(s);
                   }
               }
               if(s.contains("{sell}")) {
                   if(shopConfig.isSet(type + "." + key + ".sell-price")) {
                       s.replace("{sell}", "" + shopConfig.getInt(type + "." + key + ".sell-price"));
                   } else {
                       lore.remove(s);
                   }
               }
            }
        });


        gui.show(p, 0);
    }

}
