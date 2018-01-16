package com.rs.world.npc.dungeonnering;

import com.rs.content.actions.skills.dungeoneering.dungeon.DungeonManager;
import com.rs.content.actions.skills.dungeoneering.rooms.RoomReference;
import com.rs.world.Entity;
import com.rs.world.World;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public final class Glutenus extends NPC {

    public DungeonManager dungeon;
    public RoomReference reference;
    private int meleeNPCId;
    private int switchPrayersDelay;
    private int spawnedSpiders;

    public Glutenus(int id, WorldTile tile, DungeonManager dungeonManager,
                    RoomReference reference) {
        super(id, tile, -1, true, true);
        this.reference = reference;
        this.dungeon = dungeonManager;
        meleeNPCId = id;
        World.spawnObject(new WorldObject(49283, 10, 3, reference.getX() - 7,
                reference.getY() + 4, 0), true);
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
        super.sendDeath(source);
        dungeon.stairs(this.reference, 7, 0);
        dungeon.stairs(this.reference, 7, 15);
    }

}
