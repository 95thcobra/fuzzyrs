package com.rs.world.npc.fightkiln;

import com.rs.entity.Entity;
import com.rs.player.Player;
import com.rs.player.controlers.FightKiln;
import com.rs.world.*;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class FightKilnNPC extends NPC {

    private final FightKiln controler;

    public FightKilnNPC(final int id, final WorldTile tile,
                        final FightKiln controler) {
        super(id, tile, -1, true, true);
        setForceMultiArea(true);
        setNoDistanceCheck(true);
        this.controler = controler;
    }

    private int getDeathGfx() {
        switch (getId()) {
            case 15201:
                return 2926;
            case 15202:
                return 2927;
            case 15203:
                return 2957;
            case 15213:
            case 15214:
            case 15204:
                return 2928;
            case 15205:
                return 2959;
            case 15206:
            case 15207:
                return 2929;
            case 15208:
            case 15211:
            case 15212:
                return 2973;
            default:
                return 2926;
        }
    }

    @Override
    public void sendDeath(final Entity source) {
        final NPCCombatDefinitions defs = getCombatDefinitions();
        resetWalkSteps();
        getCombat().removeTarget();
        setNextAnimation(null);
        controler.checkCrystal();
        setNextGraphics(new Graphics(getDeathGfx()));
        WorldTasksManager.schedule(new WorldTask() {
            int loop;

            @Override
            public void run() {
                if (loop == 0) {
                    setNextAnimation(new Animation(defs.getDeathEmote()));
                } else if (loop >= defs.getDeathDelay()) {
                    reset();
                    finish();
                    controler.removeNPC();
                    stop();
                }
                loop++;
            }
        }, 0, 1);
    }

    @Override
    public ArrayList<Entity> getPossibleTargets() {
        final ArrayList<Entity> possibleTarget = new ArrayList<Entity>(1);
        final List<Integer> playerIndexes = World.getRegion(getRegionId())
                .getPlayerIndexes();
        if (playerIndexes != null) {
            for (final int npcIndex : playerIndexes) {
                final Player player = World.getPlayers().get(npcIndex);
                if (player == null || player.isDead() || player.hasFinished()
                        || !player.isRunning()) {
                    continue;
                }
                possibleTarget.add(player);
            }
        }
        return possibleTarget;
    }

    @Override
    public double getMagePrayerMultiplier() {
        return 0.1;
    }

    @Override
    public double getRangePrayerMultiplier() {
        return 0.1;
    }

    @Override
    public double getMeleePrayerMultiplier() {
        return 0.1;
    }

}
