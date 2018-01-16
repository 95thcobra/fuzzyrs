package com.rs.world.npc.familiar;

import com.rs.player.Player;
import com.rs.world.WorldTile;

/**
 * Represents the TzRek-Jad pet.
 *
 * @author Emperor
 */
public final class TzRekJad extends Familiar {

    /**
     * The serial UID.
     */
    private static final long serialVersionUID = 1345183208084953068L;

    /**
     * Constructs a new {@code TzRekJad} {@code Object}.
     *
     * @param owner The owner.
     * @param tile  The worldtask tile to spawn on.
     */
    public TzRekJad(final Player owner, final WorldTile tile) {
        super(owner, null, tile, -1, true);
    }

    @Override
    public String getSpecialName() {
        return "null";
    }

    @Override
    public String getSpecialDescription() {
        return "null";
    }

    @Override
    public int getBOBSize() {
        return 0;
    }

    @Override
    public int getSpecialAmount() {
        return 0;
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return null;
    }

    @Override
    public boolean submitSpecial(final Object object) {
        return false;
    }

}