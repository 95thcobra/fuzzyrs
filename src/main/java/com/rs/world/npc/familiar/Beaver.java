package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.content.actions.skills.woodcutting.Woodcutting;
import com.rs.player.Player;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;

public class Beaver extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = -9181393770444014076L;

    public Beaver(final Player owner, final Summoning.Pouches pouch,
                  final WorldTile tile, final int mapAreaNameHash,
                  final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Multichop";
    }

    @Override
    public String getSpecialDescription() {
        return "Chops a tree, giving the owner its logs. There is also a chance that random logs may be produced.";
    }

    @Override
    public int getBOBSize() {
        return 0;
    }

    @Override
    public int getSpecialAmount() {
        return 3;
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return SpecialAttack.OBJECT;
    }

    @Override
    public boolean submitSpecial(final Object context) {
        final WorldObject object = (WorldObject) context;
        getOwner().getActionManager().setAction(
                new Woodcutting(object, Woodcutting.TreeDefinitions.NORMAL));
        return true;
    }
}
