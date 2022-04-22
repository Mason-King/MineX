package minex.Game;

import it.unimi.dsi.fastutil.Hash;

import java.util.*;

public class Team {

    private String name;
    private List<UUID> members;
    private boolean isAlive;
    private int size;
    private int maxSize;
    private String spawn;
    private int alive;

    public Team(String name) {
        this.name = name;
        this.members = new ArrayList<>();
        this.isAlive = true;
        this.size = 0;
        this.maxSize = 4;
        this.alive = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void setMembers(List<UUID> members) {
        this.members = members;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getAlive() {
        return alive;
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }

    public String getSpawn() {
        return spawn;
    }

    public void setSpawn(String spawn) {
        this.spawn = spawn;
    }

    public void addPlayer(UUID u) {
        this.members.add(u);
        this.size = this.size + 1;
        this.alive = this.alive + 1;
    }

    public void removePlayer(UUID u){
        this.members.remove(u);
        this.size = this.size - 1;
        this.alive = this.alive - 1;
    }

    public void died(UUID u) {
        this.alive = this.alive - 1;
    }
}
