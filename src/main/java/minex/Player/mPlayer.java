package minex.Player;

import minex.Game.Game;
import minex.Party.Party;

import java.util.UUID;

public class mPlayer {

    private UUID id;
    private boolean inGame;
    private Game game;
    private Party party;


    public mPlayer(UUID uuid, boolean inGame, Game game, Party party) {
        this.id = uuid;
        this.inGame = inGame;
        this.game = game;
        this.party = party;
    }

    public mPlayer(UUID id) {
        this.id = id;
        this.inGame = false;
        this.game = null;
        this.party = null;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }
}
