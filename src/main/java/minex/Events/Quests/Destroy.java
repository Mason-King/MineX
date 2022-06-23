package minex.Events.Quests;

import minex.Managers.PlayerManager;
import minex.Objects.mPlayer;
import minex.Objects.Quest;
import minex.Enums.QuestType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class Destroy implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        mPlayer mp = PlayerManager.getmPlayer(p.getUniqueId());
        if(mp.getCurrGame() == null) return;
        List<Quest> questList = mp.getQuestByType(QuestType.BREAK);

        Block b = e.getBlock();
        Material bType = b.getType();

        for(Quest q : questList) {
            String blockNeeded = q.getId();

            if(bType.equals(Material.matchMaterial(blockNeeded))) {
                q.incProgress();
            }
        }

    }

}
