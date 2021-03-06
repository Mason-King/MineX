package minex.Gui;

import minex.Objects.Game;
import minex.Main;
import minex.Managers.GameManager;
import minex.Managers.PlayerManager;
import minex.Objects.Party;
import minex.Objects.mPlayer;
import minex.Utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class GameSelectorGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/GameGui.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);


    public void makeGui(Player p) {
        Gui.NoobPage g  = gui.create(Utils.color(config.getString("title")), config.getStringList("format").size() * 9).c().s();
        Utils.makeFormat("GameGui.yml", g, "items");
        gui.show(p, 0);

        g.onClick(e -> {
            Player clicked = (Player) e.getWhoClicked();
            mPlayer mp = PlayerManager.getmPlayer(clicked.getUniqueId());
            Party party = (mp.getParty() == null) ? null : mp.getParty();
            if(mp.getCurrGame() != null) {
                //TODO - add msg
                clicked.sendMessage("You must leave the current game you are in join another!");
            } else {
                //Our list of games, from highest to lowest i
                List<Game> games =  GameManager.getFullest();
                Game game = null;
                for(Game tempGame : games) {
                    if(tempGame.getCurrPlayers() + ((party == null) ? 1 : party.getSize()) <= tempGame.getMaxPlayers() && !tempGame.isInGame()) {
                        game = tempGame;
                    }
                }
                if(party == null) {
                    if(game == null) {
                        //TODO - add msg
                        clicked.sendMessage(Utils.color("&c&lMineX &7| All games are full or in progress, please try again shortly."));
                        return;
                    }
                    game.joinGame(clicked.getUniqueId());
                } else {
                    game.joinGame(party);
                }
                clicked.closeInventory();
            }
        });
    }

}
