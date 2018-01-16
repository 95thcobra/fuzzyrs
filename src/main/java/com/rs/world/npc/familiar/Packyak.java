package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning.Pouches;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.WorldTile;

public class Packyak extends Familiar {

    private static final long serialVersionUID = -1397015887332756680L;

    public Packyak(final Player owner, final Pouches pouch,
                   final WorldTile tile, final int mapAreaNameHash,
                   final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, false);
    }

    @Override
    public int getSpecialAmount() {
        return 12;
    }

    @Override
    public String getSpecialName() {
        return "Winter Storage";
    }

    @Override
    public String getSpecialDescription() {
        return "Use special move on an item in your inventory to send it to your bank.";
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return SpecialAttack.ITEM;
    }

    @Override
    public int getBOBSize() {
        return 30;
    }

    @Override
    public boolean isAgressive() {
        return false;
    }

    @Override
    public boolean submitSpecial(final Object object) {
        final int slotId = (Integer) object;
        if (getOwner().getBank().hasBankSpace()) {
            getOwner().getBank().depositItem(slotId, 1, false);
            getOwner().getPackets().sendGameMessage(
                    "Your Pack Yak has sent an item to your bank.");
            getOwner().setNextGraphics(new Graphics(1316));
            getOwner().setNextAnimation(new Animation(7660));
        }
        return true;
    }
}
