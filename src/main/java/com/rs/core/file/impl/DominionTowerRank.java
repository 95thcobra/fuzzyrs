package com.rs.core.file.impl;

import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
public class DominionTowerRank {

    private final String username;
    private final long dominionFactor;
    private final int mode;
    private final String bossName;
    private final int floorId;

    public DominionTowerRank(String username, long dominionFactor, int mode, String bossName, int floorId) {
        this.username = username;
        this.dominionFactor = dominionFactor;
        this.mode = mode;
        this.bossName = bossName;
        this.floorId = floorId;
    }

    public DominionTowerRank(final Player player, final int mode, final String bossName) {
        this.username = player.getUsername();
        this.mode = mode;
        this.bossName = bossName;
        this.floorId = player.getDominionTower().getProgress();
        this.dominionFactor = player.getDominionTower().getTotalScore();
    }

    public String getUsername() {
        return username;
    }

    public int getMode() {
        return mode;
    }

    public String getBossName() {
        return bossName;
    }

    public int getFloorId() {
        return floorId;
    }

    public long getDominionFactor() {
        return dominionFactor;
    }
}
