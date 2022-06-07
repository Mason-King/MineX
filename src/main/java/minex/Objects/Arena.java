package minex.Objects;

import minex.Objects.Team;
import minex.Utils.Utils;
import org.bukkit.*;

import java.util.*;

public class Arena {

    private String id;
    private String world;

    private List<String> spawns;
    private Map<String, String> spawnNames;
    private List<String> claimed;
    private Map<String, String> extractionNames;
    private List<String> extractions;
    private List<String> regions;


    public Arena(String id, Location spawn) {
        this.id = id;
        this.spawns = new ArrayList<>();
        spawns.add(Utils.toString(spawn));
    }

    public Arena(String id) {
        this.id = id;

        this.spawnNames = new HashMap<>();
        this.claimed = new ArrayList<>();
        this.spawns = new ArrayList<>();
        this.extractionNames = new HashMap<>();
        this.extractions = new ArrayList<>();
        this.regions = new ArrayList<>();

        initWorld();
    }

    public void initWorld() {
        WorldCreator wc = new WorldCreator(id + "Game");
        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.NORMAL);

        World world = wc.createWorld();
        this.world = id + "Game";
        this.spawns.add(Utils.toString(new Location(Bukkit.getWorld(this.world), 0, 100, 0)));
    }


    public Location getSpawn(int index) {
        if(getSpawns().size() <= index) return null;
        return Utils.fromString((spawnNames.get(getSpawns().get(index)) == null ? getSpawns().get(index) : spawnNames.get(getSpawns().get(index))));
    }

    public boolean exists(String name) {
        return spawnNames.containsKey(name);
    }

    public String getName(String s) {
        for(Map.Entry e : spawnNames.entrySet()) {
            if(e.getValue().equals(s)) {
                return (String) e.getKey();
            }
        }
        return null;
    }


    public Location getSpawn(String name) {
        return Utils.fromString(spawnNames.get(name));
    }

    public void addSpawn(String s, Location loc) {
        this.spawnNames.put(s, Utils.toString(loc));
        this.spawns.add(s);
    }

    public void addExtractionPoint(String name, Location loc) {
        this.extractionNames.put(name, Utils.toString(loc));
        this.extractions.add(Utils.toString(loc));
    }

    public void removeExtractionPoint(String name) {
        this.extractionNames.remove(name);
    }

    public boolean isClaimed(String s) {
        return claimed.contains(s);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public List<String> getSpawns() {
        return spawns;
    }

    public void setSpawns(List<String> spawns) {
        this.spawns = spawns;
    }

    public Map<String, String> getSpawnNames() {
        return spawnNames;
    }

    public void setSpawnNames(Map<String, String> spawnNames) {
        this.spawnNames = spawnNames;
    }

    public List<String> getClaimed() {
        return claimed;
    }

    public void setClaimed(List<String> claimed) {
        this.claimed = claimed;
    }

    public void addClaimed(String s) {
        this.claimed.add(s);
    }

    public Map<String, String> getExtractionNames() {
        return extractionNames;
    }

    public void setExtractionNames(Map<String, String> extractions) {
        this.extractionNames = extractions;
    }

    public void setExtractions(List<String> extractions) {
        this.extractions = extractions;
    }

    public List<String> getExtractions() {
        return this.extractions;
    }

    public void reset() {
        this.claimed = new ArrayList<>();
    }

}



