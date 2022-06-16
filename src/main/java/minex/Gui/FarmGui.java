package minex.Gui;

import minex.Enums.Message;
import minex.Main;
import minex.Managers.PlayerManager;
import minex.Objects.mPlayer;
import minex.Utils.Utils;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class FarmGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);



    public void makeGui(Player p) {
        Gui.NoobPage g = gui.create(Utils.color(main.getConfig().getString("farm.title")), 9).c().s();

        for(int i = 0 ; i < PlayerManager.getmPlayer(p.getUniqueId()).getCurrGPU(); i++) {
            org.bukkit.inventory.ItemStack gpu = new org.bukkit.inventory.ItemStack(Material.matchMaterial(main.getConfig().getString("farm.gpu.material")));
            ItemMeta im = gpu.getItemMeta();
            im.setDisplayName(Utils.color(main.getConfig().getString("farm.gpu.name")));
            im.setLore(Utils.color(main.getConfig().getStringList("farm.gpu.lore")));
            gpu.setItemMeta(im);
            net.minecraft.server.v1_8_R3.ItemStack nbtStack = CraftItemStack.asNMSCopy(gpu);
            NBTTagCompound tag = (nbtStack.hasTag() ? nbtStack.getTag() : new NBTTagCompound());
            tag.setBoolean("gpu", true);
            tag.setString("unstackable", UUID.randomUUID().toString());
            nbtStack.setTag(tag);
            gpu = CraftItemStack.asBukkitCopy(nbtStack);
            g.addItem(gpu);
        }

        g.onClick(e -> {
            e.setCancelled(true);
           int slow = e.getSlot();
           Player clicked = (Player) e.getWhoClicked();
           mPlayer mp = PlayerManager.getmPlayer(clicked.getUniqueId());
//            if(!CraftItemStack.asNMSCopy(stack).hasTag()) return;
            if(e.getClickedInventory().getHolder() instanceof  Player) {
                //Players inv
                System.out.println(e.getCurrentItem());
                System.out.println(e.getCursor());
                if((e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)) && !e.getCurrentItem().getType().equals(Material.AIR)) {
                    //shifting into the gui
                    if(mp.getFarmLimit() == mp.getCurrGPU()) {
                        clicked.sendMessage(Message.FARM_MAX.getMessage());
                        return;
                    } else {
                        g.addItem(e.getCurrentItem());
                        e.setCurrentItem(new ItemStack(Material.AIR));
                        mp.setCurrGPU(mp.getCurrGPU() + 1);
                    }
                }
                if(e.getCursor().getType().equals(Material.AIR) && !e.getCurrentItem().getType().equals(Material.AIR)) {
                    //picking it up
                    e.setCursor(e.getCurrentItem());
                    e.setCurrentItem(new ItemStack(Material.AIR));
                } else if(!e.getCursor().getType().equals(Material.AIR) && e.getCurrentItem().getType().equals(Material.AIR)) {
                    //placing it maybe?
                    e.setCurrentItem(e.getCursor());
                    e.setCursor(new ItemStack(Material.AIR));
                }
                } else {
                    //gui
                    if((e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)) && !e.getCurrentItem().getType().equals(Material.AIR)) {
                        p.getInventory().addItem(e.getCurrentItem());
                        g.setItem(slow, new ItemStack(Material.AIR));
                        mp.setCurrGPU(mp.getCurrGPU() - 1);
                    }
                if(e.getCursor().getType().equals(Material.AIR) && !e.getCurrentItem().getType().equals(Material.AIR)) {
                    //picking it up
                    e.setCursor(e.getCurrentItem());
                    e.setCurrentItem(new ItemStack(Material.AIR));
                    mp.setCurrGPU(mp.getCurrGPU() - 1);
                } else if(!e.getCursor().getType().equals(Material.AIR) && e.getCurrentItem().getType().equals(Material.AIR)) {
                    //placing it maybe?
                    e.setCurrentItem(e.getCursor());
                    e.setCursor(new ItemStack(Material.AIR));
                    mp.setCurrGPU(mp.getCurrGPU() + 1);
                }
                }
        });

        gui.show(p, 0);


    }

}
