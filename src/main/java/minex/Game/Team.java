package minex.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {

    private String id;
    private String game;
    //list of all people on team
    private List<UUID> members;
    private boolean hasMembers;

    //constructor

    //also unlikely to be used
    public Team(String id, String game, List<UUID> members, boolean hasMembers) {
        this.id = id;
        this.game = game;
        this.members = members;
    }

    public Team(String id, String game) {
        this.id = id;
        this.game = game;

        this.members = new ArrayList<>();
    }

    public void addMember(UUID u) {
        getMembers().add(u);
    }

    public void removeMember(UUID u) {
        if(getMembers().contains(u)) getMembers().add(u);
    }

    //getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void setMembers(List<UUID> members) {
        this.members = members;
    }

    public boolean isHasMembers() {
        return hasMembers;
    }

    public void setHasMembers(boolean hasMembers) {
        this.hasMembers = hasMembers;
    }

}
