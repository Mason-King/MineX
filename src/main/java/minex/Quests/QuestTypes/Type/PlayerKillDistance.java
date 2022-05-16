package minex.Quests.QuestTypes.Type;

import minex.Managers.PlayerManager;
import minex.Player.mPlayer;
import minex.Quests.Quest;
import minex.Quests.QuestType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

public class PlayerKillDistance implements Listener {

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if(!(p.getKiller() instanceof Player)) return;
        Player killer = p.getKiller();
        mPlayer mp = PlayerManager.getmPlayer(killer.getUniqueId());
        List<Quest> questList = mp.getQuestByType(QuestType.PLAYER_KILL_DISTANCE);

        for(Quest q : questList) {
            if(killer.getLocation().distance(p.getLocation()) >= q.getDistance()) {
                q.incProgress();
            }
        }

    }

}
