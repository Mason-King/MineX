package minex.Gui;

import minex.Main;
import minex.Managers.PlayerManager;
import minex.Utils.Utils;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BankSelectionGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/BankSelection.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    private int amount = 0;


    public void makeGui(Player p, String type) {
        Gui.NoobPage g  = gui.create(Utils.color(config.getString("title")), config.getStringList("format").size() * 9).c().s();
        Utils.makeSelectorFormat("BankSelection.yml", g, "items", type);
        gui.show(p, 0);
        g.onClick(e -> {
            Player clicked = (Player) e.getWhoClicked();
            int slot = e.getSlot();
            int confirm = config.getInt("confirm");

            List<String> keys = new ArrayList<>();

            List<String> toFormat = config.getStringList("format");

                for(int i = 0; i < (toFormat.size()); i++) {
                    String s = toFormat.get(i);
                    for(int z = 0; z < 9; z++) {
                        String removeSpaces = s.replaceAll(" ", "");
                        char individual = removeSpaces.charAt(z);
                        keys.add("" + individual);
                   }
                }


            if(type.equalsIgnoreCase("withdraw")) {
                if(slot > e.getInventory().getSize()) return;
                String key = keys.get(slot);
                if(slot == confirm) {
                    if(PlayerManager.getmPlayer(clicked.getUniqueId()).getBalance() >= amount) {
                        PlayerManager.getmPlayer(clicked.getUniqueId()).setBalance(PlayerManager.getmPlayer(clicked.getUniqueId()).getBalance() - amount);

                        ItemStack ingot = new ItemStack(Material.matchMaterial(main.getConfig().getString("economy.item.material")));
                        ItemMeta im = ingot.getItemMeta();
                        im.setDisplayName(Utils.color(main.getConfig().getString("economy.item.name")));
                        im.setLore(Utils.color(main.getConfig().getStringList("economy.item.lore")));
                        ingot.setItemMeta(im);

                        net.minecraft.server.v1_8_R3.ItemStack nbtStack = CraftItemStack.asNMSCopy(ingot);
                        NBTTagCompound nbt = (nbtStack.hasTag() ? nbtStack.getTag() : new NBTTagCompound());
                        nbt.setBoolean("eco", true);
                        ingot = CraftItemStack.asBukkitCopy(nbtStack);

                        for(int i = 0; i < amount; i++) {
                            clicked.getInventory().addItem(ingot);
                        }

                   } else {
                        clicked.closeInventory();
                        clicked.sendMessage(Utils.color("&c&lMineX &7| You do not have enough for this!"));
                    }
                } else {
                    if(config.getInt("items." + key + ".addAmount") != 0) {
                        //adding
                        amount = amount + config.getInt("items." + key + ".addAmount");
                    }  else if(config.getInt("items." + key + ".removeAmount") != 0) {
                        //removing
                        amount = ((amount -= config.getInt("items." + key + ".removeAmount")) < 0) ? 0 : amount - config.getInt("items." + key + ".removeAmount");
                    }
                    String k = keys.get(confirm);
                    ItemStack newConfirm = new ItemStack(Material.matchMaterial(config.getString("items." + k + ".material")), (config.getInt("items." + k + ".amount") == 0 ? 1 : (config.getInt("items." + k + ".amount"))), (short) (config.getInt("items." + k + ".damage")));
                    ItemMeta im = newConfirm.getItemMeta();
                    im.setDisplayName(Utils.color(config.getString("items." + k + ".name").replace("{amount}", "" + amount)));
                    im.setLore(Utils.color(config.getStringList("items." + k + ".lore")));
                    newConfirm.setItemMeta(im);
                    g.setItem(confirm, newConfirm);
                }
            } else if(type.equalsIgnoreCase("deposit")) {
                if(slot > e.getInventory().getSize()) return;
                String key = keys.get(slot);
                if(slot == confirm) {
                    ItemStack ingot = new ItemStack(Material.matchMaterial(main.getConfig().getString("economy.item.material")));
                    ItemMeta im = ingot.getItemMeta();
                    im.setDisplayName(Utils.color(main.getConfig().getString("economy.item.name")));
                    im.setLore(Utils.color(main.getConfig().getStringList("economy.item.lore")));
                    ingot.setItemMeta(im);

                    int count = 0;

                    for (ItemStack stack : clicked.getInventory().getContents()) {
                        if(stack == null || stack.getType().equals(Material.AIR)) continue;
                        net.minecraft.server.v1_8_R3.ItemStack nbtStack = CraftItemStack.asNMSCopy(stack);
                        NBTTagCompound nbt = (nbtStack.hasTag()) ? nbtStack.getTag() : new NBTTagCompound();
                        if(nbt.getBoolean("eco")) {
                            count += stack.getAmount();
                        }
                    }

                    if(count < amount) {
                        clicked.sendMessage(Utils.color("&c&lMineX &7| You do not have enough money for this!"));
                        return;
                    }
                    PlayerManager.getmPlayer(clicked.getUniqueId()).setBalance(PlayerManager.getmPlayer(clicked.getUniqueId()).getBalance() + amount);

                    for(ItemStack stack : clicked.getInventory().getContents()) {
                        if(stack == null || stack.getType().equals(Material.AIR)) continue;
                        int subtract = Math.min(stack.getAmount(), amount);
                        stack.setAmount(stack.getAmount() - subtract);
                        amount -= subtract;
                    }

                } else {
                    if(config.getInt("items." + key + ".addAmount") != 0) {
                        //adding
                        amount = amount + config.getInt("items." + key + ".addAmount");
                    }  else if(config.getInt("items." + key + ".removeAmount") != 0) {
                        //removing
                        amount = ((amount -= config.getInt("items." + key + ".removeAmount")) < 0) ? 0 : amount - config.getInt("items." + key + ".removeAmount");
                    }
                    String k = keys.get(confirm);
                    ItemStack newConfirm = new ItemStack(Material.matchMaterial(config.getString("items." + k + ".material")), (config.getInt("items." + k + ".amount") == 0 ? 1 : (config.getInt("items." + k + ".amount"))), (short) (config.getInt("items." + k + ".damage")));
                    ItemMeta im = newConfirm.getItemMeta();
                    im.setDisplayName(Utils.color(config.getString("items." + k + ".name").replace("{amount}", "" + amount)));
                    im.setLore(Utils.color(config.getStringList("items." + k + ".lore")));

                    newConfirm.setItemMeta(im);

                    g.setItem(confirm, newConfirm);
                }
            }
        });
    }

}
