package com.rs.content.minigames.castlewars;

import com.rs.player.Player;
import com.rs.world.World;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@GameTaskType(GameTaskManager.GameTaskType.SLOW)
public class PlayingGame extends GameTask {

    private static final int SAFE = 0, TAKEN = 1, DROPPED = 2;
    private static final int TIME_TILL_START = 1; //5
    private final LinkedList<WorldObject> spawnedObjects = new LinkedList<WorldObject>();
    private final LinkedList<CastleWarBarricade> barricades = new LinkedList<CastleWarBarricade>();
    private int minutesLeft;
    private int[] score;
    private int[] flagStatus;
    private int[] barricadesCount;

    public PlayingGame(ExecutionType executionType, long initialDelay, long tick, TimeUnit timeUnit) {
        super(executionType, initialDelay, tick, timeUnit);
        reset();
    }

    public static List<Player>[] getPlaying() {
        return CastleWars.getPlaying();
    }

    public int getMinutesLeft() {
        return minutesLeft;
    }

    public void reset() {
        minutesLeft = TIME_TILL_START;
        score = new int[2];
        flagStatus = new int[2];
        barricadesCount = new int[2];
        for (final WorldObject object : spawnedObjects) {
            World.destroySpawnedObject(object, !(object.getId() == 4377
                    || object.getId() == 4378));
        }
        spawnedObjects.clear();
        barricades.forEach(CastleWarBarricade::finish);
        barricades.clear();
    }

    public boolean isBarricadeAt(final WorldTile tile) {
        for (final Iterator<CastleWarBarricade> it = barricades.iterator(); it
                .hasNext(); ) {
            final CastleWarBarricade npc = it.next();
            if (npc.isDead() || npc.hasFinished()) {
                it.remove();
                continue;
            }
            if (npc.getX() == tile.getX() && npc.getY() == tile.getY()
                    && tile.getPlane() == tile.getPlane())
                return true;
        }
        return false;
    }

    public void addBarricade(final int team, final Player player) {
        if (barricadesCount[team] >= 10) {
            player.getPackets()
                    .sendGameMessage(
                            "Each team in the activity can have a maximum of 10 barricades set up.");
            return;
        }
        player.getInventory().deleteItem(new Item(4053, 1));
        barricadesCount[team]++;
        barricades.add(new CastleWarBarricade(team, new WorldTile(player)));
    }

    public void removeBarricade(final int team, final CastleWarBarricade npc) {
        barricadesCount[team]--;
        barricades.remove(npc);
    }

    public void takeFlag(final Player player, final int team,
                         final int flagTeam, final WorldObject object,
                         final boolean droped) {
        if (!droped && team == flagTeam)
            return;
        if (droped && flagStatus[flagTeam] != DROPPED)
            return;
        else if (!droped && flagStatus[flagTeam] != SAFE)
            return;

        if (flagTeam != team
                && (player.getEquipment().getWeaponId() != -1 || player
                .getEquipment().getShieldId() != -1)) {
            // TODO no space message
            player.getPackets()
                    .sendGameMessage(
                            "You can't take flag while wearing something in your hands.");
            return;
        }
        if (!droped) {
            final WorldObject flagStand = new WorldObject(
                    flagTeam == CastleWarsConstants.SARADOMIN ? 4377 : 4378, object.getType(),
                    object.getRotation(), object.getX(), object.getY(),
                    object.getPlane());
            spawnedObjects.add(flagStand);
            World.spawnObject(flagStand, false);
        } else {
            spawnedObjects.remove(object);
            World.destroySpawnedObject(object, true);
            if (flagTeam == team) {
                makeSafe(flagTeam);
                return;
            }
        }
        CastleWars.addHintIcon(flagTeam, player);
        flagStatus[flagTeam] = TAKEN;
        CastleWars.setWeapon(player, new Item(flagTeam == CastleWarsConstants.SARADOMIN ? 4037 : 4039, 1));
        CastleWars.refreshAllPlayersPlaying();
    }

    public void addScore(final Player player, final int team,
                         final int flagTeam) {
        CastleWars.setWeapon(player, null);
        score[team] += 1;
        makeSafe(flagTeam);
    }

    private void makeSafe(final int flagTeam) {
        WorldObject flagStand = null;
        for (final WorldObject object : spawnedObjects) {
            if (object.getId() == (flagTeam == CastleWarsConstants.SARADOMIN ? 4377 : 4378)) {
                flagStand = object;
                break;
            }
        }
        if (flagStand == null)
            return;
        World.destroySpawnedObject(flagStand, false);
        flagStatus[flagTeam] = SAFE;
        CastleWars.refreshAllPlayersPlaying();
    }

    public void dropFlag(final WorldTile tile, final int flagTeam) {
        CastleWars.removeHintIcon(flagTeam);
        final WorldObject flagDroped = new WorldObject(
                flagTeam == CastleWarsConstants.SARADOMIN ? 4900 : 4901, 10, 0, tile.getX(),
                tile.getY(), tile.getPlane());
        spawnedObjects.add(flagDroped);
        World.spawnObject(flagDroped, true);
        flagStatus[flagTeam] = DROPPED;
        CastleWars.refreshAllPlayersPlaying();
    }

    public void refresh(final Player player) {
        player.getPackets().sendConfigByFile(143, flagStatus[CastleWarsConstants.SARADOMIN]);
        player.getPackets().sendConfigByFile(145, score[CastleWarsConstants.SARADOMIN]);
        player.getPackets().sendConfigByFile(153, flagStatus[CastleWarsConstants.ZAMORAK]);
        player.getPackets().sendConfigByFile(155, score[CastleWarsConstants.ZAMORAK]);
    }

    @Override
    public void run() {
        minutesLeft--;
        if (minutesLeft == 5) {
            CastleWars.endGame(score[CastleWarsConstants.SARADOMIN] == score[CastleWarsConstants.ZAMORAK] ? -2
                    : score[CastleWarsConstants.SARADOMIN] > score[CastleWarsConstants.ZAMORAK] ? CastleWarsConstants.SARADOMIN
                    : CastleWarsConstants.ZAMORAK);
            reset();
        } else if (minutesLeft == 0) {
            minutesLeft = 25;
            CastleWars.startGame();
        } else if (minutesLeft > 6) {
            CastleWars.startGame();
        }
        CastleWars.refreshAllPlayersTime();
    }
}
