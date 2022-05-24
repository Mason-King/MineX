package minex.Events;

import minex.Objects.Game;
import minex.Managers.PlayerManager;
import minex.Enums.Message;
import minex.Objects.mPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onDeath(EntityDamageByEntityEvent e) {
        if(!(e.getEntity() instanceof  Player)) return;
        Player p = (Player) e.getEntity();
        if(p.getWorld().getName().contains("Lobby")) {
            e.setCancelled(true);
            return;
        }
        if(!(p.getKiller() instanceof  Player)) return;


        mPlayer mp = PlayerManager.getmPlayer(p.getUniqueId());
        if(mp.getCurrGame() != null) {
            if((p.getHealth() - e.getDamage()) <= 0) {
                e.setCancelled(true);
                Game game = mp.getCurrGame();
                game.broadcast(Message.PLAYER_KILLED.getMessage().replace("{player}", p.getName()).replace("{killer}", ((Player) p.getKiller()).getName()) );
                game.leaveGame(p.getUniqueId());
                for(ItemStack i : p.getInventory().getContents()) {
                    if(i == null || i.getType().equals(Material.AIR)) continue;
                    p.getLocation().getWorld().dropItemNaturally(p.getLocation(), i);
                    i.setType(Material.AIR);
                }
                p.setHealth(20);
            }
        }

    }

}
