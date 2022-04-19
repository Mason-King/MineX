package minex.Gui;

import minex.Game.Game;
import minex.Main;
import minex.Managers.GameManager;
import minex.Party.Party;
import minex.Player.mPlayer;
import minex.Utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class GameSelectorGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/GameSelector.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);


    public void makeGui(Player p) {
        Gui.NoobPage g  = gui.create(Utils.color(config.getString("title")), config.getStringList("format").size() * 9).c().s();
        Utils.makeFormat("GameSelector.yml", g, "items");
        gui.show(p, 0);

        g.onClick(e -> {
           Player clicked = (Player) e.getWhoClicked();
           mPlayer mp = mPlayer.uuidPlayers.get(clicked.getUniqueId());
            System.out.println(mp.getParty());
           Party party = (mp.getParty() == null) ? null : mp.getParty();
           if(mp.getCurrGame() != null) {
               clicked.sendMessage("You must leave the current game you are in join another!");
           } else {
               //Need to implement a filter with the games closes to max players!
                Game game =  GameManager.getFullest();
                int curr = game.getCurrPlayers();
                int max = game.getMaxPlayers();
                if(curr + (party == null ? 1 : party.getMembers().size()) <= max) {
                    if ((party == null)) {
                        game.addPlayer(clicked.getUniqueId());
                    } else {
                        game.addParty(party);
                    }
                }
           }
        });
    }

}
