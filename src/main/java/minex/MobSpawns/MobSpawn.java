package minex.MobSpawns;

import minex.Utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class MobSpawn {

    private String location;
    private String game;
    private int maxMobs;
    private int minMobs;
    private List<String> entities;

    public MobSpawn(String location, String game, int maxMobs, int minMobs, List<String> entities) {
        this.location = location;
        this.game = game;
        this.maxMobs = maxMobs;
        this.minMobs = minMobs;
        this.entities = entities;
    }

    public MobSpawn(String location, String game) {
        this.location = location;
        this.game = game;

        entities = new ArrayList<>();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public int getMaxMobs() {
        return maxMobs;
    }

    public void setMaxMobs(int maxMobs) {
        this.maxMobs = maxMobs;
    }

    public int getMinMobs() {
        return minMobs;
    }

    public void setMinMobs(int minMobs) {
        this.minMobs = minMobs;
    }

    public List<String> getEntities() {
        return entities;
    }

    public void setEntities(List<String> entities) {
        this.entities = entities;
    }

    public void addEntity(String type) {
        entities.add(type);
    }

    public void spawn() {
        for(String t : entities) {
            Location loc = Utils.fromString(location);
            loc.getWorld().spawnEntity(loc,EntityType.fromName(t));
        }
    }

}
