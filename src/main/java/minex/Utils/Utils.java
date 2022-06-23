package minex.Utils;

import minex.Objects.Game;
import minex.Gui.Gui;
import minex.Main;
import minex.Managers.PlayerManager;
import minex.Objects.mPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static String toString(Location loc) {
        return loc.getWorld().getName() + ";" + loc.getX()+ ";" + loc.getY() + ";" + loc.getZ();
    }

    public static Location fromString(String s) {
        String[] split = s.split(";");
        return new Location(Bukkit.getWorld(split[0]), Double.valueOf(split[1]), Double.valueOf(split[2]), Double.valueOf(split[3]));
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


    public static Gui.GuiPage makeFormat(String file, Gui.GuiPage gui, String keyForItems) {

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
                    gui.setItem((9 * i) + z, new ItemStack(Material.matchMaterial(config.getString(keyForItems + "." + individual + ".material")), (config.getInt(keyForItems + "." + individual + ".amount") == 0) ? 1 : config.getInt(keyForItems + "." + individual + ".amount"),  (short) config.getInt(keyForItems + "." + individual + ".damage")),   color(config.getString(keyForItems + "." + individual + ".name")), color(config.getStringList(keyForItems + "." + individual + ".lore")));
                }

            }

        }
        return gui;
    }

    public static Gui.GuiPage makeTemplate(String file, Gui.GuiPage gui, String keyForItems) {

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
                    gui.setItem((9 * i) + z, new ItemStack(Material.matchMaterial(config.getString(keyForItems + "." + individual + ".material")),(config.getInt(keyForItems + "." + individual + ".amount") == 0) ? 1 : config.getInt(keyForItems + "." + individual + ".amount"), (short) config.getInt(keyForItems + "." + individual + ".damage")), color(config.getString(keyForItems + "." + individual + ".name")), color(config.getStringList(keyForItems + "." + individual + ".lore")));
                }

            }

        }
        return gui;
    }

    public static void makeSelectorFormat(String file, Gui.NoobPage gui, String keyForItems, String type) {

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
                    List<String> lore = new ArrayList<>();
                    for(String loreString : config.getStringList(keyForItems + "." + individual + ".lore")) {
                        lore.add(loreString.replace("{type}", type));
                    }
                    gui.i((9 * i) + z, Material.matchMaterial(config.getString(keyForItems + "." + individual + ".material")), (short) config.getInt(keyForItems + "." + individual + ".damage"), (config.getInt(keyForItems + "." + individual + ".amount") == 0) ? 1 : config.getInt(keyForItems + "." + individual + ".amount"),   color(config.getString(keyForItems + "." + individual + ".name").replace("{amount}", "1")), color(lore));
                }

            }

        }
    }

    public static void makeBankFormat(String file, Gui.NoobPage gui, String keyForItems, Player p) {

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
                    gui.i((9 * i) + z, Material.matchMaterial(config.getString(keyForItems + "." + individual + ".material")), (short) config.getInt(keyForItems + "." + individual + ".damage"), (config.getInt(keyForItems + "." + individual + ".amount") == 0) ? 1 : config.getInt(keyForItems + "." + individual + ".amount"),   color(config.getString(keyForItems + "." + individual + ".name").replace("{bal}", "" + PlayerManager.getmPlayer(p.getUniqueId()).getBalance())), color(config.getStringList(keyForItems + "." + individual + ".lore")));
                }

            }

        }
    }

    public static void makeUpgradeFormat(String file, Gui.NoobPage gui, String keyForItems, Player p) {

        File f = new File(Main.getInstance().getDataFolder().getAbsoluteFile() + "/Guis/" + file);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
        List<String> toFormat = config.getStringList("format");
        int size = toFormat.size() * 9;
        mPlayer mp = PlayerManager.getmPlayer(p.getUniqueId());

        if(toFormat.size() == size / 9) {
            for(int i = 0; i < (size / 9); i++) {
                String s = toFormat.get(i);
                for(int z = 0; z < 9; z++) {
                    String removeSpaces = s.replaceAll(" ", "");
                    char individual = removeSpaces.charAt(z);
                    if(config.get(keyForItems + "." + individual) == null) continue;
                    ItemStack stack = new ItemStack(Material.matchMaterial(config.getString(keyForItems + "." + individual + ".material")),(config.getInt(keyForItems + "." + individual + ".amount") == 0) ? 1 : config.getInt(keyForItems + "." + individual + ".amount"), (short) config.getInt(keyForItems + "." + individual + ".damage"));
                    ItemMeta im = stack.getItemMeta();
                    im.setDisplayName(color(config.getString(keyForItems + "." + individual + ".name")));
                    List<String> lore = new ArrayList<>();
                    for(String str : config.getStringList(keyForItems + "." + individual + ".lore")) {
                        if(((9 * i) + z) == config.getInt("farm")) {
                            lore.add(Utils.color(str)
                                    .replace("{level}", mp.getFarmLevel() + "")
                                    .replace("{nextLevel}", (mp.getFarmLevel() + 1 )+ "")
                                    .replace("{cost}", config.getInt("upgrades.farm." + (mp.getFarmLevel() + 1) + ".cost") + ""));
                        } else if(((9 * i) + z) == config.getInt("stash")) {
                            lore.add(Utils.color(str)
                                    .replace("{level}", mp.getStashLevel() + "")
                                    .replace("{nextLevel}", (mp.getStashLevel() + 1 )+ "")
                                    .replace("{cost}", config.getInt("upgrades.stash." + (mp.getStashLevel() + 1) + ".cost") + ""));
                        } else {
                            lore.add(Utils.color(str));
                        }
                    }
                    im.setLore(lore);
                    stack.setItemMeta(im);

                    gui.setItem((9 * i) + z, stack);
                }
            }

        }
    }

    public static void makeSpawnFormat(Game game, String file, Gui.NoobPage gui, String keyForItems) {

        File f = new File(Main.getInstance().getDataFolder().getAbsoluteFile() + "/Guis/" + file);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

//        for(int i = 1; i < game.getArena().getSpawns().size(); i++) {
//            ItemStack stack = getItem(new ItemStack(Material.matchMaterial(config.getString("spawnItem.material")), (config.getInt("spawnItem.amount") == 0) ? 1 : config.getInt("spawnItem.amount"), (short) config.getInt("spawnItem.damage")), (config.getString("spawnItem.name")).replace("{name}", (game.getArena().getName(game.getArena().getSpawns().get(i)) == null) ? "null" : game.getArena().getName(game.getArena().getSpawns().get(i))), color(config.getStringList("spawnItem.lore")));
//            net.minecraft.server.v1_8_R3.ItemStack nbtStack = CraftItemStack.asNMSCopy(stack);
//            NBTTagCompound nbt = (nbtStack.hasTag()) ? nbtStack.getTag() : new NBTTagCompound();
//            String spawnId = (game.getArena().getName(game.getArena().getSpawns().get(i)) == null) ? "null" : game.getArena().getName(game.getArena().getSpawns().get(i));
//            nbt.setString("spawnId", spawnId);
//            nbtStack.setTag(nbt);
//            stack = CraftItemStack.asBukkitCopy(nbtStack);
//            gui.addItem(stack);
//        }

        config.getConfigurationSection(keyForItems).getKeys(false).forEach(key -> {
            gui.fill(new ItemStack(Material.matchMaterial(config.getString(keyForItems + "." + key + ".material")), (config.getInt(keyForItems + "." + key + ".amount") == 0 ? 1 : config.getInt(keyForItems + "." + key + ".amount")), (short) config.getInt(keyForItems + "." + key + ".damage")));
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

    public static String[] color(String... strings) {
        String[] s = new String[strings.length];
        for (int i = 0; i < strings.length; i++) {
            if (strings[i] != null)
                s[i] = ChatColor.translateAlternateColorCodes('&', strings[i]);
        }
        return s;
    }

    public static ItemStack getItem(ItemStack item, String name, List<String> lore) {
        ItemMeta im = item.getItemMeta();
        if (name != null)
            im.setDisplayName(color(name));
        im.setLore(lore);
        item.setItemMeta(im);
        return item;
    }

    public static ItemStack getItem(ItemStack item, String name, String... lore) {
        ItemMeta im = item.getItemMeta();
        if (name != null)
            im.setDisplayName(color(name));
        im.setLore(Arrays.asList(color(lore)));
        item.setItemMeta(im);
        return item;
    }

}
