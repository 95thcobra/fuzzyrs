package com.rs.world.npc.others;

import com.rs.content.dialogues.impl.DagonHai;
import com.rs.core.cores.CoresManager;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("serial")
public class Bork extends NPC {

    public static long deadTime;

    public Bork(final int id, final WorldTile tile, final int mapAreaNameHash,
                final boolean canBeAttackFromOutOfArea, final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
        setLureDelay(0);
        setForceAgressive(true);
    }

    public static String convertToTime() {
        final String time = "You have to wait "
                + (getTime() == 0 ? "few more seconds" : getTime() + " mins")
                + " to kill bork again.";
        return time;
    }

    public static int getTime() {
        return (int) (deadTime - System.currentTimeMillis() / 60000);
    }

    public static boolean atBork(final WorldTile tile) {
        return (tile.getX() >= 3083 && tile.getX() <= 3120)
                && (tile.getY() >= 5522 && tile.getY() <= 5550);
    }

    @Override
    public void sendDeath(final Entity source) {
        deadTime = System.currentTimeMillis() + (10 * 1 * 1);
        resetWalkSteps();
        for (final Entity e : getPossibleTargets()) {
            if (e instanceof Player) {
                final Player player = (Player) e;
                player.getInterfaceManager().sendInterface(693);
                player.getDialogueManager().startDialogue(DagonHai.class, 7137,
                        player, 1);
                WorldTasksManager.schedule(new WorldTask() {
                    @Override
                    public void run() {
                        player.stopAll();
                    }
                }, 8);
            }
        }
        getCombat().removeTarget();
        setNextAnimation(new Animation(getCombatDefinitions().getDeathEmote()));
        WorldTasksManager.schedule(new WorldTask() {

            @Override
            public void run() {
                drop();
                reset();
                setLocation(getRespawnTile());
                finish();
                if (!isSpawned()) {
                    setRespawnTask();
                }
                stop();
            }

        }, 4);
    }

    @Override
    public void setRespawnTask() {
        if (!hasFinished()) {
            reset();
            setLocation(getRespawnTile());
            finish();
        }
        CoresManager.SLOW_EXECUTOR.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    spawn();
                } catch (final Exception e) {
                    e.printStackTrace();
                } catch (final Error e) {
                    e.printStackTrace();
                }
            }
        }, 1, TimeUnit.HOURS);
    }

    @Override
    public double getMagePrayerMultiplier() {
        return 0.7;
    }

    @Override
    public double getRangePrayerMultiplier() {
        return 0.7;
    }

    @Override
    public double getMeleePrayerMultiplier() {
        return 0.7;
    }
}
