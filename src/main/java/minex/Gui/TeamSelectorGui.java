package minex.Gui;

import minex.Game.Game;
import minex.Game.Team;
import minex.Main;
import minex.Player.mPlayer;
import minex.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class TeamSelectorGui {

    Main main = Main.getInstance();
    Gui gui = new Gui(main);

    File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/TeamSelector.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);


    public void makeGui(Player p, Game game) {
        Gui.NoobPage g  = gui.create(Utils.color(config.getString("title")), config.getStringList("format").size() * 9).c().s();
        Utils.makeFormat("TeamSelector.yml", g, "items");
        gui.show(p, 0);

        g.onClick(e -> {
           Player clicked = (Player) e.getWhoClicked();
           int slot = e.getSlot();
           if(config.get(slot + "") == null) return;
           String team = config.getString(slot + "");
           if(game.getTeam(team) == null) {
               clicked.sendMessage(Utils.color("&c&lMineX &7| Internal error, please contact admin"));
           } else {
               Team gTeam = game.getTeam(team);
               //need to make sure team has space!
               mPlayer mp = mPlayer.uuidPlayers.get(clicked.getUniqueId());
               if(mp.getParty() == null) {
                   //no party
                   if(gTeam.getSize() + 1 > gTeam.getMaxSize()) {
                       clicked.sendMessage(Utils.color("&c&lMineX &7| This team is full!"));
                   } else {
                       gTeam.addPlayer(clicked.getUniqueId());
                   }
               } else {
                   //party
                   if(gTeam.getSize() + mp.getParty().getSize() > gTeam.getMaxSize()) {
                       clicked.sendMessage(Utils.color("&c&lMineX &7| This team hass to many players for you and your party to join!"));
                   } else {
                       for(UUID u : mp.getParty().getMembers()) {
                           gTeam.addPlayer(u);
                       }
                   }
               }
           }
        });

    }


}
