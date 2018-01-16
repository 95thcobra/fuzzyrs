package com.rs.world.npc.dungeonnering;

import com.rs.content.actions.skills.dungeoneering.Dungeoneering.Dungeon;
import com.rs.world.Entity;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public class DungeonBoss extends NPC {

    private Dungeon dungeon;

    public DungeonBoss(int id, WorldTile tile, Dungeon dungeon) {
        super(id, tile, dungeon.getDungeonBossRoomHash(), false, true);
        this.dungeon = dungeon;
        setForceMultiArea(true);
        setForceAgressive(true);
    }

    @Override
    public void sendDeath(Entity source) {
        super.sendDeath(source);
        dungeon.openStairs();
    }

    public Dungeon getDungeon() {
        return dungeon;
    }
}
