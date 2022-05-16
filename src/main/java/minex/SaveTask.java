package minex;

import minex.Game.Game;
import minex.Managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class SaveTask extends BukkitRunnable {
    @Override
    public void run() {
        if(GameManager.games == null) return;
        for(Game g : GameManager.games) {
            GameManager.save(g);
        }
        Bukkit.getLogger().log(Level.INFO, "Saved all games!");
    }
}
