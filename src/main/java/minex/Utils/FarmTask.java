package minex.Utils;

import minex.Enums.Message;
import minex.Main;
import minex.Managers.PlayerManager;
import minex.Objects.mPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FarmTask extends BukkitRunnable {

    Main main = Main.getInstance();
    private int amount = main.getConfig().getInt("farm.sell");

    @Override
    public void run() {
        for(mPlayer mp : PlayerManager.players) {
            if(mp.getCurrGPU() == 0) continue;
            int total = mp.getCurrGPU() * amount;
            mp.setBalance(mp.getBalance() + total);
            sendActionBar(Bukkit.getPlayer(mp.getId()), Message.FARM_SOLD.getMessage().replace("{sell}", amount + ""));
        }
    }

    public static void sendActionBar(Player p, String nachricht) {
        CraftPlayer cp = (CraftPlayer) p;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + nachricht + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        cp.getHandle().playerConnection.sendPacket(ppoc);
    }
}
