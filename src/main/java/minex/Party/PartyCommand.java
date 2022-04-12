package minex.Party;

import minex.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyCommand implements CommandExecutor {

    Main main = Main.getInstance();

    public PartyCommand() {
        main.getCommand("party").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command.");
        } else {
            Player player = (Player) sender;
            if(args.length == 0) {
                player.sendMessage("Party help!");
            } else {
                if(args[0].equalsIgnoreCase("invite")) {
                    if(args.length < 2) {
                        player.sendMessage("Party invite usage");
                    } else {
                        if(Bukkit.getPlayer(args[1]) == null) {
                            player.sendMessage("Invalid player");
                        }
                        Player target = Bukkit.getPlayer(args[1]);
                        Party party;
                        if(Party.parties.containsKey(player.getUniqueId())) {
                            //They have a party
                            party = Party.parties.get(player.getUniqueId());
                        } else {
                            party = new Party(player.getUniqueId());
                        }
                        target.sendMessage("You have been invited to join " + player.getName() + "'s party!");
                        target.sendMessage("Use /party join " + player.getName() + " to join.");

                        player.sendMessage("You have invited " + target.getName() + " to your party.");
                        Party.pendingInvites.put(target.getUniqueId(), party);
                    }
                } else if(args[0].equalsIgnoreCase("join")) {
                    if(args.length < 2) {
                        player.sendMessage("invalid player");
                    } else {
                        if(Bukkit.getPlayer(args[1]) == null) {
                            player.sendMessage("invalid player");
                        } else {
                            Player owner = Bukkit.getPlayer(args[1]);
                            if(Party.parties.containsKey(owner.getUniqueId()) && Party.pendingInvites.containsKey(player.getUniqueId())) {
                                Party party = Party.parties.get(owner.getUniqueId());
                                if(party.addMember(player.getUniqueId())) {
                                    player.sendMessage("You have joined " + owner.getName() + "'s party");
                                } else {
                                    player.sendMessage("This party has reached the max size!");
                                    System.out.println(party.getOwner());
                                }
                            } else {
                                player.sendMessage("You do not have an invite from this player!");
                            }
                        }
                    }

                }
            }
        }
        return false;
    }
}
