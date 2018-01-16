package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.WorldTile;

public class Unicornstallion extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = -1291968400159646829L;

    public Unicornstallion(final Player owner, final Summoning.Pouches pouch,
                           final WorldTile tile, final int mapAreaNameHash,
                           final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Healing Aura";
    }

    @Override
    public String getSpecialDescription() {
        return "Heals 15% of your health points.";
    }

    @Override
    public int getBOBSize() {
        return 0;
    }

    @Override
    public int getSpecialAmount() {
        return 20;
    }

    @Override
    public boolean isAgressive() {
        return false;
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return SpecialAttack.CLICK;
    }

    @Override
    public boolean submitSpecial(final Object object) {
        final Player player = (Player) object;
        if (player.getHitpoints() == player.getMaxHitpoints()) {
            player.getPackets()
                    .sendGameMessage(
                            "You need to have at least some damage before being able to heal yourself.");
            return false;
        } else {
            player.setNextAnimation(new Animation(7660));
            player.setNextGraphics(new Graphics(1300));
            final int percentHealed = player.getMaxHitpoints() / 15;
            player.heal(percentHealed);
        }
        return true;
    }

}
