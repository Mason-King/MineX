package minex.Gui;

import minex.Game.Game;
import minex.Game.Team;
import minex.Main;
import minex.Managers.GameManager;
import minex.Party.Party;
import minex.Player.mPlayer;
import minex.Utils.Utils;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

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
           int slot = e.getSlot();
           ItemStack clickedStack = e.getCurrentItem();

           net.minecraft.server.v1_8_R3.ItemStack nbtStack = CraftItemStack.asNMSCopy(clickedStack);
           if(!nbtStack.hasTag()) return;
            NBTTagCompound nbt = nbtStack.getTag();

            Location spawnLoc = game.getArena().getSpawn(nbt.getString("spawnId"));
            Team team = game.getTeam(clicked.getUniqueId());
            if(team == null) {
                clicked.sendMessage(Utils.color("&c&lMineX &7| You do not have a team!"));
            } else {
                team.setSpawn(Utils.toString(spawnLoc));
            }
        });

    }

}
