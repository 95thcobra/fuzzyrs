package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.WorldTile;

public class Abyssaltitan extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = 7635947578932404484L;

    public Abyssaltitan(final Player owner, final Summoning.Pouches pouch,
                        final WorldTile tile, final int mapAreaNameHash,
                        final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Essence Shipment";
    }

    @Override
    public String getSpecialDescription() {
        return "Sends all your inventory and beast's essence to your bank.";
    }

    @Override
    public int getBOBSize() {
        return 7;
    }

    @Override
    public int getSpecialAmount() {
        return 6;
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return SpecialAttack.CLICK;
    }

    @Override
    public boolean submitSpecial(final Object object) {
        if (getOwner().getBank().hasBankSpace()) {
            if (getBob().getBeastItems().getUsedSlots() == 0) {
                getOwner().getPackets().sendGameMessage(
                        "You clearly have no essence.");
                return false;
            }
            getOwner().getBank().depositAllBob(false);
            getOwner().setNextGraphics(new Graphics(1316));
            getOwner().setNextAnimation(new Animation(7660));
            return true;
        }
        return false;
    }
}
