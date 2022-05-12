package minex.Events;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import minex.Game.Game;
import minex.LootChest.LootChest;
import minex.LootChest.LootType;
import minex.Main;
import minex.Managers.GameManager;
import minex.Messages.Message;
import minex.Utils.Utils;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.raidstone.wgevents.events.RegionEnteredEvent;
import net.raidstone.wgevents.events.RegionLeftEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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
            clicked.sendMessage(Message.POS1.getMessage());
        } else if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if(pos2.containsKey(clicked.getUniqueId())) {
                pos2.remove(clicked.getUniqueId());
                pos2.put(clicked.getUniqueId(), e.getClickedBlock().getLocation());
            } else {
                pos2.put(clicked.getUniqueId(), e.getClickedBlock().getLocation());
            }
            clicked.sendMessage(Message.POS2.getMessage());
            e.setCancelled(true);
        } else return;

    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        Location to = e.getTo();
        Location from = e.getFrom();

        if (to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY() && to.getBlockZ() == from.getBlockZ()) return;

        RegionManager regionManager = Main.getInstance().getWorldGuard().getRegionManager(p.getWorld());

        ApplicableRegionSet set = regionManager.getApplicableRegions(to);
        ProtectedRegion r = null;;
        for(ProtectedRegion pr : set) {
            System.out.println(pr);
            if(r == null) {
                r = pr;
                break;
            }
        }

        if(r == null) return;


        if(r.contains(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ())) {
            System.out.println("inside!");
        }

    }

}
