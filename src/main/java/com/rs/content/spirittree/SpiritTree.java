package com.rs.content.spirittree;

import com.rs.player.Player;
import com.rs.player.content.Magic;
import com.rs.world.WorldTile;

/**
 * @author John (FuzzyAvacado) on 1/6/2016.
 */
public class SpiritTree {

    public static final WorldTile
            TREE_GNOME_VILLAGE = new WorldTile(2542, 3169, 0),
            TREE_GNOME_STRONGHOLD = new WorldTile(2462, 3446, 0),
            GRAND_EXCHANGE_TREE = new WorldTile(3187, 3508, 0),
            BATTLEFIELD_OF_KHAZARD = new WorldTile(2555, 3256, 0),
            MOBILISING_ARMIES_TREE = new WorldTile(2417, 2852, 0),
            MOUNTAINS_POISON_WASTE = new WorldTile(2339, 3108, 0);


    public static void openSpiritTree(final Player player) {
        player.getPackets().sendConfig(1063, 100000);
        player.getInterfaceManager().sendInterface(864);
        player.getPackets().sendIComponentSettings(864, 6, 0, 6, 2);
    }

    public static void handleSpiritTree(Player player, int componentId, int slotId) {
        if (componentId == 6) {
            if (slotId == 0)
                Magic.sendPlantTeleport(player, 0, 0, TREE_GNOME_VILLAGE);
            else if (slotId == 1)
                Magic.sendPlantTeleport(player, 0, 0, TREE_GNOME_STRONGHOLD);
            else if (slotId == 2)
                Magic.sendPlantTeleport(player, 0, 0, BATTLEFIELD_OF_KHAZARD);
            else if (slotId == 3)
                Magic.sendPlantTeleport(player, 0, 0, GRAND_EXCHANGE_TREE);
            else if (slotId == 4)
                Magic.sendPlantTeleport(player, 0, 0, MOBILISING_ARMIES_TREE);
            else if (slotId == 5)
                Magic.sendPlantTeleport(player, 0, 0, MOUNTAINS_POISON_WASTE);
            player.closeInterfaces();
            player.getPackets().sendGameMessage("You place your hands on the dry tough bark of the spirit tree and feel a surge of energy run through your veins.");
        }
    }

}
