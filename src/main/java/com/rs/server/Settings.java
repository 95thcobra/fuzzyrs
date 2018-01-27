package com.rs.server;

import com.rs.world.WorldTile;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class Settings {

    private String serverName;
    private boolean hosted;
    private boolean debug;
    private boolean economy;
    private boolean newsBoard;
    private boolean controlPanel;
    private int maxConnections;
    private String websiteLink;
    private String forumLink;
    private String playersOnlineLink;
    private String itemListLink;
    private String voteLink;
    private String[] owners;
    private int startPlayerHitpoints;
    private WorldTile dungeonPlayerLocation;
    private WorldTile startPlayerLocation;
    private WorldTile respawnPlayerLocation;
    private String startController;
    private int combatXpRate;
    private int skillingXpRate;
    private double salesTax;
    private int sailingPayoutMultiplier;
    private int dropRate;
    private int minFreeMemoryAllowed;

}
