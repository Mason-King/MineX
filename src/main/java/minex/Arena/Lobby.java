package minex.Arena;

import minex.Utils.Utils;
import org.bukkit.*;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private String id;
    private String spawn;
    private String world;

    public Lobby(String id, Location spawn) {
        this.id = id;
        this.spawn = Utils.toString(spawn);
    }

    public Lobby(String id) {
        this.id = id;

        generateWorlds();
    }

//    public boolean pasteSchematic() {
//
//        File file = new File(Main.getInstance().getDataFolder().getAbsolutePath() +  "/schematics/lobby.schem");
//
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
        WorldCreator wc = new WorldCreator(id + "Lobby");

        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.FLAT);
        wc.generatorSettings("2;0;1;");

        World world = wc.createWorld();
        this.world = id + "Lobby";
        this.spawn = Utils.toString(new Location(Bukkit.getWorld(this.world), 0, 100, 0));
    }

    public Location getSpawn() {
        return Utils.fromString(spawn);
    }

    public void setSpawn(Location spawn) {
        this.spawn = (Utils.toString(spawn));
    }

    public void setWorld(World world) {
        this.world = world.getName();
    }

}
