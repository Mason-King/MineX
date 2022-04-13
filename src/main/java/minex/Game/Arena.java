package minex.Game;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.util.Locale;

public class Arena {

    private String id;
    private Location spawn;
    private String world;
    private ArenaType type;

    public Arena(String id, Location spawn, String world, ArenaType type) {
        this.id = id;
        this.spawn = spawn;
        this.type = type;
        this.world = world;
    }

    public Arena(String id, ArenaType type) {
        this.id = id;
        this.type = type;
    }

    public void generate() {
        //Now we will create the world itself
        if(type.equals(ArenaType.LOBBY)) {
            WorldCreator wc = new WorldCreator("WORLD NAME HERE");
            wc.type(WorldType.FLAT);
            wc.generatorSettings("2;0;1;"); //This is what makes the world empty (void)
            World world = wc.createWorld();
            this.spawn = world.getSpawnLocation();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        spawn = spawn;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public ArenaType getType() {
        return type;
    }

    public void setType(ArenaType type) {
        this.type = type;
    }

    

}
