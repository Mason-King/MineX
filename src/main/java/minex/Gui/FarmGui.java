package minex.Gui;

import minex.Game.Game;
import minex.Main;
import minex.Managers.PlayerManager;
import minex.Player.mPlayer;
import minex.Utils.Utils;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class FarmGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);



    public void makeGui(Player p) {
        System.out.println("here");
        Gui.NoobPage g = gui.create(Utils.color("&lBitcoin farm"), 1).c().s();

        gui.show(p, 0);


    }

}
