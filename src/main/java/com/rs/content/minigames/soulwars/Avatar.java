package com.rs.content.minigames.soulwars;

import com.rs.content.actions.skills.Skills;
import com.rs.entity.Entity;
import com.rs.player.Player;
import com.rs.world.*;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

import java.util.ArrayList;

import static com.rs.content.minigames.soulwars.SoulWarsManager.*;

/**
 * @author John (FuzzyAvacado) on 1/6/2016.
 */
public class Avatar extends NPC {

    private static final long serialVersionUID = -7095738435000364109L;

    public Avatar(int id, WorldTile tile, int mapAreaNameHash,
                  boolean canBeAttackFromOutOfArea) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
        setLureDelay(0);
        setForceMultiAttacked(true);
        setCapDamage(500);
    }

    @Override
    public void handleIngoingHit(Hit hit) {
        if (hit.getSource() instanceof NPC || MINUTES_BEFORE_NEXT_GAME.get() < 4)
            return;
        final Teams team = Teams.values()[getId() - AVATAR_INDEX];
        if (((SoulWarsGameTask) World.getSoulWars().getTasks().get(PlayerType.IN_GAME)).getAvatarSlayerLevel(team) > ((Player) hit.getSource()).getSkills().getLevelForXp(Skills.SLAYER)) {
            ((Player) hit.getSource()).getPackets().sendGameMessage("Your slayer level is not high enough to damage this creature.");
            hit.setDamage(0);
        }
        super.handleIngoingHit(hit);
    }

    @Override
    public double getMeleePrayerMultiplier() {
        return 0.5;
    }

    @Override
    public ArrayList<Entity> getPossibleTargets() {
        ArrayList<Entity> targets = super.getPossibleTargets();
        final Teams team = Teams.values()[getId()
                - AVATAR_INDEX];
        for (Player player : ((SoulWarsGameTask) World.getSoulWars().getTasks().get(PlayerType.IN_GAME)).getPlayers(team)) {
            if (player != null && targets.contains(player))
                targets.remove(player);
        }
        return targets;
    }

    @Override
    public void sendDeath(Entity source) {
        final NPCCombatDefinitions defs = getCombatDefinitions();
        resetWalkSteps();
        getCombat().removeTarget();
        setNextAnimation(null);
        WorldTasksManager.schedule(new WorldTask() {
            int loop;

            @Override
            public void run() {
                if (loop == 0) {
                    setNextAnimation(new Animation(defs.getDeathEmote()));
                } else if (loop >= defs.getDeathDelay()) {
                    if (MINUTES_BEFORE_NEXT_GAME.get() > 3) {
                        ((SoulWarsGameTask) World.getSoulWars().getTasks().get(PlayerType.IN_GAME)).increaseKill(getId());
                        reset();
                        setLocation(getRespawnTile());
                        finish();
                        setRespawnTask();
                        super.stop();
                    }
                }
                loop++;
            }
        }, 0, 1);
    }
}
