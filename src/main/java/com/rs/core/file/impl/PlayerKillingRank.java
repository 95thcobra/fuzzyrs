package com.rs.core.file.impl;

import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
public class PlayerKillingRank {

    private final String username;
    private final int kills, deaths;

    public PlayerKillingRank(String username, int kills, int deaths) {
        this.username = username;
        this.kills = kills;
        this.deaths = deaths;
    }

    public PlayerKillingRank(final Player player) {
        this.username = player.getUsername();
        this.kills = player.getKillCount();
        this.deaths = player.getDeathCount();
    }

    public String getUsername() {
        return username;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

}
