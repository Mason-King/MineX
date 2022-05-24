package minex.Commands;

import minex.Main;
import minex.Enums.Message;
import minex.Objects.Party;
import minex.Utils.Utils;
import org.bukkit.Bukkit;
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
            sender.sendMessage(Message.CONSOLE.getMessage());
        } else {
            Player player = (Player) sender;
            if(args.length == 0) {
                for(String sm : Utils.color(main.getConfig().getStringList("partyHelp"))) {
                    player.sendMessage(sm);
                }
            } else {
                if(args[0].equalsIgnoreCase("invite")) {
                    if(args.length < 2) {
                        player.sendMessage(Message.PARTY_INVITE_USAGE.getMessage());
                    } else {
                        if(Bukkit.getPlayer(args[1]) == null) {
                            player.sendMessage(Message.INVALID_PLAYER.getMessage());
                        }
                        Player target = Bukkit.getPlayer(args[1]);
                        Party party;
                        if(Party.parties.containsKey(player.getUniqueId())) {
                            //They have a party
                            party = Party.parties.get(player.getUniqueId());
                        } else {
                            party = new Party(player.getUniqueId());
                        }
                        target.sendMessage(Message.PARTY_INVITED.getMessage().replace("{name}", player.getName()));

                        player.sendMessage(Message.PARTY_INVITED_PLAYER.getMessage().replace("{name}", target.getName()));
                        Party.pendingInvites.put(target.getUniqueId(), party);
                    }
                } else if(args[0].equalsIgnoreCase("join")) {
                    if(args.length < 2) {
                        player.sendMessage(Message.INVALID_PLAYER.getMessage());
                    } else {
                        if(Bukkit.getPlayer(args[1]) == null) {
                            player.sendMessage(Message.INVALID_PLAYER.getMessage());
                        } else {
                            Player owner = Bukkit.getPlayer(args[1]);
                            if(Party.parties.containsKey(owner.getUniqueId()) && Party.pendingInvites.containsKey(player.getUniqueId())) {
                                Party party = Party.parties.get(owner.getUniqueId());
                                if(party.addMember(player.getUniqueId())) {
                                    player.sendMessage(Message.PARTY_JOINED.getMessage().replace("{name}", owner.getName()));
                                    owner.sendMessage(Message.PARTY_JOINED_PLAYER.getMessage().replace("{name}", player.getName()));
                                } else {
                                    player.sendMessage(Message.PARTY_MAX.getMessage());
                                }
                            } else {
                                player.sendMessage(Message.NO_INVITE.getMessage());
                            }
                        }
                    }

                } else if(args[0].equalsIgnoreCase("create")) {
                    Party party;
                    if(Party.parties.containsKey(player.getUniqueId())) {
                        //They have a party
                        party = Party.parties.get(player.getUniqueId());
                        player.sendMessage(Message.ALREADY_IN_PARTY.getMessage());
                    } else {
                        party = new Party(player.getUniqueId());
                        player.sendMessage(Message.PARTY_CREATED.getMessage());
                    }
                } else if(args[0].equalsIgnoreCase("kick")) {
                    Party p = Party.parties.get(player.getUniqueId());
                    if(p == null) {
                        player.sendMessage(Message.NO_PARTY.getMessage());
                    }
                    if(p.getOwner().equals(player.getUniqueId())) {
                        if(Bukkit.getPlayer(args[1]) == null) {
                            player.sendMessage(Message.INVALID_PLAYER.getMessage());
                        } else {
                            Player target = Bukkit.getPlayer(args[1]);
                            if(p.getMembers().contains(target.getUniqueId())) {
                                p.removeMember(target.getUniqueId());
                                player.sendMessage(Message.PLAYER_KICKED.getMessage().replace("{name}", target.getName()));
                            }
                        }
                    } else {
                        player.sendMessage(Message.NOT_LOADER.getMessage());
                    }
                } else if(args[0].equalsIgnoreCase("disband")) {
                    Party p = Party.parties.get(player.getUniqueId());
                    if(p == null) {
                        player.sendMessage(Message.NO_PARTY.getMessage());
                        return false;
                    }
                    if(p.getOwner().equals(player.getUniqueId())) {
                        for(UUID u : p.getMembers()) {
                            p.removeMember(u);
                        }
                        p = null;
                        player.sendMessage(Message.PARTY_DISBAND.getMessage());
                    } else {
                        player.sendMessage(Message.NOT_LOADER.getMessage());
                    }
                }
            }
        }
        return false;
    }
}
