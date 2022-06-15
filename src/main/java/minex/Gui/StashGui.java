package minex.Gui;

import minex.Main;
import minex.Managers.PlayerManager;
import minex.Enums.Message;
import minex.Objects.mPlayer;
import minex.Utils.Utils;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StashGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/StashGui.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);


    public void makeGui(Player p) {
        mPlayer mp = PlayerManager.getmPlayer(p.getUniqueId());
        Gui.GuiPage template = Utils.makeFormat("StashGui.yml", gui.createTemplate(Utils.color(config.getString("title")), config.getStringList("format").size() * 9), "items");
        template.onClick(e -> {
            int slot = e.getSlot();
            int next = config.getInt("next");
            int previous = config.getInt("previous");

            if(e.getClickedInventory() == null) return;

            Gui.GuiPage page = gui.getViewerPage(p);

            e.setCancelled(true);

            //page functions
            if(slot == next) {
                if(isFull(gui.getViewerPage(p).getContents())) {
                    gui.nextPage(p);
                }
            } else if(slot == previous) {
                gui.prevPage(p);
            } else {
                ItemStack clickedStack = e.getCurrentItem().clone();

                if(e.getClickedInventory().getHolder() instanceof Player) {
                    //Clicking inside player inventory
                    if((e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)) && !e.getCurrentItem().getType().equals(Material.AIR)) {
                        //Shifting into the inv
                        if(mp.getFullStash().size() >= mp.getStashSize()) {
                            p.sendMessage(Message.MAX_STASH.getMessage());
                            return;
                        }
                        ItemStack add = e.getCurrentItem();
                        net.minecraft.server.v1_8_R3.ItemStack nbtStack = CraftItemStack.asNMSCopy(e.getCurrentItem());
                        NBTTagCompound tag = (nbtStack.hasTag()) ? nbtStack.getTag() : new NBTTagCompound();
                        tag.setBoolean("active", true);
                        nbtStack.setTag(tag);

                        add = CraftItemStack.asBukkitCopy(nbtStack);

                        page.addItem(add);
                        mp.addItem(add);
                        e.setCurrentItem(null);

                    }
                    if(e.getCursor().getType().equals(Material.AIR) && !e.getCurrentItem().getType().equals(Material.AIR)) {
                        e.setCursor(e.getCurrentItem());
                        p.getInventory().setItem(slot, new ItemStack(Material.AIR));
                    } else if(!e.getCursor().getType().equals(Material.AIR) && e.getCurrentItem().getType().equals(Material.AIR)) {
                        p.getInventory().setItem(slot, e.getCursor());
                        e.setCursor(new ItemStack(Material.AIR));
                    }
                } else {
                    //Clicking inside the custom gui
                    if((e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)) && !e.getCurrentItem().getType().equals(Material.AIR)) {
                        ItemStack add = e.getCurrentItem().clone();
                        mp.removeItem(add);
                        net.minecraft.server.v1_8_R3.ItemStack nbtStack = CraftItemStack.asNMSCopy(e.getCurrentItem());
                        NBTTagCompound tag = (nbtStack.hasTag()) ? nbtStack.getTag() : new NBTTagCompound();
                        if(!tag.hasKey("active")) return;
                        nbtStack.setTag(null);

                        ItemStack stack = new ItemStack(add.getType(), add.getAmount(), add.getData().getData());

                        page.setItem(slot, Material.AIR);
                        p.getInventory().addItem(stack);
                        return;
                    }
                    if(!e.getCursor().getType().equals(Material.AIR) && e.getCurrentItem().getType().equals(Material.AIR)) {
                        if(mp.getFullStash().size() >= mp.getStashSize()) {
                            p.sendMessage(Message.MAX_STASH.getMessage());
                            return;
                        }
                        net.minecraft.server.v1_8_R3.ItemStack nbtStack = CraftItemStack.asNMSCopy(e.getCursor());
                        NBTTagCompound tag = (nbtStack.hasTag()) ? nbtStack.getTag() : new NBTTagCompound();
                        tag.setBoolean("active", true);
                        nbtStack.setTag(tag);

                        ItemStack add = CraftItemStack.asBukkitCopy(nbtStack);

                        e.setCursor(null);
                        page.setItem(slot, add);
                        mp.addItem(add);
                        return;
                    }
                    if(e.getCursor().getType().equals(Material.AIR) && !e.getCurrentItem().getType().equals(Material.AIR) && e.getClick().equals(ClickType.LEFT)) {
                        ItemStack add = e.getCurrentItem().clone();
                        mp.removeItem(add);
                        net.minecraft.server.v1_8_R3.ItemStack nbtStack = CraftItemStack.asNMSCopy(e.getCurrentItem());
                        NBTTagCompound tag = (nbtStack.hasTag()) ? nbtStack.getTag() : new NBTTagCompound();
                        if(!tag.hasKey("active")) return;

                        ItemStack stack = new ItemStack(add.getType(), add.getAmount(), add.getData().getData());

                        page.setItem(slot, Material.AIR);
                        e.setCursor(stack);
                        return;
                    }
                    if(!e.getCurrentItem().getType().equals(Material.AIR) && !e.getCursor().getType().equals(Material.AIR)) {
                        ItemStack current = e.getCurrentItem().clone();
                        mp.removeItem(current);
                        net.minecraft.server.v1_8_R3.ItemStack nbtStack = CraftItemStack.asNMSCopy(current);
                        NBTTagCompound tag = (nbtStack.hasTag()) ? nbtStack.getTag() : new NBTTagCompound();
                        if(!tag.hasKey("active")) return;

                        ItemStack newCursor = new ItemStack(current.getType(), current.getAmount(), current.getData().getData());

                        ItemStack cursor = e.getCursor().clone();
                        net.minecraft.server.v1_8_R3.ItemStack cursornbtStack = CraftItemStack.asNMSCopy(cursor);
                        NBTTagCompound cursortag = (cursornbtStack.hasTag()) ? cursornbtStack.getTag() : new NBTTagCompound();
                        cursortag.setBoolean("active", true);
                        cursornbtStack.setTag(tag);

                        cursor = CraftItemStack.asBukkitCopy(cursornbtStack);

                        e.setCursor(newCursor);
                        page.setItem(slot, cursor);
                        mp.addItem(cursor);
                        return;
                    }
                }
            }
        });

        loadItems(p, mp, template);

    }

    public boolean isFull(ItemStack... items) {
        for(ItemStack i : items) {
            if(i == null || i.getType().equals(Material.AIR)) return false;
        }
        return true;
    }

    public void loadItems(Player p, mPlayer mp, Gui.GuiPage template) {
        List<Integer> slots = new ArrayList<>();
        double empty = 0;
        for(int i = 0; i < template.size; i++) {
            if(template.getContents()[i] == null || template.getContents()[i].getType().equals(Material.AIR)) {
                empty++;
                slots.add(i);
            }
        }
        double pages = (Math.ceil(mp.getFullStash().size() / empty)) == 0 ? 1 : Math.ceil(mp.getFullStash().size() / empty);
        for(int i = 0; i < pages; i++) {
            Gui.GuiPage page = gui.create(template).c().s();
            for(int x = 0; x < empty; x++) {
                if(mp.getFullStash().size() == x) break;;
                if(mp.getFullStash().get(x) == null || mp.getFullStash().get(x).getType().equals(Material.AIR)) break;
                page.addItem(mp.getFullStash().get(x));
            }
        }
        gui.show(p, 0);
    }


}
