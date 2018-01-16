package com.rs.content.christmas.funnyjoke;

import com.rs.player.Player;

import java.io.Serializable;

/**
 * @author John (FuzzyAvacado) on 12/11/2015.
 */
public class FunnyJoke implements Serializable {

    private static final long serialVersionUID = -3785105434572546424L;

    private String[] joke;
    private transient Player player;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void sendJoke() {
        if (joke == null) {
            player.getPackets().sendGameMessage("Hmm...it appears you are not deserving of a funny joke...how did you get this?!");
            return;
        }
        player.getPackets().sendGameMessage("You roll open the joke to read...");
        player.getInterfaceManager().sendInterface(275);
        for (int i = 0; i < 150; i++) {
            player.getPackets().sendIComponentText(275, i, " ");
        }
        player.getPackets().sendIComponentText(275, 1, joke[0]);
        player.getPackets().sendIComponentText(275, 12, joke[1]);
        player.getPackets().sendIComponentText(275, 13, "");
        player.getPackets().sendIComponentText(275, 14, "");
        player.getPackets().sendIComponentText(275, 15, joke[2]);
    }

    public String[] getJoke() {
        return joke;
    }

    public void setJoke(String[] joke) {
        this.joke = joke;
    }

    public void setJoke(String jokeLine) {
        this.joke = jokeLine.split(":");

    }
}
