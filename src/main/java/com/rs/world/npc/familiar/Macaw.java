package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.herblore.HerbCleaning;
import com.rs.content.actions.skills.summoning.Summoning;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;

public class Macaw extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = -7805271915467121215L;

    public Macaw(final Player owner, final Summoning.Pouches pouch, final WorldTile tile,
                 final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public String getSpecialName() {
        return "Herbcall";
    }

    @Override
    public String getSpecialDescription() {
        return "Creates a random herb.";
    }

    @Override
    public int getBOBSize() {
        return 0;
    }

    @Override
    public int getSpecialAmount() {
        return 12;
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return SpecialAttack.CLICK;
    }

    @Override
    public boolean submitSpecial(final Object object) {
        final Player player = (Player) object;
        HerbCleaning.Herbs herb;
        player.setNextGraphics(new Graphics(1300));
        player.setNextAnimation(new Animation(7660));
        // TODO too lazy to find anims and gfx
        if (Utils.getRandom(100) == 0) {
            herb = HerbCleaning.Herbs.values()[Utils.random(HerbCleaning.Herbs.values().length)];
        } else {
            herb = HerbCleaning.Herbs.values()[Utils.getRandom(3)];
        }
        World.addGroundItem(new Item(herb.getHerbId(), 1), player);
        return true;
    }
}
