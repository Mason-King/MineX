package minex.Gui;

import minex.Enums.Message;
import minex.Main;
import minex.Managers.PlayerManager;
import minex.Objects.mPlayer;
import minex.Utils.Utils;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FarmGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);



    public void makeGui(Player p) {
        Gui.NoobPage g = gui.create(Utils.color(main.getConfig().getString("farm.title")), 9).c().s();


        g.onClick(e -> {
            e.setCancelled(true);
           int slow = e.getSlot();
           Player clicked = (Player) e.getWhoClicked();
           mPlayer mp = PlayerManager.getmPlayer(clicked.getUniqueId());
            ItemStack stack = e.getCurrentItem();
            if(stack == null || stack.getType().equals(Material.AIR)) return;
            if(!CraftItemStack.asNMSCopy(stack).hasTag()) return;
            if(mp.getFarmLimit() == mp.getCurrGPU()) {
                clicked.sendMessage(Message.FARM_MAX.getMessage());
                return;
            } else {
                g.addItem(e.getCurrentItem());
                e.setCurrentItem(new ItemStack(Material.AIR));
            }
        });

        gui.show(p, 0);


    }

}
