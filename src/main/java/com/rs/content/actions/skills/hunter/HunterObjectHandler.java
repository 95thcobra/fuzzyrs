package com.rs.content.actions.skills.hunter;

import com.rs.content.actions.skills.Skills;
import com.rs.player.OwnedObjectManager;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.WorldObject;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class HunterObjectHandler {

    public static boolean handleObject(Player player, int objectId, WorldObject object) {
        final BoxAction.HunterNPC hunterNpc = BoxAction.HunterNPC.forObjectId(objectId);
        if (hunterNpc != null) {
            if (OwnedObjectManager.removeObject(player, object)) {
                player.setNextAnimation(hunterNpc.getEquipment()
                        .getPickUpAnimation());
                player.getInventory().getItems()
                        .addAll(hunterNpc.getItems());
                player.getInventory().addItem(
                        hunterNpc.getEquipment().getId(), 1);
                player.getSkills().addXp(Skills.HUNTER,
                        hunterNpc.getXp());
            } else {
                player.getPackets().sendGameMessage(
                        "This isn't your trap.");
            }
            return true;
        } else if (objectId == BoxAction.HunterEquipment.BOX.getObjectId()
                || objectId == 19192) {
            if (OwnedObjectManager.removeObject(player, object)) {
                player.setNextAnimation(new Animation(5208));
                player.getInventory().addItem(
                        BoxAction.HunterEquipment.BOX.getId(), 1);
            } else {
                player.getPackets().sendGameMessage(
                        "This isn't your trap.");
            }
            return true;
        } else if (objectId == BoxAction.HunterEquipment.BRID_SNARE.getObjectId()
                || objectId == 19174) {
            if (OwnedObjectManager.removeObject(player, object)) {
                player.setNextAnimation(new Animation(5207));
                player.getInventory().addItem(
                        BoxAction.HunterEquipment.BRID_SNARE.getId(), 1);
            } else {
                player.getPackets().sendGameMessage(
                        "This isn't your trap.");
            }
            return true;
        }
        return false;
    }
}
