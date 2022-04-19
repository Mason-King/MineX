package minex.Commands;

import minex.Party.Party;
import minex.Player.PlayerManager;
import minex.Player.mPlayer;
import minex.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;
        mPlayer mp = PlayerManager.getPlayer(player.getUniqueId());
        if(args.length == 0) {
            player.sendMessage(Utils.color("&c&lMineX &7| Party help command"));
        } else {
            if(args[0].equalsIgnoreCase("invite")) {
                if(args.length < 2) {
                    player.sendMessage(Utils.color("&c&lMineX &7| Invalid command usage try: /party invite <player>"));
                } else {
                    if(Bukkit.getPlayer(args[1]) == null) {
                        player.sendMessage(Utils.color("&c&lMineX &7| Invalid player!"));
                    } else {
                        Player target = Bukkit.getPlayer(args[1]);
                        Party party = new Party(player.getUniqueId());

                        player.sendMessage(Utils.color("&c&lMineX &7| You have invited " + target.getName() + " to join your party!"));
                        target.sendMessage(Utils.color("&c&lMineX &7| You have been invited to join " + player.getName() + "'s party!"));

                        party.invite(target);
                    }
                }
            } else if(args[0].equalsIgnoreCase("join")) {
                if(args.length < 2) {
                    player.sendMessage(Utils.color("&c&lMineX &7| Invalid command usage try: /party join <player>"));
                } else {
                    if(Bukkit.getPlayer(args[1]) == null) {
                        player.sendMessage(Utils.color("&c&lMineX &7| Invalid player!"));
                    } else {
                        Player leader = Bukkit.getPlayer(args[1]);
                        mPlayer leaderMP = PlayerManager.getPlayer(leader.getUniqueId());
                        Party party = leaderMP.getParty();
                        if(party == null) {
                            player.sendMessage(Utils.color("&c&lMineX &7| This player does not have a party!"));
                        } else {
                            if(party.hasInvite(player)) {
                                player.sendMessage(Utils.color("&c&lMineX &7| You have joined " + leader.getName() + "'s party!"));
                                leader.sendMessage(Utils.color("&c&lMineX &7| " + player.getName() + " has joined your party!"));

                                party.addPlayer(player);
                            } else {
                                player.sendMessage(Utils.color("&c&lMineX &7| You do now have an invite to join this players party"));
                            }
                        }
                    }
                }
            }
        }

        return false;
    }
}
