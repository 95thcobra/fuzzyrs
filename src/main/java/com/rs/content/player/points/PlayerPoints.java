package com.rs.content.player.points;

/**
 * @author FuzzyAvacado
 */
public enum PlayerPoints {

    DONATE_POINTS,
    LOYALTY_POINTS,
    VOTE_POINTS,
    PK_POINTS,
    PVM_POINTS,
    SLAYER_POINTS,
    RUNESPAN_POINTS,
    BOSS_POINTS,
    LEVEL_UP_POINTS,
    DOMINION_POINTS,
    LIVID_POINTS,
    DUNGEONEERING_TOKENS,
    ZEALS;

    public String formattedName() {
        return this.name().replace('_', ' ');
    }

}
