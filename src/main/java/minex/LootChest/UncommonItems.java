package minex.LootChest;

import minex.Main;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class UncommonItems {

    private static List<LootItem> lootItems = new ArrayList<>();;

    public static List<LootItem> getItems() {
        return lootItems;
    }

    //load items from config
    static {
        Main m = Main.getPlugin(Main.class);
        for (String key : m.getConfig().getConfigurationSection("lootChest.items.uncommon").getKeys(true)) {
            lootItems.add(new LootItem(new ItemStack(Material.matchMaterial(m.getConfig().getString("lootChest.items.uncommon." + key + ".material")), m.getConfig().getInt("lootChest.items.uncommon." + key + ".amount"), (short) m.getConfig().getInt("lootChest.items.uncommon." + key + ".damage")), m.getConfig().getDouble("lootChest.items.uncommon." + key + ".chance")));
        }
    }

}
