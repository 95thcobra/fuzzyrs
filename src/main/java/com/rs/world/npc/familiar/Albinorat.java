package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;

public class Albinorat extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = 558701463128149919L;

    public Albinorat(final Player owner, final Summoning.Pouches pouch,
                     final WorldTile tile, final int mapAreaNameHash,
                     final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Cheese Feast";
    }

    @Override
    public String getSpecialDescription() {
        return "Fills your inventory with four peices of cheese.YUM.";
    }

    @Override
    public int getBOBSize() {
        return 0;
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
        final Player player = (Player) object;
        player.setNextGraphics(new Graphics(1316));
        player.setNextAnimation(new Animation(7660));
        player.getInventory().addItem(new Item(1985, 4));
        return true;
    }
}
