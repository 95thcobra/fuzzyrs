package com.rs.world.npc.others;

import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

@SuppressWarnings("serial")
public class PestMonsters extends NPC {

    public PestMonsters(final int id, final WorldTile tile,
                        final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea,
                        final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
    }

    @Override
    public void sendDeath(final Entity source) {
        final NPCCombatDefinitions defs = getCombatDefinitions();
        final NPC npc = (NPC) source;
        resetWalkSteps();
        getCombat().removeTarget();
        setNextAnimation(null);
        // deathEffects(npc);
        WorldTasksManager.schedule(new WorldTask() {
            int loop;

            @Override
            public void run() {
                if (loop == 0) {
                    if (!(npc.getId() == 6142) || (npc.getId() == 6144)
                            || (npc.getId() == 6145) || (npc.getId() == 6143)) {
                        setNextAnimation(new Animation(defs.getDeathEmote()));
                    }
                } else if (loop >= defs.getDeathDelay()) {
                    drop();
                    reset();
                    finish();
                    stop();
                }
                loop++;
            }
        }, 0, 1);
    }
    /*
	 * /** Death effects of NPCs
	 *
	 * @param n The npc TODO other monsters
	 */
	/*
	 * private void deathEffects(NPC n) { if (n.getMessageIcon() == 6142) { for (Player
	 * players : PestControl.playersInGame) {
	 * players.getPackets().sendIComponentText(408, 13, "DEAD");
	 * players.getPackets
	 * ().sendGameMessage("The west portal has been destroyed."); }
	 * PestControl.setPortals(0, true); } if (n.getMessageIcon() == 6144) { for (Player
	 * players : PestControl.playersInGame) {
	 * players.getPackets().sendIComponentText(408, 15, "DEAD");
	 * players.getPackets
	 * ().sendGameMessage("The south-east portal has been destroyed."); }
	 * PestControl.setPortals(1, true); } if (n.getMessageIcon() == 6145) { for (Player
	 * players : PestControl.playersInGame) {
	 * players.getPackets().sendIComponentText(408, 16, "DEAD");
	 * players.getPackets
	 * ().sendGameMessage("The south-west portal has been destroyed."); }
	 * PestControl.setPortals(2, true); } if (n.getMessageIcon() == 6143) { for (Player
	 * players : PestControl.playersInGame) {
	 * players.getPackets().sendIComponentText(408, 14, "DEAD");
	 * players.getPackets
	 * ().sendGameMessage("The east portal has been destroyed."); }
	 * PestControl.setPortals(3, true); } }
	 */

}
