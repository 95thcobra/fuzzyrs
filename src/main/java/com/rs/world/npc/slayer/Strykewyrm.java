package com.rs.world.npc.slayer;

import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

@SuppressWarnings("serial")
public class Strykewyrm extends NPC {

    private final int stompId;

    public Strykewyrm(final int id, final WorldTile tile,
                      final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, true);
        stompId = id;
    }

    public static void handleStomping(final Player player, final NPC npc) {
        if (npc.isCantInteract())
            return;
        if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
            if (player.getAttackedBy() != npc
                    && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
                player.getPackets().sendGameMessage(
                        "You are already in combat.");
                return;
            }
            if (npc.getAttackedBy() != player
                    && npc.getAttackedByDelay() > Utils.currentTimeMillis()) {
                if (npc.getAttackedBy() instanceof NPC) {
                    npc.setAttackedBy(player); // changes enemy to player,
                    // player has priority over
                    // npc on single areas
                } else {
                    player.getPackets().sendGameMessage(
                            "That npc is already in combat.");
                    return;
                }
            }
        }
        switch (npc.getId()) {
            case 9462:
                if (player.getSkills().getLevel(18) < 93) {
                    player.getPackets()
                            .sendGameMessage(
                                    "You need at least a slayer level of 93 to fight this.");
                    return;
                }
                break;
            default:
                return;
        }
        player.setNextAnimation(new Animation(4278));
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                npc.setNextAnimation(new Animation(12795));
                npc.transformIntoNPC(npc.getId() + 1);
                npc.setTarget(player);
                npc.setAttackedBy(player);
                stop();
            }

        }, 1, 2);
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (isDead())
            return;
        if (getId() != stompId && !isCantInteract() && !isUnderCombat()) {
            setNextAnimation(new Animation(12796));
            setCantInteract(true);
            WorldTasksManager.schedule(new WorldTask() {
                @Override
                public void run() {
                    transformIntoNPC(9462);
                    setCantInteract(false);
                }
            });
        }
    }

    @Override
    public void reset() {
        setNPC(stompId);
    }

}
