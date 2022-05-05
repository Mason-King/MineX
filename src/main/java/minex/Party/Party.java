package minex.Party;

import minex.Managers.PlayerManager;
import minex.Player.mPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Party {

    public static HashMap<UUID, Party> parties = new HashMap<>();
    public static HashMap<UUID, Party> pendingInvites = new HashMap<>();

    private UUID owner;
    private List<UUID> members;
    private int maxMembers = 4;
    private int size;

    public Party(UUID owner) {
        this.owner = owner;
        members = new ArrayList<>();
        members.add(owner);
        parties.put(owner, this);

        mPlayer player = PlayerManager.getmPlayer(owner);
        player.setParty(this);

        this.size = 1;

    }

    public UUID getOwner() {
        return owner;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public boolean addMember(UUID id) {
        if(members.size() >= maxMembers) {
            return false;
        } else {
            members.add(id);
            this.size++;
            return true;
        }
    }

    public void removeMember(UUID id) {
        members.remove(id);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
