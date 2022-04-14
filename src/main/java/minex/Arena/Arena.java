package minex.Arena;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.schematic.Schematic;
import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.math.transform.Transform;
import minex.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

        Vector vec = new Vector(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ());
        BukkitWorld w = new BukkitWorld(world);
        System.out.println(vec);

        TaskManager.IMP.taskWhenFree(new Runnable() {
            @Override
            public void run() {
                try {
                    FaweAPI.load(file).paste(w, vec);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return false;
    }
}



