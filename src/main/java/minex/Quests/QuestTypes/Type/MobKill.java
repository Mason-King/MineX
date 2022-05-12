package minex.Quests.QuestTypes.Type;

import minex.Managers.PlayerManager;
import minex.Player.mPlayer;
import minex.Quests.Quest;
import minex.Quests.QuestType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;

public class MobKill implements Listener {

    @EventHandler
    public void onMobKill(EntityDeathEvent e) {
        System.out.println("mob kill");
        if(!(e.getEntity().getKiller() instanceof Player)) return;
        Player p = e.getEntity().getKiller();
        Entity ent = e.getEntity();
        mPlayer mp = PlayerManager.getmPlayer(p.getUniqueId());
        List<Quest> questList = mp.getQuestByType(QuestType.MOB_KILL);

        for(Quest q : questList) {
            String entNeeded = q.getId();

            if(EntityType.valueOf(entNeeded) == null) continue;

            if(ent.getType().equals(EntityType.valueOf(entNeeded))) {
                q.incProgress();
            }
        }

    }

}
