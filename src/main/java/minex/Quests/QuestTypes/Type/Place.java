package minex.Quests.QuestTypes.Type;

import minex.Managers.PlayerManager;
import minex.Player.mPlayer;
import minex.Quests.Quest;
import minex.Quests.QuestType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;

public class Place implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        System.out.println("place");
        Player p = e.getPlayer();
        mPlayer mp = PlayerManager.getmPlayer(p.getUniqueId());
        List<Quest> questList = mp.getQuestByType(QuestType.PLACE);

        Block b = e.getBlock();
        Material bType = b.getType();

        System.out.println(questList);

        for(Quest q : questList) {
            String blockNeeded = q.getId();

            if(bType.equals(Material.matchMaterial(blockNeeded))) {
                q.incProgress();
            }
        }

    }

}
