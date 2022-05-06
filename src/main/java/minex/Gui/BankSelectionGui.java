package minex.Gui;

import minex.Main;
import minex.Managers.PlayerManager;
import minex.Utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
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

            System.out.println(type);

            if(type.equalsIgnoreCase("withdraw")) {
                String key = keys.get(slot);
                System.out.println(slot);
                System.out.println(confirm);
                if(slot == confirm) {
                    System.out.println("confirm");
                    //if(PlayerManager.getmPlayer(clicked.getUniqueId()).getBalance() >= amount) {
                        //PlayerManager.getmPlayer(clicked.getUniqueId()).setBalance(PlayerManager.getmPlayer(clicked.getUniqueId()).getBalance() - amount);

                        ItemStack ingot = new ItemStack(Material.matchMaterial(main.getConfig().getString("economy.item.material")));
                        ItemMeta im = ingot.getItemMeta();
                        im.setDisplayName(Utils.color(main.getConfig().getString("economy.item.name")));
                        im.setLore(Utils.color(main.getConfig().getStringList("economy.item.lore")));
                        ingot.setItemMeta(im);

                        System.out.println(ingot);
                        System.out.println(amount);

                        for(int i = 0; i < amount; i++) {
                            clicked.getInventory().addItem(ingot);
                        }

//                    } else {
//                        clicked.closeInventory();
//                        clicked.sendMessage(Utils.color("&c&lMineX &7| You do not have enough for this!"));
//                    }
                } else {
                    if(config.getInt("items." + key + ".addAmount") != 0) {
                        //adding
                        amount = amount + config.getInt("items." + key + ".addAmount");
                        System.out.println(amount);
                    }  else if(config.getInt("items." + key + ".removeAmount") != 0) {
                        //removing
                        amount = ((amount -= config.getInt("items." + key + ".removeAmount")) < 0) ? 0 : amount - config.getInt("items." + key + ".removeAmount");
                    };
                }
            } else if(type.equalsIgnoreCase("deposit")) {
                String key = keys.get(slot);
                System.out.println(slot);
                System.out.println(confirm);
                if(slot == confirm) {
                    System.out.println("confirm");
                    ItemStack ingot = new ItemStack(Material.matchMaterial(main.getConfig().getString("economy.item.material")));
                    ItemMeta im = ingot.getItemMeta();
                    im.setDisplayName(Utils.color(main.getConfig().getString("economy.item.name")));
                    im.setLore(Utils.color(main.getConfig().getStringList("economy.item.lore")));
                    ingot.setItemMeta(im);

                    int count = 0;

                    for (ItemStack stack : clicked.getInventory().getContents()) {
                        if (stack != null && stack.equals(ingot)) {
                            count += stack.getAmount();
                        }
                    }

                    if(count < amount) {
                        clicked.sendMessage(Utils.color("&c&lMineX &7| You do not have enough money for this!"));
                        return;
                    }
                    //PlayerManager.getmPlayer(clicked.getUniqueId()).setBalance(PlayerManager.getmPlayer(clicked.getUniqueId()).getBalance() + amount);

                    System.out.println(ingot);
                    System.out.println(amount);

                    for(int i = 0; i < amount; i++) {
                        clicked.getInventory().addItem(ingot);
                    }

                } else {
                    if(config.getInt("items." + key + ".addAmount") != 0) {
                        //adding
                        amount = amount + config.getInt("items." + key + ".addAmount");
                        System.out.println(amount);
                    }  else if(config.getInt("items." + key + ".removeAmount") != 0) {
                        //removing
                        amount = ((amount -= config.getInt("items." + key + ".removeAmount")) < 0) ? 0 : amount - config.getInt("items." + key + ".removeAmount");
                    };
                }
            }
        });
    }

}
