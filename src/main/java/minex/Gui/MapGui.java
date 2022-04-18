package minex.Gui;

import minex.Main;
import minex.Utils.Utils;
import org.bukkit.entity.Player;

public class MapGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);


    public void makeGui(Player p) {
        Gui.NoobPage g  = gui.create("&lMap Selector", 45).c().s();
        Utils.makeFormat("MapSelector.yml", g, "items");
        gui.show(p, 0);
    }

}
