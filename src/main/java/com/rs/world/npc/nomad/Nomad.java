package com.rs.world.npc.nomad;

import com.rs.content.dialogues.Dialogue;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.player.QuestManager.Quests;
import com.rs.player.content.FadingScreen;
import com.rs.world.*;
import com.rs.world.npc.NPC;
import com.rs.world.npc.familiar.Familiar;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Nomad extends NPC {

    private int nextMove;
    private long nextMovePerform;
    private WorldTile throneTile;
    private ArrayList<NPC> copies;
    private boolean healed;
    private int notAttacked;
    private Player target;

    public Nomad(final int id, final WorldTile tile, final int mapAreaNameHash,
                 final boolean canBeAttackFromOutOfArea, final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
        setForceMultiArea(true);
        setRun(true);
        setCapDamage(750);
        setForceTargetDistance(5);
        setNextMovePerform();
        setRandomWalk(true);
    }

    public void setTarget(final Player player) {
        target = player;
        super.setTarget(player);
    }

    public void setNextMovePerform() {
        nextMovePerform = Utils.currentTimeMillis()
                + Utils.random(20000, 30000);
    }

    public boolean isMeleeMode() {
        return nextMove == -1;
    }

    public void setMeleeMode() {
        nextMove = -1;
        setForceFollowClose(true);
    }

    @Override
    public void reset() {
        notAttacked = 0;
        if (nextMove == -1) {
            nextMove = 0;
            setForceFollowClose(false);
        }
        healed = false;
        if (copies != null) {
            destroyCopies();
        }
        setNextMovePerform();
        super.reset();

    }

    @Override
    public void sendDeath(final Entity source) {
        if (throneTile != null) {
            target.lock();
            target.getPackets().sendConfigByFile(6962, 0);
            Dialogue.sendNPCDialogueNoContinue(target, getId(), 9802,
                    "You...<br>You have doomed this worldtask.");
            target.getPackets().sendVoice(8260);
            WorldTasksManager.schedule(new WorldTask() {
                @Override
                public void run() {
                    Dialogue.closeNoContinueDialogue(target);
                    target.getQuestManager().completeQuest(
                            Quests.NOMADS_REQUIEM);
                    FadingScreen.fade(target, new Runnable() {

                        @Override
                        public void run() {
                            target.getControllerManager().forceStop();
                            target.unlock();
                        }

                    });
                }
            }, getCombatDefinitions().getAttackDelay() + 1);
        }
        super.sendDeath(source);
    }

    @Override
    public void processNPC() {
        final Entity target = getCombat().getTarget();
        if (target instanceof Player && !clipedProjectile(target, false)) {
            notAttacked++;
            if (notAttacked == 10) {
                if (copies != null) {
                    destroyCopies();
                    notAttacked = 0;
                    return;
                }
                final Player player = (Player) target;
                setNextForceTalk(new ForceTalk("Face me!"));
                player.getPackets().sendVoice(7992);
            } else if (notAttacked == 20) {
                final Player player = (Player) target;
                setNextForceTalk(new ForceTalk("Coward."));
                player.getPackets().sendVoice(8055);
                reset();
                setNextFaceEntity(null);
                sendTeleport(getThroneTile());
            }
        } else if (target instanceof Familiar && this.target != null) {
            super.setTarget(this.target);
        } else {
            notAttacked = 0;
        }
        super.processNPC();

    }

    // 0 mines
    // 1 wrath
    // 2 multiplies
    // 3 k0 move
    public int getNextMove() {
        if (nextMove == 4) {
            nextMove = 0;
        }
        return nextMove++;
    }

    public void setNextMove(final int nextMove) {
        this.nextMove = nextMove;
    }

    public void sendTeleport(final WorldTile tile) {
        setNextAnimation(new Animation(12729));
        setNextGraphics(new Graphics(1576));
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                setNextWorldTile(tile);
                setNextAnimation(new Animation(12730));
                setNextGraphics(new Graphics(1577));
                setDirection(6);
            }
        }, 3);
    }

    public boolean useSpecialSpecialMove() {
        return Utils.currentTimeMillis() > nextMovePerform;
    }

    public WorldTile getThroneTile() {
        /*
		 * if no throne returns middle of area
		 */
        return throneTile == null ? new WorldTile((getRegionX() << 6) + 32,
                (getRegionY() << 6) + 32, getPlane()) : throneTile;
    }

    public void setThroneTile(final WorldTile throneTile) {
        this.throneTile = throneTile;
    }

    public void createCopies(final Player target) {
        setNextAnimation(new Animation(12729));
        setNextGraphics(new Graphics(1576));
        final int thisIndex = Utils.random(4);
        final Nomad thisNpc = this;
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                copies = new ArrayList<NPC>();
                transformIntoNPC(8529);
                for (int i = 0; i < 4; i++) {
                    NPC n;
                    if (thisIndex == i) {
                        n = thisNpc;
                        setNextWorldTile(getCopySpot(i));
                    } else {
                        n = new FakeNomad(getCopySpot(i), thisNpc);
                        copies.add(n);
                    }
                    n.setCantFollowUnderCombat(true);
                    n.setNextAnimation(new Animation(12730));
                    n.setNextGraphics(new Graphics(1577));
                    n.setTarget(target);
                }

            }
        }, 3);
    }

    public WorldTile getCopySpot(final int index) {
        final WorldTile throneTile = getThroneTile();
        switch (index) {
            case 0:
                return throneTile;
            case 1:
                return throneTile.transform(-3, -3, 0);
            case 2:
                return throneTile.transform(3, -3, 0);
            case 3:
            default:
                return throneTile.transform(0, -6, 0);
        }

    }

    public void destroyCopy(final NPC copy) {
        copy.finish();
        if (copies == null)
            return;
        copies.remove(copy);
        if (copies.isEmpty()) {
            destroyCopies();
        }
    }

    public void destroyCopies() {
        transformIntoNPC(8528);
        setCantFollowUnderCombat(false);
        setNextMovePerform();
        if (copies == null)
            return;
        for (final NPC n : copies) {
            n.finish();
        }
        copies = null;
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        if (getId() == 8529) {
            destroyCopies();
        }
        super.handleIngoingHit(hit);
    }

    public boolean isHealed() {
        return healed;
    }

    public void setHealed(final boolean healed) {
        this.healed = healed;
    }

}
