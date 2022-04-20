package minex.Game;

import minex.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitScheduler;

public class Countdown {

    private String id;
    private int taskID;
    private int time;

    public Countdown(String id, int time) {
        this.id = id;
        this.time = time;

        startTimer();
    }

    public void setTimer(int amount) {
        this.time = amount;
    }

    public void startTimer() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(time == 0) {
                    Bukkit.broadcastMessage(ChatColor.RED + "Time is up!");
                    stopTimer();
                    return;
                }
                if(time % 5 == 0) {
                    Bukkit.broadcastMessage(ChatColor.RED + "Timer remaining " + time + " seconds");
                }
                time--;
            }
        }, 0L, 20L);
    }

    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

}
