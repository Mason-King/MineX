package minex.Events;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import minex.Main;
import net.raidstone.wgevents.events.RegionEnteredEvent;
import net.raidstone.wgevents.events.RegionLeftEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class RegionEnterListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        Location to = e.getTo();
        Location from = e.getFrom();

        if (to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY() && to.getBlockZ() == from.getBlockZ()) return;

        RegionManager regionManager = Main.getInstance().getWorldGuard().getRegionManager(p.getWorld());

        ApplicableRegionSet set = regionManager.getApplicableRegions(to);
        ProtectedRegion r = null;
        for(ProtectedRegion pr : set) {
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
