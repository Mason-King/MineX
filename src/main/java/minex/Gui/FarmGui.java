package minex.Gui;

import minex.Main;
import minex.Utils.Utils;
import org.bukkit.entity.Player;

public class FarmGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);



    public void makeGui(Player p) {
        Gui.NoobPage g = gui.create(Utils.color(main.getConfig().getString("farm.title")), 1).c().s();

        gui.show(p, 0);


    }

}
