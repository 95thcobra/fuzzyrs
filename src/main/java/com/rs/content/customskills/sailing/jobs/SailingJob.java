package com.rs.content.customskills.sailing.jobs;

import com.rs.player.Player;

/**
 * @author John (FuzzyAvacado) on 3/13/2016.
 */
public abstract class SailingJob {

    private int npcId, targetNpcId;
    private int stage;
    private Player player;

    public SailingJob(Player player, int npcId, int targetNpcId) {
        setStage(0);
        this.player = player;
        this.npcId = npcId;
        this.targetNpcId = targetNpcId;
    }

    public void update() {
        setStage(stage + 1);
    }

    public abstract void start();

    public abstract void process();

    public abstract void finish();

    public void end() {
        player.getSailingManager().endJob();
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getNpcId() {
        return npcId;
    }

    public void setNpcId(int npcId) {
        this.npcId = npcId;
    }

    public int getTargetNpcId() {
        return targetNpcId;
    }

    public void setTargetNpcId(int targetNpcId) {
        this.targetNpcId = targetNpcId;
    }
}
