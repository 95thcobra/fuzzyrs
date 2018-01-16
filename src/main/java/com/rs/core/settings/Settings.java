package com.rs.core.settings;

import com.rs.world.WorldTile;

public class Settings {

    public final String SERVER_NAME;
    public final boolean HOSTED;
    public final boolean DEBUG;
    public final boolean ECONOMY;
    public final boolean NEWS_BOARD;
    public final boolean CONTROL_PANEL;
    public final int MAX_CONNECTIONS;
    public final String WEBSITE_LINK;
    public final String FORUM_LINK;
    public final String PLAYERS_ONLINE_LINK;
    public final String ITEMLIST_LINK;
    public final String VOTE_LINK;
    public final String[] OWNERS;
    public final int START_PLAYER_HITPOINTS;
    public final WorldTile DUNG_PLAYER_LOCATION;
    public final WorldTile START_PLAYER_LOCATION;
    public final String START_CONTROLER;
    public final WorldTile RESPAWN_PLAYER_LOCATION;
    public final int COMBAT_XP_RATE;
    public final int SKILLING_XP_RATE;
    public final double SALES_TAX;
    public final int SAILING_PAYOUT_MULTIPLIER;
    public final int DROP_RATE;
    public final int MIN_FREE_MEM_ALLOWED;

    public Settings(String server_name, boolean hosted, boolean debug, boolean economy, boolean newsBoard, boolean control_panel, int maxConnections, String website_link, String forum_link, String players_online_link, String itemlist_link, String vote_link, String[] owners, int start_player_hitpoints, WorldTile dung_player_location, WorldTile start_player_location, String start_controler, WorldTile respawn_player_location, int combat_xp_rate, int skilling_xp_rate, double sales_tax, int sailing_payout_multiplier, int drop_rate, int min_free_mem_allowed) {
        SERVER_NAME = server_name;
        HOSTED = hosted;
        DEBUG = debug;
        ECONOMY = economy;
        NEWS_BOARD = newsBoard;
        CONTROL_PANEL = control_panel;
        MAX_CONNECTIONS = maxConnections;
        WEBSITE_LINK = website_link;
        FORUM_LINK = forum_link;
        PLAYERS_ONLINE_LINK = players_online_link;
        ITEMLIST_LINK = itemlist_link;
        VOTE_LINK = vote_link;
        OWNERS = owners;
        START_PLAYER_HITPOINTS = start_player_hitpoints;
        DUNG_PLAYER_LOCATION = dung_player_location;
        START_PLAYER_LOCATION = start_player_location;
        START_CONTROLER = start_controler;
        RESPAWN_PLAYER_LOCATION = respawn_player_location;
        COMBAT_XP_RATE = combat_xp_rate;
        SKILLING_XP_RATE = skilling_xp_rate;
        SALES_TAX = sales_tax;
        SAILING_PAYOUT_MULTIPLIER = sailing_payout_multiplier;
        DROP_RATE = drop_rate;
        MIN_FREE_MEM_ALLOWED = min_free_mem_allowed;
    }
}
