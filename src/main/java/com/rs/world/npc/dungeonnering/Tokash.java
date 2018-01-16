package com.rs.world.npc.dungeonnering;

import com.rs.content.actions.skills.dungeoneering.dungeon.DungeonManager;
import com.rs.content.actions.skills.dungeoneering.rooms.RoomReference;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public final class Tokash extends NPC {

    public DungeonManager dungeon;
    public RoomReference reference;

    public Tokash(int id, WorldTile tile, DungeonManager dungeonManager,
                  RoomReference reference) {
        super(id, tile, -1, true, true);
        this.reference = reference;
        this.dungeon = dungeonManager;
        this.setHitpoints((this.getCombatLevel() * 10) + 200);
        this.setName("Zenith's Slave");
        this.getCombatDefinitions().setMaxHit((this.getCombatLevel() * 2) + 50);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (isDead())
            return;
    }

    @Override
    public void sendDeath(Entity source) {
        this.setNextAnimation(new Animation(14369));
        super.sendDeath(source);
        dungeon.stairs(this.reference, 7, 0);
        dungeon.stairs(this.reference, 7, 15);
    }

}
