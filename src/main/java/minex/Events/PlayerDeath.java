package minex.Events;

import minex.Managers.PlayerManager;
import minex.Player.mPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onDeath(EntityDamageByEntityEvent e) {
        if(!(e.getEntity() instanceof  Player)) return;
        Player p = (Player) e.getEntity();
        if(!(p.getKiller() instanceof  Player)) return;

        mPlayer mp = PlayerManager.getmPlayer(p.getUniqueId());
        if(mp.getCurrGame() != null) {
            if((p.getHealth() - e.getDamage()) <= 0) {
                e.setCancelled(true);
                mp.getCurrGame().leaveGame(mp.getId());
                for(ItemStack i : p.getInventory().getContents()) {
                    if(i == null || i.getType().equals(Material.AIR)) continue;
                    p.getLocation().getWorld().dropItemNaturally(p.getLocation(), i);
                }
                p.setHealth(20);
                p.sendMessage("dead!");
            }
        }

    }

}
