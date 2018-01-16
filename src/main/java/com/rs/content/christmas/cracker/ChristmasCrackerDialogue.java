package com.rs.content.christmas.cracker;

import com.rs.content.christmas.funnyjoke.FunnyJokeHandler;
import com.rs.content.dialogues.Dialogue;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.ForceTalk;
import com.rs.world.Graphics;

/**
 * @author John (FuzzyAvacado) on 12/11/2015.
 */
public class ChristmasCrackerDialogue extends Dialogue {

    private Player usedOn;

    @Override
    public void start() {
        usedOn = (Player) parameters[0];
        sendOptionsDialogue("If you pull the cracker, it will be destroyed.",
                "That's okay, I might get a party hat!",
                "Stop. I want to keep my cracker.");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (componentId) {
            case OPTION_1:
                player.sendMessage("You pull a Christmas cracker...");
                player.getInventory().deleteItem(962, 1);
                usedOn.faceEntity(player);
                player.setNextAnimation(new Animation(15153));
                usedOn.setNextAnimation(new Animation(15153));
                Player victor = Utils.random(100) <= 50 ? player : usedOn;
                victor.setNextGraphics(new Graphics(176));
                victor.setNextForceTalk(new ForceTalk("Hey! I got the cracker!"));
                victor.getInventory().addItem(ChristmasCrackerItems.getPartyhats());
                victor.getInventory().addItem(ChristmasCrackerItems.getExtraItems());
                FunnyJokeHandler.giveFunnyJoke(victor);
                if (victor.equals(player)) {
                    player.sendMessage("The person with whom you pull the cracker gets the prize!");
                } else {
                    player.sendMessage("You receive the prize from the cracker!");
                }
                end();
                break;
            default:
                end();
                break;
        }

    }

    @Override
    public void finish() {
    }

}
