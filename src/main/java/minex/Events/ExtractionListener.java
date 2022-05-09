package minex.Events;

import minex.Game.Game;
import minex.Main;
import minex.Managers.PlayerManager;
import minex.Player.mPlayer;
import minex.Utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExtractionListener implements Listener {

    private final List<UUID> extracted = new ArrayList<>();
    private BukkitTask runnable;

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        //No need to even fire if they dont move a whole block!
        if (e.getFrom().getX() == e.getTo().getX() || e.getFrom().getZ() == e.getTo().getZ()) return;
        Player player = e.getPlayer();
        mPlayer mp = PlayerManager.getmPlayer(player.getUniqueId());
        if(mp.getCurrGame() == null) return;
        Game game = mp.getCurrGame();
        List<String> locs = game.getArena().getExtractions();
        Location closest = null;
        for(String s : locs) {
            if(closest == null) {
                closest = Utils.fromString(s);
            } else if(Utils.fromString(s).distanceSquared(player.getLocation()) < closest.distanceSquared(player.getLocation())) {
                closest = Utils.fromString(s);
            }
        }

        if(extracted.contains(player.getUniqueId())) {
            closest.distanceSquared(player.getLocation());
            if(closest.distanceSquared(player.getLocation()) > 2) {
                System.out.println(closest.distanceSquared(player.getLocation()));
                extracted.remove(player.getUniqueId());
                player.sendMessage(Utils.color("&c&lMineX &7| Extraction cancled!"));
                runnable.cancel();
                return;
            }
            return;
        }
        if(closest.distanceSquared(player.getLocation()) < 2) {
            runnable = new BukkitRunnable() {
                int count = 5;
                @Override
                public void run() {
                    player.sendMessage(Utils.color("&c&lMineX &7| You will be extracted in " + count + " seconds! Do not move!"));
                    extracted.add(player.getUniqueId());
                    if(count == 0) cancel();
                    count--;
                }
            }.runTaskTimer(Main.getInstance(), 0, 20);
        }


    }
}
