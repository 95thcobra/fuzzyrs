package com.rs.content.customskills.sailing;

import com.rs.content.customskills.CustomSkills;
import com.rs.content.customskills.sailing.dialogues.PickShipDialogue;
import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.utils.TextUtils;
import com.rs.player.Player;
import com.rs.world.WorldTile;

/**
 * @author John (FuzzyAvacado) on 12/17/2015.
 */
public class SailingMap {

    private static final int MAP_INTERFACE_ID = 95;

    public static void showInterface(Player player) {
        player.getInterfaceManager().sendInterface(MAP_INTERFACE_ID);
    }

    public static void handleInterfaceButton(Player player, int componentId) {
        MapRequirements req = null;
        for (MapRequirements r : MapRequirements.values()) {
            if (componentId == r.getComponentId()) {
                req = r;
                break;
            }
        }
        if (req != null) {
            if (player.getCustomSkills().getLevel(CustomSkills.SAILING) < req.getLevel()) {
                player.getDialogueManager().startDialogue(SimpleMessage.class, "You need a Sailing level of " + req.getLevel() + " in order to venture to " + TextUtils.upperCase(req.name().toLowerCase().replace("_", " ")) + ".");
                return;
            }
            player.getDialogueManager().startDialogue(PickShipDialogue.class, req);
        }
    }

    public enum MapRequirements {

        PORT_SARIM(0, 30, new WorldTile(3053, 3247, 0)),
        CATHERBY(10, 25, new WorldTile(2804, 3431, 0)),
        CRANDOR(20, 32, new WorldTile(2854, 3250, 0)),
        BRIMHAVEN(30, 28, new WorldTile(2906, 3187, 0)),
        KARAMJA(40, 27, new WorldTile(2946, 3147, 0)),
        SHIPYARD(50, 26, new WorldTile(2952, 3038, 0)),
        PORT_KHAZARD(60, 29, new WorldTile(2673, 3150, 0)),
        PORT_TYRAS(70, 23, new WorldTile(2154, 3122, 0)),
        MOS_LE_HARMLESS(80, 31, new WorldTile(3678, 2956, 0)),
        OO_GLOG(90, 33, new WorldTile(2614, 2856, 0)),
        PORT_PHASMATYS(95, 24, new WorldTile(3699, 3487, 0));

        private final int level;
        private final int componentId;
        private final WorldTile tile;

        MapRequirements(int level, int componentId, WorldTile tile) {
            this.level = level;
            this.componentId = componentId;
            this.tile = tile;
        }

        public int getLevel() {
            return level;
        }

        public int getComponentId() {
            return componentId;
        }

        public WorldTile getTile() {
            return tile;
        }
    }
}
