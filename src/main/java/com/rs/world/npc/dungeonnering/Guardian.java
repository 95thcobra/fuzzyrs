package com.rs.world.npc.dungeonnering;

import com.rs.content.actions.skills.dungeoneering.dungeon.DungeonManager;
import com.rs.content.actions.skills.dungeoneering.rooms.RoomReference;
import com.rs.entity.Entity;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public class Guardian extends NPC {

    private DungeonManager manager;
    private RoomReference reference;

    public Guardian(int id, WorldTile tile, DungeonManager manager,
                    RoomReference reference) {
        super(id, tile, -1, true, true);
        this.manager = manager;
        this.reference = reference;
    }

    @Override
    public void sendDeath(Entity source) {
        super.sendDeath(source);
        manager.updateGuardian(reference);
    }

}
