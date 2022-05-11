package minex.Events;

import minex.Game.Game;
import minex.LootChest.LootChest;
import minex.LootChest.LootType;
import minex.Managers.GameManager;
import minex.Utils.Utils;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegionListener implements Listener {

    public static Map<UUID, Location> pos1 = new HashMap<>();
    public static Map<UUID, Location> pos2 = new HashMap<>();

    @EventHandler
    public void onPlace(PlayerInteractEvent e) {
        Player clicked = e.getPlayer();
        if(!clicked.getInventory().getItemInHand().getType().equals(Material.GOLD_AXE)) return;
        ItemStack stack = CraftItemStack.asNMSCopy(clicked.getItemInHand());
        if(!stack.hasTag()) return;
        if(!stack.getTag().getBoolean("region")) return;
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            e.setCancelled(true);
            if(pos1.containsKey(clicked.getUniqueId())) {
                pos1.remove(clicked.getUniqueId());
                pos1.put(clicked.getUniqueId(), e.getClickedBlock().getLocation());
            } else {
                pos1.put(clicked.getUniqueId(), e.getClickedBlock().getLocation());
            }
            clicked.sendMessage(Utils.color("&c&lMineX &7| Set pos1"));
        } else if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if(pos2.containsKey(clicked.getUniqueId())) {
                pos2.remove(clicked.getUniqueId());
                pos2.put(clicked.getUniqueId(), e.getClickedBlock().getLocation());
            } else {
                pos2.put(clicked.getUniqueId(), e.getClickedBlock().getLocation());
            }
            clicked.sendMessage(Utils.color("&c&lMineX &7| Set pos2"));
            e.setCancelled(true);
        } else return;

    }

}
