package minex.Utils;

import minex.Gui.Gui;
import minex.Main;
import net.minecraft.server.v1_8_R3.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String toString(Location loc) {
        return loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
    }

    public static Location fromString(String s) {
        String[] split = s.split(";");
        return new Location(Bukkit.getWorld(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]));
    }

    public static void makeFormat(String file, Gui.NoobPage gui, String keyForItems) {

        File f = new File(Main.getInstance().getDataFolder().getAbsoluteFile() + "/Guis/" + file);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
        List<String> toFormat = config.getStringList("format");
        int size = toFormat.size() * 9;

        if(toFormat.size() == size / 9) {
            for(int i = 0; i < (size / 9); i++) {
                String s = toFormat.get(i);
                for(int z = 0; z < 9; z++) {
                    String removeSpaces = s.replaceAll(" ", "");
                    char individual = removeSpaces.charAt(z);
                    if(config.get(keyForItems + "." + individual) == null) continue;
                    gui.i((9 * i) + z, Material.matchMaterial(config.getString(keyForItems + "." + individual + ".material")), (short) config.getInt(keyForItems + "." + individual + ".damage"), (config.getInt(keyForItems + "." + individual + ".amount") == 0) ? 1 : config.getInt(keyForItems + "." + individual + ".amount"),   color(config.getString(keyForItems + "." + individual + ".name")), color(config.getStringList(keyForItems + "." + individual + ".lore")));
                }

            }

        }
    }

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> color(List<String> list) {
        List<String> colored = new ArrayList<>();
        for(String s : list) {
            colored.add(color(s));
        }
        return colored;
    }

}
