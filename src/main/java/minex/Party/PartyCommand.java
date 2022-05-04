package minex.Party;

import minex.Main;
import minex.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

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
                                    owner.sendMessage(player.getName() + " has joined your party!");
                                } else {
                                    player.sendMessage("This party has reached the max size!");
                                }
                            } else {
                                player.sendMessage("You do not have an invite from this player!");
                            }
                        }
                    }

                } else if(args[0].equalsIgnoreCase("create")) {
                    Party party;
                    if(Party.parties.containsKey(player.getUniqueId())) {
                        //They have a party
                        party = Party.parties.get(player.getUniqueId());
                        player.sendMessage("You already have a party!");
                    } else {
                        party = new Party(player.getUniqueId());
                        player.sendMessage("You have created a new party. Invite people using /party invite <player>");
                    }
                } else if(args[0].equalsIgnoreCase("kick")) {
                    Party p = Party.parties.get(player.getUniqueId());
                    if(p == null) {
                        player.sendMessage("You do not have a party!");
                    }
                    if(p.getOwner().equals(player.getUniqueId())) {
                        if(Bukkit.getPlayer(args[1]) == null) {
                            player.sendMessage("Invalid player!");
                        } else {
                            Player target = Bukkit.getPlayer(args[1]);
                            if(p.getMembers().contains(target.getUniqueId())) {
                                p.removeMember(target.getUniqueId());
                                player.sendMessage("You have kicked " + target.getName());
                            }
                        }
                    } else {
                        player.sendMessage("You are not the party leader!");
                    }
                } else if(args[0].equalsIgnoreCase("disband")) {
                    Party p = Party.parties.get(player.getUniqueId());
                    if(p == null) {
                        player.sendMessage(Utils.color("&c&lMineX &7| You do nto have a party!"));
                        return false;
                    }
                    if(p.getOwner().equals(player.getUniqueId())) {
                        for(UUID u : p.getMembers()) {
                            p.removeMember(u);
                        }
                        p = null;
                    } else {
                        player.sendMessage(Utils.color("&c&lMineX &7| You are not the leader of this party!"));
                    }
                }
            }
        }
        return false;
    }
}
