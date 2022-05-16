package minex.Gui;

import minex.Main;
import minex.Managers.PlayerManager;
import minex.Messages.Message;
import minex.Player.mPlayer;
import minex.Utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.List;

public class SellGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/Sell.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    File file2 = new File(main.getDataFolder().getAbsolutePath() + "/shops.yml");
    YamlConfiguration shopConfig = YamlConfiguration.loadConfiguration(file2);


    public void makeGui(Player p, String type) {
        Gui.NoobPage g  = gui.create(Utils.color(config.getString("title").replace("{type}", WordUtils.capitalizeFully(type))), config.getInt("size"));

        ItemStack stack = new ItemStack(Material.matchMaterial(config.getString("confirm.item.material")));
        ItemMeta im = stack.getItemMeta();
        im.setLore(Utils.color(config.getStringList("confirm.item.lore")));

        im.setDisplayName(Utils.color(config.getString("confirm.item.name").replace("{amount}", "0")));
        stack.setItemMeta(im);

        g.setItem(config.getInt("confirm.slot"), stack);

        g.onClick(e -> {
            //return if they click else where
            if(e.getClickedInventory() == null) return;
            int slot = e.getSlot();
            int confirm = config.getInt("confirm.slot");
            int total = 0;

            Player player = (Player) e.getWhoClicked();
            mPlayer mp = PlayerManager.getmPlayer(player.getUniqueId());

            if(slot == confirm) {
                e.setCancelled(true);
                //run confirm feature here
                int worth = 0;
                ItemStack[] contents = e.getInventory().getContents();
                for(int i = 0; i < contents.length; i++) {
                    if(i == confirm) continue;
                    ItemStack curr = contents[i];
                    if(curr == null || curr.getType().equals(Material.AIR)) continue;
                    if(shopConfig.isSet("sell." + type + "." + curr.getType())) {
                        //the item is set in config
                        int sell = shopConfig.getInt("sell." + type + "." + curr.getType());
                        worth += (sell * curr.getAmount());
                        g.setItem(i, Material.AIR);
                    } else {
                        if(contents[i] == null || contents[i].getType().equals(Material.AIR)) continue;
                        player.getInventory().addItem(contents[i]);
                        g.setItem(i, Material.AIR);
                    }
                }
                //p.closeInventory();
                mp.setBalance(mp.getBalance() + worth);
                player.sendMessage(Message.SOLD.getMessage().replace("{amount}", worth + ""));
            } else {
                ItemStack[] contents = e.getInventory().getContents();
                for(int i = 0; i < contents.length; i++) {
                    if(i == confirm) continue;
                    ItemStack curr = contents[i];
                    if(curr == null || curr.getType().equals(Material.AIR)) continue;
                    if(shopConfig.isSet("sell." + type + "." + curr.getType())) {
                        //the item is set in config
                        int sell = shopConfig.getInt("sell." + type + "." + curr.getType());
                        total += (sell * curr.getAmount());
                    }
                }
                boolean contains = false;
                for(ItemStack i : e.getInventory().getContents()) {
                    if(i == null || i.getType().equals(Material.AIR)) continue;
                    if(i.equals(e.getCurrentItem())) {
                        contains = true;
                    }
                }

                if(contains) {
                    //it is in the inv, so we need to remove from worth
                    if(e.getInventory().getHolder() instanceof Player) return;
//                    if(e.getClick().equals(ClickType.SHIFT_RIGHT) || e.getClick().equals(ClickType.SHIFT_LEFT)) {
//                        System.out.println("shift taking out");
//                        System.out.println();
//                        return;
//                    }
                    System.out.println(e.getCurrentItem());
                    System.out.println(e.getCursor());
                    if(shopConfig.isSet("sell." + type + "." + e.getCurrentItem().getType())) {
                        int price = shopConfig.getInt("sell." + type + "." + e.getCurrentItem().getType());
                        total -= (price * e.getCurrentItem().getAmount());
                    }
                } else {
                    if(e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
                        System.out.println(e.getCurrentItem());
                        System.out.println(e.getCursor());
                        if(e.getClickedInventory().getHolder() instanceof Player) return;
                        if(shopConfig.isSet("sell." + type + "." + e.getCursor().getType())) {
                            int price = shopConfig.getInt("sell." + type + "." + e.getCursor().getType());
                            total += (price * e.getCursor().getAmount());
                        }
                    } else {
                        if(e.getClick().equals(ClickType.SHIFT_RIGHT) || e.getClick().equals(ClickType.SHIFT_LEFT)) {
                            if(shopConfig.isSet("sell." + type + "." + e.getCurrentItem().getType())) {
                                int price = shopConfig.getInt("sell." + type + "." + e.getCurrentItem().getType());
                                total += (price * e.getCurrentItem().getAmount());
                            }

                        }
                    }
                }

                ItemStack replace = new ItemStack(Material.matchMaterial(config.getString("confirm.item.material")));
                ItemMeta replaceM = replace.getItemMeta();
                replaceM.setLore(Utils.color(config.getStringList("confirm.item.lore")));
                replaceM.setDisplayName(Utils.color(config.getString("confirm.item.name").replace("{amount}", total + "")));
                replace.setItemMeta(replaceM);
                g.setItem(confirm, replace);
            }

        });

        gui.show(p, 0);
    }

}
