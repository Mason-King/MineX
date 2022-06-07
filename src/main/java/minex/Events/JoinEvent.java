package minex.Events;

import minex.Managers.PlayerManager;
import minex.Objects.mPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player joined = e.getPlayer();

        System.out.println(PlayerManager.getmPlayer(joined.getUniqueId()));
        System.out.println(joined.getUniqueId());

        if(!joined.hasPlayedBefore() || PlayerManager.getmPlayer(joined.getUniqueId()) == null) {
            mPlayer mp = PlayerManager.createPlayer(joined);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player quit = e.getPlayer();
        mPlayer mp = PlayerManager.getmPlayer(quit.getUniqueId());
        if(mp.getCurrGame() != null) {
            mp.getCurrGame().leaveGame(mp.getId());
        }
    }


}
