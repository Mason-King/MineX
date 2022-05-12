package minex.Events;

import minex.Game.Game;
import minex.Main;
import minex.Managers.PlayerManager;
import minex.Messages.Message;
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
        Location to = e.getTo();
        Location from = e.getFrom();
        if (to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY() && to.getBlockZ() == from.getBlockZ()) return;
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

        if(!player.getWorld().getName().contains("Game")) return;

        if(extracted.contains(player.getUniqueId())) {
            //they have an active extraction
            System.out.println(extracted);
            if(closest.distanceSquared(player.getLocation()) > 2) {
                extracted.remove(player.getUniqueId());
                player.sendMessage(Message.EXTRACTION_CANCELED.getMessage());
                runnable.cancel();
                return;
            }
            return;
        } else {
            if(closest.distanceSquared(player.getLocation()) < 2) {
                runnable = new BukkitRunnable() {
                    int count = 5;
                    @Override
                    public void run() {
                        if(extracted.contains(player.getUniqueId())) cancel();
                        if(count == 5 && !extracted.contains(player.getUniqueId())) {
                            extracted.add(player.getUniqueId());
                        }
                        player.sendMessage(Message.EXTRACTION.getMessage().replace("{time}", count + ""));
                        if(count == 0) cancel();
                        count--;
                    }
                }.runTaskTimer(Main.getInstance(), 0, 20);
            }
        }


    }
}
