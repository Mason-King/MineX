package minex.Party;

import minex.Player.PlayerManager;
import minex.Player.mPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Party {

    private UUID leader;
    private int size;
    private boolean ingame;
    private List<UUID> uuids;
    private List<UUID> pendingInvites;

    public Party(UUID id, int size, boolean ingame, List<UUID> uuid, List<UUID> pendingInvites) {
        this.leader = id;
        this.size = size;
        this.ingame = ingame;
        this.uuids = uuid;
        this.pendingInvites = pendingInvites;
    }

    public Party(UUID id) {
        this.leader = id;
        this.size = 1;
        this.ingame = false;
        this.uuids = new ArrayList<>();
        this.pendingInvites = new ArrayList<>();
    }

    public UUID getLeader() {
        return leader;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isIngame() {
        return ingame;
    }

    public void setIngame(boolean ingame) {
        this.ingame = ingame;
    }

    public List<UUID> getUuids() {
        return uuids;
    }

    public void setUuids(List<UUID> uuids) {
        this.uuids = uuids;
    }

    public List<UUID> getPendingInvites() {
        return pendingInvites;
    }

    public void setPendingInvites(List<UUID> pendingInvites) {
        this.pendingInvites = pendingInvites;
    }

    public boolean hasInvite(Player p) {
        if(pendingInvites.contains(p.getUniqueId())) return true;
        return false;
    }

    public void invite(Player p) {
        pendingInvites.add(p.getUniqueId());
    }

    public void addPlayer(Player p) {
        pendingInvites.remove(p.getUniqueId());
        this.uuids.add(p.getUniqueId());

        mPlayer mp = PlayerManager.getPlayer(p.getUniqueId());
        mp.setParty(this);
    }


}
