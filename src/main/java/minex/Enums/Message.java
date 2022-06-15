package minex.Enums;

import minex.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public enum Message {
    // Declare all the messages
    GAME_WORLD("gameWorld"),
    LOOTCHEST_PLACED("lootchestPlaced"),
    EXTRACTION_CANCELED("extractionCanceled"),
    EXTRACTION("extraction"),
    POS1("pos1"),
    POS2("pos2"),
    BANK_GIVE_USAGE("bankGiveUsage"),
    NO_PERMISSION("noPermission"),
    CONSOLE("console"),
    GAME_CREATE_USAGE("gameCreateUsage"),
    GAME_EXISTS("gameExists"),
    GAME_CREATING("gameCreating"),
    ADD_MOB_SPAWN_USAGE("addMobSpawnUsage"),
    INVALID_GAME("invalidGame"),
    INVALID_MOB_TYPE("invalidMobType"),
    GAME_TELEPORT_USAGE("gameTeleportUsage"),
    INVALID_SPAWN("invalidSpawn"),
    REGION_WAND("regionWand"),
    REGION_CREATE_USAGE("regionCreateUsage"),
    REGION_CREATED("regionCreated"),
    NO_GAME("noGame"),
    LEFT_GAME("leftGame"),
    LOOTCHEST_GIVE_USAGE("lootchestGiveUsage"),
    LOOTCHEST_GIVE("lootchestGive"),
    INVALID_TYPE("invalidType"),
    ADD_SPAWN_USAGE("addspawnUsage"),
    ADDED_SPAWN("addedSpawn"),
    ADD_EXTRACTION_USAGE("addextractionUsage"),
    EXTRACTION_EXISTS("extractionExists"),
    NEW_EXTRACTION("newExtraction"),
    IN_GAME("inGame"),
    GAME_LOBBY_SETSPAWN_USAGE("gameLobbySetspawnUsage"),
    GAME_LOBBY_SPAWNSET("gameLobbySpawnSet"),
    GAME_LOBBY_TP_USAGE("gameLobbyTpUsage"),
    PARTY_INVITE_USAGE("partyInviteUsage"),
    INVALID_PLAYER("invalidPlayer"),
    PARTY_INVITED("partyInvited"),
    PARTY_INVITED_PLAYER("partyInvitePlayer"),
    PARTY_JOINED("partyJoined"),
    PARTY_JOINED_PLAYER("partyJoinedPlayer"),
    PARTY_MAX("partyMax"),
    NO_INVITE("noInvite"),
    ALREADY_IN_PARTY("alreadyInParty"),
    PARTY_CREATED("partyCreated"),
    NO_PARTY("noParty"),
    PLAYER_KICKED("playerKicked"),
    NOT_LOADER("notLoader"),
    PARTY_DISBAND("partyDisband"),
    SPAWN_EXISTS("spawnExists"),
    MOBSPAWN_ADDED("mobspawnAdded"),
    BANK_DISABLED("bankDisabled"),
    SOLD("sold"),
    PLAYER_KILLED("playerKilled"),
    MAX_STASH("maxStash"),
    TIME_REMAINING("timeRemaining"),
    GAME_ENDING("gameEnding"),
    GAME_STARTING("gameStarting"),
    FARM_MAX("farmMax");



    private final String text;
    // The values from the config
    private static Map<String, String> messages = new HashMap<>();

    // Constructor
    Message(final String text) {
        this.text = text;
    }

    public String getMessage() {
        Main m = Main.getPlugin(Main.class);
        return ChatColor.translateAlternateColorCodes('&',  messages.get(text));
    }

    // The send method, makes it so you can use Message.PERMISSION.send(player);
    public void send(CommandSender sender) {
        Main m = Main.getPlugin(Main.class);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.get(text)));
    }

    // Static initializer to initialize the messages from the config
    static {
        Main m = Main.getPlugin(Main.class);
        for (String key :m.getConfig().getConfigurationSection("messages").getKeys(true))
            messages.put(key, m.getConfig().getString("messages." + key));
    }
}
