package minex.Messages;

import minex.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public enum Message {
    // Declare all the messages
    PREFIX("prefix"),
    CONSOLE_ERROR("consoleError"),
    GAME_CREATE_USAGE("gameCreateUsage"),
    GAME_EXISTS("gameExists"),
    GAME_CREATING("gameCreating"),
    GAME_TP_USAGE("gameTpUsage"),
    NO_GAME("noGame"),
    NO_SPAWN("noSpawn"),
    GAME_ADDSPAWN_USAGE("gameAddspawnUsage"),
    GAME_SPAWN_SET("gameSpawnSet"),
    //GAME_LOBBY_HELP("gameLobbyHelp"),
    GAME_LOBBY_SPAWN("gameLobbySpawn"),
    GAME_LOBBY_SPAWN_USAGE("gameLobbySpawnUsage"),
    GAME_LOBBY_TP_USAGE("gameLobbyTpUsage");


    private final String text;
    // The values from the config
    private static Map<String, String> messages = new HashMap<>();

    // Constructor
    Message(final String text) {
        this.text = text;
    }

    public String getMessage() {
        Main m = Main.getPlugin(Main.class);
        return ChatColor.translateAlternateColorCodes('&',  m.getConfig().getString("messages.prefix") +  messages.get(text));
    }

    // The send method, makes it so you can use Message.PERMISSION.send(player);
    public void send(CommandSender sender) {
        Main m = Main.getPlugin(Main.class);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', m.getConfig().getString("messages.prefix") + messages.get(text)));
    }

    // Static initializer to initialize the messages from the config
    static {
        Main m = Main.getPlugin(Main.class);
        for (String key :m.getConfig().getConfigurationSection("messages").getKeys(true))
            messages.put(key, m.getConfig().getString("messages." + key));
    }
}
