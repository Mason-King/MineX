package minex.Utils;

import minex.Game.Game;
import minex.Gui.Gui;
import minex.Main;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
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

    public static void makeSpawnFormat(Game game, String file, Gui.NoobPage gui, String keyForItems) {

        File f = new File(Main.getInstance().getDataFolder().getAbsoluteFile() + "/Guis/" + file);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

        for(int i = 1; i < game.getArena().getSpawns().size(); i++) {
            ItemStack stack = getItem(new ItemStack(Material.matchMaterial(config.getString("spawnItem.material")), (config.getInt("spawnItem.amount") == 0) ? 1 : config.getInt("spawnItem.amount"), (short) config.getInt("spawnItem.damage")), (config.getString("spawnItem.name")).replace("{name}", (game.getArena().getName(game.getArena().getSpawns().get(i)) == null) ? "null" : game.getArena().getName(game.getArena().getSpawns().get(i))), color(config.getStringList("spawnItem.lore")));
            net.minecraft.server.v1_8_R3.ItemStack nbtStack = CraftItemStack.asNMSCopy(stack);
            NBTTagCompound nbt = (nbtStack.hasTag()) ? nbtStack.getTag() : new NBTTagCompound();
            String spawnId = (game.getArena().getName(game.getArena().getSpawns().get(i)) == null) ? "null" : game.getArena().getName(game.getArena().getSpawns().get(i));
            nbt.setString("spawnId", spawnId);
            nbtStack.setTag(nbt);
            stack = CraftItemStack.asBukkitCopy(nbtStack);
            gui.addItem(stack);
        }

        config.getConfigurationSection(keyForItems).getKeys(false).forEach(key -> {
            gui.fill(new ItemStack(Material.matchMaterial(config.getString(keyForItems + "." + key + ".material")), config.getInt(keyForItems + "." + key + ".amount"), (short) config.getInt(keyForItems + "." + key + ".damage")));
        });

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

    public static ItemStack getItem(ItemStack item, String name, List<String> lore) {
        ItemMeta im = item.getItemMeta();
        if (name != null)
            im.setDisplayName(color(name));
        im.setLore(lore);
        item.setItemMeta(im);
        return item;
    }

}
