package minex.Arena;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import minex.Game.Team;
import minex.Main;
import minex.Utils.Utils;
import org.bukkit.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Arena {

    private String id;
    private List<String> spawns;
    private Map<String, String> spawnNames;
    private Map<UUID, String> playerSpawns;
    private String world;

    public Arena(String id, Location spawn) {
        this.id = id;
        this.spawns = new ArrayList<>();
        spawns.add(Utils.toString(spawn));
    }

    public Arena(String id) {
        this.id = id;

        this.spawnNames = new HashMap<>();
        this.playerSpawns = new HashMap<>();
        this.spawns = new ArrayList<>();

        generateWorlds();
    }

//    public boolean pasteSchematic() {
//
//        File file = new File(Main.getInstance().getDataFolder().getAbsolutePath() +  "/schematics/lobby.schem");
//
//        System.out.println(file.isFile());
//
//        WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
//        EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(spawn.getWorld()), 100000);
//        try {
//            CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(file).load(file);
//            clipboard.setOrigin(new Vector(spawn.getBlockX(),spawn.getBlockY(), spawn.getBlockZ()));
//            clipboard.paste(session, clipboard.getOrigin(), false);
//        } catch(MaxChangedBlocksException e) {
//            e.printStackTrace();
//        } catch (DataException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }

    public void generateWorlds() {
        WorldCreator wc = new WorldCreator(id + "Game");
        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.NORMAL);

        World world = wc.createWorld();
        this.world = id + "Game";
        this.spawns.add(Utils.toString(new Location(Bukkit.getWorld(this.world), 0, 100, 0)));
    }

    public Location getSpawn(int id) {
        return Utils.fromString(spawns.get(id));
    }

    public Location getSpawn(String name) {
        return Utils.fromString(spawnNames.get(name));
    }

    public void addSpawn(Location spawn, String name) {
        this.spawns.add(Utils.toString(spawn));
        this.spawnNames.put(name, Utils.toString(spawn));
    }

    public List<String> getSpawns() {
        return this.spawns;
    }

    public String getName(String s) {
        for(Map.Entry e : spawnNames.entrySet()) {
            if(e.getValue().equals(s)) {
                return (String) e.getKey();
            }
        }
        return null;
    }

    public void setWorld(World world) {
        this.world = world.getName();
    }

}



