package minex.Gui;

import minex.Game.Game;
import minex.Game.Team;
import minex.Main;
import minex.Managers.PlayerManager;
import minex.Player.mPlayer;
import minex.Utils.Utils;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class MapGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/MapSelector.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);


    public void makeGui(Player p, Game game) {
        Gui.NoobPage g  = gui.create(Utils.color(config.getString("title")), config.getStringList("format").size() * 9).c().s();
        Utils.makeFormat("MapSelector.yml", g, "items");
        Utils.makeSpawnFormat(game,"MapSelector.yml", g, "items");
        gui.show(p, 0);

        g.onClick(e -> {
           Player clicked = (Player) e.getWhoClicked();
           mPlayer mp = PlayerManager.getmPlayer(clicked.getUniqueId());
           int slot = e.getSlot();
           ItemStack clickedStack = e.getCurrentItem();

           net.minecraft.server.v1_8_R3.ItemStack nbtStack = CraftItemStack.asNMSCopy(clickedStack);
           if(!nbtStack.hasTag()) return;
            NBTTagCompound nbt = nbtStack.getTag();

//            Location spawnLoc = game.getArena().getSpawn(nbt.getString("spawnId"));
//            Team team = mp.getTeam();
//            game.setTeamSpawn(team, Utils.toString(spawnLoc));
            clicked.closeInventory();
        });

    }

}
