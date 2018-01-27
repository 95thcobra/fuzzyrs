package com.rs.world.npc.dungeonnering;

import com.rs.content.actions.skills.dungeoneering.dungeon.Dungeon;
import com.rs.content.actions.skills.dungeoneering.dungeon.DungeonManager;
import com.rs.content.actions.skills.dungeoneering.rooms.RoomReference;
import com.rs.entity.Entity;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public class CopyOfDungeonBoss extends NPC {

    private DungeonManager dungeon;
    private RoomReference room;

    public CopyOfDungeonBoss(int id, WorldTile tile, DungeonManager dungeon) {
        super(id, tile, -1, false, true);
        this.dungeon = dungeon;
        this.room = room;
        setForceMultiArea(true);
        setForceAgressive(true);
    }

    @Override
    public void sendDeath(Entity source) {
        super.sendDeath(source);
        // dungeon.openStairs(player);
    }

    public Dungeon getDungeon() {
        return dungeon.getDungeon();
    }
}
