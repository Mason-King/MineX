package minex.Events.Quests;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import minex.Main;
import minex.Managers.PlayerManager;
import minex.Objects.mPlayer;
import minex.Objects.Quest;
import minex.Enums.QuestType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class Enter implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        mPlayer mp = PlayerManager.getmPlayer(p.getUniqueId());
        List<Quest> questList = mp.getQuestByType(QuestType.VISIT);

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

        for(Quest q : questList) {
            if(q.getArea().equals(r.getId()) && r.contains(to.getBlockX(), to.getBlockY(), to.getBlockZ()) && !r.contains(from.getBlockX(), from.getBlockY(), from.getBlockX())) {
                q.incProgress();
            }
        }

    }

}
