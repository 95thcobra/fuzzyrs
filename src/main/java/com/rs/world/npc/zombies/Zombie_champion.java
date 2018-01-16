package com.rs.world.npc.zombies;

import com.rs.player.controlers.Controller;
import com.rs.world.WorldTile;
//import com.rs.gametask.minigames.zombies.Zombies;

/**
 * @author Adam
 * @since Jully 31st 2012 NPC 3066
 */

@SuppressWarnings("serial")
public class Zombie_champion extends ZombiesNPC {

    private final Controller controller;
    private boolean spawnedZombies;

    public Zombie_champion(final int id, final WorldTile tile,
                           final Controller controller) {
        super(id, tile);
        this.controller = controller;
    }

	/*
     * @Override public void processNPC() { super.processNPC();
	 * if(!spawnedZombies && getHitpoints() < getMaxHitpoints() / 2) {
	 * spawnedZombies = true; controller.spawnZombieMembers(); } }
	 * 
	 * @Override public void sendDeath(Entity source) { final
	 * NPCCombatDefinitions defs = getCombatDefinitions(); resetWalkSteps();
	 * getCombat().removeTarget(); setNextAnimation(null);
	 * WorldTasksManager.scheduleTask(new WorldTask() { int loop;
	 * 
	 * @Override public void run() { if (loop == 0) { setNextAnimation(new
	 * Animation(defs.getDeathEmote())); setNextGraphics(new Graphics(2924 +
	 * getSize())); } else if (loop >= defs.getDeathDelay()) { reset();
	 * finish(); controller.win(); stop(); } loop++; } }, 0, 1); }
	 */
}
