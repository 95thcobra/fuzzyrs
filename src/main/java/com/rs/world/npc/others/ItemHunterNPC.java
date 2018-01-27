package com.rs.world.npc.others;

import com.rs.content.actions.skills.hunter.BoxAction;
import com.rs.content.actions.skills.hunter.Hunter;
import com.rs.utils.Utils;
import com.rs.player.OwnedObjectManager;
import com.rs.player.Player;
import com.rs.world.World;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

import java.util.List;

@SuppressWarnings("serial")
public class ItemHunterNPC extends NPC {

    public ItemHunterNPC(final int id, final WorldTile tile,
                         final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea,
                         final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
    }

    @Override
    public void processNPC() {
        super.processNPC();
        final List<WorldObject> objects = World.getRegion(getRegionId())
                .getSpawnedObjects();
        if (objects != null) {
            final BoxAction.HunterNPC info = BoxAction.HunterNPC.forId(getId());
            final int objectId = info.getEquipment().getObjectId();
            for (final WorldObject object : objects) {
                if (object.getId() == objectId) {
                    if (OwnedObjectManager.convertIntoObject(
                            object,
                            new WorldObject(info
                                    .getSuccessfulTransformObjectId(), 10, 0,
                                    this.getX(), this.getY(), this.getPlane()),
                            new OwnedObjectManager.ConvertEvent() {
                                @Override
                                public boolean canConvert(final Player player) {
                                    if (player == null
                                            || player.getLockDelay() > Utils
                                            .currentTimeMillis())
                                        return false;
                                    if (Hunter.isSuccessful(player,
                                            info.getLevel(),
                                            new Hunter.DynamicFormula() {
                                                @Override
                                                public int getExtraProbablity(
                                                        final Player player) {
                                                    // bait here
                                                    return 1;
                                                }
                                            })) {
                                        failedAttempt(object, info);
                                        return false;
                                    } else {
                                        setNextAnimation(info
                                                .getSuccessCatchAnim());
                                        return true;
                                    }
                                }
                            })) {
                        setRespawnTask(); // auto finishes
                    }
                }
            }
        }
    }

    private void failedAttempt(final WorldObject object, final BoxAction.HunterNPC info) {
        setNextAnimation(info.getFailCatchAnim());
        if (OwnedObjectManager.convertIntoObject(
                object,
                new WorldObject(info.getFailedTransformObjectId(), 10, 0, this
                        .getX(), this.getY(), this.getPlane()),
                new OwnedObjectManager.ConvertEvent() {
                    @Override
                    public boolean canConvert(final Player player) {
                        // if(larupia)
                        // blablabla
                        return true;
                    }
                })) {
        }
    }
}