package com.rs.world.npc.fightkiln;

import com.rs.player.Player;
import com.rs.player.controlers.FightKiln;
import com.rs.world.Entity;
import com.rs.world.Hit;
import com.rs.world.WorldTile;

@SuppressWarnings("serial")
public class TokHaarKetDill extends FightKilnNPC {

    private int receivedHits;

    public TokHaarKetDill(final int id, final WorldTile tile,
                          final FightKiln controler) {
        super(id, tile, controler);
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        if (hit.getLook() != Hit.HitLook.MELEE_DAMAGE
                && hit.getLook() != Hit.HitLook.RANGE_DAMAGE
                && hit.getLook() != Hit.HitLook.MAGIC_DAMAGE)
            return;
        if (receivedHits != -1) {
            final Entity source = hit.getSource();
            if (source == null || !(source instanceof Player))
                return;
            hit.setDamage(0);
            final Player playerSource = (Player) source;
            final int weaponId = playerSource.getEquipment().getWeaponId();
            if (weaponId == 1275 || weaponId == 13661 || weaponId == 15259) {
                receivedHits++;
                if ((weaponId == 1275 && receivedHits >= 5)
                        || ((weaponId == 13661 || weaponId == 15259) && receivedHits >= 3)) {
                    receivedHits = -1;
                    transformIntoNPC(getId() + 1);
                    playerSource
                            .getPackets()
                            .sendGameMessage(
                                    "Your pickaxe breaks the TokHaar-Ket-Dill's thick armour!");
                } else {
                    playerSource
                            .getPackets()
                            .sendGameMessage(
                                    "Your pickaxe slowy  cracks its way through the TokHaar-Ket-Dill's armour.");
                }
            }
        }

    }

}
