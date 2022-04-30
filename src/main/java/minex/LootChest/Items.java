package minex.LootChest;

import minex.Main;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Items {

    private static List<LootItem> uncommonItems = new ArrayList<>();;
    private static List<LootItem> commonItems = new ArrayList<>();;
    private static List<LootItem> rareItems = new ArrayList<>();;

    public static List<LootItem> getItems(LootType type) {
        if(type.equals(LootType.uncommon)){
            return uncommonItems;
        } else if(type.equals(LootType.common)) {
            return commonItems;
        } else if(type.equals(LootType.rare)) {
            return rareItems;
        }
        return null;
    }

    //load items from config
    public static void loadItem(Main m) {
        for (String key : m.getConfig().getConfigurationSection("lootChest.items.uncommon").getKeys(false)) {
            uncommonItems.add(new LootItem(new ItemStack(Material.matchMaterial(m.getConfig().getString("lootChest.items.uncommon." + key + ".material")), m.getConfig().getInt("lootChest.items.uncommon." + key + ".amount"), (short) m.getConfig().getInt("lootChest.items.uncommon." + key + ".damage")), m.getConfig().getDouble("lootChest.items.uncommon." + key + ".chance")));
        }
        for (String key : m.getConfig().getConfigurationSection("lootChest.items.common").getKeys(false)) {
            commonItems.add(new LootItem(new ItemStack(Material.matchMaterial(m.getConfig().getString("lootChest.items.common." + key + ".material")), m.getConfig().getInt("lootChest.items.common." + key + ".amount"), (short) m.getConfig().getInt("lootChest.items.common." + key + ".damage")), m.getConfig().getDouble("lootChest.items.common." + key + ".chance")));
        }
        for (String key : m.getConfig().getConfigurationSection("lootChest.items.rare").getKeys(false)) {
            rareItems.add(new LootItem(new ItemStack(Material.matchMaterial(m.getConfig().getString("lootChest.items.rare." + key + ".material")), m.getConfig().getInt("lootChest.items.rare." + key + ".amount"), (short) m.getConfig().getInt("lootChest.items.rare." + key + ".damage")), m.getConfig().getDouble("lootChest.items.rare." + key + ".chance")));
        }
    }

}
