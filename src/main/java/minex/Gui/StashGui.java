package minex.Gui;

import minex.Game.Game;
import minex.Main;
import minex.Player.mPlayer;
import minex.Utils.Utils;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.List;

public class StashGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/Stash.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);


    public void makeGui(Player p, Game game) {
        mPlayer mp = mPlayer.uuidPlayers.get(p.getUniqueId());
        Gui.NoobPage g  = gui.create(Utils.color(config.getString("title")), config.getStringList("format").size() * 9).c().s();
        Utils.makeFormat("Stash.yml", g, "items");
        gui.show(p, 0);

        for(ItemStack stack : mp.getSelectedStash()) {
            List<String> lore = config.getStringList("deselectItemLore");
            ItemMeta im = stack.getItemMeta();
            im.setLore(Utils.color(lore));
            stack.setItemMeta(im);
            g.addItem(stack);
        }

        for(ItemStack stack : mp.getFullStash()) {
            if(mp.getSelectedStash().contains(stack)) continue;
            List<String> lore = config.getStringList("selectItemLore");
            ItemMeta im = stack.getItemMeta();
            im.setLore(Utils.color(lore));
            stack.setItemMeta(im);
            g.addItem(stack);
        }

        g.onClick(e -> {
            Player clicked = (Player) e.getWhoClicked();
            int slot = e.getSlot();

            if(e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)) {
                //They are shift clicking an item in or out
                if(!(e.getClickedInventory().getHolder() instanceof Player)) {
                    //They clicked inside the gui so they are taking it out
                    ItemStack remove = e.getCurrentItem().clone();
                    remove.getItemMeta().setLore(null);
                    ItemMeta im = e.getCurrentItem().getItemMeta();
                    im.setLore(null);
                    remove.setItemMeta(im);
                    clicked.getInventory().addItem(remove);
                    e.setCurrentItem(null);
                } else {
                    //They clicked inside their inv so they are putting it in
                    ItemStack put = e.getCurrentItem().clone();
                    List<String> lore = config.getStringList("selectItemLore");
                    ItemMeta im = put.getItemMeta();
                    im.setLore(Utils.color(lore));
                    put.setItemMeta(im);
                    g.addItem(put);
                    e.setCurrentItem(null);
                }
            } else if(e.getClick().equals(ClickType.LEFT)) {
                if(!(e.getClickedInventory().getHolder() instanceof Player)) {
                    //inside gui
                    if(e.getCursor().getType().equals(Material.AIR)) {
                        //picking up item
                        ItemStack remove = e.getCurrentItem().clone();
                        remove.getItemMeta().setLore(null);
                        ItemMeta im = e.getCurrentItem().getItemMeta();
                        im.setLore(null);
                        remove.setItemMeta(im);
                        e.setCursor(remove);
                        e.setCurrentItem(null);
                    } else if(e.getCurrentItem().getType().equals(Material.AIR)) {
                        //placing item
                        ItemStack put = e.getCursor().clone();
                        List<String> lore = config.getStringList("selectItemLore");
                        ItemMeta im = put.getItemMeta();
                        im.setLore(Utils.color(lore));
                        put.setItemMeta(im);
                        g.setItem(slot, put);
                        e.setCursor(null);
                    }
                }
            } else if(e.getClick().equals(ClickType.RIGHT)) {
                
            }

        });

    }

}
