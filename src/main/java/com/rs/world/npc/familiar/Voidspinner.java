package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning.Pouches;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.WorldTile;

public class Voidspinner extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = -1639238550551778316L;

    public Voidspinner(final Player owner, final Pouches pouch,
                       final WorldTile tile, final int mapAreaNameHash,
                       final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Call To Arms";
    }

    @Override
    public String getSpecialDescription() {
        return "Teleports the player to Void Outpost.";
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
        return SpecialAttack.CLICK;
    }

    @Override
    public boolean submitSpecial(final Object object) {
        final Player player = (Player) object;
        player.setNextGraphics(new Graphics(1316));
        player.setNextAnimation(new Animation(7660));
        // Magic.sendTeleportSpell(player, upEmoteId, downEmoteId, upGraphicId,
        // downGraphicId, 0, 0, tile, 3, true, Magic.OBJECT_TELEPORT);
        return true;
    }
}
