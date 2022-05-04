package minex.CustomEnchants;

import minex.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomEnchantManager {

    Main main = Main.getInstance();
    private static List<CustomEnchant> enchants = new ArrayList<>();
    private int id = 0;

    public void loadEnchants() {
        File file = new File(main.getDataFolder().getAbsolutePath() + "/Enchantments.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.getConfigurationSection("enchants").getKeys(false).forEach(key -> {
            CustomEnchant enchant = new CustomEnchant(id, key, config.getString("enchants." + key + ".displayName"), config.getInt("enchants." + key + ".maxLevel"), config.getInt("enchants." + key + ".startLevel"), config.getString("enchants." + key + ".description"), EnchantmentType.valueOf(config.getString("enchants." + key + ".type")));
            main.getServer().getPluginManager().registerEvents(enchant, main);
            enchants.add(enchant);
            id = id + 1;
        });
    }

    public void register() {
        for(Enchantment ce : enchants) {
            try {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, true);
                Enchantment.registerEnchantment(ce);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void runEffect(Object o) {
    }

}
