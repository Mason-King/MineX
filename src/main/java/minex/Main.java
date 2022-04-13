package minex;

import minex.Game.GameCommand;
import minex.Party.PartyCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.saveResource("schematics/lobby.schem", false);
        // Plugin startup logic
        instance = this;
        new PartyCommand();
        new GameCommand();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance() {
        return instance;
    }

}
