package minex.Party;

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

    public Party(UUID owner) {
        this.owner = owner;
        members = new ArrayList<>();
        members.add(owner);

        parties.put(owner, this);
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
            return true;
        }
    }

}
