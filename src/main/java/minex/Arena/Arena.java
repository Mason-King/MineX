package minex.Arena;

import com.sk89q.worldedit.Vector;
import minex.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.io.File;
import java.io.IOException;

public class Arena {

    private String id;
    private Location spawn;
    private World world;
    private ArenaType type;

    public Arena(String id, Location spawn, ArenaType type) {
        this.id = id;
        this.spawn = spawn;
        this.type = type;
    }

    public Arena(String id) {
        this.id = id;

        if(world == null) {
            WorldCreator wc = new WorldCreator(id + type);

            wc.environment(World.Environment.NORMAL);
            wc.type(WorldType.FLAT);
            wc.generatorSettings("2;0;1;");

            this.world = wc.createWorld();

            this.spawn = world.getSpawnLocation();
        }

        pasteSchematic();
    }

    public boolean pasteSchematic() {

        File file = new File(Main.getInstance().getDataFolder().getAbsolutePath() +  "/schematics/lobby.schem");
        
    }
}



